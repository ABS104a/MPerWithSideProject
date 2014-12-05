package com.abs104a.mperwithsideproject.main;

import android.app.Service;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.main.listener.MainHandleActionOnClickListenerImpl;

/**
 * ���C���r���[�̐����Ɛݒ���s���N���X
 * @author Kouki
 *
 */
public class MainViewController {

	/**
	 * Main��View�𐶐����郁�\�b�h
	 * �T�[�r�X�̃R���e�L�X�g���󂯂Ƃ�View�𐶐�����D
	 * @param mService
	 * @return�@��������ViewGroup
	 */
	public static View createView(Service mService){
		//TODO View�̐���
		// View����C���t���[�^���쐬����
		LayoutInflater layoutInflater = LayoutInflater.from(mService);
		// ���C�A�E�g�t�@�C������d�ˍ��킹����View���쐬����
		View mView = layoutInflater.inflate(com.abs104a.mperwithsideproject.R.layout.player_view, null);
		initButtonOfView(mService,mView);
		return mView;
	}
	
	/**
	 * �����������C��View
	 * @param mService
	 * @param mView
	 */
	public static void initButtonOfView(Service mService,View mView){
		//TODO View�̃{�^���ɓ��������
		Button handleButton = (Button)mView.findViewById(R.id.imageButton_handle);
		handleButton.setOnClickListener(new MainHandleActionOnClickListenerImpl(mService));
	}
	
}
