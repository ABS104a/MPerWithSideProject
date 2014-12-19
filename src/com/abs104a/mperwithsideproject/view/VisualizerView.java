package com.abs104a.mperwithsideproject.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

/**
 * Visualizerを表示するView
 * @author Kouki-Mobile
 *
 */
public class VisualizerView extends View {

	private byte[] mData = null;

	public VisualizerView(Context context) {
		super(context);
	}
	
	public void updateVisualizer(byte[] data){
		mData = data;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO dataを基にVisualizerを実装する
		super.onDraw(canvas);
	}

}
