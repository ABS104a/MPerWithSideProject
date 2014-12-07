package com.abs104a.mperwithsideproject.music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * ���y�v���[���[�ɂ��킦�ăv���C���X�g�Đ�����������
 * ArrayList�^�̃v���C���X�g��ǂݍ��ނ��Ƃ�
 * 
 * �E���s�[�g�Đ��@1��or���X�g��
 * �E�V���b�t���Đ��@���X�g��
 * �E�O�̋Ȃւ̈ړ�
 * �E��̋Ȃւ̈ړ�
 * 
 * ��񋟂���D
 * 
 * @author Kouki
 *
 */
public class MusicPlayerWithPlayLists extends MusicPlayer {

	/**===================================
	 * �萔
	 ===================================*/
	
	//���[�v���L���ł͂Ȃ�
	public final static int NOT_LOOP = 0;
	//1�ȃ��[�v
	public final static int ONE_LOOP = 1;
	//�S�ȃ��[�v
	public final static int ALL_LOOP = 2;
	
	/**===================================
	 * �����ϐ�
	 ===================================*/
	
	//�Đ��Ȃ̃v���C���X�g
	private ArrayList<?> mPlayList = null;
	//�v���C���X�g�̍Đ��ԍ����Ǘ�����J�[�\��
	private int mCursor = 0;
	
	//���[�v��Ԃ��L������ϐ�
	private int loopState = NOT_LOOP;
	
	//�V���b�t���@�\���L�����ǂ����̃t���O
	private boolean isShuffle = false;
	
	
	/**===================================
	 * ���\�b�h
	 ===================================*/

	//===================================
	// �E �V���b�t���@�\�ɂ���
	// ==================================
	
	/**
	 * �V���b�t���@�\�L���t���O�̏�Ԃ��擾����
	 * @return�@�V���b�t���@�\���L�����ǂ���
	 */
	public final boolean isShuffle() {
		return isShuffle;
	}

	/**
	 * �V���b�t���@�\��ݒ肷��D
	 * @param isShuffle�@�V���b�t�����L�����ǂ���
	 */
	public final boolean setShuffle(boolean isShuffle) {
		return this.isShuffle = isShuffle;
	}
	
	//===================================
	// �E �V���b�t���@�\�ɂ���
	// ==================================
	
	/**
	 * ���[�v�̏�Ԃ��擾����
	 * @return ���[�v���
	 */
	public final int getLoopState() {
		return loopState;
	}

	/**
	 * ���[�v�̏�Ԃ�ݒ肷��
	 * �ݒ�O�̒l�͐ݒ肳��Ȃ�
	 * @param loopState ���[�v���
	 */
	public final int setLoopState(int loopState) {
		if(loopState == NOT_LOOP || 
				loopState == ALL_LOOP || loopState == ONE_LOOP)
			return this.loopState = loopState;
		else
			return this.loopState;
			
	}
	
	
	//===================================
	// �E�Đ��R���g���[���@�\
	// ==================================
	
	/**
	 * �v���C���X�g���Z�b�g����
	 * @param playList�@�Z�b�g����v���C���X�g
	 */
	public void setPlayList(ArrayList<?> playList){
		this.mPlayList = playList;
	}
	
	/**
	 * ���̋Ȃֈړ�����
	 * @return ���̋Ȃ̃J�[�\��
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public int playNext() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
		//TODO List�`������������
		//�V���b�t���@�\���L���̎�
		if(isShuffle()){
			//�����_���ɃJ�[�\���l��ݒ肷��D
			Random random = new Random();
			int oldCursor = mCursor;
			mCursor = random.nextInt(mPlayList.size()-1);
			//�O�̋ȂƓ����l�ɂȂ����ꍇ�͂��̎��̋�or�O�̋Ȃɂ���D
			if(oldCursor == mCursor)
				mCursor = mCursor == (mPlayList.size()-1) ? Math.max(0, --mCursor) : ++mCursor;
		}
		//�V���b�t���@�\�������̎�
		else{
			//�Đ����I�����邩�ǂ����ݒ肷��t���O
			boolean flag = false;
			//���[�v�̏��
			switch(loopState){
			case NOT_LOOP: 	//���[�v�����̎�
				//�I���t���O�𗧂Ă�D
				flag = true;
			case ALL_LOOP: 	//�S�ȃ��[�v�̎�
				++mCursor;	//�J�[�\����i�߂�
				if(mPlayList.size() == mCursor){
					//�v���C���X�g�̍Ō�܂ŗ����Ƃ�
					//�J�[�\����0�ɖ߂�
					mCursor = 0;
					//�S�ȃ��[�v�łȂ��Ƃ��͍Đ����I������D
					if(flag)return mCursor;
				}
			case ONE_LOOP:	//1�ȃ��[�v�̎�
			}
		}
		//���̋ȏ����擾
		String nextMusic = (String) mPlayList.get(mCursor);
		//�f�[�^���Z�b�g����
		setSource(nextMusic);
		//�Đ����J�n
		playStartAndPause();
		return mCursor;
		
	}
	
	/**
	 * �O�̋Ȃֈړ�����D
	 * 
	 * �Đ��b����1s�����ł���ΑO�̋�
	 * �Đ��b����1s�ȏ�ł���΋Ȃ̐擪�փV�[�N����
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public int playBack() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
		//TODO �����\��
		//���̋ȏ����擾
		if(--mCursor < 0){
			mCursor = 0;
		}
		String nextMusic = (String) mPlayList.get(mCursor);
		//�f�[�^���Z�b�g����
		setSource(nextMusic);
		//�Đ����J�n
		playStartAndPause();
		return mCursor;
		
	}




	
}


