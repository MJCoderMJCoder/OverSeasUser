package com.ltt.overseasuser.main.tab.fragment.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ltt.overseasuser.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yunwen on 2018/7/30.
 */

public class NewAudioImageActivity {
    private ImageView mSoundIamge;
    private SeekBar seekBar;
    private TextView musicCur;
    private String mMp3Path = "";//录音存放位置
    private View mView = null;
    private Activity mParentActivity;
    private SimpleDateFormat format = null;
    private Timer timer;
    private MediaPlayer mediaPlayer;

    public NewAudioImageActivity(View view, String mp3Path, Activity activity) {
        format = new SimpleDateFormat("mm:ss");
        mView = view;
        mMp3Path = mp3Path;
        mParentActivity = activity;
        initUI();

    }

    private void initUI() {

        mSoundIamge = mView.findViewById(R.id.play_stop);
        seekBar = mView.findViewById(R.id.seekbar);
        musicCur = mView.findViewById(R.id.music_cur);
        mSoundIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clickPlayVoice(mMp3Path);

            }
        });

    }

    protected void finalize() {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            ;
        }


    }


    //由Uri转成路径的方法
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    //点击语音播放按钮
    private void clickPlayVoice(String mp3Path) {
        if (mp3Path.isEmpty())
            return;
        mediaPlayer = new MediaPlayer();
        timer = new Timer();

        try {
            mediaPlayer.setDataSource(mp3Path);//指定音频文件的路径
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();//让mediaplayer进入准备状态
        } catch (IOException e) {
            e.printStackTrace();
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                ;
            }

        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // 在播放完毕被回调
                mSoundIamge.setImageResource(R.mipmap.play);
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    ;
                }


            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                ;
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    ;
                }
                return true;
            }
        });
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(final MediaPlayer mp) {
                seekBar.setMax(mp.getDuration());
                musicCur.setText("00:00");
                mSoundIamge.setImageResource(R.mipmap.stop);
                mp.start();//开始播放
                mp.seekTo(0);

                //监听播放时回调函数
                timer.schedule(new TimerTask() {

                    Runnable updateUI = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (mp == null)
                                    return;
                                musicCur.setText(format.format(mp.getCurrentPosition()) + "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    @Override
                    public void run() {
                        if (mp == null)
                            return;
                        seekBar.setProgress(mp.getCurrentPosition());
                        mParentActivity.runOnUiThread(updateUI);
                    }
                }, 0, 50);

            }
        });
    }


    private String getFileName(String pathandname) {

        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }

    }


}
