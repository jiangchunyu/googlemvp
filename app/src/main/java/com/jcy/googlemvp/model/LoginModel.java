package com.jcy.googlemvp.model;

import android.util.Log;

import com.jcy.googlemvp.base.BaseModel;
import com.jcy.googlemvp.data.RequestData;
import com.jcy.googlemvp.presenter.LoginPresenter;
import com.jcy.googlemvp.rxbus.EventType;
import com.jcy.googlemvp.utils.StringUtil;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @desc:
 * @author: Jiangcy
 * @datetime: 2016/9/3
 */
public class LoginModel extends BaseModel<LoginPresenter>{
    private final int LOGIN_ERROR = 0x0010;
    private final int LOGIN_SUCCESS = 0x0011;
    private static final String TAG = "LoginModel";

    public LoginModel(LoginPresenter mPresenter) {
       super(mPresenter);
        initReciever(EventType.REC_LOGIN);
    }




    public void login(final String userName, final String pwd) {
        Log.d(TAG, "login: userName "+userName+"  isBlank "+StringUtil.isBlank(userName));
        Log.d(TAG, "login: pwd      "+pwd+"  isBlank "+StringUtil.isBlank(pwd));
        if (StringUtil.isBlank(userName) || StringUtil.isBlank(pwd)) {
            mPresenter.loginError("用户名或者密码为空，请输入正确的用户名或密码");
            return;
        }
        //模拟网络登陆环境
        Observable.create(new Observable.OnSubscribe<RequestData>() {
            @Override
            public void call(Subscriber<? super RequestData> subscriber) {
                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                RequestData requestData = new RequestData();
                if (userName.equals("one")) {
                    //用户名不存在
                    requestData.setCode(LOGIN_ERROR);
                    requestData.setInfo("用户名不存在，请先注册");

                } else if (userName.equals("admin") || pwd.equals("admin")) {
                    //登陆成功
                    requestData.setCode(LOGIN_SUCCESS);
                    requestData.setInfo("");
                } else {
                    //用户名或者密码不正确
                    requestData.setCode(LOGIN_ERROR);
                    requestData.setInfo("用户名或者密码不正确");
                }
                subscriber.onNext(requestData);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<RequestData>() {
            @Override
            public void call(RequestData requestData) {
                if(requestData.getCode()==LOGIN_ERROR){
                    mPresenter.loginError(requestData.getInfo());
                }else if(requestData.getCode()==LOGIN_SUCCESS){
                    mPresenter.loginSuccess();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mPresenter.loginError("网络出现问题，请重新登陆"+throwable.toString());
            }
        });
    }

    @Override
    public void onMainEvent(String action, Object event) {

    }

    @Override
    public void onThreadEvent(String action, Object event) {

    }
}
