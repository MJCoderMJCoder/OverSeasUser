package com.ltt.overseasuser.main.tab.fragment.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.XApplication;
import com.ltt.overseasuser.base.AudioRecoderUtils;
import com.ltt.overseasuser.base.MediaPlayObject;
import com.ltt.overseasuser.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/6/24 0024.
 */

public class AudioImageActivity {
    private ImageView mChooseIamge;//选择照片
    private ImageView mSoundIamge;
    private ImageView mChoosePdf;
    private AudioRecoderUtils mAudioRecoderUtils;
    private List<Uri> mPicPath = new ArrayList<>();//图片存放位置
    private List<Uri> mPdfFilePath = new ArrayList<>();//pdf文件存放位置
    private List<String> mMp3Path = new ArrayList<>();//录音存放位置
    private SimpleDateFormat format;
    private LinearLayout ly_questionImage = null;
    private LinearLayout ly_question = null;
    View mView = null;
    private Activity mParentActivity;
    private LayoutInflater mlflater;

    public AudioImageActivity(LayoutInflater lflater, Activity rquestActivity) {

        initVoice();
        mlflater = lflater;
        mParentActivity = rquestActivity;
        initUI();

    }

    private void initUI() {
        View imageRecordView = mlflater.inflate(R.layout.image_soundlayout, null);
        mChooseIamge = imageRecordView.findViewById(R.id.iv_chooseimage);//选择照片
        mChoosePdf = imageRecordView.findViewById(R.id.iv_choosefile);
        ly_questionImage = imageRecordView.findViewById(R.id.ly_imagequestion);
        ly_question = imageRecordView.findViewById(R.id.ly_question);
        mSoundIamge = imageRecordView.findViewById(R.id.iv_choosesound);
        mView = imageRecordView;
        mChooseIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPermisson())
                    return;
                //选择图片 choose image
                clickChooseFile();
            }
        });
        mChoosePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPermisson())
                    return;
                clickchoosePdfFile();
            }
        });

        mSoundIamge.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isPermisson()) {
                            return false;
                        }
                        mSoundIamge.setImageResource(R.mipmap.aftervoice);
                        mAudioRecoderUtils.startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                        mSoundIamge.setImageResource(R.mipmap.prevoice);
                        mAudioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
                        break;
                }
                return true;
            }
        });

    }

    protected void finalize() {

        if (mAudioRecoderUtils != null) {
            mAudioRecoderUtils.stopRecord();
            // mAudioRecoderUtils.cancelRecord();
        }
    }

    //初始化音频相关功能代码
    private void initVoice() {

        format = new SimpleDateFormat("mm:ss");
        mAudioRecoderUtils = new AudioRecoderUtils();
        //录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {
            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                //根据分贝值来设置录音时进度，下面有讲解
//                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
//                mTextView.setText(TimeUtils.long2String(time));

            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath) {
                if (mMp3Path.size() + mPicPath.size() + mPdfFilePath.size() >= 10)
                    return;
                Toast.makeText(mView.getContext(), "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
                mMp3Path.add(filePath);
                setVoiceMessage(filePath);
            }
        });

    }

    //点击选择图片文件
    private void clickChooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mParentActivity.startActivityForResult(intent, 1);
    }

    //点击选择pdf文件
    private void clickchoosePdfFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        mParentActivity.startActivityForResult(intent, 2);

    }


    //由Uri转成路径的方法
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
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


    public void setVoiceMessage(final String mp3Path) {
        final View voiceView = mlflater.inflate(R.layout.questionvoicelayout, null);
        ImageView lv_xx = voiceView.findViewById(R.id.lv_voicedelete);
        TextView voiceTv = voiceView.findViewById(R.id.tv_tittle);
        voiceTv.setText(String.format("Voice Message(%d)", mMp3Path.size()));
        ly_question.addView(voiceView);
        //播放音乐对象
        MediaPlayObject mediaPlayer = new MediaPlayObject(voiceView, mp3Path, mParentActivity);
        lv_xx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ly_question.removeView(voiceView);
                mMp3Path.remove(mp3Path);
            }
        });
    }

    public void setImageShow(final Uri uri) {
        if (mPicPath.size() >= 3)
            return;
        ContentResolver cr = mParentActivity.getContentResolver();
        mPicPath.add(uri);
        try {
            final View questionIamgeView = mlflater.inflate(R.layout.image_reqeust_layout, null);
            ImageView ivImageView = questionIamgeView.findViewById(R.id.iv_request);
            Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
            ivImageView.setImageBitmap(bitmap);
            ly_questionImage.addView(questionIamgeView);
            ImageView ivxx = questionIamgeView.findViewById(R.id.lv_xx);
            ivxx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPicPath.remove(uri);
                    ly_questionImage.removeView(questionIamgeView);
                }
            });
        } catch (FileNotFoundException e) {
            Log.e("Exception", e.getMessage(), e);
        }
    }

    public void setPdfShow(final Uri uri) {
        if (mPicPath.size() + mPdfFilePath.size() >= 10)
            return;
        String path = getRealFilePath(mView.getContext(), uri);
        String filename = getFileName(path);
        if (!path.endsWith(".pdf")) {
            Toast.makeText(mParentActivity, "Please select the PFD file!", Toast.LENGTH_LONG).show();
            return;
        }
        final View questionPdfView = mlflater.inflate(R.layout.questionpdflayout, null);
        TextView tvPdfName = questionPdfView.findViewById(R.id.tv_pdf);
        ImageView iv_xx = questionPdfView.findViewById(R.id.iv_xx);
        ly_question.addView(questionPdfView);
        iv_xx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPdfFilePath.remove(uri);
                ly_question.removeView(questionPdfView);
            }
        });
        tvPdfName.setText(filename + ".pdf");
        mPdfFilePath.add(uri);
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

    public List<File> getUploadFileList() {
        List<File> uploadFileList = new ArrayList<>();
        for (Uri uri : mPicPath) {
            uploadFileList.add(new File(getRealFilePath(mView.getContext(), uri)));
        }
        for (Uri uri : mPdfFilePath) {
            uploadFileList.add(new File(getRealFilePath(mView.getContext(), uri)));
        }
        for (String uri : mMp3Path) {
            uploadFileList.add(new File(uri));
        }

        return uploadFileList;
    }

    private boolean isPermisson() {
        PackageManager pm = mParentActivity.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.RECORD_AUDIO", "com.ltt.overseasuser"));
        if (!permission) {
            ToastUtils.showToast("Please open the recordings  card permissions！");
            return false;
        }
        boolean permissionStorage = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "com.ltt.overseasuser"));
        if (!permissionStorage) {
            ToastUtils.showToast("Please open the EXTERNAL STORAGE permissions！");
            return false;
        }
        return true;
    }

}