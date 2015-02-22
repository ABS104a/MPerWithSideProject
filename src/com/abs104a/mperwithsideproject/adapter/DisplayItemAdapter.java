package com.abs104a.mperwithsideproject.adapter;

import java.util.ArrayList;

import org.fourthline.cling.model.meta.Device;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.upnp.DisplayItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayItemAdapter extends ArrayAdapter<DisplayItem> {

	private final LayoutInflater layoutInflater;
	private final int resource;

	public DisplayItemAdapter(Context context, int resource,
			ArrayList<DisplayItem> objects) {
		super(context, resource, objects);
		this.resource = resource;
		this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/* (非 Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自動生成されたメソッド・スタブ
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(resource, null);
			holder.title = (TextView) convertView.findViewById(R.id.textView_item_title);
			holder.subTitle = (TextView)convertView.findViewById(R.id.textView_item_subtitle);
			holder.icon = (ImageView)convertView.findViewById(R.id.imageView_item);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		DisplayItem item = this.getItem(position);
		
		if(item != null){
			holder.title.setText(item.getTitle());
			holder.subTitle.setText(item.getSubTitle());
			holder.icon.setImageResource(R.drawable.no_image);
			Class<?> type = item.getObjType();
			if(type == Device.class){
				try{
					@SuppressWarnings("rawtypes")
					Device device = (Device) item.getContent();
					byte[] bytes = device.getIcons()[0].getData();
					Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
					holder.icon.setImageBitmap(bmp);
				}catch(NullPointerException e){
					e.printStackTrace();
				}
			}
			//TODO ICONの取得ルーチンの作成
		}
		
		return convertView;
	}
	
	
	public static class ViewHolder{
		public TextView title = null;
		public TextView subTitle = null;
		public ImageView icon = null;
	}
	

}
