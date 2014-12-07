package com.abs104a.mperwithsideproject.viewctl.listener;

import java.io.IOException;

import com.abs104a.mperwithsideproject.music.MusicPlayer.OnPlayCompletedListener;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;

/**
 * �Đ��������������ɌĂ΂�郊�X�iImpl
 * @author Kouki-Mobile
 *
 */
public final class OnPlayCompletedImpl implements OnPlayCompletedListener{

	//�~���[�W�b�N�R���g���[���N���X�̃C���X�^���X
	private final MusicPlayerWithPlayLists _mpwpl;

	/**
	 * �C���X�^���X�̍쐬
	 * @param mpwpl�@�~���[�W�b�N�R���g���[���N���X�̃C���X�^���X�D
	 */
	public OnPlayCompletedImpl(MusicPlayerWithPlayLists mpwpl) {
		this._mpwpl = mpwpl;
	}

	/**
	 * �Đ�������������
	 */
	@Override
	public void onPlayCompleted() {
		//�Đ����I�������Ƃ� ���̋Ȃ��Z�b�g����D
		try {
			//���̋Ȃ��Đ�
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
