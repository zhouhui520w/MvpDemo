package com.xiaoxi.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xiaoxi.R;
import com.xiaoxi.utils.StringUtils;


/**
 * Created by zhouhui on 2018/4/18.
 *
 */

public class DialogLoading extends Dialog {

    private TextView mLoadingMsg;

    public DialogLoading(Context context) {
        super(context, R.style.DialogLoading);
        setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(false);
        mLoadingMsg = findViewById(R.id.loading_msg);
    }

    public void setMsg(String msg) {
        if (StringUtils.isNotEmpty(msg)) {
            if (mLoadingMsg == null) {
                mLoadingMsg = findViewById(R.id.loading_msg);
            }
            mLoadingMsg.setVisibility(View.VISIBLE);
            mLoadingMsg.setText(msg);
        } else {
            mLoadingMsg.setVisibility(View.GONE);
        }
    }

}
