package com.abs104a.mperwithsideproject.adapter;

import java.util.ArrayList;
import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.DialogUtils;
import com.abs104a.mperwithsideproject.utl.ItemViewFactory;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * アルバムのリスト管理をするAdapter
 * @author Kouki
 *
 */
public final class MusicListAdapter extends ArrayAdapter<Music> {

	//変数////////////////////////////////////////////
	
	//ミュージックプレイヤーコントロールインスタンス
	private final MusicPlayerWithQueue mpwpl;
	//RootView
	private final View rootView;
	//自身のインスタンス．
	private ListAdapter adapter = this;
	//参照されているcolumn
	private final int column;

	public MusicListAdapter(
			Context context,
			View rootView, ArrayList<Music> items,
			int column,
			MusicPlayerWithQueue mpwpl) 
	{
		super(context, R.layout.album_item_row, items);
		this.mpwpl = mpwpl;
		this.rootView = rootView;
		this.column = column;
	}

	/**
	 * Viewの生成
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try{
			//ItemのViewを生成する．
			final Music item = getItem(position);
			return ItemViewFactory.getChildView(convertView, item, getContext(), column, rootView, adapter, mpwpl);
		}catch(IndexOutOfBoundsException e){
			//Itemが存在しないときはEmptyViewを表示する．
			if(position == 0)
				return createEmptyView();
			else
				return createFooterView();
		}
	}
	
	
	
	
	/* (非 Javadoc)
	 * @see android.widget.ArrayAdapter#getCount()
	 */
	@Override
	public int getCount() {
		//EmptyViewがあるため最低1つのItemは存在することにする．
		return Math.max(1, super.getCount() + 1);
	}
	
	/**
	 * EmptyViewを生成する．
	 * @return　生成したView
	 */
	private View createEmptyView(){
		//Viewの高さを取得する．
		final int viewHeight = 
				getContext()
				.getResources()
				.getDimensionPixelSize(R.dimen.album_item_height);
		
		//ParentView用のLayoutParams
		ListView.LayoutParams mParentParams = 
				new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,ListView.LayoutParams.WRAP_CONTENT);
		
		//ChildView用のLayoutParams
		LinearLayout.LayoutParams mChildParams = 
				new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, viewHeight);
		mChildParams.gravity = Gravity.CENTER;
		
		//生成するParentView（LinearLayout）
		LinearLayout ll = new LinearLayout(getContext());
		ll.setLayoutParams(mParentParams);

		//生成するChildView（TextView）
		TextView emptyView = new TextView(getContext());
		emptyView.setLayoutParams(mChildParams);
		emptyView.setGravity(Gravity.CENTER);
		emptyView.setBackgroundResource(R.drawable.button);
		emptyView.setText(R.string.row_empty);
		
		//ParentViewにChildViewを追加
		ll.addView(emptyView);
		
		return ll;//ReturnParentView
	}
	
	private View createFooterView(){
		//Viewの高さを取得する．
		final int viewHeight = 
				getContext()
				.getResources()
				.getDimensionPixelSize(R.dimen.album_item_height);
		
		//ParentView用のLayoutParams
		ListView.LayoutParams mParentParams = 
				new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,ListView.LayoutParams.WRAP_CONTENT);

		//ChildView用のLayoutParams
		LinearLayout.LayoutParams mChildParams = 
				new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, viewHeight);
		mChildParams.gravity = Gravity.CENTER;

		//生成するParentView（LinearLayout）
		LinearLayout ll = new LinearLayout(getContext());
		ll.setLayoutParams(mParentParams);
		//Footerの設定
		TextView footerView = new TextView(getContext());
		footerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
		footerView.setGravity(Gravity.CENTER);
		footerView.setBackgroundResource(R.drawable.button);
		footerView.setText(R.string.playlist_to_queue);
		
		footerView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				ArrayList<Music> queue = mpwpl.getQueue();
				new DialogUtils().createIfSelectPlayListDialog(getContext(),queue.toArray(new Music[queue.size()]),column);
			}
			
		});
		//ParentViewにChildViewを追加
		ll.addView(footerView);
				
		return ll;
	}


	


}
