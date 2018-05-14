package com.ltt.overseasuser.main.tab.fragment.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yunwen on 2018/5/14.
 */

public class WebviewActivity extends BaseActivity {
    @BindView(R.id.wv_webview)
    WebView wvWebview;

    @Override
    protected int bindLayoutID() {
        return R.layout.activity_webview;
    }

    @Override
    protected void prepareActivity() {
        String weburl = getIntent().getExtras().getString("weburl");
        wvWebview.loadUrl(weburl);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
