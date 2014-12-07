package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * �Đ��{�^�������������ɌĂ΂�郊�X�iImpl
 * @author Kouki-Mobile
 *
 */
public final class PlayButtonOnClickImpl implements OnClickListener {

	//�v���C���[�R���g���[���C���X�^���X
	private final MusicPlayerWithPlayLists _mpwpl;

	public PlayButtonOnClickImpl(MusicPlayerWithPlayLists mpwpl) {
		this._mpwpl = mpwpl;
	}

	@Override
	public void onClick(View v) {
		//�Đ�������s��
		_mpwpl.playStartAndPause();
	}

}
