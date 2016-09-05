package com.jcy.googlemvp.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ViewDataBinding;

import com.jcy.googlemvp.R;
import com.jcy.googlemvp.base.BaseDialog;
import com.jcy.googlemvp.databinding.DigLoginwaitBinding;

/**
 * @desc:
 * @author: Jiangcy
 * @datetime: 2016/9/3
 */
public class DigLoginWait extends BaseDialog {
    private DigLoginwaitBinding mBinding;

    public DigLoginWait(Context context) {
        super(context, R.style.Dialog_Fullscreen);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dig_loginwait;
    }

    @Override
    protected void initBinding(ViewDataBinding binding) {
        mBinding = (DigLoginwaitBinding) binding;
    }

    @Override
    protected void initInfo(Object... objects) {

    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {

    }
}
