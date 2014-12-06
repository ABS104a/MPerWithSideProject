package com.abs104a.mperwithsideproject.music;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

/**
 * �Đ����s�����y���\�[�X�������N���X�D
 * MediaPlayer�N���X�̃��b�p
 * @author Kouki-Mobile
 *
 */
public class MusicPlayer implements OnCompletionListener {
	
	/**===================================
	 * �萔
	 ===================================*/
	
	//�f�[�^�\�[�X�����݂��Ȃ��Ƃ�
	public final static int NOSOURCE = -1;
	
	//�Đ���
	public final static int PLAYING = 2;
	
	//�Đ����Ă��Ȃ��i�\�[�X�ǂݍ��݊������j
	public final static int NOTPLAYING = 5;
	//�|�[�Y��
	public final static int PAUSEING = 6;
	//��~��
	public final static int STOPPING = 7;
	
	
	/**===================================
	 * �����ϐ�
	 ===================================*/
	
	//�Đ����s��MediaPlayer�N���X
	protected MediaPlayer mMediaPlayer = null;
	//���ݍĐ����Ă��邩�ǂ���
	private int _status = NOSOURCE;
	
	/**===================================
	 * Impl
	 ===================================*/
	
	/**
	 * �Đ��������������ɌĂ΂�郊�X�iImpl
	 * @author Kouki
	 *
	 */
	public interface OnPlayCompletedListener{
		/**
		 * �Đ�������������
		 */
		public void onPlayCompleted();
	}
	
	/**
	 * �Đ��������������ɌĂ΂�郊�X�i
	 */
	private OnPlayCompletedListener mOnPlayCompletedListener = null;
	
	/**
	 * �Đ��������ɌĂ΂�郊�X�i���擾���郁�\�b�h
	 * @return
	 */
	public OnPlayCompletedListener getOnPlayCompletedListener() {
		return mOnPlayCompletedListener;
	}

	/**
	 * �Đ��������ɌĂ΂�郊�X�i��ݒ肷�郁�\�b�h
	 * @param mOnPlayCompletedListener
	 */
	public void setOnPlayCompletedListener(OnPlayCompletedListener mOnPlayCompletedListener) {
		this.mOnPlayCompletedListener = mOnPlayCompletedListener;
	}
	
	
	/**===================================
	 * �R���g���[�����\�b�h
	 ===================================*/
	
	
	/**
	 * �Đ��󋵂�ݒ肷��D
	 * @param status
	 * @return
	 */
	protected int setStatus(int status){
		switch(status){
		case NOSOURCE:
		case PLAYING:
		case NOTPLAYING:
		case PAUSEING:
		case STOPPING:
			this._status = status;
		default:
		}
		return this._status;
	}
	
	/**
	 * �Đ��󋵂��擾����
	 * @return
	 */
	public int getStatus(){
		return _status;
	}
	
	/**
	 * ���[�v��Ԃ�ݒ肷��D
	 * @param isLoop ���[�v���邩�ǂ���
	 * @return ���[�v����Ă��邩�DMediaPlayer��Null�̏ꍇ��null��Ԃ��D
	 */
	public Boolean setLoop(boolean isLoop){
		if(mMediaPlayer != null){
			mMediaPlayer.setLooping(isLoop);
			return mMediaPlayer.isLooping();
		}
		return null;
	}
	
	/**
	 * ���[�v�󋵂��m�F����D
	 * @return�@���[�v����Ă��邩�DMediaPlayer��Null�̏ꍇ��null��Ԃ��D
	 */
	public Boolean isLoop(){
		if(mMediaPlayer != null)
			return mMediaPlayer.isLooping();
		else
			return null;
	}
	
	/**
	 * �f�[�^�\�[�X��ݒ肷��
	 * 
	 * @param pass�@���y�t�@�C����URI�@String
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws IOException
	 * @return �Đ���ԁi����o�����STOPPING���Ԃ�j
	 */
	public final int setSource(String pass) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
		//MediaPlayer�̃C���X�^���X���擾
		if(mMediaPlayer == null)
			mMediaPlayer =  new MediaPlayer();
		//�f�[�^�\�[�X�̐ݒ�
		mMediaPlayer.setDataSource(pass);
		//�f�[�^�ǂݍ���
		mMediaPlayer.prepare();
		//�󋵂̔��f
		setStatus(STOPPING);
		return getStatus();
	}
	
	/**
	 * �Đ����鎞�̓���
	 * @return�@���݂̏�
	 */
	public final int playStartAndPause(){
		if(mMediaPlayer != null){
			//�Đ����Ă��Ȃ��ꍇ�@�iNOTPLAYING or PAUSEING or STOPPING�j
			if(getStatus() >= NOTPLAYING){
				//�Đ�����
				mMediaPlayer.start();
				mMediaPlayer.setOnCompletionListener(this);
				setStatus(PLAYING);
			}
			//�Đ����̏ꍇ�@PLAYING
			else if(getStatus() == PLAYING){
				//�|�[�Y��Ԃɂ���
				mMediaPlayer.pause();
				setStatus(PAUSEING);
			}
		}
		return getStatus();
	}
	
	/**
	 * �X�g�b�v���鎞�̓���
	 * @return�@���݂̏�
	 */
	public final int playStop(){
		//�Đ����@or�@�|�[�Y���̎��@�iPLAYING or PAUSEING�j
		if(mMediaPlayer != null && 
				(getStatus() == PLAYING || getStatus() == PAUSEING)){
			//�Đ����~����D
			mMediaPlayer.stop();
			setStatus(STOPPING);
		}
		return getStatus();
	}

	/**
	 * �Đ�������������
	 */
	@Override
	public void onCompletion(MediaPlayer mp) {
		setStatus(STOPPING);
		if(mOnPlayCompletedListener != null)
			mOnPlayCompletedListener.onPlayCompleted();
		
	}


	
}
