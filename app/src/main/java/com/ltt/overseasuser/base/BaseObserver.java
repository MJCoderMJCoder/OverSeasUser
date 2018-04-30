package com.ltt.overseasuser.base;

import com.ltt.overseasuser.utils.L;
import com.ltt.overseasuser.utils.ToastUtils;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by lin on 2017/7/19.
 */

public abstract class BaseObserver<T extends BaseBean> implements Observer<T> {

    private boolean isFail;
    private int eventId;

    public abstract void handle(T data);

    public abstract void onSuccess();

    public abstract void onFail();

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        isFail = false;
    }

    @Override
    public void onNext(@NonNull T t) {
        if (t.getCode() == 1)
            handle(t);
        else {
            isFail = true;
            ToastUtils.showToast(t.getMsg());
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
        L.e("RxError", e.toString());
        onFail();
    }

    @Override
    public void onComplete() {
        if (isFail)
            onFail();
        else
            onSuccess();
    }

    public int getEventId() {
        return eventId;
    }

    public void cancel() {
        eventId++;
    }
}
