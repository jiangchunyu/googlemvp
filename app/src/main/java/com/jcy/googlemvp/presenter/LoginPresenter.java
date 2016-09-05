package com.jcy.googlemvp.presenter;

import android.content.Context;
import android.util.Log;

import com.jcy.googlemvp.contract.LoginCotract;
import com.jcy.googlemvp.model.LoginModel;

/**
 * @className: LoginPresenter
 * @desc:
 * @author: Jiangcy
 * @datetime: 2016/9/3
 */
public class LoginPresenter implements LoginCotract.Presenter {

    private LoginCotract.View view;
    private LoginModel mModel;


    public LoginPresenter(LoginCotract.View view) {
        this.view = view;
        view.setPresenter(this);
        mModel = new LoginModel(this);
    }

    @Override
    public void login(String userName, String pwd) {
        mModel.login(userName, pwd);

    }

    @Override
    public void loginSuccess() {
        view.loginSuccess();
    }

    @Override
    public void loginError(String error) {
        view.loginError(error);
    }


    @Override
    public void onResume(Context context) {
        Log.e("jcy", "  onResume ");
        mModel.onResume(context);
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestroy() {
        mModel.onDestroy();
    }
}
