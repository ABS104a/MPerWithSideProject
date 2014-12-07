package com.abs104a.mperwithsideproject.viewctl.listener;

import java.io.IOException;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;

import android.app.Service;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * ���փ{�^�������������ɌĂ΂�郊�X�iImpl
 * @author Kouki-Mobile
 *
 */
public final class NextButtonOnClickImpl implements OnClickListener {

	//�~���[�W�b�N�R���g���[���C���X�^���X
	private final MusicPlayerWithPlayLists _mpwpl;
	//�T�[�r�X�̃R���e�L�X�g�i��O���̃��b�Z�[�W�����Ȃ�
	private final Service _service;

	/**
	 * �C���X�^���X�̐���
	 * @param _service�@�T�[�r�X�̃R���e�L�X�g
	 * @param mpwpl�@�~���[�W�b�N�R���g���[���N���X
	 */
	public NextButtonOnClickImpl(Service _service,MusicPlayerWithPlayLists mpwpl) {
		this._mpwpl = mpwpl;
		this._service = _service;
	}

	@Override
	public void onClick(View v) {
		try {
			//���ւ̍Đ����s��s
			_mpwpl.playNext();
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

}
