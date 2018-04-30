package com.ltt.overseasuser.base;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lin on 2017/7/19.
 */

public class RxTransformer implements ObservableTransformer {

    TagOperator operator;

    public RxTransformer(TagOperator operator) {
        this.operator = operator;
    }

    @Override
    public ObservableSource apply(@NonNull Observable upstream) {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .lift(operator);
    }
}
