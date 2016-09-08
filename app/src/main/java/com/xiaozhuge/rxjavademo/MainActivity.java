package com.xiaozhuge.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.xiaozhuge.rxjavademo.bean.DdmBean;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * @author xiaozhuge007
 */
public class MainActivity extends AppCompatActivity {

    private String[] s1 = {"三星", "LG", "Iphone"};
    private String[] s2 = {"华为", "小米"};
    private Subscriber<String> subscriber;
    private Action1<String> onNextAction;
    private Action1<Throwable> onErrorAction;
    private Action0 onCompletedAction;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tv_0);
        init();
        getData();
        androidDemo();
    }

    private void androidDemo() {

    }

    private void getData() {
//        DdmBean.getDdm().subscribe(subscriber);
//        DdmBean.justData().subscribe(subscriber);
        DdmBean.formData()
//                .map(new Func1<String, String>() {
//                    @Override
//                    public String call(String s) {
//                        return "小明";
//                    }
//                })
//                .flatMap(new Func1<String, Observable<String>>() {
//                    @Override
//                    public Observable<String> call(String s) {
//                        return Observable.just(s+"小明");
//                    }
//                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private void init() {
        subscriber = new Subscriber<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Mlog.e("start");
            }

            @Override
            public void onCompleted() {
                Mlog.e("Completed");
            }

            @Override
            public void onError(Throwable e) {
                Mlog.e(e.toString());
            }

            @Override
            public void onNext(String s) {
                Mlog.e(s);
            }
        };

        onNextAction = new Action1<String>() {
            // onNext()
            @Override
            public void call(String s) {
                Mlog.e(s);
            }
        };
        onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {
                Mlog.e(throwable.toString());
            }
        };
        onCompletedAction = new Action0() {
            // onCompleted()
            @Override
            public void call() {
                Mlog.e("完成");
            }
        };
    }


    private void sku() {
        Schedulers.io()
                .createWorker()
                .schedule(new Action0() {
                    @Override
                    public void call() {

                    }
                });
    }

    private void megra() {
        Observable<String> observable1 = Observable.from(s1);
        Observable<String> observable2 = Observable.from(s2);
        Subscription mergedObservable = Observable.merge(observable1, observable2)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Mlog.e(s);
                    }
                });
    }

    private void m4() {
        Observable.range(20, 10)
                .take(4)//只发射前边几个元素
                .repeat(3)
                .distinct()
//                .filter(new Func1<Integer, Boolean>() {
//                    @Override
//                    public Boolean call(Integer integer) {
//                        return !(integer==21);
//                    }
//                })
//                .takeLast(4)//只发射后边几个元素
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Mlog.e(integer + "");
                    }
                });
    }

    private void m3() {
        final PublishSubject<Boolean> publishSubject = PublishSubject.create();
        publishSubject.subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                Log.e("x", "完成了");
            }
        });
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 5; i++) {
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {
                publishSubject.onNext(true);
            }
        }).subscribe();

    }

    private String print() {
        return "如果我有机器猫";
    }
}
