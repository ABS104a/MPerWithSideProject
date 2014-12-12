package com.abs104a.mperwithsideproject.view;

import com.abs104a.mperwithsideproject.R;

import android.view.View;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;

/**
 * テキストのマーキー表示を行うカスタムビュー
 * 
 */
public class MarqueeView extends View {
    private Paint mTextPaint;
    private String mText;
    private int mAscent;
    
    private int mRepeatCount;      // リピートした回数
    private int mRepeatLimit;      // 最大リピート回数
    private int mCurrentX;         // 現在のテキストの位置
    private int mTextMoveSpeed;    // 1フレームで動く距離
    private Thread mThread = null; // テキストを移動させるスレッド

    // マーキー表示処理(テキストの移動＋表示)
    private Runnable runnable = new Runnable() {
        public void run() {
            // 左端と判断するX座標
            int lastX = getLastX();

            while(mRepeatCount < mRepeatLimit) {
                mCurrentX = getMarqueeStartX(); // テキスト位置を戻す

                long beforeTime = System.currentTimeMillis();
                long afterTime = beforeTime;
                int fps = 30;
                long frameTime = 1000 / fps;

                // 1回のマーキー処理
                while(true) {               
                    // 左端まで到達したらリピート1回としてカウント
                    if(mCurrentX <= lastX) {
                        mRepeatCount += 1;
                        break;
                    }

                    mCurrentX -= mTextMoveSpeed;
                    postInvalidate();
                
                    afterTime = System.currentTimeMillis();
                    long pastTime = afterTime - beforeTime;
                    
                    long sleepTime = frameTime - pastTime;
                    
                    if(sleepTime > 0) {
                        try {
                            Thread.sleep(sleepTime);
                        }catch(Exception e){}
                    }
                    beforeTime = System.currentTimeMillis();
                }
                
            }
        }
    };
    
    /**
     * マーキー処理を停止する
     */
    public void clearMarquee() {
        mCurrentX = getMarqueeStartX();
        mRepeatCount = 0;
        mThread = null;
    }

    /**
     * マーキー処理を開始する
     */
    public void startMarquee() {
        clearMarquee();
        mThread = new Thread(runnable);
        mThread.start();
    }

    /**
     * コンストラクタ(XMLを使用しない場合)
     * 
     * @param context
     */
    public MarqueeView(Context context) {
        super(context);
        initMarqueeView();
    }

    /**
     * コンストラクタ(XMLを使用する場合)
     * 
     * @param context
     * @param attrs XMLで定義した属性
     */
    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMarqueeView();

        // XMLから属性を取得
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MarqueeView);

        String s = a.getString(R.styleable.MarqueeView_text);
        if(s != null) {
            setText(s);
        }

        int textSize = a.getDimensionPixelOffset(R.styleable.MarqueeView_textSize, 0);
        if (textSize > 0) {
            setTextSize(textSize);
        }

        setTextColor(a.getColor(R.styleable.MarqueeView_textColor, 0xFFFFFFFF));
        setBackgroundColor(a.getColor(R.styleable.MarqueeView_background, 0xFF000000));
        setRepeatLimit(a.getInteger(R.styleable.MarqueeView_repeatLimit, 1));
        setTextMoveSpeed(a.getInteger(R.styleable.MarqueeView_textMoveSpeed, 5));

        a.recycle();
    }

    /**
     * 初期化処理
     * このメソッドは必ずコンストラクタ内で呼び出す必要がある
     */
    private final void initMarqueeView() {
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(16);
        mTextPaint.setColor(0xFFFFFFFF);
        mTextMoveSpeed = 5;
        mRepeatCount = 0;
        setRepeatLimit(1);
        setText("");
        setPadding(0, 0, 0, 0);
        setBackgroundColor(0xFF000000);
    }
    
    /**
     * リピート回数を設定する
     * @param repeatLimit
     */
    public void setRepeatLimit(int repeatLimit) {
        if(repeatLimit > 0) {
            mRepeatLimit = repeatLimit;
        }else {
            mRepeatLimit = 1;
        }
    }
    
    /**
     * テキストの移動速度(px)を設定する
     * 
     * @param speed 移動速度(ピクセルで指定)
     */
    public void setTextMoveSpeed(int speed) {
        if(speed > 0) {
            mTextMoveSpeed = speed;
        }
    }

    /**
     * テキストを設定する
     * 
     * @param text 表示するテキスト
     */
    public void setText(String text) {
        mText = text;
        requestLayout();
        invalidate();
    }

    /**
     * テキストサイズを設定する
     * 
     * @param size フォントサイズ
     */
    public void setTextSize(int size) {
        mTextPaint.setTextSize(size);
        requestLayout();
        invalidate();
    }

    /**
     * テキストカラーを設定する
     * 
     * @param color
     */
    public void setTextColor(int color) {
        mTextPaint.setColor(color);
        invalidate();
    }

    /**
     * ビューのサイズを設定する
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
            measureWidth(widthMeasureSpec),
            measureHeight(heightMeasureSpec)
        );
    }

    /**
     * ビューの幅を返す
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = (int) mTextPaint.measureText(mText) + getPaddingLeft()
                    + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    /**
     * ビューの高さを返す
     */
    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        mAscent = (int) mTextPaint.ascent();
        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = (int) (-mAscent + mTextPaint.descent()) + getPaddingTop()
                    + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
    
    /**
     * マーキーの開始位置のX座標を返す
     */
    private int getMarqueeStartX() {
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int measureText = (int)mTextPaint.measureText(mText);
        int measureWidth = getMeasuredWidth();

        if(display.getWidth() == measureWidth) {
            return measureWidth;
        }else if(measureText > display.getWidth()) {
            // テキストが画面サイズを超える場合
            return display.getWidth();
        }else if(measureWidth > measureText) {
            return measureWidth;
        }else {
            return measureText;
        }
    }
    
    /**
     * 左端と判定するX座標
     * @return
     */
    private int getLastX() {
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
    
        int measureText = (int)mTextPaint.measureText(mText);
        int measureWidth = getMeasuredWidth();

        if(measureText >= display.getWidth()) {
            // テキストが画面サイズを超える場合
            return -measureText;
        }else if(measureWidth > measureText) {
            // テキストの幅がビューのサイズより小さい
            return -measureWidth;
        }else {
            return -measureText;
        }
    }
    
    /**
     * 描画処理
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        int x = getPaddingLeft() + mCurrentX;
        int y = getPaddingTop() - mAscent;
        canvas.drawText(mText, x, y, mTextPaint);
    }
}