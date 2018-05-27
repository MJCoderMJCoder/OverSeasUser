package com.ltt.overseasuser.main.tab.fragment.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ltt.overseasuser.Manifest;
import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.AudioRecoderUtils;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.base.BaseBean;
import com.ltt.overseasuser.base.Xcircleindicator;
import com.ltt.overseasuser.core.ActionBar;
import com.ltt.overseasuser.http.CustomerCallBack;
import com.ltt.overseasuser.http.RetrofitUtil;
import com.ltt.overseasuser.main.tab.fragment.adapter.RequestAdapter;
import com.ltt.overseasuser.model.ListQuestionBean;
import com.ltt.overseasuser.model.QuestionBean;
import com.ltt.overseasuser.model.QuestionDataBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

public class RequestActivity extends BaseActivity {

    private StorageReference mStorageRef;//firebase storage
    @BindView(R.id.ly_dot)
    LinearLayout mlydot;
    private ArrayList mViewList;
    private int mViewPos = 0;
    private QuestionBean mQuestionBean;
    private LayoutInflater mlflater;
    private ActionBar bar;
    private int mDotSum;
    private String mSectionId;
    private ImageView mChooseIamge;
    private ImageView mShowIamge;
    private ImageView mSoundIamge;
    public String imageDir = "/sdcard/ht/";
    public ImageView mplay_stop;
    private boolean soundState = false;
    private AudioRecoderUtils mAudioRecoderUtils;

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private SeekBar mSeekBar;
    private TextView musicCur;
    private AudioManager audioManager;
    private String mMp3Path="";
    private Timer timer;

    int maxVolume, currentVolume;

    private boolean isSeekBarChanging;//互斥变量，防止进度条与定时器冲突。
    private int currentPosition;//当前音乐播放的进度
    @BindView(R.id.layall)
     LinearLayout pagerLayout;
    SimpleDateFormat format;

    @Override
    protected int bindLayoutID() {
        return R.layout.activity_request;
    }

    @Override
    protected void prepareActivity() {

        bar = ActionBar.init(this);
        bar.setRight(R.mipmap.x3x, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mStorageRef = FirebaseStorage.getInstance().getReference();
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
                Toast.makeText(RequestActivity.this, "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
                mMp3Path = filePath;
            }
        });


        mSectionId = this.getIntent().getExtras().getString("sectionid");
        mViewList = new ArrayList<View>();
        mlflater = getLayoutInflater().from(RequestActivity.this);

        getQuestionList();


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isSeekBarChanging = true;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }
    private void initMediaPlayer() {
        try {
            mediaPlayer.setDataSource(mMp3Path);//指定音频文件的路径
            mediaPlayer.prepare();//让mediaplayer进入准备状态
            mediaPlayer.setLooping(true);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    mSeekBar.setMax(mediaPlayer.getDuration());
                    // musicLength.setText(format.format(mediaPlayer.getDuration())+"");
                    musicCur.setText("00:00");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getQuestionList() {
        showLoadingView();
        Call<QuestionDataBean> call = RetrofitUtil.getAPIService().getQuestionList(mSectionId);
        call.enqueue(new CustomerCallBack<QuestionDataBean>() {
            @Override
            public void onResponseResult(QuestionDataBean response) {
                dismissLoadingView();
                mQuestionBean = response.getData().get(0);
                refreshQuestionView();

            }

            @Override
            public void onResponseError(BaseBean errorMessage, boolean isNetError) {
                dismissLoadingView();
            }

        });
    }

    //创建texterea
    private void CreateTextView(ListQuestionBean questionBean) {
        View textEreaView = mlflater.inflate(R.layout.textarealayout, null);
        TextView textTitle = (TextView) textEreaView.findViewById(R.id.tv_title);
        textTitle.setText(questionBean.getQuestion_title());
        TextView textplaceholder = (TextView) textEreaView.findViewById(R.id.tv_placeholder);
        textplaceholder.setText(questionBean.getPlaceholder());
        mViewList.add(textEreaView);


    }

    //checkbox
    private void CreateCheckView(ListQuestionBean questionBean) {
        View checkBoxView = mlflater.inflate(R.layout.checkboxlayout, null);
        TextView textTitle = (TextView) checkBoxView.findViewById(R.id.tv_title);
        textTitle.setText(questionBean.getQuestion_title());
        LinearLayout checkBoxLayout1 = (LinearLayout) checkBoxView.findViewById(R.id.ly_checkbox1);
        LinearLayout checkBoxLayout2 = (LinearLayout) checkBoxView.findViewById(R.id.ly_checkbox2);
        for (int i = 0; i < questionBean.getForm_optional_value().size(); i++) {
            String checkBoxText = questionBean.getForm_optional_value().get(i);
            CheckBox checkBox = new CheckBox(checkBoxView.getContext());
            checkBox.setText(checkBoxText);
            if (i > 5) //一列控件只够放6个
                checkBoxLayout2.addView(checkBox);
            else
                checkBoxLayout1.addView(checkBox);
        }
        mViewList.add(checkBoxView);
    }

    //Radio
    private void CreateRadioView(ListQuestionBean questionBean) {
        View radioView = mlflater.inflate(R.layout.radiolayout, null);
        TextView textTitle = (TextView) radioView.findViewById(R.id.tv_title);
        textTitle.setText(questionBean.getQuestion_title());
        RadioGroup rgGroup = (RadioGroup) radioView.findViewById(R.id.rg_radio);
        for (String radioText :
                questionBean.getForm_optional_value()) {
            RadioButton radioBtn = new RadioButton(radioView.getContext());
            radioBtn.setText(radioText);
            rgGroup.addView(radioBtn);
        }
        mViewList.add(radioView);
    }

    //camera and record sound
    private void CreateImageAndRecordView() {
        View imageRecordView = mlflater.inflate(R.layout.image_soundlayout, null);
        mChooseIamge = imageRecordView.findViewById(R.id.iv_chooseimage);
        mShowIamge = imageRecordView.findViewById(R.id.iv_showimae);
        mplay_stop = imageRecordView.findViewById(R.id.play_stop);
        mSeekBar = imageRecordView.findViewById(R.id.seekBar);
        musicCur = imageRecordView.findViewById(R.id.music_cur);
        mplay_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMp3Path.isEmpty())
                    return;
                initMediaPlayer();
                if (!soundState) {
                    mplay_stop.setImageResource(R.mipmap.stop);
                    soundState = true;
                    mediaPlayer.start();//开始播放
                    mediaPlayer.seekTo(currentPosition);

                    //监听播放时回调函数
                    timer = new Timer();
                    timer.schedule(new TimerTask() {

                        Runnable updateUI = new Runnable() {
                            @Override
                            public void run() {
                                if (mediaPlayer==null)
                                    return;
                                musicCur.setText(format.format(mediaPlayer.getCurrentPosition()) + "");
                            }
                        };

                        @Override
                        public void run() {
                            if (!isSeekBarChanging) {
                                mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                                runOnUiThread(updateUI);
                            }
                        }
                    }, 0, 50);
                } else {
                    mplay_stop.setImageResource(R.mipmap.play);
                    soundState = false;
                    mediaPlayer.reset();//停止播放
                    initMediaPlayer();
                }

            }
        });
        mChooseIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //选择图片 choose image
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
        mSoundIamge = imageRecordView.findViewById(R.id.iv_choosesound);
        mSoundIamge.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

//                        mPop.showAtLocation(rl,Gravity.CENTER,0,0);
//
//                        mButton.setText("松开保存");
                        mAudioRecoderUtils.startRecord();


                        break;

                    case MotionEvent.ACTION_UP:

                        mAudioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
//                        mAudioRecoderUtils.cancelRecord();    //取消录音（不保存录音文件）
//                        mPop.dismiss();
//                        mButton.setText("按住说话");

                        break;
                }
                return true;
            }
        });
        mViewList.add(imageRecordView);
    }

    private void refreshQuestionView() {
        if (mQuestionBean == null)
            return;
        mViewList.clear();
        for (ListQuestionBean questionBean : mQuestionBean.getist_question()
                ) {
            if (questionBean.getForm_type().equals("textarea")) {
                CreateTextView(questionBean);
            } else if (questionBean.getForm_type().equals("checkbox")) {
                CreateCheckView(questionBean);

            } else if (questionBean.getForm_type().equals("radio")) {
                CreateRadioView(questionBean);
            } else if (questionBean.getForm_type().equals("text")) {
            }
        }
        CreateImageAndRecordView();
        chooseView(0);


    }

    private void chooseView(int iPos) {
        if (mViewList.size() <= 0 || iPos >= mViewList.size())
            return;
        pagerLayout.removeAllViews();
        pagerLayout.addView((View) mViewList.get(iPos));
        setPageDotSum(mViewList.size());
        setCurrentPageDot(iPos);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();

            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                //将选择的图片，显示在主界面的imageview上
                mShowIamge.setImageBitmap(bitmap);
                // 保存选择的图片文件 到指定目录
                //  SaveImage(bitmap,imageDir + "win_back.png");

            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(), e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 保存图像文件
    public void SaveImage(Bitmap bitmap, String filePath) {

        File file = new File(filePath);
        FileOutputStream fos;

        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
        intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
        sendBroadcast(intent);
    }
    private void uploadFireBaseFile(String filepath){

        Uri file = Uri.fromFile(new File(filepath));
        StorageReference riversRef = mStorageRef.child("images/rivers.jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    private void setPageDotSum(int sum) {
        mDotSum = sum;
    }

    private void setCurrentPageDot(int iCurrentPage) {
        mlydot.removeAllViews();
        for (int i = 0; i < mDotSum; i++) {
            ImageView imgeDot = new ImageView(this);
            imgeDot.setPadding(4, 4, 4, 4);
            if (i == iCurrentPage)
                imgeDot.setImageResource(R.mipmap.ellipse);
            else
                imgeDot.setImageResource(R.mipmap.backdot);
            mlydot.addView(imgeDot);
        }
    }

    @OnClick({R.id.btn_next})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                mViewPos++;
             if (mViewPos>=mViewList.size()){
                 mViewPos=mViewList.size()-1;
                 return;
             }
             chooseView(mViewPos);
             break;
        }

    }
}
