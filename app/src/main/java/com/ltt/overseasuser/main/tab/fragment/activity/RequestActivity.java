package com.ltt.overseasuser.main.tab.fragment.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.ltt.overseasuser.model.CitiesListBean;
import com.ltt.overseasuser.model.CountriesListBean;
import com.ltt.overseasuser.model.ListQuestionBean;
import com.ltt.overseasuser.model.PostRequestQuestionsBean;
import com.ltt.overseasuser.model.QuestionBean;
import com.ltt.overseasuser.model.QuestionDataBean;
import com.ltt.overseasuser.model.QuestionViewBean;
import com.ltt.overseasuser.model.SectionBean;
import com.ltt.overseasuser.model.SectionInitQuestionBean;
import com.ltt.overseasuser.model.UploadSucessBean;
import com.ltt.overseasuser.model.postRequestBean;
import com.ltt.overseasuser.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

import static android.widget.Toast.LENGTH_SHORT;

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
    private String authorization = XApplication.globalUserBean.getAccess_token();
    private AudioImageActivity audioImageView = null;
    private LocationActivity locationActivity = null;
    private RequestFinishActivity requstFinishActivity = null;
    private View requestUploadView = null;
    private List<File> mUploadFileList = new ArrayList<File>();
    private List<File> mUploadFaileFileList = new ArrayList<File>();
    @BindView(R.id.layall)
    LinearLayout pagerLayout;

    private final String CHECKBOX = "checkbox";
    private final String RADIO = "radio";
    private final String TEXTEREA = "textarea";
    private final String NUMBER = "number";
    private final String VOIDRECORD = "voicerecord";
    private final String RUEQESTUPLOAD = "uploadview";
    private final String RUEQESTFINISH = "finishview";
    private final String FILE = "file";
    private final String LOCATION = "location";

    private final int POSTUPLOADSuc = 201;
    private final int POSTUPLOADAFail = 401;
    private final int POSTSUCESS = 200;
    private final int POSTFAIL = 4001;
    private int postRequestStatus = -1;

    @Override
    protected int bindLayoutID() {
        return R.layout.activity_request;
    }

    @Override
    protected void prepareActivity() {

        init();
        //得到问题
        getQuestionList();

    }

    private void init() {
        bar = ActionBar.init(this);
        bar.setRight(R.mipmap.x3x, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //获取setciton
        mSectionId = this.getIntent().getExtras().getString("sectionid");
        mViewList = new ArrayList();
        mlflater = getLayoutInflater().from(RequestActivity.this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    //获取任务列表
    private void getQuestionList() {
        showLoadingView();
        SectionInitQuestionBean answerparam = new SectionInitQuestionBean();
        answerparam.setSection_id(mSectionId);
        Call<QuestionDataBean> call = RetrofitUtil.getAPIService().getInitQuestionList(answerparam, authorization);
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
        mViewList.add(new QuestionViewBean(TEXTEREA, textEreaView, questionBean.getQuestion_id()));


    }

    //创建number
    private void CreateNumberView(ListQuestionBean questionBean) {
        View numberView = mlflater.inflate(R.layout.requestnumberlayout, null);
        TextView textTitle = (TextView) numberView.findViewById(R.id.tv_title);
        textTitle.setText(questionBean.getQuestion_title());
        TextView textplaceholder = (TextView) numberView.findViewById(R.id.tv_placeholder);
        textplaceholder.setText(questionBean.getPlaceholder());
        mViewList.add(new QuestionViewBean(NUMBER, numberView, questionBean.getQuestion_id()));


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

        mViewList.add(new QuestionViewBean(CHECKBOX, checkBoxView, questionBean.getQuestion_id()));
    }

    //adio
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
                RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                String selectText = radioButton.getText().toString();
                setValue(questionBean.getQuestion_id(), selectText);
            }
        });
        mViewList.add(new QuestionViewBean(RADIO, radioView, questionBean.getQuestion_id()));
    }


    //camera and record sound
    private void CreateImageAndRecordView() {
        View imageRecordView = mlflater.inflate(R.layout.image_soundlayout, null);
        audioImageView = new AudioImageActivity(mlflater, this);
        mViewList.add(new QuestionViewBean(VOIDRECORD, audioImageView.mView, "0"));
    }

    //camera and record sound
    private void CreateLocationView(String tittle) {

        locationActivity = new LocationActivity(mlflater, this, tittle);
        mViewList.add(new QuestionViewBean(LOCATION, locationActivity.mView, "0"));
    }

    private void CreateRequestFinishView() {
        requstFinishActivity = new RequestFinishActivity(mlflater, this);
        mViewList.add(new QuestionViewBean(RUEQESTFINISH, requstFinishActivity.mView, "0"));
    }

    private void CreateRequestUploadView() {
        View uploadView = mlflater.inflate(R.layout.requestuploadlayout, null);
        requestUploadView = uploadView;
        Button btnRetry = uploadView.findViewById(R.id.btn_retry);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginUpload();
            }
        });
        mViewList.add(new QuestionViewBean(RUEQESTUPLOAD, uploadView, "0"));
    }

    private void setUploadFinish() {
        if (requestUploadView == null)
            return;
        Button btnRetry = requestUploadView.findViewById(R.id.btn_retry);
        btnRetry.setVisibility(View.INVISIBLE);
        TextView textView = requestUploadView.findViewById(R.id.tv_placeholder);
        textView.setText("All files have been uploaded，please continue！");
        textView.setTextColor(Color.parseColor("#222222"));
    }

    private void setUploadFail() {
        if (requestUploadView == null)
            return;
        List<String> faileFileList = new ArrayList<>();
        for (File file :
                mUploadFaileFileList) {
            String ss = String.format("%s file failed to upload\r\n", file.getName());
            faileFileList.add(ss);

        }
        TextView textView = requestUploadView.findViewById(R.id.tv_placeholder);
        textView.setText("");
        textView.setText(listToString(faileFileList));
        textView.setTextColor(Color.parseColor("#FF4081"));
    }

    private void refreshQuestionView() {
        if (mQuestionBean == null)
            return;
        mViewList.clear();
        for (ListQuestionBean questionBean : mQuestionBean.getist_question()
                ) {
            if (questionBean.getForm_type().equals(TEXTEREA)) {
                CreateTextView(questionBean);
            } else if (questionBean.getForm_type().equals(NUMBER)) {
                CreateNumberView(questionBean);

            } else if (questionBean.getForm_type().equals(CHECKBOX)) {
                CreateCheckView(questionBean);

            } else if (questionBean.getForm_type().equals(RADIO)) {
                CreateRadioView(questionBean);
            } else if (questionBean.getForm_type().equals(FILE)) {
                CreateImageAndRecordView();
            } else if (questionBean.getForm_type().equals(LOCATION)) {
                CreateLocationView(questionBean.getQuestion_title());
            } else if (questionBean.getForm_type().equals("text")) {
            }
        }
        //该上传得前屈条件
        if (mQuestionBean.isEnable_upload() && audioImageView != null)
            CreateRequestUploadView();
        CreateRequestFinishView();
        chooseView(0);


    }

    private void chooseView(int iPos) {
        if (mViewList.size() <= 0 || iPos >= mViewList.size())
            return;
        pagerLayout.removeAllViews();
        pagerLayout.addView(mViewList.get(iPos).getView());
        setPageDotSum(mViewList.size());
        setCurrentPageDot(iPos);
    }


    public void uploadFile(final File file) {
        if (file == null)
            return;
        String contenType = "";
        if (file.getName().endsWith(".jpg")) {
            contenType = "image/jpeg";
        } else if (file.getName().endsWith("png")) {
            contenType = "image/png";
        } else if (file.getName().endsWith(".pdf")) {
            contenType = "application/pdf";
        } else if (file.getName().endsWith(".mp3")) {
            contenType = "audio/mp3";
        }else if (file.getName().endsWith(".wav")) {
            contenType = "audio/wav";
        }
// 创建RequestBody，传入参数："multipart/form-data"，String
        RequestBody requestUploadid = RequestBody.create(MediaType.parse("multipart/form-data"), mQuestionBean.getUpload_id());
        RequestBody requestApiSecret = RequestBody.create(MediaType.parse("multipart/form-data"), mQuestionBean.getCreate_request_token());
        RequestBody requestImgFile = RequestBody.create(MediaType.parse(contenType), file);
        MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("upload", file.getName(), requestImgFile);
        Call<UploadSucessBean> call = RetrofitUtil.getAPIService().uploadflie(requestUploadid, requestApiSecret, requestImgPart, authorization);
        call.enqueue(new CustomerCallBack<UploadSucessBean>() {
            @Override
            public void onResponseResult(UploadSucessBean response) {

                UploadSucessBean a = response;
                //成功了移除文件
                removeUploadFile(file);
                mUploadFaileFileList.remove(file);
                //上传完成后开始上传问题
                if (mUploadFileList.isEmpty())
                    dismissLoadingView();
                if (mUploadFileList.isEmpty() && mUploadFaileFileList.isEmpty()) {
                    setUploadFinish();
                }

            }

            @Override
            public void onResponseError(BaseBean errorMessage, boolean isNetError) {
                BaseBean re = errorMessage;
                removeUploadFile(file);
                if (mUploadFileList.isEmpty())
                    dismissLoadingView();
                if (!mUploadFaileFileList.contains(file))
                    mUploadFaileFileList.add(file);
                setUploadFail();
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

    private void clickBtnNext() {
        if (mViewList.isEmpty())
            return;
        if (mViewList.get(mViewPos).getViewType().equals(RUEQESTFINISH)) {
            finish();
        } else if (mViewList.get(mViewPos).getViewType().equals(TEXTEREA)) {

            EditText textView = mViewList.get(mViewPos).getView().findViewById(R.id.et_mssage_code);
            mViewList.get(mViewPos).addValue(textView.getText().toString());

        } else if (mViewList.get(mViewPos).getViewType().equals(CHECKBOX)) {

            LinearLayout checkLy = mViewList.get(mViewPos).getView().findViewById(R.id.ly_checkbox);
            for (int i = 0; i < checkLy.getChildCount(); i++) {
                if (checkLy.getChildAt(i) instanceof CheckBox) {
                    //操作代码
                    if (((CheckBox) checkLy.getChildAt(i)).isChecked())
                        mViewList.get(mViewPos).addValue(((CheckBox) checkLy.getChildAt(i)).getText().toString());
                }
            }
        } else if (mViewList.get(mViewPos).getViewType().equals(NUMBER)) {

            EditText nuberView = mViewList.get(mViewPos).getView().findViewById(R.id.et_mssage_code);
            mViewList.get(mViewPos).addValue(nuberView.getText().toString());

        }
        //判断是否填写答案
        if(!(mViewList.get(mViewPos).getViewType().equals(RUEQESTFINISH)||mViewList.get(mViewPos).getViewType().equals(RUEQESTUPLOAD)||
        mViewList.get(mViewPos).getViewType().equals(FILE)||mViewList.get(mViewPos).getViewType().equals(LOCATION))){
           if( mViewList.get(mViewPos).getValue().isEmpty()){
               ToastUtils.showToast("Please answer the question first!");
               return;
           }
        }
        mViewPos++;
        if (mViewPos >= mViewList.size()) {
            mViewPos = mViewList.size() - 1;
            return;
        }
        chooseView(mViewPos);
        //进入最后界面，完成上传，问题上传，结束
        if (mViewList.get(mViewPos).getViewType().equals(RUEQESTUPLOAD)) {
            beginUpload();
        } else if (mViewList.get(mViewPos).getViewType().equals(RUEQESTFINISH)) {
            //上传
            postRequest();
        }
    }

    private void postRequest() {
        showLoadingView();
        List<PostRequestQuestionsBean> questions = new ArrayList();
        for (int i = 0; i < mViewList.size(); i++) {
            PostRequestQuestionsBean questionBean = new PostRequestQuestionsBean();
            if (!mViewList.get(i).getReqeustid().isEmpty()) {
                questionBean.setQuestion_id(Integer.parseInt(mViewList.get(i).getReqeustid()));
                questionBean.setValue(listToString(mViewList.get(i).getValue()));
                questions.add(questionBean);
            }

        }
        postRequestBean answerparam = new postRequestBean();
        answerparam.setCreate_request_token(mQuestionBean.getCreate_request_token());
        answerparam.setQuestions(questions);
        if (locationActivity != null) {
            answerparam.setCity(locationActivity.mCurrentCity);
            answerparam.setCountry_id(locationActivity.mCurrentCountriId);
            answerparam.setState_id(locationActivity.mCurrentStateId);
        }

        Call<BaseBean> call = RetrofitUtil.getAPIService().requestcreate(answerparam, authorization);
        call.enqueue(new CustomerCallBack<BaseBean>() {
            @Override
            public void onResponseResult(BaseBean response) {
                dismissLoadingView();
                postRequestStatus = POSTSUCESS;
                requstFinishActivity.postFinishSuc();

            }

            @Override
            public void onResponseError(BaseBean errorMessage, boolean isNetError) {
                dismissLoadingView();
                postRequestStatus = POSTFAIL;
                requstFinishActivity.postFinishFail();
            }

        });
    }

    private void setValue(String sRequestid, String value) {
        if (sRequestid.isEmpty() || value.isEmpty())
            return;
        for (int i = 0; i < mViewList.size(); i++) {
            if (mViewList.get(i).getReqeustid().equals(sRequestid)) {
                mViewList.get(i).addValue(value);
            }
        }
    }


    //接收选择完成的pdf和图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {//选完图片后保存
            if (requestCode == 1) {
                if (audioImageView == null)
                    return;

                Uri uri = data.getData();
                audioImageView.setImageShow(uri);


            } else if (requestCode == 2) {
                //选完pdf文件后保存
                if (audioImageView == null)
                    return;
                Uri uri = data.getData();
                audioImageView.setPdfShow(uri);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //上传文件
    public void beginUpload() {
        mUploadFileList.clear();
        //只有首次才能加载
        if (mUploadFaileFileList.isEmpty())
            mUploadFileList = audioImageView.getUploadFileList();
        //将所有失败得文件转给上传容器
        showLoadingView();
        Iterator<File> stuIter = mUploadFaileFileList.iterator();
        while (stuIter.hasNext()) {
            File file = stuIter.next();
            if (!mUploadFileList.contains(file))
                mUploadFileList.add(file);
           stuIter.remove();
        }
        if (!mUploadFileList.isEmpty())
            showLoadingView();
        for (File uploadFile : mUploadFileList) {
            uploadFile(uploadFile);
        }
    }

    public void removeUploadFile(File file) {
        mUploadFileList.remove(file);
    }

    public static String listToString(List<String> stringList) {
        if (stringList == null || stringList.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }
}
