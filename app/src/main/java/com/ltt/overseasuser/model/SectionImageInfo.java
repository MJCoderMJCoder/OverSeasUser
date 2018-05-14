package com.ltt.overseasuser.model;

/**
 * Created by Administrator on 2018/5/12 0012.
 */
public class SectionImageInfo
{
    private String sSecion;
    private int iResouseId;
    private int iResousePress;
    public SectionImageInfo(String sSecion,int iResouseId,int iResousePress){
        this.sSecion=sSecion;
        this.iResouseId=iResouseId;
        this.iResousePress =iResousePress;
    }
    public String getsSecion(){
        return sSecion;
    }
    public int getiResouseId(){
        return iResouseId;
    }
    public int getiResouseIdPess(){
        return iResousePress;
    }
}