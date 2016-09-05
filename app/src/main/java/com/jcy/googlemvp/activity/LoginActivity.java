package com.jcy.googlemvp.activity;

import android.databinding.ViewDataBinding;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jcy.googlemvp.R;
import com.jcy.googlemvp.base.BaseActivity;
import com.jcy.googlemvp.contract.LoginCotract;
import com.jcy.googlemvp.databinding.ActLoginBinding;
import com.jcy.googlemvp.dialog.DigLoginWait;
import com.jcy.googlemvp.presenter.LoginPresenter;

public class LoginActivity extends BaseActivity implements LoginCotract.View {

    private ActLoginBinding mBinding;
    private LoginPresenter mPresenter;
    private DigLoginWait mDigLoginWait;

    @Override
    protected int getLayoutId() {
        return R.layout.act_login;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume(this);
        mDigLoginWait = new DigLoginWait(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    protected void initBinding(ViewDataBinding binding) {
        mBinding = (ActLoginBinding) binding;
    }


    /**
     * 登陆按钮
     *
     * @param view
     */
    public void onLogin(View view) {
        //通过databinding 实现对数据双向绑定
        Log.e("jcy", "UserName  " + mBinding.getUserName() + "  Password " + mBinding.getPassword());
        mDigLoginWait.showDialog();
        mPresenter.login(mBinding.getUserName(), mBinding.getPassword());
    }

    /**
     * 登陆成功
     */
    @Override
    public void loginSuccess() {
        mDigLoginWait.closeDialog();
        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 登陆失败
     *
     * @param error 登陆失败原因
     */
    @Override
    public void loginError(String error) {
        mDigLoginWait.closeDialog();
        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
    }

    /**
     * 初始化Presenter
     */
    public void initPresenter() {
        Log.e("jcy", "  initPresenter ");
        mPresenter = new LoginPresenter(this);
    }

    /**
     * 不如果是在Fragment中可以在初始化调用Fragment时传入
     *
     * @param presenter
     */
    @Override
    public void setPresenter(LoginCotract.Presenter presenter) {
    }
}
