package com.ltt.overseasuser.main.tab.fragment.activity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseBean;
import com.ltt.overseasuser.http.CustomerCallBack;
import com.ltt.overseasuser.http.RetrofitUtil;
import com.ltt.overseasuser.model.CitiesListBean;
import com.ltt.overseasuser.model.CountriesListBean;
import com.ltt.overseasuser.model.CountryAndStatiesBean;
import com.ltt.overseasuser.model.CountryBean;
import com.ltt.overseasuser.model.QuestionDataBean;
import com.ltt.overseasuser.model.SectionInitQuestionBean;
import com.ltt.overseasuser.model.StateBean;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/7/2 0002.
 */

public class LocationActivity {
    private RequestActivity mParentActivity;
    private LayoutInflater mlflater;
    private CountriesListBean mCountriesListBean = null;
    List<String> optionsCountry = new ArrayList<>();
    List<String> optionsState = new ArrayList<>();
    List<String> optionsCity = new ArrayList<>();
    private ArrayAdapter<CharSequence> adapterCoutries;
    private ArrayAdapter<String> adapterStates;
    private ArrayAdapter<String> adapterCities;
    private Spinner spinnerCountry;
    private Spinner spinnerState;
    private Spinner spinnerCity;
    public String mCurrentCountriId;
    public String mCurrentStateId;
    public String mCurrentCity;
    View mView;

    public LocationActivity(LayoutInflater lflater, RequestActivity rquestActivity, String tittle) {
        mParentActivity = rquestActivity;
        mlflater = lflater;
        initUi(tittle);
        initData();


    }

    private void initData() {
        mCountriesListBean = mParentActivity.mCountriesListBean;
        getContries();
        String sCurrentCoutryName = "";
        if (!optionsCountry.isEmpty()) {
            sCurrentCoutryName = optionsCountry.get(0);
            mCurrentCountriId = getCountryId(optionsCountry.get(0));
            mCurrentStateId = getStateId(mCurrentCountriId, optionsCountry.get(0));
        }
        getStates(sCurrentCoutryName);
        if (!optionsState.isEmpty())
            mCurrentCity = optionsState.get(0);
        adapterCoutries = new ArrayAdapter(mParentActivity, android.R.layout.simple_spinner_item, optionsCountry);
        spinnerCountry.setAdapter(adapterCoutries);
        adapterStates = new ArrayAdapter(mParentActivity, android.R.layout.simple_spinner_item, optionsState);
        spinnerState.setAdapter(adapterStates);
        adapterCities = new ArrayAdapter(mParentActivity, android.R.layout.simple_spinner_item, optionsCity);
        spinnerCity.setAdapter(adapterCities);

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //设置当前coutryid 添加states列表
                adapterStates.clear();
                adapterStates.addAll(getStates(optionsCountry.get(i)));
                mCurrentCountriId = getCountryId(optionsCountry.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //设置当前stateid,添加city列表
                mCurrentStateId = getStateId(mCurrentCountriId, optionsState.get(i));
                adapterCities.clear();
                getCityList(mCurrentStateId);
                mCurrentCity=optionsState.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrentCity = optionsCity.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //   mParentActivity.getCitiesList(optionsState);
    }

    private List<String> getContries() {
        List<String> dataList = new ArrayList<String>();
        if (mCountriesListBean == null)
            return dataList;
        for (CountryAndStatiesBean contries :
                mCountriesListBean.getData()) {
            dataList.add(contries.getCountry().getName());
        }
        optionsCountry.clear();
        optionsCountry = dataList;
        return dataList;
    }

    private List<String> getStates(String countryName) {
        List<String> dataList = new ArrayList<String>();
        if (mCountriesListBean == null)
            return dataList;
        for (CountryAndStatiesBean contries :
                mCountriesListBean.getData()) {
            if (countryName.equals(contries.getCountry().getName())) {
                for (StateBean states :
                        contries.getStates()) {
                    dataList.add(states.getState_name());
                }
            }
        }
        optionsState.clear();
        optionsState = dataList;
        return dataList;
    }

    private String getCountryId(String coutryName) {
        String countryId = "";
        if (coutryName.isEmpty())
            return "";
        for (CountryAndStatiesBean contries :
                mCountriesListBean.getData()) {
            if (coutryName.equals(contries.getCountry().getName())) {
                countryId = contries.getCountry().getCountry_id();
            }
        }
        return countryId;
    }

    private String getStateId(String contryId, String stateName) {
        String stateId = "";
        if (contryId.isEmpty() || stateName.isEmpty())
            return "";
        for (CountryAndStatiesBean contries :
                mCountriesListBean.getData()) {
            if (contryId.equals(contries.getCountry().getCountry_id())) {
                for (StateBean states :
                        contries.getStates()) {
                    if (stateName.equals(states.getState_name())) {
                        stateId = states.getStates_id();
                    }
                }
            }
        }
        return stateId;
    }

    private void initUi(String tittle) {
        mView = mlflater.inflate(R.layout.requestlocationlayout, null);
        spinnerCountry = mView.findViewById(R.id.spinner_country);

        spinnerState = mView.findViewById(R.id.spinner_state);
        //cityspinner
        spinnerCity = mView.findViewById(R.id.spinner_city);
        TextView tvTittle = mView.findViewById(R.id.tv_title);
        tvTittle.setText(tittle);
    }

    // 获取city
    private void getCityList(String stateid) {
        if (stateid.isEmpty())
            return;
        Call<CitiesListBean> call = RetrofitUtil.getAPIService().getCities(stateid);
        call.enqueue(new CustomerCallBack<CitiesListBean>() {
            @Override
            public void onResponseResult(CitiesListBean response) {
                optionsCity = response.getData();
                adapterCities.clear();
                adapterCities.addAll(optionsCity);

            }

            @Override
            public void onResponseError(BaseBean errorMessage, boolean isNetError) {
                BaseBean ret = errorMessage;
            }

        });
    }
}
