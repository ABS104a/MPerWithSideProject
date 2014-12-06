package com.abs104a.mperwithsideproject;

import com.abs104a.mperwithsideproject.main.MainViewController;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * ���C����ʂ�\������Service
 * 
 * WindowManager�����View��o�^����
 * ��ʏ�ɏd���\������D
 * 
 * �@�\�Ƃ���
 * �E�v���C���X�g���X�g�\��		�i�^�u
 * �E�ȈՃA���o�����X�g�\���@	�i�^�u
 * �E���y�̏ڍו\��
 * �E�Đ��E�ꎞ��~�Ȃǂ̑���
 * �E���ʑ���
 * �E�V�[�N
 * �E�����_��&���s�[�g
 * �E���X�g�\����ʂ̑J�ڃ{�^��
 * �E�ݒ��ʂւ̑J�ڃ{�^��
 * 
 * ��{�I�ȑ���͂�����ŃJ�o�[������D
 * 
 * @author Kouki
 *
 */
public class MainService extends Service{
	
	//�����̃T�[�r�X�iContext�擾�p)
	private final Service mService = this;
	//���C���r���[�����pWindowManager
	private WindowManager mWindowManager = null;
	//���C���r���[�ێ��p
	private View mMainView = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Activity����o�C���h���ꂽ��
		return null;
	}

	/**
	 * Service���J�n���ꂽ��
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		//MainView�̐���
		LayoutInflater inflater = LayoutInflater.from( mService );
		inflater.inflate(R.layout.player_view, null);

		mWindowManager  = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

		// �d�ˍ��킹����View�̐ݒ���s��
		LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_TOAST,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,// | 
				//WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL ,//| WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING,
				//WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.TOP | Gravity.RIGHT;
		params.x = 0;
		params.y = 0;

		//�d���\������View���擾����D
		mMainView = MainViewController.createView(mService);
		
		//WindowManager��View��LayoutParams��o�^���C�\������
		try{
			mWindowManager.updateViewLayout(mMainView, params);
		}catch(NullPointerException mNullPointerException){
			mNullPointerException.printStackTrace();
		}

	}

	/**
	 * Service���I��������
	 */
	@Override
	public void onDestroy() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		try{
			mWindowManager.removeView(mMainView);
		}catch(NullPointerException mNullPointerException){
			mNullPointerException.printStackTrace();
		}
		super.onDestroy();
	}

}
