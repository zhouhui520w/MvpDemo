package com.xiaoxi.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoxi.R;
import com.xiaoxi.app.App;


/**
 * Created by zhouhui on 2018/4/18.
 *
 */

public class ToastUtils {
    static ToastUtils td;

    public static void show(int resId) {
        show(App.getInstance().getString(resId));
    }

    public static void show(String msg) {
        if (td == null) {
            td = new ToastUtils(App.getInstance());
        }
        td.setText(msg);
        td.create().show();
    }

    public static void shortShow(String msg) {
        if (td == null) {
            td = new ToastUtils(App.getInstance());
        }
        td.setText(msg);
        td.createShort().show();
    }


    Context context;
    Toast toast;
    String msg;

    public ToastUtils(Context context) {
        this.context = context;
    }

    public Toast create() {
        View contentView = View.inflate(context, R.layout.toast, null);
        TextView tvMsg =  contentView.findViewById(R.id.tv_toast_msg);
        toast = new Toast(context);
        toast.setView(contentView);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        tvMsg.setText(msg);
        return toast;
    }

    public Toast createShort() {
        View contentView = View.inflate(context, R.layout.toast, null);
        TextView tvMsg =  contentView.findViewById(R.id.tv_toast_msg);
        toast = new Toast(context);
        toast.setView(contentView);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        tvMsg.setText(msg);
        return toast;
    }

    public void show() {
        if (toast != null) {
            toast.show();
        }
    }

    public void setText(String text) {
        msg = text;
    }
}
