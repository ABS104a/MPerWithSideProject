package com.abs104a.mperwithsideproject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.WindowManager;

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

	@Override
	public IBinder onBind(Intent intent) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
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
		
		
		
		WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		
	}

	/**
	 * Service���I��������
	 */
	@Override
	public void onDestroy() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onDestroy();
	}

}
