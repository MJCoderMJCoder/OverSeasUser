package com.ltt.overseasuser.main.tab.fragment.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

public class RequestActivity extends BaseActivity{
   // @BindView(R.id.view_pager)
    ViewPager viewPager;
//    @BindView(R.id.btn_next)
//    Button btnNext;
    @BindView(R.id.ly_dot)
    LinearLayout mlydot;
    private ArrayList mViewList;
    private QuestionBean mQuestionBean;
    private  LayoutInflater mlflater;
    private RequestAdapter mRequestAdapter;
    private ActionBar bar;
    private int mDotSum;
    private String mSectionId;
    private ImageView mChooseIamge;
    private ImageView mShowIamge;
    private ImageView mSoundIamge;
    public String imageDir = "/sdcard/ht/";

    private AudioRecoderUtils mAudioRecoderUtils;
    @Override
    protected int bindLayoutID() {
        return R.layout.activity_request;
    }

    @Override
    protected void prepareActivity() {

        bar=ActionBar.init(this);
        bar.setRight(R.mipmap.x3x, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initVierpage();
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

            }
        });


        mSectionId = this.getIntent().getExtras().getString("sectionid");
        mViewList = new ArrayList<View>();
        mRequestAdapter= new RequestAdapter();
        mlflater = getLayoutInflater().from(RequestActivity.this);
        viewPager.setAdapter(mRequestAdapter);
       getQuestionList();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int arg0) {
              //  mXcircleindicator.setCurrentPage(arg0);
                setCurrentPageDot(viewPager.getCurrentItem());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
    private void getQuestionList(){
        showLoadingView();
        Call<QuestionDataBean> call = RetrofitUtil.getAPIService().getQuestionList(mSectionId);
        call.enqueue(new CustomerCallBack<QuestionDataBean>() {
            @Override
            public void onResponseResult(QuestionDataBean response) {
                dismissLoadingView();
                mQuestionBean=response.getData().get(0);
                refreshQuestionView();

            }

            @Override
            public void onResponseError(BaseBean errorMessage, boolean isNetError) {
                dismissLoadingView();
            }

        });
    }
    //创建texterea
    private void CreateTextView(ListQuestionBean questionBean){
        View textEreaView = mlflater.inflate(R.layout.textarealayout, null);
        TextView textTitle= (TextView) textEreaView.findViewById(R.id.tv_title);
        textTitle.setText(questionBean.getQuestion_title());
        TextView textplaceholder= (TextView) textEreaView.findViewById(R.id.tv_placeholder);
        textplaceholder.setText(questionBean.getPlaceholder());
        mViewList.add(textEreaView);
        Button btn=textEreaView.findViewById(R.id.btn_next);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1,true);
            }
        });

    }
    //checkbox
    private void CreateCheckView(ListQuestionBean questionBean){
        View checkBoxView = mlflater.inflate(R.layout.checkboxlayout, null);
        TextView textTitle= (TextView) checkBoxView.findViewById(R.id.tv_title);
        textTitle.setText(questionBean.getQuestion_title());
        LinearLayout  checkBoxLayout1 = (LinearLayout) checkBoxView.findViewById(R.id.ly_checkbox1);
        LinearLayout  checkBoxLayout2 = (LinearLayout) checkBoxView.findViewById(R.id.ly_checkbox2);
        for (int i=0;i<questionBean.getForm_optional_value().size();i++) {
            String checkBoxText = questionBean.getForm_optional_value().get(i);
            CheckBox checkBox = new CheckBox(checkBoxView.getContext());
            checkBox.setText(checkBoxText);
            if(i>5) //一列控件只够放6个
                checkBoxLayout2.addView(checkBox);
            else
                checkBoxLayout1.addView(checkBox);
        }
        Button btn=checkBoxView.findViewById(R.id.btn_next);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1,true);
            }
        });
        mViewList.add(checkBoxView);
    }
    //Radio
    private void CreateRadioView(ListQuestionBean questionBean){
        View radioView = mlflater.inflate(R.layout.radiolayout, null);
        TextView textTitle= (TextView) radioView.findViewById(R.id.tv_title);
        textTitle.setText(questionBean.getQuestion_title());
        RadioGroup rgGroup = (RadioGroup) radioView.findViewById(R.id.rg_radio);
        for (String radioText:
                questionBean.getForm_optional_value()) {
            RadioButton radioBtn = new RadioButton(radioView.getContext());
            radioBtn.setText(radioText);
            rgGroup.addView(radioBtn);
        }
        Button btn=radioView.findViewById(R.id.btn_next);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1,true);
            }
        });
        mViewList.add(radioView);
    }
//camera and record sound
    private void CreateImageAndRecordView(){
        View imageRecordView = mlflater.inflate(R.layout.image_soundlayout, null);
        mChooseIamge = imageRecordView.findViewById(R.id.iv_chooseimage);
        mShowIamge = imageRecordView.findViewById(R.id.iv_showimae);
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

                switch (event.getAction()){

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
    private void refreshQuestionView(){
        if (mQuestionBean==null)
            return;
        mViewList.clear();
        mRequestAdapter.clear();
        for (ListQuestionBean questionBean:mQuestionBean.getist_question()
             ) {
            if (questionBean.getForm_type().equals("textarea")){
                CreateTextView(questionBean);
            }
            else if (questionBean.getForm_type().equals("checkbox")) {
                CreateCheckView(questionBean);

            }
            else if (questionBean.getForm_type().equals("radio"))
            {
                CreateRadioView(questionBean);
            }
            else if (questionBean.getForm_type().equals("text"))
            {
            }
        }
        CreateImageAndRecordView();
        mRequestAdapter.addAll(mViewList);
        setPageDotSum(mViewList.size());
        setCurrentPageDot(0);


    }
    private void initVierpage(){
        //从布局文件中获取ViewPager父容器
        LinearLayout  pagerLayout = (LinearLayout) findViewById(R.id.layall);
        //创建ViewPager
        viewPager = new ViewPager(this);
        //获取屏幕像素相关信息
        //   DisplayMetrics dm = new DisplayMetrics();
        //   getWindowManager().getDefaultDisplay().getMetrics(dm);
        //根据屏幕信息设置ViewPager广告容器的宽高
        //   viewPager.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, dm.heightPixels * 2 / 5));
        pagerLayout.addView(viewPager);
    }
    private int getItem(int i){
        return viewPager.getCurrentItem() + i;
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

            }

            catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    // 保存图像文件
    public void SaveImage(Bitmap bitmap,String filePath){

        File file = new File(filePath);
        FileOutputStream fos;

        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
        intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
        sendBroadcast(intent);
    }


    private void setPageDotSum(int sum){
        mDotSum = sum;
    }
    private void setCurrentPageDot(int iCurrentPage){
        mlydot.removeAllViews();
        for (int i=0;i<mDotSum;i++){
            ImageView imgeDot = new ImageView(this);
            imgeDot.setPadding(4,4,4,4);
            if(i==iCurrentPage)
                imgeDot.setImageResource(R.mipmap.ellipse);
            else
                imgeDot.setImageResource(R.mipmap.backdot);
            mlydot.addView(imgeDot);
        }
    }
}
