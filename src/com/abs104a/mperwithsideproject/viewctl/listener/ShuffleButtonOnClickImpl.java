package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * �V���b�t���{�^���������ꂽ���ɌĂ΂�郊�X�iImpl
 * @author Kouki-Mobile
 *
 */
public final class ShuffleButtonOnClickImpl implements OnClickListener {

	//�V���b�t���{�^����View
	private final Button _button;
	//�~���[�W�b�N�R���g���[���N���X�̃C���X�^���X
	private final MusicPlayerWithPlayLists _mpwpl;

	/**
	 * �C���X�^���X�̐���
	 * @param shuffleButton�@�V���b�t���{�^����View
	 * @param mpwpl�@�~���[�W�b�N�N���X�̃C���X�^���X
	 */
	public ShuffleButtonOnClickImpl(Button shuffleButton,
			MusicPlayerWithPlayLists mpwpl) {
		_button = shuffleButton;
		_mpwpl = mpwpl;
	}

	/**
	 * �N���b�N���ꂽ��
	 */
	@Override
	public void onClick(View v) {
		boolean isShuffle = _mpwpl.setShuffle(!_mpwpl.isShuffle());
		if(isShuffle){
			//�V���b�t����ON�̎�
			//View�ւ̔��f
		}else{
			//�V���b�t����OFF�̎�
		}
	}

}
