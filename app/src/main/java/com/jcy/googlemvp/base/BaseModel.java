package com.jcy.googlemvp.base;

import android.content.Context;

import com.jcy.googlemvp.rxbus.EventType;
import com.jcy.googlemvp.rxbus.RxBus;
import com.jcy.googlemvp.rxbus.RxEvent;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @desc:
 * @author: Jiangcy
 * @datetime: 2016/9/3
 */
public abstract class BaseModel<T> {
    private Subscription rxMainBus;
    private Subscription rxThreadBus;
    protected Context mContext;
    //设置默认接受类型为全部接收
    private
    @EventType.ReceiveType
    int recieveType = EventType.REC_ALL;
    protected T mPresenter;

    public BaseModel(T mPresenter) {
        this.mPresenter = mPresenter;
    }

    /**
     * 进入
     */
    public void onResume(Context mContext) {
        this.mContext = mContext;
        initRxbus();
    }

    /**
     * 初始化接收类型
     *
     * @param recieveType
     */
    public void initReciever(@EventType.ReceiveType int recieveType) {
        this.recieveType = recieveType;
    }

    ;

    /**
     * 注册rxbus事件接收
     */
    private void initRxbus() {
        rxMainBus = RxBus.getDefault().toObserverable(RxEvent.class)
                .filter(new Func1<RxEvent, Boolean>() {
                    @Override
                    public Boolean call(RxEvent rxEvent) {
                        //此处可以通过Rxjava的filter过滤函数对数据进行过滤，从而得到自己想要的数据
                        if ((rxEvent.reciveType == recieveType ||
                                rxEvent.reciveType == EventType.REC_ALL) && (rxEvent.threadType ==
                                EventType.THREAD_ALL || rxEvent.threadType == EventType.THREAD_UI)) {
                            return true;
                        }
                        return false;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//设置为主线程接收数据
                .subscribe(new Action1<RxEvent>() {
                               @Override
                               public void call(RxEvent rxEvent) {
                                   onMainEvent(rxEvent.eventAction, rxEvent.event);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                // TODO: 处理异常
                            }
                        });

        /**
         *
         */
        rxThreadBus = RxBus.getDefault().toObserverable(RxEvent.class)
                .filter(new Func1<RxEvent, Boolean>() {
                    @Override
                    public Boolean call(RxEvent rxEvent) {
                        //此处可以通过Rxjava的filter过滤函数对数据进行过滤，从而得到自己想要的数据
                        if ((rxEvent.reciveType == recieveType ||
                                rxEvent.reciveType == EventType.REC_ALL) && (rxEvent.threadType ==
                                EventType.THREAD_ALL || rxEvent.threadType == EventType.THREAD_CHILD)) {
                            return true;
                        }
                        return false;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//设置为子线程接收数据
                .subscribe(new Action1<RxEvent>() {
                               @Override
                               public void call(RxEvent rxEvent) {
                                   onThreadEvent(rxEvent.eventAction, rxEvent.event);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                // TODO: 处理异常
                            }
                        });
    }


    public abstract void onMainEvent(String action, Object event);

    public abstract void onThreadEvent(String action, Object event);


    /**
     * 解除事件的注销，以保证不出现内存泄漏
     */
    public void onDestroy() {
        if (rxMainBus != null && !rxMainBus.isUnsubscribed()) {
            rxMainBus.unsubscribe();
        }
        if (rxThreadBus != null && !rxThreadBus.isUnsubscribed()) {
            rxThreadBus.unsubscribe();
        }
        mContext = null;
    }
}
