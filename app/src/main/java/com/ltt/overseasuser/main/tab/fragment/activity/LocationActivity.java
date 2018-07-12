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
        getCountriesList();

    }

    private void initData() {
        optionsCity.clear();
        optionsState.clear();
        optionsCountry.clear();
        getContries();
        String sCurrentCoutryName = "";
        if (!optionsCountry.isEmpty()) {
            sCurrentCoutryName = optionsCountry.get(0);
            mCurrentCountriId = getCountryId(optionsCountry.get(0));
            mCurrentStateId = getStateId(mCurrentCountriId, optionsCountry.get(0));
            optionsState= getStates(sCurrentCoutryName);
            getCityList(mCurrentStateId);
        }
        adapterCoutries.clear();
        adapterStates.clear();
        adapterCities.clear();
        adapterCoutries.addAll(optionsCountry);
        adapterStates.addAll(optionsState);
        adapterCities.addAll(optionsCity);
    }

    private List<String> getContries() {
        List<String> dataList = new ArrayList<String>();
        if (mCountriesListBean == null) {
            return dataList;
        }

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

    private String getStateName(String stateId) {
        String stateName = "";
        if (stateId.isEmpty())
            return "";
        for (CountryAndStatiesBean contries :
                mCountriesListBean.getData()) {
            for (StateBean states :
                    contries.getStates()) {
                if (stateId.equals(states.getStates_id())) {
                    stateName = states.getState_name();
                    return stateName;
                }
            }
        }
        return stateName;
    }

    private void initUi(String tittle) {
        mView = mlflater.inflate(R.layout.requestlocationlayout, null);
        spinnerCountry = mView.findViewById(R.id.spinner_country);

        spinnerState = mView.findViewById(R.id.spinner_state);
        //cityspinner
        spinnerCity = mView.findViewById(R.id.spinner_city);
        TextView tvTittle = mView.findViewById(R.id.tv_title);
        tvTittle.setText(tittle);

        adapterCoutries = new ArrayAdapter(mParentActivity, android.R.layout.simple_spinner_item, optionsCountry);
        spinnerCountry.setAdapter(adapterCoutries);
        adapterStates = new ArrayAdapter(mParentActivity, android.R.layout.simple_spinner_item, optionsState);
        spinnerState.setAdapter(adapterStates);
        adapterCities = new ArrayAdapter(mParentActivity, android.R.layout.simple_spinner_item, optionsCity);
        spinnerCity.setAdapter(adapterCities);
        spinnerCountry.setSelection(0,false);
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //设置当前coutryid 添加states列表
                if (mCountriesListBean == null||mCountriesListBean.getData().isEmpty()||optionsCountry.isEmpty()) {
                    getCountriesList();
                    return;
                }

                adapterStates.clear();
                adapterCities.clear();
                optionsState.clear();
                optionsState = getStates(optionsCountry.get(i));
                adapterStates.addAll(optionsState);
                mCurrentCountriId = getCountryId(optionsCountry.get(i));
                mCurrentStateId = getStateId(mCurrentCountriId, optionsState.get(0));
                getCityList(mCurrentStateId);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //设置当前stateid,添加city列表
                adapterCities.clear();
                mCurrentStateId = getStateId(mCurrentCountriId, optionsState.get(i));
                getCityList(mCurrentStateId);
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

    }

    // 获取city
    private void getCityList(final String stateid) {
        if (stateid.isEmpty())
            return;
        mParentActivity.showLoadingView();
        adapterCities.clear();
        optionsCity.clear();
        Call<CitiesListBean> call = RetrofitUtil.getAPIService().getCities(stateid);
        call.enqueue(new CustomerCallBack<CitiesListBean>() {
            @Override
            public void onResponseResult(CitiesListBean response) {
                optionsCity = response.getData();
                adapterCities.addAll(optionsCity);
                mCurrentCity = optionsCity.get(0);
                mParentActivity.dismissLoadingView();

            }

            @Override
            public void onResponseError(BaseBean errorMessage, boolean isNetError) {
                BaseBean ret = errorMessage;
                mCurrentCity = getStateName(stateid);
                optionsCity.add(mCurrentCity);
                adapterCities.addAll(optionsCity);
                mParentActivity.dismissLoadingView();
            }

        });
    }

    //获取地区信息
    public void getCountriesList() {
        mParentActivity.showLoadingView();
        Call<CountriesListBean> call = RetrofitUtil.getAPIService().getCountries();
        call.enqueue(new CustomerCallBack<CountriesListBean>() {
            @Override
            public void onResponseResult(CountriesListBean response) {
                mParentActivity.dismissLoadingView();
                mCountriesListBean = response;
                initData();

            }

            @Override
            public void onResponseError(BaseBean errorMessage, boolean isNetError) {
                mParentActivity.dismissLoadingView();
            }

        });
    }

}
