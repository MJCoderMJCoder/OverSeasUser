package com.ltt.overseasuser.main.tab.fragment.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.core.ActionBar;
import com.ltt.overseasuser.main.tab.fragment.adapter.PopupAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/25.
 */
public class ExploreActivity extends BaseActivity {
    @BindView(R.id.parent_lv)
    ListView parentLv;
    @BindView(R.id.child_lv)
    ListView childLv;
    ActionBar bar;
    private PopupAdapter parentAdapter,childAdapter;
    private List<String> parentList;
    private List<List<String>> chlidList;
    private List<String> childValues;
    @Override
    protected int bindLayoutID() {
        return R.layout.activity_explore;
    }

    @Override
    protected void prepareActivity() {
        bar=ActionBar.init(this);
        bar.setTitle("Explore");
        bar.setLeft(R.mipmap.back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bar.showNotify();
        initData();
        parentAdapter = new PopupAdapter(this,R.layout.popup_item,parentList,R.drawable.normal,R.drawable.press2);
        childAdapter = new PopupAdapter(this,R.layout.popup_item,childValues,R.drawable.normal,R.drawable.press);
        parentLv.setAdapter(parentAdapter);
        childLv.setAdapter(childAdapter);
    }

    public void initData(){
        parentList=new ArrayList<>();
        parentList.add("Construction");
        parentList.add("Logistics");
        parentList.add("Road");
        parentList.add("Transportation");
        parentList.add("Maintenance Service");
        parentList.add("Transport Service");
        parentList.add("Finance Service");
        childValues=new ArrayList<>();
        childValues.add("Repair");
        childValues.add("Road");
        childValues.add("Sweepers");
        childValues.add("Golf Carts");
        childValues.add("Utility Tool Carries");
        childValues.add("Utility Machines");
        childValues.add("Pressure Wash Equipment");
        childValues.add("Flail Cutters");
        childValues.add("Two Wheeled Tractors");
        childValues.add("Roundscare Equipment");

    }

}
