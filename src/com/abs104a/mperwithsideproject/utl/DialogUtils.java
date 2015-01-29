package com.abs104a.mperwithsideproject.utl;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.Column;
import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicViewPagerAdapter;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.music.PlayList;
import com.abs104a.mperwithsideproject.viewctl.MusicViewCtl;
import android.content.Context;
import android.graphics.PixelFormat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * アプリケーションに関するダイアログを表示するためのUtilクラス
 * @author Kouki
 *
 */
public class DialogUtils {
	
	//アプリケーションのタグ
	public static final String TAG = "DISPLAY_UTILS";
	
	/**
	 * WindowManagerの生成と配置を行う
	 * @param mContext	アプリケーションのコンテキスト
	 * @param setView	セットするためのView
	 * @return			作成したWindowManager
	 */
	public final WindowManager setWindowManager(Context mContext,View setView){
		final WindowManager mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		
		// 重ね合わせするViewの設定を行う
		LayoutParams params = new WindowManager.LayoutParams(
				mContext.getResources().getDimensionPixelSize(R.dimen.dialog_view_width),
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				//WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
				WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED ,
				PixelFormat.TRANSLUCENT);
		
		params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
		params.x = mContext.getResources().getDimensionPixelSize(R.dimen.dialog_view_margin);
		
		LinearLayout layout = (LinearLayout) setView.findViewById(R.id.linearLayout_dialog);
		Animation anim = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
		layout.startAnimation(anim);
		
		//Viewの追加
		mWindowManager.addView(setView, params);
		
		return mWindowManager;
	}
	
	/**
	 * 生成したDialog（WindowManager）を消去する．
	 * @param mWindowManager	対象となるWindowManager
	 * @param mView				生成したView
	 */
	private static void removeForWindowManager(final WindowManager mWindowManager,final View mView){
		try{
			LinearLayout layout = (LinearLayout) mView.findViewById(R.id.linearLayout_dialog);
			Animation anim = AnimationUtils.loadAnimation(mView.getContext(), android.R.anim.slide_out_right);
			anim.setAnimationListener(new AnimationListener(){

				@Override
				public void onAnimationStart(Animation animation) {}

				@Override
				public void onAnimationEnd(Animation animation) {
					mWindowManager.removeView(mView);//Viewの消去
				}

				@Override
				public void onAnimationRepeat(Animation animation) {}

			});
			layout.startAnimation(anim);
		}catch(NullPointerException e){
			e.printStackTrace();
		}

	}
	
	/**
	 * PlayListを選択するDialogを表示する．
	 * (最初の項目はQueue，3番目～がPlayList)
	 * （2番目の項目はPlayListを作成する．）
	 * @param mContext
	 * @param music 対象とするMusicInstance
	 * @param column  キューへ追加するItemを設定するかどうか
	 * @param expandposition 
	 */
	public final void createIfSelectPlayListDialog(Context mContext,final Music music,final int column ){
		Music[] musics = null;
		if(music != null){
			musics = new Music[1];
			musics[0] = music;
		}
		createIfSelectPlayListDialog( mContext,  musics,  column );
	}
	
	/**
	 * PlayListを選択するDialogを表示する．
	 * (最初の項目はQueue，3番目～がPlayList)
	 * （2番目の項目はPlayListを作成する．）
	 * @param mContext
	 * @param music 対象とするMusicInstance
	 * @param column  キューへ追加するItemを設定するかどうか
	 * @param expandposition 
	 */
	public final void createIfSelectPlayListDialog(Context mContext,final Music music[],final int column ){
			
		//MainViewの生成
		LayoutInflater inflater = LayoutInflater.from( mContext );
		final ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.dialog, (ViewGroup)MusicViewCtl.getPlayerView(),false);
		
		//WindowManager~の起動にする．
		final WindowManager mWindowManager = setWindowManager(mContext, mView);

		//ダイアログに挿入するListView
		final ListView mListView = new ListView(mContext);
		
		final FrameLayout mLayout = (FrameLayout)mView.findViewById(R.id.frameLayout_dialog);
		mLayout.addView(mListView);
		
		final TextView titleView = (TextView)mView.findViewById(R.id.textView_dialog_title);
		titleView.setText(R.string.add_to_playlist);
		
		final Button positiveButton = (Button)mView.findViewById(R.id.button_dialog_positive);
		positiveButton.setVisibility(View.GONE);
		
		final Button negativeButton = (Button)mView.findViewById(R.id.button_dialog_negative);
		negativeButton.setText(R.string.cancel);
		negativeButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//Viewの消去
				removeForWindowManager(mWindowManager,mView);
			}
			
		});
		
		//表示するPlayList
		ArrayList<PlayList> mPlayLists = PlayList.getPlayList(mContext);
		
		//ListViewへ適用させるAdapter
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1);
		
		//Queueへの追加をするItemの追加
		if(column != Column.QUEUE)
			adapter.add(mContext.getString(R.string.add_queue));
		//PlayListを作成するItemの追加
		adapter.add(mContext.getString(R.string.create_playlist));
		
		//データを格納する．
		try{
			for(PlayList pl :mPlayLists)
				adapter.add(pl.getAlbum());
		}catch(NullPointerException e){
			android.util.Log.e(TAG,"NotFoundPlayLists");
			android.util.Log.e(TAG,e.getMessage());
		}
		//Adapterへのセット
		mListView.setAdapter(adapter);
		
		//dividerの高さを設定
		mListView.setDividerHeight(
				mContext.getResources()
				.getDimensionPixelSize(R.dimen.listview_divider));
		
		//ListViewのItemを選択したときの動作を設定する．
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ArrayList<PlayList> mPlayLists = PlayList.getPlayList(view.getContext());
				//Viewの消去
				removeForWindowManager(mWindowManager,mView);
				
				//Queueへの追加Itemを追加しない場合はPositionを1つ増やす
				if(column == Column.QUEUE )position += 1;
				
				//MusicControllerInstanceを取得
				final MusicPlayerWithQueue mpwpl 
				= MusicUtils.getMusicController(view.getContext());
				
				//Itemが選択された時の動作
				if(position == 0){
					//Queueへの追加を行う
					ArrayList<Music> musicList = new ArrayList<Music>();
					for(Music mu : music){
						musicList.add(mu);
					}
					try {
						mpwpl.addPlayList(musicList);
						ItemViewFactory.clearExpandPosition();
						Toast.makeText(
								view.getContext(), 
								musicList.get(0).getTitle() + "... " + 
								view.getContext().getString(R.string.add_to_queue),
								Toast.LENGTH_SHORT)
								.show();
						if(MusicViewCtl.getPlayerView() != null){
							//ViewPagerの更新を行う
							ViewPager v = (ViewPager) MusicViewCtl.getPlayerView().findViewById(R.id.player_list_part);
							((MusicViewPagerAdapter)v.getAdapter()).notifitionDataSetChagedForQueueView();
						}
					}catch (Exception e) {
						e.printStackTrace();
					}

				}else if(position == 1){
					//PlayListの作成
					createPlayListDialog(view.getContext(),music, mPlayLists);
				}else{
					//既存のプレイリストへの追加
					//追加するプレイリストのインデックス．
					final int index = Math.max(0, position - 2);
					try{
						//現在のプレイリスト曲を読み込む
						Music[] musics = mPlayLists.get(index).getMusics();
						//新しいプレイリスト曲の配列を生成する．
						ArrayList<Music> newMusics = new ArrayList<Music>();
						for(Music mu : musics)newMusics.add(mu);

						//最後の要素に追加する．
						for(Music mu :music){
							if(newMusics.indexOf(mu) == -1){
								newMusics.add(mu);
							}else{
								newMusics.remove(mu);
								newMusics.add(mu);
							}
						}
						
						//新しい配列をセットする．
						mPlayLists.get(index).setMusics(newMusics.toArray(new Music[newMusics.size()]));
						//データを保存する．
						PlayList.writePlayList(view.getContext());
						ItemViewFactory.clearExpandPosition();
						Toast.makeText(
								view.getContext(), 
								music[0].getTitle() + " → " + 
								mPlayLists.get(index).getAlbum(),
								Toast.LENGTH_SHORT)
								.show();
						if(MusicViewCtl.getPlayerView() != null){
							//ViewPagerの更新を行う
							ViewPager v = (ViewPager) MusicViewCtl.getPlayerView().findViewById(R.id.player_list_part);
							((MusicViewPagerAdapter)v.getAdapter()).notifitionDataSetChagedForQueueView();
						}
					}catch(NullPointerException e){
						android.util.Log.e(TAG,"NotFoundPlayLists");
						android.util.Log.e(TAG,e.getMessage());
					}
					
				}
			}
			
		});
	}
	
	/**
	 * プレイリストを新たに作成する時に表示するダイアログを設定する．
	 * @param expandposition 
	 */
	public final void createPlayListDialog(
			Context mContext,
			final Music music[],
			final ArrayList<PlayList> mPlayLists)
	{
		//MainViewの生成
		LayoutInflater inflater = LayoutInflater.from( mContext );
		final ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.dialog, (ViewGroup)MusicViewCtl.getPlayerView(),false);
		
		//WindowManager~の起動にする．
		final WindowManager mWindowManager = setWindowManager(mContext, mView);
		
		//Titleの設定
		final TextView titleView = (TextView)mView.findViewById(R.id.textView_dialog_title);
		titleView.setText(R.string.create_playlist_title);

		//プレイリスト名を入力するEditTextを生成
		final EditText mEditText = new EditText(mContext);
		mEditText.setHint(R.string.create_playlist_text_hint);
		mEditText.requestFocus();
		
		DisplayUtils.showInputMethodEditor(mContext, mView);
		
		//Viewをセットする．
		FrameLayout contentLayout = (FrameLayout)mView.findViewById(R.id.frameLayout_dialog);
		contentLayout.addView(mEditText);
		
		//DialogのOKボタンを押したときの設定
		final Button positiveButton = (Button)mView.findViewById(R.id.button_dialog_positive);
		positiveButton.setText(R.string.ok);
		positiveButton.setOnClickListener(new OnClickListener(){
			
			/**
			 * 新しいプレイリストを作成する．
			 */
			@Override
			public void onClick(View view) {
				String title = mEditText.getText().toString();
				//文字が入力されていた時
				if(title != null && title.length() > 0){
					// プレイリストを生成して曲を追加する．
					final PlayList newPlayList = 
							new PlayList(title, "PlayList", System.currentTimeMillis(), null);
					
					if(music == null){
						final Music[] musics = new Music[0];
						//Musicをセット
						newPlayList.setMusics(musics);
						mPlayLists.add(newPlayList);
					}else{
						final Music[] musics = music;
						//Musicをセット
						newPlayList.setMusics(musics);
						mPlayLists.add(newPlayList);
					}
					//ViewPagerForPlayListViewCtl.writePlayList(view.getContext());
					ItemViewFactory.clearExpandPosition();
					Toast.makeText(
							view.getContext(), 
							"PlayList \"" + newPlayList.getAlbum() + "\" created!", 
							Toast.LENGTH_SHORT)
							.show();
					//Viewの消去
					if(MusicViewCtl.getPlayerView() != null){
						//ViewPagerの更新を行う
						ViewPager v = (ViewPager) MusicViewCtl.getPlayerView().findViewById(R.id.player_list_part);
						((MusicViewPagerAdapter)v.getAdapter()).notifitionDataSetChagedForQueueView();
					}
					removeForWindowManager(mWindowManager,mView);
					DisplayUtils.hideInputMethodEditor(view.getContext(), mView);
				}
			}
		});
		
		//DialogのCancelボタンを押したときの動作
		final Button negativeButton = (Button)mView.findViewById(R.id.button_dialog_negative);
		negativeButton.setText(R.string.cancel);
		negativeButton.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View view) {
				// Dialogを閉じる．
				//Viewの消去
				removeForWindowManager(mWindowManager,mView);
				DisplayUtils.hideInputMethodEditor(view.getContext(), mView);
			}
		});
	}
	
	/**
	 * プレイリストを新たに作成する時に表示するダイアログを設定する．
	 * @param expandposition 
	 */
	/*
	public final void createPlayListWithQueueDialog(
			Context mContext)
	{
		//MainViewの生成
		LayoutInflater inflater = LayoutInflater.from( mContext );
		final ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.dialog, (ViewGroup)MusicViewCtl.getPlayerView(),false);
		
		//WindowManager~の起動にする．
		final WindowManager mWindowManager = setWindowManager(mContext, mView);
		
		//Titleの設定
		final TextView titleView = (TextView)mView.findViewById(R.id.textView_dialog_title);
		titleView.setText(R.string.create_playlist_with_queue_title);

		//プレイリスト名を入力するEditTextを生成
		final EditText mEditText = new EditText(mContext);
		mEditText.setHint(R.string.create_playlist_text_hint);
		mEditText.requestFocus();
		
		DisplayUtils.showInputMethodEditor(mContext, mView);
		
		//Viewをセットする．
		FrameLayout contentLayout = (FrameLayout)mView.findViewById(R.id.frameLayout_dialog);
		contentLayout.addView(mEditText);
		
		//DialogのOKボタンを押したときの設定
		final Button positiveButton = (Button)mView.findViewById(R.id.button_dialog_positive);
		positiveButton.setText(R.string.ok);
		positiveButton.setOnClickListener(new OnClickListener(){
			
			//**
			//* 新しいプレイリストを作成する．
			//*
			@Override
			public void onClick(View view) {
				String title = mEditText.getText().toString();
				//文字が入力されていた時
				if(title != null && title.length() > 0){
					// プレイリストを生成して曲を追加する．
					final PlayList newPlayList = 
							new PlayList(title, "PlayList", System.currentTimeMillis(), null);
					ArrayList<PlayList> mPlayLists = PlayList.getPlayList(view.getContext());
					try{
						ArrayList<Music> musics = MusicUtils.getMusicController(view.getContext()).getQueue();
						newPlayList.setMusics(musics.toArray(new Music[musics.size()]));
						mPlayLists.add(newPlayList);
					}catch(NullPointerException e){
						final Music[] musics = new Music[0];
						//Musicをセット
						newPlayList.setMusics(musics);
						mPlayLists.add(newPlayList);
					}
					PlayList.writePlayList(view.getContext());

					Toast.makeText(
							view.getContext(), 
							"PlayList \"" + newPlayList.getAlbum() + "\" created!", 
							Toast.LENGTH_SHORT)
							.show();
					//Viewの消去
					if(MusicViewCtl.getPlayerView() != null){
						//ViewPagerの更新を行う
						ViewPager v = (ViewPager) MusicViewCtl.getPlayerView().findViewById(R.id.player_list_part);
						((MusicViewPagerAdapter)v.getAdapter()).notifitionDataSetChagedForQueueView();
					}
					removeForWindowManager(mWindowManager,mView);
					DisplayUtils.hideInputMethodEditor(view.getContext(), mView);
				}
			}
		});
		
		//DialogのCancelボタンを押したときの動作
		final Button negativeButton = (Button)mView.findViewById(R.id.button_dialog_negative);
		negativeButton.setText(R.string.cancel);
		negativeButton.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View view) {
				// Dialogを閉じる．
				//Viewの消去
				removeForWindowManager(mWindowManager,mView);
				DisplayUtils.hideInputMethodEditor(view.getContext(), mView);
			}
		});
	}
	*/
	
	/**
	 * プレイリストを編集する時に表示するダイアログを作成する．
	 * @param mContext
	 * @param music
	 * @param column
	 */
	public final void createDialogIfEditPlayList(Context mContext,final int index,final ArrayList<PlayList> mPlayLists ){
		
		//MainViewの生成
		LayoutInflater inflater = LayoutInflater.from( mContext );
		final ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.dialog, (ViewGroup)MusicViewCtl.getPlayerView(),false);
		
		//WindowManager~の起動にする．
		final WindowManager mWindowManager = setWindowManager(mContext, mView);

		//ダイアログに挿入するListView
		final ListView mListView = new ListView(mContext);
		
		final FrameLayout mLayout = (FrameLayout)mView.findViewById(R.id.frameLayout_dialog);
		mLayout.addView(mListView);
		
		final TextView titleView = (TextView)mView.findViewById(R.id.textView_dialog_title);
		titleView.setText(R.string.edit_to_playlist);
		
		final Button positiveButton = (Button)mView.findViewById(R.id.button_dialog_positive);
		positiveButton.setVisibility(View.GONE);
		
		final Button negativeButton = (Button)mView.findViewById(R.id.button_dialog_negative);
		negativeButton.setText(R.string.cancel);
		negativeButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//Viewの消去
				removeForWindowManager(mWindowManager,mView);
			}
			
		});
		
		//ListViewへ適用させるAdapter
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1);
		
		//PlayListを作成するItemの追加
		//Rename
		adapter.add(mContext.getString(R.string.rename_playlist));
		//delete
		adapter.add(mContext.getString(R.string.delete_playlist));
		
		//Adapterへのセット
		mListView.setAdapter(adapter);
		
		//dividerの高さを設定
		mListView.setDividerHeight(
				mContext.getResources()
				.getDimensionPixelSize(R.dimen.listview_divider));
		
		//ListViewのItemを選択したときの動作を設定する．
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Viewの消去
				removeForWindowManager(mWindowManager,mView);
				
				switch(position){
				case 0://プレイリストの名前を変更する．
					//変更するダイアログの表示
					renamePlayListDialog(view.getContext(), index, mPlayLists);
					break;
				case 1://プレイリストを消去する
					//確認ダイアログの表示
					deletePlayListDialog(view.getContext(), index, mPlayLists);
				default:
				}
			}	
		});
	}
	
	/**
	 * プレイリストの名前を変更するダイアログを作成する．
	 */
	public final void renamePlayListDialog(
			Context mContext,
			final int index,
			final ArrayList<PlayList> mPlayLists)
	{
		//MainViewの生成
		LayoutInflater inflater = LayoutInflater.from( mContext );
		final ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.dialog, (ViewGroup)MusicViewCtl.getPlayerView(),false);
		
		//WindowManager~の起動にする．
		final WindowManager mWindowManager = setWindowManager(mContext, mView);
		
		//Titleの設定
		final TextView titleView = (TextView)mView.findViewById(R.id.textView_dialog_title);
		titleView.setText(R.string.rename_playlist_title);

		//プレイリスト名を入力するEditTextを生成
		final EditText mEditText = new EditText(mContext);
		mEditText.setHint(R.string.rename_playlist_text_hint);
		mEditText.requestFocus();
		
		DisplayUtils.showInputMethodEditor(mContext, mView);
		
		//Viewをセットする．
		FrameLayout contentLayout = (FrameLayout)mView.findViewById(R.id.frameLayout_dialog);
		contentLayout.addView(mEditText);
		
		//DialogのOKボタンを押したときの設定
		final Button positiveButton = (Button)mView.findViewById(R.id.button_dialog_positive);
		positiveButton.setText(R.string.ok);
		positiveButton.setOnClickListener(new OnClickListener(){
			
			/**
			 * プレイリストの名前を変更する．
			 */
			@Override
			public void onClick(View view) {
				String title = mEditText.getText().toString();
				//文字が入力されていた時
				if(title != null && title.length() > 0){
					// プレイリストを生成して曲を追加する．
					android.util.Log.v(TAG, "position : " + index);
					final PlayList newPlayList = 
							mPlayLists.get(index);
					//Musicをセット
					newPlayList.setAlbum(title);
					Toast.makeText(
							view.getContext(), 
							"PlayList \"" + newPlayList.getAlbum() + "\" renamed!", 
							Toast.LENGTH_SHORT)
							.show();
					//Viewの消去
					removeForWindowManager(mWindowManager,mView);
					DisplayUtils.hideInputMethodEditor(view.getContext(), mView);
					//Viewの消去
					if(MusicViewCtl.getPlayerView() != null){
						//ViewPagerの更新を行う
						ViewPager v = (ViewPager) MusicViewCtl.getPlayerView().findViewById(R.id.player_list_part);
						((MusicViewPagerAdapter)v.getAdapter()).notifitionDataSetChagedForQueueView();
					}
				}
			}
		});
		
		//DialogのCancelボタンを押したときの動作
		final Button negativeButton = (Button)mView.findViewById(R.id.button_dialog_negative);
		negativeButton.setText(R.string.cancel);
		negativeButton.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View view) {
				// Dialogを閉じる．
				//Viewの消去
				removeForWindowManager(mWindowManager,mView);
				DisplayUtils.hideInputMethodEditor(view.getContext(), mView);
			}
		});
	}
	
	/**
	 * プレイリストを消去するダイアログを作成する．
	 */
	public final void deletePlayListDialog(
			Context mContext,
			final int index,
			final ArrayList<PlayList> mPlayLists)
	{
		//MainViewの生成
		LayoutInflater inflater = LayoutInflater.from( mContext );
		final ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.dialog, (ViewGroup)MusicViewCtl.getPlayerView(),false);
		
		//WindowManager~の起動にする．
		final WindowManager mWindowManager = setWindowManager(mContext, mView);
		
		//Titleの設定
		final TextView titleView = (TextView)mView.findViewById(R.id.textView_dialog_title);
		titleView.setText(R.string.delete_playlist);

		final TextView subTitleView = new TextView(mContext);
		subTitleView.setText("PlayList : " + mPlayLists.get(index).getAlbum());
		
		//Viewをセットする．
		FrameLayout contentLayout = (FrameLayout)mView.findViewById(R.id.frameLayout_dialog);
		contentLayout.addView(subTitleView);
		
		//DialogのOKボタンを押したときの設定
		final Button positiveButton = (Button)mView.findViewById(R.id.button_dialog_positive);
		positiveButton.setText(R.string.ok);
		positiveButton.setOnClickListener(new OnClickListener(){
			
			/**
			 * プレイリストを消去する．
			 */
			@Override
			public void onClick(View view) {
				
				PlayList removePL = mPlayLists.remove(index);
				Toast.makeText(
						view.getContext(), 
						"PlayList \"" + removePL.getAlbum() + "\" removed!", 
						Toast.LENGTH_SHORT)
						.show();
				//Viewの消去
				removeForWindowManager(mWindowManager,mView);
				//Viewの消去
				if(MusicViewCtl.getPlayerView() != null){
					//ViewPagerの更新を行う
					ViewPager v = (ViewPager) MusicViewCtl.getPlayerView().findViewById(R.id.player_list_part);
					((MusicViewPagerAdapter)v.getAdapter()).notifitionDataSetChagedForQueueView();
				}
				
			}
		});
		
		//DialogのCancelボタンを押したときの動作
		final Button negativeButton = (Button)mView.findViewById(R.id.button_dialog_negative);
		negativeButton.setText(R.string.cancel);
		negativeButton.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View view) {
				// Dialogを閉じる．
				//Viewの消去
				removeForWindowManager(mWindowManager,mView);
			}
		});
	}
	
	/**
	 * プレイリストの曲を消去するダイアログを作成する．
	 */
	public final void deletePlayListItemDialog(
			Context mContext,
			final int index,
			final PlayList mPlayList)
	{
		//MainViewの生成
		LayoutInflater inflater = LayoutInflater.from( mContext );
		final ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.dialog, (ViewGroup)MusicViewCtl.getPlayerView(),false);
		
		//WindowManager~の起動にする．
		final WindowManager mWindowManager = setWindowManager(mContext, mView);
		
		//Titleの設定
		final TextView titleView = (TextView)mView.findViewById(R.id.textView_dialog_title);
		titleView.setText(R.string.delete_from_playlist);

		final TextView subTitleView = new TextView(mContext);
		subTitleView.setText("Music : " + mPlayList.getMusics()[index].getTitle() + " / " + mPlayList.getMusics()[index].getArtist());
		
		//Viewをセットする．
		FrameLayout contentLayout = (FrameLayout)mView.findViewById(R.id.frameLayout_dialog);
		contentLayout.addView(subTitleView);
		
		//DialogのOKボタンを押したときの設定
		final Button positiveButton = (Button)mView.findViewById(R.id.button_dialog_positive);
		positiveButton.setText(R.string.ok);
		positiveButton.setOnClickListener(new OnClickListener(){
			
			/**
			 * プレイリストの曲を消去する．
			 */
			@Override
			public void onClick(View view) {
				
				Music removeMusic = mPlayList.removeMusic(index);
				if(removeMusic != null){
					Toast.makeText(
							view.getContext(), 
							"Music \"" + removeMusic.getTitle() + "\" removed!", 
							Toast.LENGTH_SHORT)
							.show();
					ItemViewFactory.clearExpandPosition();
				}
				//Viewの消去
				removeForWindowManager(mWindowManager,mView);
				//Viewの消去
				if(MusicViewCtl.getPlayerView() != null){
					//ViewPagerの更新を行う
					ViewPager v = (ViewPager) MusicViewCtl.getPlayerView().findViewById(R.id.player_list_part);
					((MusicViewPagerAdapter)v.getAdapter()).notifitionDataSetChagedForQueueView();
				}
				
			}
		});
		
		//DialogのCancelボタンを押したときの動作
		final Button negativeButton = (Button)mView.findViewById(R.id.button_dialog_negative);
		negativeButton.setText(R.string.cancel);
		negativeButton.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View view) {
				// Dialogを閉じる．
				//Viewの消去
				removeForWindowManager(mWindowManager,mView);
			}
		});
	}
	
	/**
	 * Queueのアイテムを消去するダイアログを作成する．
	 */
	public final void deleteQueueDialog(Context mContext, final Music item)
	{
		final MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(mContext);
		//MainViewの生成
		LayoutInflater inflater = LayoutInflater.from( mContext );
		final ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.dialog, (ViewGroup)MusicViewCtl.getPlayerView(),false);
		
		//WindowManager~の起動にする．
		final WindowManager mWindowManager = setWindowManager(mContext, mView);
		
		//Titleの設定
		final TextView titleView = (TextView)mView.findViewById(R.id.textView_dialog_title);
		titleView.setText(R.string.delete_from_queue);

		final TextView subTitleView = new TextView(mContext);
		subTitleView.setText("Music : " + item.getTitle() + " / " + item.getArtist());
		
		//Viewをセットする．
		FrameLayout contentLayout = (FrameLayout)mView.findViewById(R.id.frameLayout_dialog);
		contentLayout.addView(subTitleView);
		
		//DialogのOKボタンを押したときの設定
		final Button positiveButton = (Button)mView.findViewById(R.id.button_dialog_positive);
		positiveButton.setText(R.string.ok);
		positiveButton.setOnClickListener(new OnClickListener(){
			
			/**
			 * Queueから曲を消去する．
			 */
			@Override
			public void onClick(View view) {
				
				boolean result = mpwpl.getQueue().remove(item);
				if(result){
					Toast.makeText(
							view.getContext(), 
							"Music \"" + item.getTitle() + "\" removed!", 
							Toast.LENGTH_SHORT)
							.show();
					ItemViewFactory.clearExpandPosition();
				}
				//Viewの消去
				removeForWindowManager(mWindowManager,mView);
				//Viewの消去
				if(MusicViewCtl.getPlayerView() != null){
					//ViewPagerの更新を行う
					ViewPager v = (ViewPager) MusicViewCtl.getPlayerView().findViewById(R.id.player_list_part);
					((MusicViewPagerAdapter)v.getAdapter()).notifitionDataSetChagedForQueueView();
				}
				
			}
		});
		
		//DialogのCancelボタンを押したときの動作
		final Button negativeButton = (Button)mView.findViewById(R.id.button_dialog_negative);
		negativeButton.setText(R.string.cancel);
		negativeButton.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View view) {
				// Dialogを閉じる．
				//Viewの消去
				removeForWindowManager(mWindowManager,mView);
			}
		});
	}
	
	/**
	 * Queueのアイテムを全て消去するDialog
	 */
	public final void clearQueueDialog(Context mContext)
	{
		final MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(mContext);
		//MainViewの生成
		LayoutInflater inflater = LayoutInflater.from( mContext );
		final ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.dialog, (ViewGroup)MusicViewCtl.getPlayerView(),false);
		
		//WindowManager~の起動にする．
		final WindowManager mWindowManager = setWindowManager(mContext, mView);
		
		//Titleの設定
		final TextView titleView = (TextView)mView.findViewById(R.id.textView_dialog_title);
		titleView.setText(R.string.delete_from_queue);

		final TextView subTitleView = new TextView(mContext);
		subTitleView.setText("Queue All Items");
		
		//Viewをセットする．
		FrameLayout contentLayout = (FrameLayout)mView.findViewById(R.id.frameLayout_dialog);
		contentLayout.addView(subTitleView);
		
		//DialogのOKボタンを押したときの設定
		final Button positiveButton = (Button)mView.findViewById(R.id.button_dialog_positive);
		positiveButton.setText(R.string.ok);
		positiveButton.setOnClickListener(new OnClickListener(){
			
			/**
			 * Queueから曲を消去する．
			 */
			@Override
			public void onClick(View view) {
				
				mpwpl.setCursor(0);
				mpwpl.getQueue().clear();
				
				mpwpl.writeQueue();
				Toast.makeText(
						view.getContext(), 
						"Queue removed!", 
						Toast.LENGTH_SHORT)
						.show();
				ItemViewFactory.clearExpandPosition();

				//Viewの消去
				removeForWindowManager(mWindowManager,mView);
				//Viewの消去
				if(MusicViewCtl.getPlayerView() != null){
					//ViewPagerの更新を行う
					ViewPager v = (ViewPager) MusicViewCtl.getPlayerView().findViewById(R.id.player_list_part);
					((MusicViewPagerAdapter)v.getAdapter()).notifitionDataSetChagedForQueueView();
				}
				
			}
		});
		
		//DialogのCancelボタンを押したときの動作
		final Button negativeButton = (Button)mView.findViewById(R.id.button_dialog_negative);
		negativeButton.setText(R.string.cancel);
		negativeButton.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View view) {
				// Dialogを閉じる．
				//Viewの消去
				removeForWindowManager(mWindowManager,mView);
			}
		});
	}
	
	/**
	 * Queueをアルバムに差し替えるダイアログ
	 */
	public final void playAndAddQueueDialog(Context mContext,final PlayList mPlayList)
	{
		//MainViewの生成
		LayoutInflater inflater = LayoutInflater.from( mContext );
		final ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.dialog, (ViewGroup)MusicViewCtl.getPlayerView(),false);
		
		//WindowManager~の起動にする．
		final WindowManager mWindowManager = setWindowManager(mContext, mView);
		
		//Titleの設定
		final TextView titleView = (TextView)mView.findViewById(R.id.textView_dialog_title);
		titleView.setText(R.string.set_queue);

		final TextView subTitleView = new TextView(mContext);
		subTitleView.setText("Play Musics (with set queue)");
		
		//Viewをセットする．
		FrameLayout contentLayout = (FrameLayout)mView.findViewById(R.id.frameLayout_dialog);
		contentLayout.addView(subTitleView);
		
		//DialogのOKボタンを押したときの設定
		final Button positiveButton = (Button)mView.findViewById(R.id.button_dialog_positive);
		positiveButton.setText(R.string.ok);
		positiveButton.setOnClickListener(new OnClickListener(){
			
			/**
			 * Queueに曲をセットする．
			 */
			@Override
			public void onClick(View view) {
				//PlayListをセットする
				MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(view.getContext());
				try {
					ArrayList<Music> list = mPlayList.getMusicList();
					mpwpl.setPlayList(list);
					mpwpl.setCursor(0);
					if(list.size() > 0){
						MusicViewCtl.playOrPauseWithView();
					}
					Toast.makeText(view.getContext(), mPlayList.getAlbum() + " → Queue And Play.", Toast.LENGTH_SHORT).show();
					//Viewの消去
					removeForWindowManager(mWindowManager,mView);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		//DialogのCancelボタンを押したときの動作
		final Button negativeButton = (Button)mView.findViewById(R.id.button_dialog_negative);
		negativeButton.setText(R.string.cancel);
		negativeButton.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View view) {
				// Dialogを閉じる．
				//Viewの消去
				removeForWindowManager(mWindowManager,mView);
			}
		});
	}
	
}
