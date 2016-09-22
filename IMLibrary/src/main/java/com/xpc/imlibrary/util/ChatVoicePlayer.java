package com.xpc.imlibrary.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemClock;

import com.xpc.imlibrary.config.IMConstant;
import com.xpc.imlibrary.model.RecMessageItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * 语音播放
 * @author qiaocbao
 * @time 2015-1-22 上午10:07:53
 */
public class ChatVoicePlayer {

	public static interface OnPlayListener extends OnCompletionListener {
		public void onCancel();

		public void onDownloadBegin();

		public void onDownloadEnd();

		public void onError();

		public void onPlayBegin(MediaPlayer player);

		public void onReplay();

		public void setRecMessageItem(RecMessageItem item);
	}

	/**
	 * 管理其他音乐播放器
	 * **/
	public interface OtherPlayerManagerI {
		/**
		 * 暂停其他播放器
		 * **/
		public void pauseOtherPlayer();

		/**
		 * 恢复其他播放器
		 * **/
		public void replyOtherPlayer();

		/**
		 * 释放资源
		 * **/
		public void releaseResource();
	}

	/**
	 * 播放语音，管理音频焦点
	 * **/
	public static class OtherPlayerManagerImpl implements OtherPlayerManagerI {
		public static Context otcontext = null;
		public static AudioManager am = null;
		public static OtherPlayerManagerImpl playerManagerImpl = null;

		// 单例返回
		public static OtherPlayerManagerImpl getInstance(Context context) {
			OtherPlayerManagerImpl.otcontext = context;
			if (playerManagerImpl == null) {
				playerManagerImpl = new OtherPlayerManagerImpl();
			}
			if (am == null) {
				am = (AudioManager) otcontext
						.getSystemService(Context.AUDIO_SERVICE);
			}
			return playerManagerImpl;
		}

		@Override
		public void pauseOtherPlayer() {
			int result = am.requestAudioFocus(null, AudioManager.STREAM_MUSIC,
					AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
		}

		@Override
		public void replyOtherPlayer() {
			am.abandonAudioFocus(null);
		}

		@Override
		public void releaseResource() {
			context = null;
			am = null;
			playerManagerImpl = null;
		}
	}

	public static Context context;
	public static MediaPlayer player;
	public static RecMessageItem curPlayMessage;
	public static OnPlayListener curOnPlayListener;
	private static Set<String> downloading = Collections
			.synchronizedSet(new HashSet<String>());

	public static void clear() {
		player = null;
		curPlayMessage = null;
	}

	private static boolean doPlay(final RecMessageItem item,
			final OnPlayListener onPlayListener) {
		// 停掉前一个播放器
		if (player != null) {
			RecMessageItem cpmsg = curPlayMessage;
			try {
				player.release();
				curOnPlayListener.onCancel();
				SystemClock.sleep(300);
			} catch (Exception e) {
				// ignore...
			} finally {
				player = null;
				curOnPlayListener = null;
			}
			if (cpmsg != null && cpmsg.equals(item)) {
				return true;
			}
		}

		curPlayMessage = item;
		curOnPlayListener = onPlayListener;

		curOnPlayListener.setRecMessageItem(item);

		String path = Environment.getExternalStorageDirectory().getPath()
				+ IMConstant.HHXH_RECORD + (item.getMsgId()) + ".hhxh";
		File encryptedFile = new File(path);

		// 播放的文件已经缓存在本地，则直接播放
		if (encryptedFile.exists()) {
			FileInputStream voiceIS = null;
			File voiceFile = null;
			try {
				// then play it
				player = new MediaPlayer();
				player.setDataSource(path);
				player.prepare();
				player.setOnCompletionListener(curOnPlayListener);
				player.start();
				curOnPlayListener.onPlayBegin(player);
			} catch (Exception e) {
				MyLog.i("VoicePlayer:" + e.getMessage());
				encryptedFile.delete();
				curOnPlayListener.onError();
				return false;
			} finally {
				if (voiceIS != null) {
					try {
						voiceIS.close();
					} catch (Exception e) {
					}
				}
				if (voiceFile != null)
					voiceFile.delete();
			}

			return true;
		} else if (downloading.contains(item.getMsgId())) {
			return true;
		}

		return false;
	}

	private static void downloadAndPlay(final RecMessageItem item,
			final OnPlayListener onPlayListener) {
		if (!downloading.add(item.getMsgId())) {
			return;
		}

		onPlayListener.onDownloadBegin();

		new AsyncTask<String, Integer, Boolean>() {

			@Override
			protected Boolean doInBackground(String... params) {

				try {
					String path = Environment.getExternalStorageDirectory()
							.getPath()
							+ IMConstant.HHXH_RECORD
							+ (item.getMsgId()) + ".hhxh";
					File recordFile = new File(path);
					if (!recordFile.exists()) {
						recordFile.createNewFile();
					}
					byte[] bytes=null;
					if(item.getContent()!=null && item.getContent().startsWith("http")){
						bytes=HttpURLTools.downloadBytes(item.getContent());
					}else{
						ByteArrayOutputStream bos=new ByteArrayOutputStream();
						if(item.getContent()!=null){
							File localFile=new File(item.getContent());
							InputStream is=new FileInputStream(localFile);
							int len = 0;
							byte[] buf = new byte[1024];
							while ((len = is.read(buf)) != -1) {
								bos.write(buf, 0, len);
							}
							is.close();
							bytes=bos.toByteArray();
							bos.close();
						}
						
					}
					if(bytes!=null){
						FileOutputStream outputStream = new FileOutputStream(
								recordFile);
						outputStream.write(bytes);// 写入内容
						outputStream.close();// 关闭流
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					return false;

				}
				return true;
			};

			protected void onPostExecute(Boolean result) {
				onPlayListener.onDownloadEnd();
				downloading.remove(item.getMsgId());
				if (!result) {
					// AndroidUtils.toastShort("无法下载当前音频");
					onPlayListener.onError();
					return;
				}

				if (curPlayMessage == null
						|| curPlayMessage.getMsgId().equals(item.getMsgId())) {
					doPlay(item, onPlayListener);
				}
			}

		}.execute("");
	}

	public static void onDestroy() {
		// 停掉前一个播放器
		if (player != null) {
			player.release();
			if (curOnPlayListener != null)
				curOnPlayListener.onCancel();
			player = null;
			curPlayMessage = null;
		}
	}

	public static void play(final RecMessageItem item,
			final OnPlayListener onPlayListener, Context context) {
		ChatVoicePlayer.context = context;
		if (item == null)
			return;
		if (doPlay(item, onPlayListener)) {
			return;
		}
		// 文件还未下载，则直接下载之后再播放
		downloadAndPlay(item, onPlayListener);
	}

}
