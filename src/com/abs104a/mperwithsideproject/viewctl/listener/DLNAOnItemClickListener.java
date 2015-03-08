package com.abs104a.mperwithsideproject.viewctl.listener;

import java.util.List;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.support.contentdirectory.callback.Browse;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.item.Item;

import com.abs104a.mperwithsideproject.upnp.DisplayItem;
import com.abs104a.mperwithsideproject.upnp.PortMappingUtil;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DLNAOnItemClickListener implements OnItemClickListener {

	private final ListView mListView;

	public DLNAOnItemClickListener(ListView mListView) {
		this.mListView = mListView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ArrayAdapter<?> adapter = (ArrayAdapter<?>) mListView.getAdapter();
		
		if(adapter == null)return;
		
		try{
		Object obj = adapter.getItem(position);
			if(obj instanceof DisplayItem){
				DisplayItem item = (DisplayItem)obj;
				if(item.getObjType() == Device.class){
					//TODO ブラウジングする．
					Device dev = (Device) item.getContent();
					
					PortMappingUtil.getUpnpService().getControlPoint()
					.execute(new Browse(dev.getServices()[0], "1", BrowseFlag.DIRECT_CHILDREN) {

                        @Override public void updateStatus(Status arg0) { };

						@Override
						public void received(ActionInvocation arg0,
								DIDLContent content) {
							// TODO 自動生成されたメソッド・スタブ
							List<Item> items = content.getItems();
							for(Item item : items){
								android.util.Log.v(item.getTitle(),item.toString());
							}
							//TODO Adapter作成
							
						}

						@Override
						public void failure(ActionInvocation arg0,
								UpnpResponse arg1, String arg2) {
							// TODO 自動生成されたメソッド・スタブ
							
						};
                    });
					
				}else{
					
				}
			}
		}catch(NullPointerException e){
			e.printStackTrace();
		}

	}

}
