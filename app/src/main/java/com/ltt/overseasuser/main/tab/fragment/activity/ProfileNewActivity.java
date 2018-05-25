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
        bar = ActionBar.init(this);
        bar.setLeft(R.mipmap.back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bar.setTitle("My Profile");
        bar.setRightButton2("Edit", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isshowchanger) {
                    isshowchanger = !isshowchanger;
                    ivFirstnamechange.setVisibility(View.VISIBLE);
                    ivLastnamechange.setVisibility(View.VISIBLE);
                    ivEmailchange.setVisibility(View.VISIBLE);
                    ivAddresschange.setVisibility(View.VISIBLE);
                    ivStatechange.setVisibility(View.VISIBLE);
                    ivPochangechange.setVisibility(View.VISIBLE);
                    ivContactchange.setVisibility(View.VISIBLE);
                } else {
                    isshowchanger = !isshowchanger;
                    ivFirstnamechange.setVisibility(View.INVISIBLE);
                    ivLastnamechange.setVisibility(View.INVISIBLE);
                    ivEmailchange.setVisibility(View.INVISIBLE);
                    ivAddresschange.setVisibility(View.INVISIBLE);
                    ivStatechange.setVisibility(View.INVISIBLE);
                    ivPochangechange.setVisibility(View.INVISIBLE);
                    ivContactchange.setVisibility(View.INVISIBLE);
                }


            }
        });
        initData();
    }

    private void initData() {
        Call<UserProfileBean> userProfileBeanCall = RetrofitUtil.getAPIService().getUserProfileLists();
        userProfileBeanCall.enqueue(new Callback<UserProfileBean>() {
            @Override
            public void onResponse(Call<UserProfileBean> call, Response<UserProfileBean> response) {
                userdata = response.body().getData();
                updateUI(userdata);
            }

            @Override
            public void onFailure(Call<UserProfileBean> call, Throwable t) {

            }
        });
    }

    private void updateUI(UserProfileBean.DataBean data) {
        tvFirstnamechange.setText(data.getFirstname());
        tvLastnamechange.setText(data.getLastname());
        tvEmailchange.setText(data.getEmail());
        tvAddresschange.setText(data.getAddress());
        tvStatechange.setText(data.getState());
        tvPostcodechange.setText(data.getPostcode());
        tvContactchange.setText(data.getCompany_contact_number());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({ R.id.iv_firstnamechange, R.id.iv_lastnamechange, R.id.iv_emailchange, R.id.iv_addresschange, R.id.iv_statechange, R.id.iv_pochangechange, R.id.iv_contactchange})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_firstnamechange:
                updateUserCon("firstname");
                break;
            case R.id.iv_lastnamechange:
                updateUserCon("lastname");
                break;
            case R.id.iv_emailchange:
                updateUserCon("email");
                break;
            case R.id.iv_addresschange:
                updateUserCon("address");
                break;
            case R.id.iv_statechange:
                updateUserCon("state");
                break;
            case R.id.iv_pochangechange:
                updateUserCon("postcode");
                break;
            case R.id.iv_contactchange:
                updateUserCon("phone");
                break;
            case R.id.btn_left_profile:
                finish();
                break;
            case R.id.tv_edit_right_profile:
                finish();
                break;
        }
    }

    private void updateUserCon(final String con) {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.update_usermsg_popupview, null);
            final EditText et_con = view.findViewById(R.id.et_con);
            Button bt_submit =  view.findViewById(R.id.bt_submit);

            bt_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upCon = et_con.getText().toString();
                    update_change(con,upCon);
                    et_con.setText("");
                }
            });
            // 创建一个PopuWidow对象
            popupWindow = new PopupWindow(view, 800, 400);
        }

        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);

        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        int xPos = windowManager.getDefaultDisplay().getWidth()/2
                - popupWindow.getWidth()/2;
        Log.i("coder", "xPos:" + xPos);

        popupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0,0);
    }

    private void update_change(String con, String upCon) {
        updateUserBean userParams = new updateUserBean();
        if (con.equals("firstname")) {
            userParams.setFirstname(upCon);
        } else {
            userParams.setFirstname(tvFirstnamechange.getText().toString());
        }
        if (con.equals("lastname")) {
            userParams.setLastname(upCon);
        } else {
            userParams.setLastname(tvLastnamechange.getText().toString());
        }
        if (con.equals("email")) {
            userParams.setEmail(upCon);
        } else {
            userParams.setEmail(tvEmailchange.getText().toString());
        }
        if (con.equals("address")) {
            userParams.setAddress(upCon);
        } else {
            userParams.setAddress(tvAddresschange.getText().toString());
        }
        if (con.equals("country_id")) {
            userParams.setCountry_id(Integer.getInteger(upCon));
        } else {
            userParams.setCountry_id(158);
        }
        if (con.equals("state")) {
            userParams.setState(upCon);
        } else {
            userParams.setState(tvStatechange.getText().toString());
        }
        if (con.equals("phone")) {
            userParams.setPhone(upCon);
        } else {
            userParams.setPhone(tvContactchange.getText().toString());
        }
        if (con.equals("postcode")) {
            userParams.setPostcode(upCon);
        } else {
            userParams.setPostcode(tvPostcodechange.getText().toString());
        }

        Call<BaseBean> loginCall = RetrofitUtil.getAPIService().updateUserProfileLists(userParams);
        loginCall.enqueue(new CustomerCallBack<BaseBean>() {
            @Override
            public void onResponseResult(BaseBean response) {
                int code=response.getCode();
                Log.d("code:", ""+code);
                if(code==200) {
                    popupWindow.dismiss();
                    initData();
                }
            }

            @Override
            public void onResponseError(BaseBean errorMessage, boolean isNetError) {
                dismissLoadingView();
                ToastUtils.showToast(errorMessage.getMsg().toString());
            }
        });
    }

}
