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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.EditText;
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
import com.ltt.overseasuser.XApplication;
import com.ltt.overseasuser.base.AudioRecoderUtils;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.base.BaseBean;
import com.ltt.overseasuser.base.Xcircleindicator;
import com.ltt.overseasuser.core.ActionBar;
import com.ltt.overseasuser.http.CustomerCallBack;
import com.ltt.overseasuser.http.RetrofitUtil;
import com.ltt.overseasuser.main.tab.fragment.adapter.RequestAdapter;
import com.ltt.overseasuser.model.ListQuestionBean;
import com.ltt.overseasuser.model.PostRequestQuestionsBean;
import com.ltt.overseasuser.model.QuestionBean;
import com.ltt.overseasuser.model.QuestionDataBean;
import com.ltt.overseasuser.model.QuestionViewBean;
import com.ltt.overseasuser.model.postRequestBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

public class RequestActivity extends BaseActivity {

    private StorageReference mStorageRef;//firebase storage
    @BindView(R.id.ly_dot)
    LinearLayout mlydot;
    @BindView(R.id.btn_next)
    Button btn_next;
    private List<QuestionViewBean> mViewList;
    private int mViewPos = 0;
    private QuestionBean mQuestionBean;
    private LayoutInflater mlflater;
    private ActionBar bar;
    private int mDotSum;
    private String mSectionId;
    private ImageView mChooseIamge;//选择照片
    private ImageView mShowIamge;  //显示照片
    private ImageView mSoundIamge;
    private ImageView mChoosePdf;
    private TextView mTvPdfName;
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
    private String msCurRequestionId;
    private String msCurRequestionVal;
    private Uri mPicPath=null;
    private Uri mPdfFilePath=null;
    int maxVolume, currentVolume;
    private String authorization = XApplication.globalUserBean.getAccess_token();
    private boolean isSeekBarChanging;//互斥变量，防止进度条与定时器冲突。
    private int currentPosition;//当前音乐播放的进度
    @BindView(R.id.layall)
     LinearLayout pagerLayout;
    SimpleDateFormat format;

    private final String CHECKBOX ="checkbox";
    private final String RADIO="radio";
    private final String TEXTEREA="textarea";
    private final String NUMBER="number";
    private final String VOIDRECORD = "voicerecord";
    private final String RUEQESTFINISH = "finishview";

    private int postRequestStatus=-1;
    private int postImageStatus=-1;
    private int postVoiceStatus=-1;
    private final int POSTSUCESS=0;
    private final int POSTFAIL=1;

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
        initFirbaseStore();
        initVoice();
        mSectionId = this.getIntent().getExtras().getString("sectionid");
        mViewList = new ArrayList();
        mlflater = getLayoutInflater().from(RequestActivity.this);
        getQuestionList();


    }
    private void initFirbaseStore(){
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }
    //初始化音频相关功能代码
    private  void initVoice(){

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
        mAudioRecoderUtils.stopRecord();
        mAudioRecoderUtils.cancelRecord();

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
        mViewList.add(new QuestionViewBean(TEXTEREA,textEreaView,questionBean.getQuestion_id()));


    }
    //创建number
    private void CreateNumberView(ListQuestionBean questionBean) {
        View numberView = mlflater.inflate(R.layout.requestnumberlayout, null);
        TextView textTitle = (TextView) numberView.findViewById(R.id.tv_title);
        textTitle.setText(questionBean.getQuestion_title());
        TextView textplaceholder = (TextView) numberView.findViewById(R.id.tv_placeholder);
        textplaceholder.setText(questionBean.getPlaceholder());
        mViewList.add(new QuestionViewBean(NUMBER,numberView,questionBean.getQuestion_id()));


    }
    //checkbox
    private void CreateCheckView(ListQuestionBean questionBean) {
        View checkBoxView = mlflater.inflate(R.layout.checkboxlayout, null);
        TextView textTitle = (TextView) checkBoxView.findViewById(R.id.tv_title);
        textTitle.setText(questionBean.getQuestion_title());
        LinearLayout checkBoxLayout1 = (LinearLayout) checkBoxView.findViewById(R.id.ly_checkbox);

        for (int i = 0; i < questionBean.getForm_optional_value().size(); i++) {
            String checkBoxText = questionBean.getForm_optional_value().get(i);
            CheckBox checkBox = new CheckBox(checkBoxView.getContext());
            checkBox.setText(checkBoxText);
            checkBoxLayout1.addView(checkBox);
        }

        mViewList.add(new QuestionViewBean(CHECKBOX,checkBoxView,questionBean.getQuestion_id()));
    }

    //Radio
    private void CreateRadioView(final ListQuestionBean questionBean) {
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
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
                String selectText = radioButton.getText().toString();
                setValue(questionBean.getQuestion_id(),selectText);
            }
        });
        mViewList.add(new QuestionViewBean(RADIO,radioView,questionBean.getQuestion_id()));
    }

    //点击语音播放按钮
private void clickPlayVoice(){
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

//点击选择图片文件
private void clickChooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
    //点击选择pdf文件
    private void clickchoosePdfFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 2);

    }
    //camera and record sound
    private void CreateImageAndRecordView() {
        View imageRecordView = mlflater.inflate(R.layout.image_soundlayout, null);
        mChooseIamge = imageRecordView.findViewById(R.id.iv_chooseimage);//选择照片
        mChoosePdf = imageRecordView.findViewById(R.id.iv_choosefile);
//        mShowIamge = imageRecordView.findViewById(R.id.iv_showimae);
        mplay_stop = imageRecordView.findViewById(R.id.play_stop);
        mSeekBar = imageRecordView.findViewById(R.id.seekBar);
        musicCur = imageRecordView.findViewById(R.id.voice_cur);
        mTvPdfName = imageRecordView.findViewById(R.id.tv_pdf);
        mplay_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickPlayVoice();
            }
        });
        mChooseIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //选择图片 choose image
                clickChooseFile();
            }
        });
        mChoosePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickchoosePdfFile();
            }
        });
        mSoundIamge = imageRecordView.findViewById(R.id.iv_choosesound);
        mSoundIamge.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
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
        mViewList.add(new QuestionViewBean(VOIDRECORD,imageRecordView,"0"));
    }
    private void CreateRequestFinishView() {
        View finishView = mlflater.inflate(R.layout.requestfinishlayout, null);
        TextView textTitle = (TextView) finishView.findViewById(R.id.tv_title);
        textTitle.setText("Finish Reuqest");
        TextView textShow = (TextView) finishView.findViewById(R.id.tv_placeholder);
        textShow.setText("You request has success post!");
        mViewList.add(new QuestionViewBean(RUEQESTFINISH,finishView,"0"));
    }

    private void refreshQuestionView() {
        if (mQuestionBean == null)
            return;
        mViewList.clear();
        for (ListQuestionBean questionBean : mQuestionBean.getist_question()
                ) {
            if (questionBean.getForm_type().equals(TEXTEREA)) {
                CreateTextView(questionBean);
            } else if (questionBean.getForm_type().equals(NUMBER)){
                CreateNumberView(questionBean);

            }else if (questionBean.getForm_type().equals(CHECKBOX)) {
                CreateCheckView(questionBean);

            } else if (questionBean.getForm_type().equals(RADIO)) {
                CreateRadioView(questionBean);
            } else if (questionBean.getForm_type().equals("text")) {
            }
        }
        CreateImageAndRecordView();
        CreateRequestFinishView();
        chooseView(0);


    }

    private void chooseView(int iPos) {
        if (mViewList.size() <= 0 || iPos >= mViewList.size())
            return;
        if(mViewList.get(mViewPos).getViewType().equals(RUEQESTFINISH)){
            btn_next.setText("Finish");
            String sError="";
            if (postImageStatus==POSTFAIL){
                sError +="comit image failed!\n";
            }
            if (postVoiceStatus==POSTFAIL){
                sError +="comit voice failed!\n";
            }
            if (postRequestStatus==POSTFAIL){
                sError +="comit request failed!\n";
            }
            if (!sError.isEmpty()){
                TextView tv  = mViewList.get(mViewPos).getView().findViewById(R.id.tv_placeholder);
                tv.setText(sError);
            }


        }
        pagerLayout.removeAllViews();
        pagerLayout.addView( mViewList.get(iPos).getView());
        setPageDotSum(mViewList.size());
        setCurrentPageDot(iPos);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {//选完图片后保存
            if (requestCode==1){
                Uri uri = data.getData();
                mPicPath=uri;
                ContentResolver cr = this.getContentResolver();

                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    //将选择的图片，显示在主界面的imageview上
//                    mShowIamge.setImageBitmap(bitmap);
                    // 保存选择的图片文件 到指定目录
                    //  SaveImage(bitmap,imageDir + "win_back.png");

                } catch (FileNotFoundException e) {
                    Log.e("Exception", e.getMessage(), e);
                }
            }else if (requestCode==2){
             //选完pdf文件后保存
                    mPdfFilePath = data.getData();
                    mTvPdfName.setText(getFileName(getRealFilePath(getContext(),mPdfFilePath)));
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
    private void uploadFireBaseFile(){

        if (mPicPath==null)
            return;
        showLoadingView();
        StorageReference riversRef = mStorageRef.child("images/"+mAudioRecoderUtils.getNowTime()+".jpg");
        riversRef.putFile(mPicPath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        dismissLoadingView();
                        postImageStatus=POSTSUCESS;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        dismissLoadingView();
                       String sMsg =   exception.getMessage();
                       postImageStatus=POSTFAIL;
                    }
                });
    }
    private void uploadFireBaseVoiceFile(){
        if (mMp3Path.isEmpty())
            return;
        showLoadingView();
        Uri mp3Uri = Uri.fromFile(new File(mMp3Path));
        StorageReference riversRef = mStorageRef.child("voice/"+mAudioRecoderUtils.getNowTime()+".mp3");
        riversRef.putFile(mp3Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get a URL to the uploaded content
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                dismissLoadingView();
                postVoiceStatus=POSTSUCESS;
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        dismissLoadingView();
                        String sMsg =   exception.getMessage();
                        postVoiceStatus=POSTFAIL;
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
              clickBtnNext();
             break;
        }

    }
    private void clickBtnNext(){
        if (mViewList.get(mViewPos).getViewType().equals(RUEQESTFINISH)){
            finish();
        }
       else if (mViewList.get(mViewPos).getViewType().equals(VOIDRECORD)) {
            //last comit
            postRequest();
            uploadFireBaseFile();
            uploadFireBaseVoiceFile();
        }else{
            if (mViewList.get(mViewPos).getViewType().equals(TEXTEREA)) {
                EditText textView = mViewList.get(mViewPos).getView().findViewById(R.id.et_mssage_code);
                mViewList.get(mViewPos).addValue(textView.getText().toString());
            }else if (mViewList.get(mViewPos).getViewType().equals(CHECKBOX)){
                LinearLayout checkLy=    mViewList.get(mViewPos).getView().findViewById(R.id.ly_checkbox);
                for(int i = 0; i < checkLy.getChildCount(); i++){
                    if(checkLy.getChildAt(i) instanceof CheckBox){
                        //操作代码
                        if (((CheckBox) checkLy.getChildAt(i)).isChecked())
                            mViewList.get(mViewPos).addValue(((CheckBox) checkLy.getChildAt(i)).getText().toString());
                    }
                }
            }else if (mViewList.get(mViewPos).getViewType().equals(NUMBER)){
                EditText nuberView = mViewList.get(mViewPos).getView().findViewById(R.id.et_mssage_code);
                mViewList.get(mViewPos).addValue(nuberView.getText().toString());
            }

        }

        mViewPos++;
        if (mViewPos>=mViewList.size()){
            mViewPos=mViewList.size()-1;
            return;
        }
        chooseView(mViewPos);
    }
   private void postRequest(){
       showLoadingView();
       List<PostRequestQuestionsBean> questions = new ArrayList();
      for (int i=0;i<mViewList.size()-2;i++){
          PostRequestQuestionsBean questionBean = new PostRequestQuestionsBean();
          questionBean.setQuestion_id(Integer.parseInt(mViewList.get(i).getReqeustid()));
          questionBean.setValue(mViewList.get(i).getValue().toString());
          questions.add(questionBean);
      }
       postRequestBean answerparam = new postRequestBean();
       answerparam.setSection_id(Integer.parseInt(mSectionId));
       answerparam.setQuestions(questions);
       Call<BaseBean> call = RetrofitUtil.getAPIService().requestcreate(answerparam,authorization);
       call.enqueue(new CustomerCallBack<BaseBean>() {
           @Override
           public void onResponseResult(BaseBean response) {
               dismissLoadingView();
                postRequestStatus = POSTSUCESS;

           }

           @Override
           public void onResponseError(BaseBean errorMessage, boolean isNetError) {
               dismissLoadingView();
               postRequestStatus = POSTFAIL;
           }

       });
    }
    private void setValue(String sRequestid,String value){
       if(sRequestid.isEmpty()||value.isEmpty())
           return;
       for (int i=0;i<mViewList.size();i++){
           if(mViewList.get(i).getReqeustid().equals(sRequestid)){
               mViewList.get(i).addValue(value);
           }
       }
    }

    //由Uri转成路径的方法
    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    public String getFileName(String pathandname){

        int start=pathandname.lastIndexOf("/");
        int end=pathandname.lastIndexOf(".");
        if(start!=-1 && end!=-1){
            return pathandname.substring(start+1,end);
        }else{
            return null;
        }

    }


}
