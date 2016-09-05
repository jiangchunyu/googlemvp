package com.jcy.googlemvp.rxbus;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @desc:
 * @author: Jiangcy
 * @datetime: 2016/9/3
 */
public abstract class EventType {
    @IntDef({REC_ALL,REC_LOGIN,REC_SIGNTUP,REC_MAIN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ReceiveType {
    }

    @IntDef({THREAD_ALL, THREAD_UI, THREAD_CHILD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ThreadType {
    }


    //接收事件的Modle类型
    public static final int REC_ALL = 0x10000;//所有model均可接收
    public static final int REC_LOGIN = 0x10001;//登陆model接收
    public static final int REC_SIGNTUP = 0x10002;//注册页面model接收
    public static final int REC_MAIN = 0x10003;//主页面model接收


    //接收事件的线程
    public static final int THREAD_ALL = 0x20000;//所有线程均可接收
    public static final int THREAD_UI = 0x20001;//UI线程均可接收
    public static final int THREAD_CHILD = 0x20002;//子线程均可接收
}
