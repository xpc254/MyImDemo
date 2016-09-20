package com.xpc.imlibrary.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.xpc.imlibrary.ChatActivity;
import com.xpc.imlibrary.R;
import com.xpc.imlibrary.util.ConvertToMp3Thread;
import com.xpc.imlibrary.util.MyLog;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 新录音view
 * @author 李永德
 * @time 2015-11-3 下午10:51:38
 */
@SuppressLint("NewApi")
public class NewRecordButton extends Button {

    public NewRecordButton(Context context) {
        super(context);
    }

    public NewRecordButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public NewRecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 录音文件路径
     */
    private String recordPath;
    /**
     * 转化后mp3路径
     */
    private String mp3Path;
    /**
     * 当前声音状态
     */
    private int volumeState = 0;
    /**
     * 录音数据缓冲区字节数组
     */
    private short[] mBuffer;
    /**
     * 录音类
     */
    private AudioRecord mRecorder;
    /**
     * 录音采样率
     */
    private static final int SAMPLE_RATE = 8000;
    /**
     * 回调接口
     */
    private OnFinishedRecordListener finishedListener;

    private static final int MIN_INTERVAL_TIME = 2000;// 2s

    private long startTime;

    /**
     * 音量状态对话框
     */
    private Dialog recordIndicator;
    /**
     * 更新音量动画图标
     */
    private static final int CHANGE_VOLUME_ICON = 1;
    /**
     * 音量动画资源图片
     */
    private static int[] res = {R.drawable.ic_mic_2, R.drawable.ic_mic_3, R.drawable.ic_mic_4, R.drawable.ic_mic_5};
    /**
     * 音量动画资源图片
     */
    private static ImageView view;

    /**
     * 获取音量线程
     */
    private ObtainDecibelThread thread;

    /**
     * 是否正在录音
     */
    private boolean isRecording;
    /**
     * 转换线程
     */
    private ConvertToMp3Thread convertThread;
    /**
     * 录音时长
     */
    private long intervalTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setText(getContext().getResources().getString(R.string.new_loose_send));
                initDialogAndStartRecord();
                break;
            case MotionEvent.ACTION_UP:
                this.setText(getContext().getResources().getString(R.string.new_press_record));
                finishRecord();
                break;
            case MotionEvent.ACTION_CANCEL:// 当手指移动到view外面，会cancel
                cancelRecord();
                Toast.makeText(getContext(), "cancel", 1).show();
                break;
        }

        return true;
    }

    private void initDialogAndStartRecord() {

        startTime = System.currentTimeMillis();
        String path = ChatActivity.RECORD_ROOT_PATH;
        File file = new File(path);
        file.mkdirs();
        path += startTime;
        recordPath = path + ".wav";
        mp3Path = path + ".mp3";
        recordIndicator = new Dialog(getContext(), R.style.like_toast_dialog_style);
        view = new ImageView(getContext());
        view.setImageResource(R.drawable.ic_mic_2);
        recordIndicator.setContentView(view, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutParams lp = recordIndicator.getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        startRecording();
        recordIndicator.show();
    }

    public void setOnFinishedRecordListener(OnFinishedRecordListener listener) {
        finishedListener = listener;
    }

    private void finishRecord() {
        stopRecording();
        intervalTime = System.currentTimeMillis() - startTime;
        if (intervalTime < MIN_INTERVAL_TIME) {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.new_time_to_low), Toast.LENGTH_SHORT).show();
            deleteRecordFile();
            return;
        }
        if (finishedListener != null) {
            finishedListener.onStartConvert();
        }
        convertThread = new ConvertToMp3Thread(recordPath, mp3Path, mHandler);
        convertThread.start();

    }

    private void cancelRecord() {
        stopRecording();
        Toast.makeText(getContext(), getContext().getResources().getString(R.string.new_cancel_record), Toast.LENGTH_SHORT).show();
        deleteRecordFile();
    }

    private void startRecording() {
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        mBuffer = new short[bufferSize];
        mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        System.out.println("------------------------");
        mRecorder.startRecording();
        isRecording = true;
        startBufferedWrite(new File(recordPath));
        thread = new ObtainDecibelThread();
        thread.start();

    }

    private void startBufferedWrite(final File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataOutputStream output = null;
                try {
                    output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
                    while (isRecording) {
                        int readSize = mRecorder.read(mBuffer, 0, mBuffer.length);

                        for (int i = 0; i < readSize; i++) {
                            output.writeShort(mBuffer[i]);
                        }
                        volumeState = getVolumeLevel(mBuffer, readSize);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (output != null) {
                        try {
                            output.flush();
                        } catch (IOException e) {
                            e.printStackTrace();

                        } finally {
                            try {
                                output.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();
    }

    private void stopRecording() {
        isRecording = false;
        if (thread != null) {
            thread.exit();
            thread = null;
        }
        recordIndicator.dismiss();
        if (mRecorder != null) {
            mRecorder.stop();
            try {
                mRecorder.release();
            } catch (Exception e) {
                MyLog.e("Stop Error:" + e.getMessage());
            }
            mRecorder = null;
        }
    }

    public void setFinishedListener(OnFinishedRecordListener finishedListener) {
        this.finishedListener = finishedListener;
    }

    public int getVolumeLevel(short[] mBuffer, int size) {
        int result;
        long sum = 0;
        double volume;
        for (short data : mBuffer) {
            sum = sum + data * data;
        }
        volume = 10 * Math.log10((sum * 1.0f / size));

        if (volume < 45) {
            result = 0;
        } else if (volume < 58) {
            result = 1;
        } else if (volume < 67) {
            result = 2;
        } else {
            result = 3;
        }

        return result;
    }

    private class ObtainDecibelThread extends Thread {
        private volatile boolean running = true;

        public void exit() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                Message message = Message.obtain();
                message.what = CHANGE_VOLUME_ICON;
                message.arg1 = volumeState;
                mHandler.sendMessage(message);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            super.run();
        }

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHANGE_VOLUME_ICON: // 改变音量图标
                    view.setImageResource(res[msg.arg1]);
                    break;
                case ConvertToMp3Thread.CONVERT_SUCCESS:// 转化mp3成功
                    deleteRecordFile();
                    if (finishedListener != null)
                        finishedListener.onFinishedRecord(mp3Path, (int) (intervalTime / 1000));
                    break;
                case ConvertToMp3Thread.CONVERT_FAIL:// 转化mp3失败
                    if (finishedListener != null) finishedListener.onFinishedRecord(null, -1);
                    deleteMp3File();
                    break;
            }
            super.handleMessage(msg);
        }

    };

    public interface OnFinishedRecordListener {
        public void onFinishedRecord(String audioPath, int time);

        public void onStartConvert();
    }

    private void deleteRecordFile() {
        try {
            if (recordPath != null) {
                File file = new File(recordPath);
                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void deleteMp3File() {
        try {
            if (mp3Path != null) {
                File file = new File(mp3Path);
                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
