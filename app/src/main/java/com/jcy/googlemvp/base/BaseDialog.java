package com.jcy.googlemvp.base;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.LayoutInflater;

/**
 * @desc:
 * @author: Jiangcy
 * @datetime: 2016/9/3
 */
public abstract class BaseDialog extends Dialog implements Dialog.OnDismissListener{

    private Context mContext;
    private ViewDataBinding binding;

    public BaseDialog(Context context) {
        super(context);
        mContext = context;
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    protected abstract int getLayoutId();

    protected abstract void initBinding(ViewDataBinding binding);

    protected abstract void initInfo(Object... objects);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),getLayoutId(),null,false);
        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();
        setOnDismissListener(this);
    }

    public void showDialog(){
        if(isShowing())return;
        show();
    }

    public void closeDialog(){
        if(!isShowing())return;
        dismiss();
    }
}
