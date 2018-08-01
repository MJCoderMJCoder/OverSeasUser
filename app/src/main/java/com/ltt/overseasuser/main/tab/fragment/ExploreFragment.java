package com.ltt.overseasuser.main.tab.fragment;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lin.widget.SwipeRecyclerView;
import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseFragment;
import com.ltt.overseasuser.base.RecyclerAdapter;
import com.ltt.overseasuser.core.ActionBar;
import com.ltt.overseasuser.main.tab.fragment.activity.ExploreActivity;
import com.ltt.overseasuser.main.tab.fragment.activity.NotificationActivity;
import com.ltt.overseasuser.main.tab.fragment.activity.RequestActivity;
import com.ltt.overseasuser.main.tab.fragment.activity.WebviewActivity;
import com.ltt.overseasuser.main.tab.fragment.adapter.ExploreAdapter;
import com.ltt.overseasuser.main.tab.fragment.adapter.ImageAdapter;
import com.ltt.overseasuser.model.SectionBean;
import com.ltt.overseasuser.model.SectionImageInfo;

import butterknife.BindView;
import butterknife.OnClick;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * Created by Administrator on 2018/1/18.
 */
public class ExploreFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.action_bar)
    View actionBar;
    ActionBar bar;
    @BindView(R.id.lv_machinery)
    RecyclerView mlvViewMachinery;
    @BindView(R.id.lv_parts)
    RecyclerView mlvViewParts;
    @BindView(R.id.lv_maitenance)
    RecyclerView mlvViewMaitenance;
    @BindView(R.id.lv_vehicle)
    RecyclerView mlvViewVehicle;
    private final int MACHINERY = 0, VEHICLES = 1,PARTS=2,MAINTENANCE=3;
    @Override
    protected int bindLayoutID() {
        return R.layout.fragment_explore;
    }

    @Override
    protected void prepareFragment() {
        bar = ActionBar.init(actionBar);
        bar.setBackground(R.mipmap.bg_title_popmach);
        bar.showNotify();
        initData();
    }


    @OnClick({R.id.iv_menu, R.id.iv_notify,R.id.iv_macheniry,R.id.iv_parts,R.id.iv_mantenance,R.id.iv_vehicle,
            R.id.right_machinery,R.id.right_maitenance,R.id.right_parts,R.id.right_vehicle,R.id.golisting})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_menu:
                Intent intent_menu = new Intent(getContext(), ExploreActivity.class);
                intent_menu.putExtra("selected",MACHINERY);
                startActivity(intent_menu);
                break;
            case R.id.iv_notify:
                startActivity(new Intent(getActivity(), NotificationActivity.class));
                break;
            case R.id.iv_macheniry:
                Intent intent_macheniry = new Intent(getContext(), ExploreActivity.class);
                intent_macheniry.putExtra("selected",MACHINERY);
                startActivity(intent_macheniry);
                break;
            case R.id.iv_parts:
                Intent intent_parts = new Intent(getContext(), ExploreActivity.class);
                intent_parts.putExtra("selected",PARTS);
                startActivity(intent_parts);
                break;
            case R.id.iv_vehicle:
                Intent intent_vehicle = new Intent(getContext(), ExploreActivity.class);
                intent_vehicle.putExtra("selected",VEHICLES);
                startActivity(intent_vehicle);
                break;
            case R.id.iv_mantenance:
                Intent intent_mmantenance = new Intent(getContext(), ExploreActivity.class);
                intent_mmantenance.putExtra("selected",MAINTENANCE);
                startActivity(intent_mmantenance);
                break;
            case R.id.right_machinery:
                Intent intent_right_machinery = new Intent(getContext(), ExploreActivity.class);
                intent_right_machinery.putExtra("selected",MACHINERY);
                startActivity(intent_right_machinery);
                break;
            case  R.id.right_maitenance:
                Intent intent_right_maitenance = new Intent(getContext(), RequestActivity.class);
                intent_right_maitenance.putExtra("sectionid","4");
                startActivity(intent_right_maitenance);
                break;
            case R.id.right_parts:
                Intent intent_right_parts = new Intent(getContext(), RequestActivity.class);
                intent_right_parts.putExtra("sectionid","13");
                startActivity(intent_right_parts);
                break;
            case R.id.right_vehicle:
                Intent intent_right_vehicle = new Intent(getContext(), RequestActivity.class);
                intent_right_vehicle.putExtra("sectionid","10");
                startActivity(intent_right_vehicle);
                break;
            case R.id.golisting:
                Intent intentweb = new Intent(getContext(),WebviewActivity.class);
                intentweb.putExtra("weburl","https://popmach.com/buy?parent_id=6&sort=newest&page=1");
                startActivity(intentweb);
                break;

        }

    }

    @Override
    public void onRefresh() {
      //  refreshLayout.setRefreshing(false);
    }

    public void initData()
    {

        //machinery
        LinearLayoutManager machineryManager = new LinearLayoutManager(getContext());
        machineryManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mlvViewMachinery.setLayoutManager(machineryManager);
        final ImageAdapter imageAdapter1 = new ImageAdapter(getContext());
        imageAdapter1.add("3",R.drawable.excavator,R.mipmap.excavator_b);
        imageAdapter1.add("1",R.drawable.forklift,R.mipmap.forklift_b);
        imageAdapter1.add("11",R.drawable.boomlift,R.mipmap.boomlift_b);
        imageAdapter1.add("2",R.drawable.generator,R.mipmap.generator_b);
        imageAdapter1.add("5",R.drawable.pallettruck,R.mipmap.pallettruck_b);
        imageAdapter1.add("6",R.drawable.stacker,R.mipmap.stacker_b);
        imageAdapter1.add("8",R.drawable.other,R.mipmap.others_b);
        imageAdapter1.add("9",R.drawable.container_handler,R.mipmap.backhoeloader_b);
        imageAdapter1.add("24",R.drawable.reach_stacker,R.mipmap.compactor_b);
        mlvViewMachinery.setAdapter(imageAdapter1);
        imageAdapter1.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object object, View view, int position) {
                imageAdapter1.setSelected(position);
                imageAdapter1.notifyDataSetChanged();
                Intent intent = new Intent(getContext(), RequestActivity.class);
                intent.putExtra("sectionid",((SectionImageInfo)object).getsSecion());
                startActivity(intent);
            }
        });

        //Parts Accessorties
        LinearLayoutManager partManager = new LinearLayoutManager(getContext());
        partManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mlvViewParts.setLayoutManager(partManager);
        final ImageAdapter imageAdapter2 = new ImageAdapter(getContext());
        for(int i=0;i<9;i++)
        {
            imageAdapter2.add("13",R.drawable.parts_accessories,R.mipmap.parts_b);
        }
        mlvViewParts.setAdapter(imageAdapter2);
        imageAdapter2.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object object, View view, int position) {
                imageAdapter2.setSelected(position);
                imageAdapter2.notifyDataSetChanged();
                Intent intent = new Intent(getContext(), RequestActivity.class);
                intent.putExtra("sectionid",((SectionImageInfo)object).getsSecion());
                startActivity(intent);

            }
        });
        //Maintenance
        LinearLayoutManager maitenanceManager = new LinearLayoutManager(getContext());
        maitenanceManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mlvViewMaitenance.setLayoutManager(maitenanceManager);
        final ImageAdapter imageAdapter3 = new ImageAdapter(getContext());
        for(int i=0;i<9;i++) {
            imageAdapter3.add("4",R.drawable.maintenance,R.mipmap.repair_b);
        }
        mlvViewMaitenance.setAdapter(imageAdapter3);
        imageAdapter3.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object object, View view, int position) {
                imageAdapter3.setSelected(position);
                imageAdapter3.notifyDataSetChanged();
                Intent intent = new Intent(getContext(), RequestActivity.class);
                intent.putExtra("sectionid",((SectionImageInfo)object).getsSecion());
                startActivity(intent);
            }
        });
        //vehicle
        LinearLayoutManager vehicleManager = new LinearLayoutManager(getContext());
        vehicleManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mlvViewVehicle.setLayoutManager(vehicleManager);
        final ImageAdapter imageAdapter4 = new ImageAdapter(getContext());
        for(int i=0;i<9;i++) {
            imageAdapter4.add("10",R.drawable.commercial_vehicle,R.mipmap.truck_b);
        }
        mlvViewVehicle.setAdapter(imageAdapter4);
        imageAdapter4.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object object, View view, int position) {
                imageAdapter4.setSelected(position);
                imageAdapter4.notifyDataSetChanged();
                Intent intent = new Intent(getContext(), RequestActivity.class);
                intent.putExtra("sectionid",((SectionImageInfo)object).getsSecion());
                startActivity(intent);
            }
        });
    }
}
