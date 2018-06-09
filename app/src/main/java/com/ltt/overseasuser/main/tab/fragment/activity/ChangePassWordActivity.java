package com.ltt.overseasuser.main.tab.fragment.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.base.BaseBean;
import com.ltt.overseasuser.http.CustomerCallBack;
import com.ltt.overseasuser.http.RetrofitUtil;
import com.ltt.overseasuser.model.PWBean;
import com.ltt.overseasuser.model.UpdatePWBean;
import com.ltt.overseasuser.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class ChangePassWordActivity extends BaseActivity {
    @BindView(R.id.btn_left_profile)
    ImageView btnLeftProfile;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_edit_right_profile)
    TextView tvEditRightProfile;
    @BindView(R.id.rl_content)
    RelativeLayout rlContent;
    @BindView(R.id.action_bar)
    LinearLayout actionBar;
    @BindView(R.id.et_current)
    EditText etCurrent;
    @BindView(R.id.et_new)
    EditText etNew;
    @BindView(R.id.et_confirm)
    EditText etConfirm;
    @BindView(R.id.ib_changepw)
    ImageButton ibChangepw;

    @Override
    protected int bindLayoutID() {
        return R.layout.activity_changepw;
    }

    @Override
    protected void prepareActivity() {
        tvTitle.setText("update password");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ib_changepw, R.id.btn_left_profile, R.id.tv_edit_right_profile, R.id.et_current, R.id.et_new, R.id.et_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left_profile:
                break;
            case R.id.tv_edit_right_profile:
                break;
            case R.id.et_current:
                break;
            case R.id.et_new:
                break;
            case R.id.et_confirm:
                break;
            case R.id.ib_changepw:
                if (etNew.getText().toString().trim().isEmpty()) {
                    ToastUtils.showToast("Please input New password");
                    return;
                }
                if (etCurrent.getText().toString().trim().isEmpty()) {
                    ToastUtils.showToast("Please input Current password");
                    return;
                }
                if (etConfirm.getText().toString().trim().isEmpty()) {
                    ToastUtils.showToast("Please input Confirm New password");
                    return;
                }
                updatePw();
                break;
        }
    }

    private void updatePw() {
        PWBean userParams=new PWBean();
        userParams.setCurrent_password(etCurrent.getText().toString().trim());
        userParams.setNew_password(etNew.getText().toString().trim());
        userParams.setRepeat_password(etConfirm.getText().toString().trim());
        Call<UpdatePWBean> updatePWBeanCall = RetrofitUtil.getAPIService().updatePW(userParams);
        updatePWBeanCall.enqueue(new CustomerCallBack<UpdatePWBean>() {
            @Override
            public void onResponseResult(UpdatePWBean response) {
                if (response.isStatus()){
                    ToastUtils.showToast("PassWord Change Successful");
                }
            }

            @Override
            public void onResponseError(BaseBean errorMessage, boolean isNetError) {
                ToastUtils.showToast(""+errorMessage.getMsg());
            }
        });
    }

    @OnClick()
    public void onClick() {
    }
}
