package com.jcy.googlemvp.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @desc:
 * @author: Jiangcy
 * @datetime: 2016/9/3
 */
public abstract class BaseActivity extends AppCompatActivity {
    private ViewDataBinding binding;

    protected abstract  int getLayoutId();

    protected abstract void initBinding(ViewDataBinding binding);

    protected abstract void initPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,getLayoutId());
        initBinding(binding);

    }

    @CallSuper
    @Override
    protected void onResume() {
        initPresenter();
        super.onResume();
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
