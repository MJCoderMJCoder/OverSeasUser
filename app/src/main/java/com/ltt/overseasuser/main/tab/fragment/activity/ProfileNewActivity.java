package com.ltt.overseasuser.main.tab.fragment.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileNewActivity extends BaseActivity {
    @BindView(R.id.tv_my_profilenew)
    TextView tvMyProfilenew;
    @BindView(R.id.tv_firstname)
    TextView tvFirstname;
    @BindView(R.id.tv_firstnamechange)
    TextView tvFirstnamechange;
    @BindView(R.id.iv_firstnamechange)
    ImageView ivFirstnamechange;
    @BindView(R.id.tv_lastname)
    TextView tvLastname;
    @BindView(R.id.tv_lastnamechange)
    TextView tvLastnamechange;
    @BindView(R.id.iv_lastnamechange)
    ImageView ivLastnamechange;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_emailchange)
    TextView tvEmailchange;
    @BindView(R.id.iv_emailchange)
    ImageView ivEmailchange;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_addresschange)
    TextView tvAddresschange;
    @BindView(R.id.iv_addresschange)
    ImageView ivAddresschange;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_statechange)
    TextView tvStatechange;
    @BindView(R.id.iv_statechange)
    ImageView ivStatechange;
    @BindView(R.id.tv_postcode)
    TextView tvPostcode;
    @BindView(R.id.tv_postcodechange)
    TextView tvPostcodechange;
    @BindView(R.id.iv_pochangechange)
    ImageView ivPochangechange;
    @BindView(R.id.tv_contact)
    TextView tvContact;
    @BindView(R.id.tv_contactchange)
    TextView tvContactchange;
    @BindView(R.id.iv_contactchange)
    ImageView ivContactchange;
    @BindView(R.id.btn_left_profile)
    ImageView btnLeftProfile;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_edit_right_profile)
    TextView tvEditRightProfile;

    @Override
    protected int bindLayoutID() {
        return R.layout.activity_profile;
    }

    @Override
    protected void prepareActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_left_profile, R.id.tv_edit_right_profile, R.id.iv_firstnamechange, R.id.iv_lastnamechange, R.id.iv_emailchange, R.id.iv_addresschange, R.id.iv_statechange, R.id.iv_pochangechange, R.id.iv_contactchange})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_firstnamechange:
                //gittest
                break;
            case R.id.iv_lastnamechange:
                break;
            case R.id.iv_emailchange:
                break;
            case R.id.iv_addresschange:
                break;
            case R.id.iv_statechange:
                break;
            case R.id.iv_pochangechange:
                break;
            case R.id.iv_contactchange:
                break;

            case R.id.btn_left_profile:
                finish();
                break;
            case R.id.tv_edit_right_profile:
                break;
        }
    }

}
