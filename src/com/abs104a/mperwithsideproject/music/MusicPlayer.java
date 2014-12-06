package com.abs104a.mperwithsideproject.music;

import java.io.IOException;

import android.media.MediaPlayer;

/**
 * �Đ����s�����y���\�[�X�������N���X�D
 * MediaPlayer�N���X�̃��b�p
 * @author Kouki-Mobile
 *
 */
public final class MusicPlayer {
	
	//�Đ����s��MediaPlayer�N���X
	private MediaPlayer mMediaPlayer = null;
	//���ݍĐ����Ă��邩�ǂ���
	private boolean isPlaying = false;
	
	public final void setSource(String pass) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
		mMediaPlayer =  new MediaPlayer();
		mMediaPlayer.setDataSource(pass);
		mMediaPlayer.prepare();
	}
	
}
