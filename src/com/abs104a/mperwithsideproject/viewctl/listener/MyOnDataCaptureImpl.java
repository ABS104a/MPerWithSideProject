package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.view.FFTView;

import android.media.audiofx.Visualizer;
import android.media.audiofx.Visualizer.OnDataCaptureListener;

/**
 * Visualizerにデータが流れてきたときのImplリスナ
 * @author Kouki
 *
 */
public class MyOnDataCaptureImpl implements OnDataCaptureListener {

	private FFTView fftView;
	
	public MyOnDataCaptureImpl(FFTView fftView){
		this.fftView = fftView;
	}

	//Wave形式のキャプチャーデータ
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
            int samplingRate) {
    	if(fftView != null)
    		((FFTView)fftView).updateWave(bytes);
    	//android.util.Log.v("Visualizer","setVisualizer");
    }

    //高速フーリエ変換のキャプチャーデータ
    public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
            int samplingRate) {
    	if(fftView != null)
    		((FFTView)fftView).updateFFT(bytes);
    	//android.util.Log.v("Visualizer","setVisualizerFFT");
    }

}
