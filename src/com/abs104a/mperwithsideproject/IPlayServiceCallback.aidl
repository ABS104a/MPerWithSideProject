package com.abs104a.mperwithsideproject;

import com.abs104a.mperwithsideproject.music.Music;

oneway interface IPlayServiceCallback{

	/**
	 * 再生曲が変更された時
	 */
	void onMusicChanged(inout Music nextMusic);
}