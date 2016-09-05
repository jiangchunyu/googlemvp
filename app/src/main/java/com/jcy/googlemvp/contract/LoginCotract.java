package com.jcy.googlemvp.contract;

import com.jcy.googlemvp.base.BasePresenter;
import com.jcy.googlemvp.base.BaseView;

/**
 * @className: LoginCotract
 * @desc:
 * @author: Jiangcy
 * @datetime: 2016/9/3
 */
public class LoginCotract {
    public interface View extends BaseView<Presenter>{
        void loginSuccess();
        void loginError(String error);

    }

    public interface Presenter extends BasePresenter{
        void login(String userName,String pwd);
        void loginSuccess();
        void loginError(String error);
    }
}
