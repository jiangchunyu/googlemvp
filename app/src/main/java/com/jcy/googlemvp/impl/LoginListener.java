package com.jcy.googlemvp.impl;

/**
 * @className: LoginListener
 * @desc:
 * @author: Jiangcy
 * @datetime: 2016/9/3
 */
public interface LoginListener {

    void loginSuccess();
    void loginError(String error);

}
