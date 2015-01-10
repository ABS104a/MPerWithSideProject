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

	//対象となるView
	private FFTView fftView;
	
	public MyOnDataCaptureImpl(FFTView fftView){
		this.fftView = fftView;
	}

	/**
	 * 生波形データ
	 */
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
            int samplingRate) {
    	if(fftView != null)
    		((FFTView)fftView).updateWave(bytes);
    }

    /**
     * FFTデータ
     */
    public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
            int samplingRate) {
    	if(fftView != null)
    		((FFTView)fftView).updateFFT(bytes);
    }

}
