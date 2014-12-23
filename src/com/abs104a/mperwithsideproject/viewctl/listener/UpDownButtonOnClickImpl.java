package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.adapter.MusicListAdapter;
import com.abs104a.mperwithsideproject.adapter.PlayListForExpandableListAdapter;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.MusicUtils;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

public final class UpDownButtonOnClickImpl implements OnClickListener {

	private final Object adapter;
	private final boolean isDown;
	private final boolean isUp;
	private final Context mContext;
	private final Music item;

	public UpDownButtonOnClickImpl(Context mContext,Music item,boolean isUp,boolean isDown,Object adapter){
		this.isUp = isUp;
		this.isDown = isDown;
		this.adapter = adapter;
		this.mContext = mContext;
		this.item = item;
	}
	
	@Override
	public void onClick(View v) {
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(mContext);
		
		if(isUp){
			//上に移動する場合
			mpwpl.upIndexOfQueue(item);
		}else if(isDown){
			//下に移動する場合
			mpwpl.downIndexOfQueue(item);
		}
		if(adapter != null){
			if(adapter instanceof MusicListAdapter){
				((MusicListAdapter)adapter).notifyDataSetChanged();
			}else if(adapter instanceof PlayListForExpandableListAdapter){
				((PlayListForExpandableListAdapter)adapter).notifyDataSetChanged();
			}
		}
	}

}
