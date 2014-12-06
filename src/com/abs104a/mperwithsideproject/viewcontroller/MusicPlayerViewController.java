package com.abs104a.mperwithsideproject.viewcontroller;


import java.io.IOException;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.MusicPlayer.OnPlayCompletedListener;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;
import com.abs104a.mperwithsideproject.music.listener.ExitActionOnClickListenerImpl;

import android.app.Service;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * �v���C���[���C��View�̐����Ɛݒ���s���N���X
 * 
 * 
 * @author Kouki
 *
 */
public class MusicPlayerViewController {

	/**
	 * Player��View�𐶐����郁�\�b�h
	 * �T�[�r�X�̃R���e�L�X�g���󂯂Ƃ�View�𐶐�����D
	 * @param mService
	 * @return�@��������ViewGroup
	 */
	public static View createView(Service mService){
		//TODO View�̐���
		// View����C���t���[�^���쐬����
		LayoutInflater layoutInflater = LayoutInflater.from(mService);
		// ���C�A�E�g�t�@�C������d�ˍ��킹����View���쐬����
		View mView = layoutInflater.inflate(com.abs104a.mperwithsideproject.R.layout.main_service_view, null);
		//Action Settings 
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
		Button exitButton = (Button)mView.findViewById(R.id.button_action_exit);
		exitButton.setOnClickListener(new ExitActionOnClickListenerImpl(mService));
		
		//�Đ��{�^���̐ݒ�
		Button playButton = (Button)mView.findViewById(R.id.button_play);
		//TODO�@�Đ��{�^���̓����o�^����D
		//ex.. playButton.setOnClickListener(hoge));
		
		//���ւ̃{�^���̐ݒ�
		Button afterButton = (Button)mView.findViewById(R.id.button_next_seek);
		//TODO ���փ{�^���̓����o�^����D
		
		Button beforeButton = (Button)mView.findViewById(R.id.button_back_seek);
		//TODO �O�փ{�^���̓����o�^����D
		
		Button repeatButton = (Button)mView.findViewById(R.id.button_repeat);
		//TODO ���s�[�g�{�^���̓����o�^����D
		
		Button shuffleButton = (Button)mView.findViewById(R.id.button_shuffle);
		//TODO �V���b�t���{�^���̓����o�^����D
		
		Button showListButton = (Button)mView.findViewById(R.id.button_action_show_list);
		//TODO ���X�g�\���{�^���̐ݒ��o�^����
		
		Button showSettigsButton = (Button)mView.findViewById(R.id.button_action_show_settings);
		//TODO �ݒ�\���{�^���̐ݒ��o�^����D
		
	}
	
	public static void initAction(Service mService,View mView){
		//�v���[���[�R���g���[���N���X�̃C���X�^���X���擾
		final MusicPlayerWithPlayLists mpwpl = new MusicPlayerWithPlayLists();
		//�Đ����I���������ɌĂ΂�郊�X�i����������D
		OnPlayCompletedListener mOnPlayCompletedListener = new OnPlayCompletedListener(){

			@Override
			public void onPlayCompleted() {
				//�Đ����I�������Ƃ� ���̋Ȃ��Z�b�g����D
				try {
					//���̋Ȃ��Đ�
					mpwpl.playNext();
				} catch (IllegalArgumentException e) {
					// TODO �����������ꂽ catch �u���b�N
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO �����������ꂽ catch �u���b�N
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO �����������ꂽ catch �u���b�N
					e.printStackTrace();
				} catch (IOException e) {
					// TODO �����������ꂽ catch �u���b�N
					e.printStackTrace();
				}
			}
			
		};
		//�Đ������������Ƃ��̃��X�i���Z�b�g�D
		mpwpl.setOnPlayCompletedListener(mOnPlayCompletedListener);
		//TODO �v���C���X�g��ݒ�
		
	}
	
}
