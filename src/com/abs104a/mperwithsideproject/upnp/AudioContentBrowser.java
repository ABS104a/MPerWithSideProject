package com.abs104a.mperwithsideproject.upnp;

import java.util.List;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.contentdirectory.callback.Browse;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.item.Item;

public class AudioContentBrowser extends Browse {

	public AudioContentBrowser(Service service, String containerId,
			BrowseFlag flag) {
		super(service, containerId, flag);
	}

	@Override
	public void received(ActionInvocation actionInvocation, DIDLContent didlContent) {
		// TODO 自動生成されたメソッド・スタブ
		List<Item> items = didlContent.getItems();
		//TODO LISTVIEWを取ってきてContentを表示する．

	}

	@Override
	public void updateStatus(Status status) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String defaultMsg) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
