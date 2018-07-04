package com.ltt.overseasuser.model;

import com.ltt.overseasuser.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/30 0030.
 */

public class UploadSucessBean extends BaseBean {
    private List<UploadSucFileInfoBean> data;
    public void setData(List<UploadSucFileInfoBean> data){this.data=data;}
    public List<UploadSucFileInfoBean> getData(){return data;}
}
