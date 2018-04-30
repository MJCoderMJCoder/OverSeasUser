package com.ltt.overseasuser.base;

import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by lin on 2017/3/24.
 */

public class TagOperator implements ObservableOperator {

    private boolean isDestroy;

    public void onDestroy() {
        isDestroy = true;
    }

    @Override
    public Observer apply(@NonNull final Observer observer) throws Exception {
        return new TagObserver(observer);

    }

    private class TagObserver implements Observer {

        private Observer observer;
        private boolean cancelable;
        private int eventId;

        public TagObserver(Observer observer) {
            this.observer = observer;
            if (observer instanceof BaseObserver) {
                cancelable = true;
                eventId = ((BaseObserver) observer).getEventId();
            }
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {
            if (!isDestroy && !isCancel())
                observer.onSubscribe(d);
        }

        @Override
        public void onNext(@NonNull Object o) {
            if (!isDestroy && !isCancel())
                observer.onNext(o);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            if (!isDestroy && !isCancel())
                observer.onError(e);
        }

        @Override
        public void onComplete() {
            if (!isDestroy && !isCancel())
                observer.onComplete();
        }

        private boolean isCancel() {
            if (cancelable && ((BaseObserver) observer).getEventId() != eventId)
                return true;
            return false;
        }
    }
}
