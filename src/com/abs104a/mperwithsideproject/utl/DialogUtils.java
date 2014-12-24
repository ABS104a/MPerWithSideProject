package com.abs104a.mperwithsideproject.utl;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.music.PlayList;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * アプリケーションに関するダイアログを表示するためのUtilクラス
 * @author Kouki
 *
 */
public class DialogUtils {
	
	public static final String TAG = "DISPLAY_UTILS";
	
	public static final WindowManager setWindowManager(Context mContext,View setView){
		final WindowManager mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		
		// 重ね合わせするViewの設定を行う
		LayoutParams params = new WindowManager.LayoutParams(
				mContext.getResources().getDimensionPixelSize(R.dimen.dialog_view_width),
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_TOAST,
				//WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
				WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED ,
				PixelFormat.TRANSLUCENT);
		
		params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
		params.x = mContext.getResources().getDimensionPixelSize(R.dimen.dialog_view_margin);
		
		LinearLayout layout = (LinearLayout) setView.findViewById(R.id.linearLayout_dialog);
		Animation anim = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
		layout.startAnimation(anim);
		
		//Viewの追加
		mWindowManager.addView(setView, params);
		
		return mWindowManager;
	}
	
	private final static void removeForWindowManager(final WindowManager mWindowManager,final View mView){
		try{
			LinearLayout layout = (LinearLayout) mView.findViewById(R.id.linearLayout_dialog);
			Animation anim = AnimationUtils.loadAnimation(mView.getContext(), android.R.anim.slide_out_right);
			anim.setAnimationListener(new AnimationListener(){

				@Override
				public void onAnimationStart(Animation animation) {}

				@Override
				public void onAnimationEnd(Animation animation) {
					mWindowManager.removeView(mView);//Viewの消去
				}

				@Override
				public void onAnimationRepeat(Animation animation) {}

			});
			layout.startAnimation(anim);
		}catch(NullPointerException e){
			e.printStackTrace();
		}

	}
	
	/**
	 * PlayListを選択するDialogを表示する．
	 * (最初の項目はQueue，3番目～がPlayList)
	 * （2番目の項目はPlayListを作成する．）
	 * @param mContext
	 * @param music 対象とするMusicInstance
	 * @param isQueue キューへ追加するItemを設定するかどうか
	 */
	public final static void createIfSelectPlayListDialog(Context mContext,final Music music,final boolean isQueue){
			
		//MainViewの生成
		LayoutInflater inflater = LayoutInflater.from( mContext );
		final ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.dialog, null);
		
		//WindowManager~の起動にする．
		final WindowManager mWindowManager = setWindowManager(mContext, mView);

		//ダイアログに挿入するListView
		final ListView mListView = new ListView(mContext);
		
		final FrameLayout mLayout = (FrameLayout)mView.findViewById(R.id.frameLayout_dialog);
		mLayout.addView(mListView);
		
		final TextView titleView = (TextView)mView.findViewById(R.id.textView_dialog_title);
		titleView.setText(R.string.add_to_playlist);
		
		final Button positiveButton = (Button)mView.findViewById(R.id.button_dialog_positive);
		positiveButton.setVisibility(View.GONE);
		
		final Button negativeButton = (Button)mView.findViewById(R.id.button_dialog_negative);
		negativeButton.setText(R.string.cancel);
		negativeButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//Viewの消去
				removeForWindowManager(mWindowManager,mView);
			}
			
		});
		
		//表示するPlayList
		final ArrayList<PlayList> mPlayLists = FileUtils.readSerializablePlayList(mContext);
		
		//ListViewへ適用させるAdapter
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1);
		
		//Queueへの追加をするItemの追加
		if(isQueue)
			adapter.add(mContext.getString(R.string.add_queue));
		//PlayListを作成するItemの追加
		adapter.add(mContext.getString(R.string.create_playlist));
		
		//データを格納する．
		try{
			for(PlayList pl :mPlayLists)
				adapter.add(pl.getAlbum());
		}catch(NullPointerException e){
			android.util.Log.e(TAG,"NotFoundPlayLists");
			android.util.Log.e(TAG,e.getMessage());
		}
		//Adapterへのセット
		mListView.setAdapter(adapter);
		
		//dividerの高さを設定
		mListView.setDividerHeight(
				mContext.getResources()
				.getDimensionPixelSize(R.dimen.listview_divider));
		
		//ListViewのItemを選択したときの動作を設定する．
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Viewの消去
				removeForWindowManager(mWindowManager,mView);
				
				//Queueへの追加Itemを追加しない場合はPositionを1つ増やす
				if(!isQueue)position += 1;
				
				//MusicControllerInstanceを取得
				final MusicPlayerWithQueue mpwpl 
				= MusicUtils.getMusicController(view.getContext());
				
				//Itemが選択された時の動作
				if(position == 0){
					//Queueへの追加を行う
					mpwpl.addMusic(music);
					Toast.makeText(
							view.getContext(), 
							music.getTitle() + " " + 
							view.getContext().getString(R.string.add_to_queue),
							Toast.LENGTH_SHORT)
							.show();
				}else if(position == 1){
					//TODO PlayListの作成
					createPlayListDialog(view.getContext(),music, mPlayLists);
				}else{
					//既存のプレイリストへの追加
					//追加するプレイリストのインデックス．
					final int index = Math.max(0, position - 2);
					try{
						//現在のプレイリスト曲を読み込む
						Music[] musics = mPlayLists.get(index).getMusics();
						//新しいプレイリスト曲の配列を生成する．
						Music[] newMusics = new Music[musics.length + 1];
						for(int i = 0;i < musics.length;i++){
							newMusics[i] = musics[i];
						}
						//最後の要素に追加する．
						newMusics[newMusics.length - 1] = music;
						//新しい配列をセットする．
						mPlayLists.get(index).setMusics(newMusics);
						//データを保存する．
						FileUtils.writeSerializablePlayList(view.getContext(), mPlayLists);
					}catch(NullPointerException e){
						android.util.Log.e(TAG,"NotFoundPlayLists");
						android.util.Log.e(TAG,e.getMessage());
					}
					
				}
			}
			
		});
		
	}
	
	/**
	 * プレイリストを新たに作成する時に表示するダイアログを設定する．
	 */
	public final static void createPlayListDialog(
			Context mContext,
			final Music music,
			final ArrayList<PlayList> mPlayLists)
	{
		//MainViewの生成
		LayoutInflater inflater = LayoutInflater.from( mContext );
		final ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.dialog, null);
		
		//WindowManager~の起動にする．
		final WindowManager mWindowManager = setWindowManager(mContext, mView);
		
		//Titleの設定
		final TextView titleView = (TextView)mView.findViewById(R.id.textView_dialog_title);
		titleView.setText(R.string.create_playlist_title);

		//プレイリスト名を入力するEditTextを生成
		final EditText mEditText = new EditText(mContext);
		mEditText.setHint(R.string.create_playlist_text_hint);
		
		DisplayUtils.showInputMethodEditor(mContext, mView);
		
		//Viewをセットする．
		FrameLayout contentLayout = (FrameLayout)mView.findViewById(R.id.frameLayout_dialog);
		contentLayout.addView(mEditText);
		
		//DialogのOKボタンを押したときの設定
		final Button positiveButton = (Button)mView.findViewById(R.id.button_dialog_positive);
		positiveButton.setText(R.string.ok);
		positiveButton.setOnClickListener(new OnClickListener(){
			
			/**
			 * 新しいプレイリストを作成する．
			 */
			@Override
			public void onClick(View view) {
				String title = mEditText.getText().toString();
				//文字が入力されていた時
				if(title != null && title.length() > 0){
					// プレイリストを生成して曲を追加する．
					final Music[] musics = new Music[1];
					musics[0] = music;
					
					final PlayList newPlayList = 
							new PlayList(title, title, System.currentTimeMillis(), null);
					//Musicをセット
					newPlayList.setMusics(musics);
					mPlayLists.add(newPlayList);
					//Viewの消去
					removeForWindowManager(mWindowManager,mView);
					DisplayUtils.hideInputMethodEditor(view.getContext(), mView);
				}
			}
		});
		
		//DialogのCancelボタンを押したときの動作
		final Button negativeButton = (Button)mView.findViewById(R.id.button_dialog_negative);
		negativeButton.setText(R.string.cancel);
		negativeButton.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View view) {
				// Dialogを閉じる．
				//Viewの消去
				removeForWindowManager(mWindowManager,mView);
				DisplayUtils.hideInputMethodEditor(view.getContext(), mView);
			}
		});
	}
	
}