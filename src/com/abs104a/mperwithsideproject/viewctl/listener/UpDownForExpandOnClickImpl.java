package com.abs104a.mperwithsideproject.viewctl.listener;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.adapter.MusicListAdapter;
import com.abs104a.mperwithsideproject.adapter.PlayListForExpandableListAdapter;
import com.abs104a.mperwithsideproject.music.PlayList;
import android.view.View;
import android.view.View.OnClickListener;

public class UpDownForExpandOnClickImpl implements OnClickListener {

	
	private final Object adapter;
	private final boolean isDown;
	private final boolean isUp;
	private final ArrayList<PlayList> playLists;
	private final int groupPos;
	private final int childPos;

	public UpDownForExpandOnClickImpl(ArrayList<PlayList> playLists,int groupPos,int childPos,boolean isUp,boolean isDown,Object adapter){
		this.isUp = isUp;
		this.isDown = isDown;
		this.adapter = adapter;
		this.playLists = playLists;
		this.groupPos = groupPos;
		this.childPos = childPos;
	}
	
	@Override
	public void onClick(View v) {
		PlayList pList = playLists.get(groupPos);
		if(isUp){
			//上に移動する場合
			pList.swapMusic(Math.max(0, childPos - 1), childPos);
		}else if(isDown){
			//下に移動する場合
			pList.swapMusic(Math.min(pList.getMusics().length - 1, childPos + 1), childPos);
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
