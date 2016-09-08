package com.xiaozhuge.rxjavademo.bean;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by admin on 2016/4/28.
 * 叮当猫
 */
public class DdmBean {
    private static String[] strings = {"苹果", "三星", "华为"};
    private static List<Integer> list = new ArrayList<>();

    public static Observable<String> cretateDdm() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("如");
                subscriber.onNext("果");
                subscriber.onNext("我");
                subscriber.onNext("有");
                subscriber.onNext("叮");
                subscriber.onNext("当");
                subscriber.onNext("猫");
                subscriber.onCompleted();
            }
        });
    }

    public static Observable<String> justData() {
        return Observable.just("苹果", "三星", "华为", "华为");
    }

    public static Observable<String> formData() {
        return Observable.from(strings);
    }

    public static Observable<Integer> formIntegerData() {
        for (int i = 0; i < 100; i++) {
            list.add(i + 1);
        }
        return Observable.from(list);
    }
}
