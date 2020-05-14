package com.android.hq.androidutils.thememode.themeutil;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class ThemePublisher {
    private final Subject<Object> mSubject;
    private ThemePublisher() {
        mSubject = PublishSubject.create().toSerialized();
    }

    private static class ThemePublisherHolder{
        private static ThemePublisher INSTANCE = new ThemePublisher();
    }

    public static ThemePublisher getInstance() {
        return ThemePublisherHolder.INSTANCE;
    }

    public void post(Object obj) {
        mSubject.onNext(obj);
    }

    public Observable<ThemeMode> toObservable(Class<ThemeMode> type) {
        return mSubject.ofType(type);
    }

    public Flowable<ThemeMode> toFlowable(Class<ThemeMode> type) {
        return toObservable(type).toFlowable(BackpressureStrategy.BUFFER);
    }

    public Disposable toDisposable(Class<ThemeMode> type, Consumer<ThemeMode> consumer) {
        return toFlowable(type).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
    }


}
