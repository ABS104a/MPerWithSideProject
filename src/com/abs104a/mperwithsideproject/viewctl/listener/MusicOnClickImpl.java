package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicViewPagerAdapter;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.viewctl.MusicPlayerViewController;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * ListView上のMusicItemが選択されたときの動作
 * @author Kouki
 *
 */
public final class MusicOnClickImpl implements OnClickListener {

	//ミュージックコントロールクラス
	private final MusicPlayerWithQueue mpwpl;
	//サービスのコンテキスト
	private final Context context;
	//音楽リスト
	private final Music music;
	//RootView
	private final View rootView;
	//タップした時にキューに追加するかどうか
	private final boolean isAddQueue;

	/**
	 * インスタンスの生成
	 * @param context	アプリケーションのコンテキスト
	 * @param rootView	ルートのView
	 * @param item		対象となるMusicインスタンス
	 * @param mpwpl		ミュージックコントロールインスタンス
	 * @param isAddQueue	タップされたときにキューに登録するかどうか
	 */
	public MusicOnClickImpl(Context context, View rootView, Music item,
			MusicPlayerWithQueue mpwpl, boolean isAddQueue) {
		this.context = context;
		this.music = item;
		this.mpwpl = mpwpl;
		this.isAddQueue = isAddQueue;
		this.rootView = rootView;
	}

	@Override
	public void onClick(View view) {
		view.setSelected(true);
		if(music != null){
			//TODO
			try {
				int index = 0;
				if(isAddQueue){
					//キューに追加する時
					mpwpl.addMusic(music, index);
				}else{
					index = mpwpl.getQueue().indexOf(music);
					//曲がQueueにないときだけ追加する．
					if(index == -1){
						mpwpl.addMusic(music, index);
					}
				}
				mpwpl.seekQueue(index);
				mpwpl.playStartAndPause();
				MusicPlayerViewController.setPartOfPlayerView(context, rootView, music, mpwpl);
				//ViewPager の設定
				ViewPager mViewPager = (ViewPager)rootView.findViewById(R.id.player_list_part);
				//Viewへの反映
				((MusicViewPagerAdapter)mViewPager.getAdapter()).notifitionDataSetChagedForQueueView();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
