package com.ltt.overseasuser.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import io.reactivex.ObservableTransformer;

/**
 * Created by lin on 2017/7/16.
 */

public abstract class BaseFragment extends Fragment {

    protected String TAG = getClass().getSimpleName();

    private TagOperator operator = new TagOperator();
    private ObservableTransformer transformer = new RxTransformer(operator);

    protected abstract int bindLayoutID();
    protected abstract void prepareFragment();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(bindLayoutID(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prepareFragment();
    }

    @Override
    public void onDestroy() {
        operator.onDestroy();
        super.onDestroy();
    }

    public ObservableTransformer transform() {
        return transformer;
    }

    protected void showLoadingView(){
        if(!(getActivity() instanceof BaseActivity))
            throw new RuntimeException("Activity not extends BaseActivity");
        ((BaseActivity)getActivity()).showLoadingView();
    }

    protected void dismissLoadingView(){
        if(!(getActivity() instanceof BaseActivity))
            throw new RuntimeException("Activity not extends BaseActivity");
        ((BaseActivity)getActivity()).dismissLoadingView();
    }
}
