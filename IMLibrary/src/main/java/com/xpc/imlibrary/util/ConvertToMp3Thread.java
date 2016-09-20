package com.xpc.imlibrary.util;

import android.os.Handler;

import com.pocketdigi.utils.FLameUtils;

/**
 * mp3转换线程
 * @author liyongde
 * @time 2014-5-7 上午11:16:51
 */
public class ConvertToMp3Thread extends Thread {

	/**原文件路径 */
	private String rawPath=null;
	/**目标mp3路径 */
	private String mp3Path=null;
	/**转换成功 */
	public static final int CONVERT_SUCCESS=1001;
	/**转换失败 */
	public static final int CONVERT_FAIL=1002;
	/**Handler*/
	private Handler mHandler=null;
	/**采样率*/
	private int sampleRate;
	
	public ConvertToMp3Thread(String rawPath, String mp3Path, Handler hanler){
		this.rawPath=rawPath;
		this.mp3Path=mp3Path;
		mHandler=hanler;
		sampleRate=8000;
	}
	


	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}


	@Override
	public void run() {
		
		try{
			FLameUtils lameUtils = new FLameUtils(1, sampleRate, 96);
			boolean result = lameUtils.raw2mp3(rawPath, mp3Path);
			if(result=true){
				mHandler.sendEmptyMessage(CONVERT_SUCCESS);
			}else{
				mHandler.sendEmptyMessage(CONVERT_FAIL);
			}
		}catch(Exception e){
			e.printStackTrace();
			mHandler.sendEmptyMessage(CONVERT_FAIL);
		}
		
	}

}
