package com.ltt.overseasuser.main.tab.fragment.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ltt.overseasuser.R;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

public class RequestActivity extends BaseActivity{
   // @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.ly_dot)
    LinearLayout mlydot;
    private ArrayList mViewList;
    private QuestionBean mQuestionBean;
    private  LayoutInflater mlflater;
    private RequestAdapter mRequestAdapter;
    private ActionBar bar;
    private int mDotSum;
    private String mSectionId;


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
    private void refreshQuestionView(){
        if (mQuestionBean==null)
            return;
        mViewList.clear();
        mRequestAdapter.clear();
        for (ListQuestionBean questionBean:mQuestionBean.getist_question()
             ) {
            if (questionBean.getForm_type().equals("textarea")){
                View textEreaView = mlflater.inflate(R.layout.textarealayout, null);
                TextView textTitle= (TextView) textEreaView.findViewById(R.id.tv_title);
                textTitle.setText(questionBean.getQuestion_title());
                TextView textplaceholder= (TextView) textEreaView.findViewById(R.id.tv_placeholder);
                textplaceholder.setText(questionBean.getPlaceholder());
                mViewList.add(textEreaView);

            }
            else if (questionBean.getForm_type().equals("checkbox"))
            {
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
                mViewList.add(checkBoxView);

            }
            else if (questionBean.getForm_type().equals("radio"))
            {
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
                mViewList.add(radioView);

            }
            else if (questionBean.getForm_type().equals("text"))
            {
            }
        }
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
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

//根据屏幕信息设置ViewPager广告容器的宽高
        viewPager.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, dm.heightPixels * 2 / 5));

//将ViewPager容器设置到布局文件父容器中
        pagerLayout.addView(viewPager);
    }
    private int getItem(int i){
        return viewPager.getCurrentItem() + i;
    }


    @OnClick({R.id.btn_next})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1,true);
                break;
        }
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
