package com.abs104a.mperwithsideproject.viewctl;


import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;
import com.abs104a.mperwithsideproject.music.listener.ExitActionOnClickListenerImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.BackButtonOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.NextButtonOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.OnPlayCompletedImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.PlayButtonOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.RepeatButtonOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.ShuffleButtonOnClickImpl;

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

	//���y���\�[�X�̃R���g���[���N���X�̃C���X�^���X
	private final static MusicPlayerWithPlayLists _mpwpl = new MusicPlayerWithPlayLists();
	
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
		//�Đ��{�^���̓����o�^����D
		playButton.setOnClickListener(new PlayButtonOnClickImpl(_mpwpl));
		
		//���ւ̃{�^���̐ݒ�
		Button nextButton = (Button)mView.findViewById(R.id.button_next_seek);
		//���փ{�^���̓����o�^����D
		nextButton.setOnClickListener(new NextButtonOnClickImpl(mService,_mpwpl));
		
		Button backButton = (Button)mView.findViewById(R.id.button_back_seek);
		//�O�փ{�^���̓����o�^����D
		backButton.setOnClickListener(new BackButtonOnClickImpl(mService,_mpwpl));
		
		Button repeatButton = (Button)mView.findViewById(R.id.button_repeat);
		//���s�[�g�{�^���̓����o�^����D
		repeatButton.setOnClickListener(new RepeatButtonOnClickImpl(repeatButton,_mpwpl));
		
		Button shuffleButton = (Button)mView.findViewById(R.id.button_shuffle);
		//�V���b�t���{�^���̓����o�^����D
		shuffleButton.setOnClickListener(new ShuffleButtonOnClickImpl(shuffleButton,_mpwpl));
		
		Button showListButton = (Button)mView.findViewById(R.id.button_action_show_list);
		//TODO ���X�g�\���{�^���̐ݒ��o�^����
		
		Button showSettigsButton = (Button)mView.findViewById(R.id.button_action_show_settings);
		//TODO �ݒ�\���{�^���̐ݒ��o�^����D
		
	}
	
	public static void initAction(Service mService,View mView){
		//�Đ����I���������ɌĂ΂�郊�X�i����������D
		//�Đ������������Ƃ��̃��X�i���Z�b�g�D
		_mpwpl.setOnPlayCompletedListener(new OnPlayCompletedImpl(_mpwpl));
		//TODO �v���C���X�g��ݒ�
		
	}
	
}
