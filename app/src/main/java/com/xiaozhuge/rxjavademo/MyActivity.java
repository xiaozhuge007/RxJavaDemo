package com.xiaozhuge.rxjavademo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.xiaozhuge.rxjavademo.bean.DdmBean;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

/**
 * Date: 2016-05-30
 * Time: 11:04
 *
 * @author xiaozhuge007
 *         RxJava 中文文档学习
 */
public class MyActivity extends RxAppCompatActivity {

    private Subscriber<String> subscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        initSubscription();
//        createData();
//        justData();
//        fromData();

//        SubjectData();
//        repeatData();
//        deferData();
//        rangeData();
//        intervalData();
//        timerData();

//        filterData();
//        takeData();
//        distinctData();
//        firstOrLastData();
//        skipData();
//        ElementAtData();
//        SamplingData();
//        timOutData();

//        mapData();
        bufferData();
        ioData();
    }

    private void ioData() {
        Schedulers.io().createWorker().schedule(new Action0() {
            @Override
            public void call() {
                //执行io耗时任务
            }
        });
    }

    /**
     *
     */
    private void bufferData() {
        DdmBean.formIntegerData()
//                .buffer(3)//把数据包装成3个一组的发送
                .buffer(2, 3)//每隔skip（3）个数据，把2个数据包装发送 eg：1 4 7 10
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<List<Integer>>bindToLifecycle())//RxLifecycle 防止内存泄漏 需要继承RxAppCompatActivity类
                .subscribe(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> integers) {
                        Mlog.e(integers.get(0) + "");
                    }
                });
    }

    private void mapData() {
        DdmBean.formIntegerData()
//                .map(new Func1<Integer, Integer>() {
//                    @Override
//                    public Integer call(Integer integer) {
//                        return integer+10;
//                    }
//                })
                .flatMap(new Func1<Integer, Observable<String>>() {//转换数据源发射，但是不保证发射的数据和源数据顺序一样
                    @Override
                    public Observable<String> call(Integer integer) {
                        return changeData(integer);
                    }
                })
//                .concatMap(new Func1<Integer, Observable<String>>() {//发射后的数据是按顺序排列的
//                    @Override
//                    public Observable<String> call(Integer integer) {
//                        return changeData(integer);
//                    }
//                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Mlog.e(s);
                    }
                });
    }

    private synchronized Observable<String> changeData(final Integer s) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(s + "-王尼玛");
                subscriber.onCompleted();
            }
        });
    }

    private void timOutData() {
        DdmBean.formIntegerData()
//                .timeout(3,TimeUnit.SECONDS)
                .debounce(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Mlog.e(integer + "");
                    }
                });
    }

    /**
     * 创建一个新的可观测序列，它将在一个指定的时间间隔里由Observable发射最近一次的数值
     */
    private void SamplingData() {
        Observable.just(1, 1, 1, 2, 2, 3, 4, 4, 5)
                .sample(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Mlog.e(integer + "");
                    }
                });
    }

    /**
     * 发射指定的元素
     */
    private void ElementAtData() {
        DdmBean.formIntegerData()
//                .elementAt(90)
                .elementAtOrDefault(101, 95)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Mlog.e(integer + "");
                    }
                });
    }

    /**
     * skip()和skipLast()函数与take()和takeLast()相对应。它们用整数N作参数，
     * 从本质上来说，它们不让Observable发射前N个或者后N个值。如果我们知道一个序列以没有太多用的“可控”元素开头或结尾时我们可以使用它。
     */
    private void skipData() {
        DdmBean.formIntegerData()
//                .skip(3)//发送第三个元素后的
                .skipLast(5)//发送最后5个元素前的
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Mlog.e(integer + "");
                    }
                });
    }

    /**
     * 与first()和last()相似的变量有：
     * firstOrDefault()和lastOrDefault().这两个函数当可观测序列完成时不再发射任何值时用得上。
     * 在这种场景下，如果Observable不再发射任何值时我们可以指定发射一个默认的值
     */
    private void firstOrLastData() {
        DdmBean.formIntegerData()
//                .first()
//                .last()
//                .last(new Func1<Integer, Boolean>() {
//                    @Override
//                    public Boolean call(Integer integer) {
//                        return integer==100;
//                    }
//                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Mlog.e(integer + "");
                    }
                });
    }

    /**
     * 去除发射源中的重复元素
     */
    private void distinctData() {
        Observable.just(1, 1, 1, 2, 2, 3, 4, 4, 5)
//                .repeat(3)
//                .distinct()
//                .distinctUntilChanged()//发射元素1 后边元素变化后在此发送
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Mlog.e(integer + "");
                    }
                });
    }

    /**
     * 取出其中的哪些
     */
    private void takeData() {
        DdmBean.formIntegerData()
//                .take(3)//取出前三个元素
//                .takeLast(3)//取出最后三个元素
                .take(3, TimeUnit.SECONDS)//在指定三秒内返回的元素
//                .takeLast(20,3,TimeUnit.SECONDS)//返回最后20个元素在指定的3秒内
//                .takeFirst(new Func1<Integer, Boolean>() {//返回第一个符合条件的元素
//                    @Override
//                    public Boolean call(Integer integer) {
//                        return integer % 3 == 0;
//                    }
//                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Mlog.e(integer + "");
                    }
                });
    }

    /**
     * 增加过滤规则
     * 本例中过滤掉奇数
     */
    private void filterData() {
        DdmBean.formIntegerData()
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer % 2 == 0;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Mlog.e(integer + "");
                    }
                });
    }


    /**
     * 三秒后发送数字 0
     */
    private void timerData() {
        Observable
                .timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Mlog.e(aLong + "");
                    }
                });
    }

    /**
     * 在你需要创建一个轮询程序时非常好用
     * 没隔3秒开始连续发送数字 0。。。
     */
    private void intervalData() {
        Observable.interval(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Mlog.e(aLong + "");
                    }
                });
    }

    /**
     * 你需要从一个指定的数字X开始发射N个数字吗？你可以用range
     * 第一个是起始点，第二个是我们想发射数字的个数。
     */
    private void rangeData() {
        Observable.range(15, 5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Mlog.e(integer + "");
                    }
                });
    }

    /**
     * 有这样一个场景，你想在这声明一个Observable但是你又想推迟这个Observable的创建直到观察者订阅时
     */
    private void deferData() {
        Observable<String> observable = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return DdmBean.justData();
            }
        });
        observable.subscribe(subscription);
    }

    /**
     * 重复发送数据
     */
    private void repeatData() {
        DdmBean.justData()
                .repeat(3)//重复发射三次数据
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscription);
    }

    private void SubjectData() {
//        PublishSubject.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext("王尼玛");
//                subscriber.onCompleted();
//            }
//        }).subscribe(subscription);

        final PublishSubject<Boolean> subject = PublishSubject.create();

        //BehaviorSubject会首先向他的订阅者发送截至订阅前最新的一个数据对象（或初始值）,然后正常发送订阅后的数据流。
        final BehaviorSubject<String> behaviorSubject = BehaviorSubject.create("你好，中国");
        //ReplaySubject会缓存它所订阅的所有数据,向任意一个订阅它的观察者重发
        final ReplaySubject<String> replaySubject = ReplaySubject.create();
        //当Observable完成时AsyncSubject只会发布最后一个数据给已经订阅的每一个观察者。
        final AsyncSubject<String> asyncSubject = AsyncSubject.create();
        behaviorSubject.subscribe(subscription);
        replaySubject.subscribe(subscription);
        asyncSubject.subscribe(subscription);

        subject.subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                Mlog.e("Observable Completed");
            }
        });

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 0; i < 5; i++) {
                    subscriber.onNext(i + "");
                }
                subscriber.onCompleted();
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {
//                subject.onNext(true);
//                behaviorSubject.onNext("behaviorSubject");
//                replaySubject.onNext("replaySubject");
                asyncSubject.onNext("asyncSubject");
            }
        }).subscribe(subscription);
    }

    private void fromData() {
        DdmBean.formData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscription);
    }

    private void justData() {
        DdmBean.justData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscription);
    }

    private void createData() {
        DdmBean.cretateDdm()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscription);
    }

    /**
     * 初始话订阅
     */
    private void initSubscription() {
        subscription = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Mlog.e("数据加载完成");
            }

            @Override
            public void onError(Throwable e) {
                Mlog.e(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                Mlog.e(s);
            }
        };
    }
}