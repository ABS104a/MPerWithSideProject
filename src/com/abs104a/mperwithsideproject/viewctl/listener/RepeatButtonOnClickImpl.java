package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * ���s�[�g�{�^�������������ɌĂ΂�郊�X�iImpl
 * @author Kouki-Mobile
 *
 */
public final class RepeatButtonOnClickImpl implements OnClickListener {

	private final Button _button;
	private final MusicPlayerWithPlayLists _mpwpl;

	/**
	 * �C���X�^���X�̐���
	 * @param mView ���ʔ��f�pView
	 * @param mpwpl ���y�R���g���[���N���X
	 */
	public RepeatButtonOnClickImpl(Button button, MusicPlayerWithPlayLists mpwpl) {
		this._button = button;
		this._mpwpl = mpwpl;
	}

	/**
	 * �^�b�v���ꂽ��
	 * �i1�ȃ��[�v�j�����[�v�Ȃ����S�ȃ��[�v��1�ȃ��[�v
	 */
	@Override
	public void onClick(View v) {
		//���݂̏�Ԃ��擾����
		int loopState = _mpwpl.getLoopState();
		//LOOP���ĂȂ���
		if(loopState == MusicPlayerWithPlayLists.NOT_LOOP){
			loopState = MusicPlayerWithPlayLists.ALL_LOOP;
		}
		//�S��LOOP�̎�
		else if(loopState == MusicPlayerWithPlayLists.ALL_LOOP){
			loopState = MusicPlayerWithPlayLists.ONE_LOOP;
		}
		//1�ȃ��[�v�̎�
		else if(loopState == MusicPlayerWithPlayLists.ONE_LOOP){
			loopState = MusicPlayerWithPlayLists.NOT_LOOP;
		}
		//LOOP��Ԃ̎擾
		loopState = _mpwpl.setLoopState(loopState);

		//TODO View�ւ̔��f
		
		//_button.setBackground(hoge);
		
	}

}
