package com.xiaoxi.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xiaoxi.R;
import com.xiaoxi.utils.Utils;


/**
 * 状态view,包含：加载中、异常、无数据、无网络等
 * <p>
 * xiejingwen
 */
public class LoadingLayout extends FrameLayout {

    public final static int Success = 0;
    public final static int Empty = 1;
    public final static int Error = 2;
    public final static int No_Network = 3;
    public final static int Loading = 4;
    private int state;

    private Context mContext;

    private View loadingPage;
    private View errorPage;
    private View emptyPage;
    private View networkPage;

    private ProgressBar pbLoading;

    private ImageView errorImg;
    private ImageView emptyImg;
    private ImageView networkImg;

    private TextView loadingText;
    private TextView errorText;
    private TextView emptyText;
    private TextView networkText;

    private LinearLayout errorReloadBtn;
    private LinearLayout networkReloadBtn;
    private TextView emptyReloadBtn;

    private View contentView;
    private OnReloadListener listener;
    private boolean isFirstVisible; //是否一开始显示contentView，默认不显示
    private static Config mConfig = new Config();   //配置

    public LoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout);
        isFirstVisible = a.getBoolean(R.styleable.LoadingLayout_isFirstVisible, true);
        a.recycle();
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    public LoadingLayout(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            throw new IllegalStateException("LoadingLayout can host only one direct child");
        }
        contentView = this.getChildAt(0);
        if (!isFirstVisible) {
            contentView.setVisibility(View.GONE);
        }
    }

    private void initNetworkPage() {
        networkPage = LayoutInflater.from(mContext).inflate(R.layout.widget_nonetwork_page, null);
        networkPage.setBackgroundColor(Utils.getColor(mContext, mConfig.backgroundColor));
        networkText = Utils.findViewById(networkPage, R.id.no_network_text);
        networkImg = Utils.findViewById(networkPage, R.id.no_network_img);
        networkReloadBtn = Utils.findViewById(networkPage, R.id.no_network_reload_btn);
        networkReloadBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null) {
                    setStatus(Loading);
                    listener.onReload(v);
                }
            }
        });
        networkText.setText(mConfig.netwrokStr);
        networkText.setTextSize(mConfig.tipTextSize);
        networkText.setTextColor(Utils.getColor(mContext, mConfig.tipTextColor));
        networkImg.setImageResource(mConfig.networkImgId);
        networkReloadBtn.setBackgroundResource(mConfig.reloadBtnId);
        this.addView(networkPage);
    }


    private void initEmptyPage() {
        emptyPage = LayoutInflater.from(mContext).inflate(R.layout.widget_empty_page, null);
        emptyPage.setBackgroundColor(Utils.getColor(mContext, mConfig.backgroundColor));
        emptyText = Utils.findViewById(emptyPage, R.id.empty_text);
        emptyImg = Utils.findViewById(emptyPage, R.id.empty_img);
        emptyReloadBtn = Utils.findViewById(emptyPage, R.id.empty_reload_btn);
        emptyReloadBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onReload(v);
                }
            }
        });
        emptyText.setText(mConfig.emptyStr);
        emptyText.setTextSize(mConfig.tipTextSize);
        emptyText.setTextColor(Utils.getColor(mContext, mConfig.tipTextColor));
        emptyImg.setImageResource(mConfig.emptyImgId);
        this.addView(emptyPage);

    }

    private void initErrorPage() {
        errorPage = LayoutInflater.from(mContext).inflate(R.layout.widget_error_page, null);
        errorPage.setBackgroundColor(Utils.getColor(mContext, mConfig.backgroundColor));
        errorImg = Utils.findViewById(errorPage, R.id.error_img);
        errorText = Utils.findViewById(errorPage, R.id.error_text);
        errorReloadBtn = Utils.findViewById(errorPage, R.id.error_reload_btn);
        errorReloadBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    setStatus(Loading);
                    listener.onReload(v);
                }
            }
        });
        errorText.setText(mConfig.errorStr);
        errorText.setTextSize(mConfig.tipTextSize);
        errorText.setTextColor(Utils.getColor(mContext, mConfig.tipTextColor));
        errorImg.setImageResource(mConfig.errorImgId);
        this.addView(errorPage);
    }

    private void initLoadingPage() {
        if (mConfig.loadingView == null) {
            loadingPage = LayoutInflater.from(mContext).inflate(mConfig.loadingLayoutId, null);
            loadingText = Utils.findViewById(loadingPage, R.id.loading_text);
            loadingText.setTextSize(mConfig.tipTextSize);
            loadingText.setTextColor(Utils.getColor(mContext, mConfig.tipTextColor));
            pbLoading = Utils.findViewById(loadingPage, R.id.pb_loading);
            /*pbLoading.setBackgroundResource(mConfig.progressBgId);
            Drawable drawable = ContextCompat.getDrawable(getContext(), mConfig.progressIndeterminateId);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            pbLoading.setIndeterminateDrawable(drawable);*/
        } else {
            loadingPage = mConfig.loadingView;
        }
        loadingPage.setBackgroundColor(Utils.getColor(mContext, mConfig.backgroundColor));
        this.addView(loadingPage);
    }

    public void setStatus(@Flavour int status) {
        this.state = status;
        if (Success != status && contentView != null) {
            contentView.setVisibility(View.GONE);
        }
        if (Loading != status && loadingPage != null) {
            loadingPage.setVisibility(View.GONE);
        }
        if (Empty != status && emptyPage != null) {
            emptyPage.setVisibility(View.GONE);
        }
        if (Error != status && errorPage != null) {
            errorPage.setVisibility(View.GONE);
        }
        if (No_Network != status && networkPage != null) {
            networkPage.setVisibility(View.GONE);
        }
        switch (status) {
            case Success:
                contentView.setVisibility(View.VISIBLE);
                break;

            case Loading:
                getLoadingPage().setVisibility(View.VISIBLE);
                break;

            case Empty:
                getEmptyPage().setVisibility(View.VISIBLE);
                break;

            case Error:
                getErrorPage().setVisibility(View.VISIBLE);
                break;

            case No_Network:
                getNetworkPage().setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }

    }

    /**
     * 返回当前状态{Success, Empty, Error, No_Network, Loading}
     *
     * @return
     */
    public int getStatus() {

        return state;
    }

    /**
     * 设置Empty状态提示文本，仅对当前所在的地方有效
     *
     * @param text
     * @return
     */
    public LoadingLayout setEmptyText(String text) {

        getEmptyText().setText(text);
        return this;
    }

    /**
     * 设置Error状态提示文本，仅对当前所在的地方有效
     *
     * @param text
     * @return
     */
    public LoadingLayout setErrorText(String text) {

        getErrorText().setText(text);
        return this;
    }

    /**
     * 设置No_Network状态提示文本，仅对当前所在的地方有效
     *
     * @param text
     * @return
     */
    public LoadingLayout setNoNetworkText(String text) {

        getNetworkText().setText(text);
        return this;
    }

    /**
     * 设置Empty状态显示图片，仅对当前所在的地方有效
     *
     * @param id
     * @return
     */
    public LoadingLayout setEmptyImage(@DrawableRes int id) {

        getEmptyImg().setImageResource(id);
        return this;
    }

    /**
     * 设置Error状态显示图片，仅对当前所在的地方有效
     *
     * @param id
     * @return
     */
    public LoadingLayout setErrorImage(@DrawableRes int id) {

        getErrorImg().setImageResource(id);
        return this;
    }

    /**
     * 设置No_Network状态显示图片，仅对当前所在的地方有效
     *
     * @param id
     * @return
     */
    public LoadingLayout setNoNetworkImage(@DrawableRes int id) {

        getNetworkImg().setImageResource(id);
        return this;
    }

    /**
     * 设置Empty状态提示文本的字体大小，仅对当前所在的地方有效
     *
     * @param sp
     * @return
     */
    public LoadingLayout setEmptyTextSize(int sp) {

        getEmptyText().setTextSize(sp);
        return this;
    }

    /**
     * 设置Error状态提示文本的字体大小，仅对当前所在的地方有效
     *
     * @param sp
     * @return
     */
    public LoadingLayout setErrorTextSize(int sp) {

        getErrorText().setTextSize(sp);
        return this;
    }

    /**
     * 设置No_Network状态提示文本的字体大小，仅对当前所在的地方有效
     *
     * @param sp
     * @return
     */
    public LoadingLayout setNoNetworkTextSize(int sp) {

        getNetworkText().setTextSize(sp);
        return this;
    }

    /**
     * 设置Empty状态图片的显示与否，仅对当前所在的地方有效
     *
     * @param bool
     * @return
     */
    public LoadingLayout setEmptyImageVisible(boolean bool) {

        if (bool) {
            getEmptyImg().setVisibility(View.VISIBLE);
        } else {
            getEmptyImg().setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置Error状态图片的显示与否，仅对当前所在的地方有效
     *
     * @param bool
     * @return
     */
    public LoadingLayout setErrorImageVisible(boolean bool) {

        if (bool) {
            getErrorImg().setVisibility(View.VISIBLE);
        } else {
            getErrorImg().setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置No_Network状态图片的显示与否，仅对当前所在的地方有效
     *
     * @param bool
     * @return
     */
    public LoadingLayout setNoNetworkImageVisible(boolean bool) {

        if (bool) {
            getNetworkImg().setVisibility(View.VISIBLE);
        } else {
            getNetworkImg().setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置ReloadButton的背景，仅对当前所在的地方有效
     *
     * @param id
     * @return
     */
    public LoadingLayout setReloadButtonBackgroundResource(@DrawableRes int id) {
        getErrorReloadBtn().setBackgroundResource(id);
        getNetworkReloadBtn().setBackgroundResource(id);
        return this;
    }

    /**
     * 设置ReloadButton的监听器
     *
     * @param listener
     * @return
     */
    public LoadingLayout setOnReloadListener(OnReloadListener listener) {

        this.listener = listener;
        return this;
    }

    /**
     * 自定义加载页面，仅对当前所在的Activity有效
     *
     * @param view
     * @return
     */
    public LoadingLayout setLoadingPage(View view) {
        if (loadingPage != null) {
            this.removeView(loadingPage);
        }
        loadingPage = view;
        loadingPage.setVisibility(View.GONE);
        this.addView(loadingPage);
        return this;
    }

    /**
     * 自定义加载页面，仅对当前所在的地方有效
     *
     * @param id
     * @return
     */
    public LoadingLayout setLoadingPage(@LayoutRes int id) {
        if (loadingPage != null) {
            this.removeView(loadingPage);
        }
        View view = LayoutInflater.from(mContext).inflate(id, null);
        loadingPage = view;
        loadingPage.setVisibility(View.GONE);
        this.addView(view);
        return this;
    }

    /**
     * 设置各种状态下view的背景色，仅对当前所在的地方有效
     *
     * @param color
     * @return
     */
    public LoadingLayout setDefineBackgroundColor(@ColorRes int color) {
        getLoadingPage().setBackgroundColor(Utils.getColor(mContext, color));
        getErrorPage().setBackgroundColor(Utils.getColor(mContext, color));
        getEmptyPage().setBackgroundColor(Utils.getColor(mContext, color));
        getNetworkPage().setBackgroundColor(Utils.getColor(mContext, color));
        return this;
    }

    /**
     * 获取loadingPage
     *
     * @return
     */
    public View getLoadingPage() {
        if (loadingPage == null) {
            initLoadingPage();
        }
        return loadingPage;
    }

    public View getErrorPage() {
        if (errorPage == null) {
            initErrorPage();
        }
        return errorPage;
    }

    public View getEmptyPage() {
        if (emptyPage == null) {
            initEmptyPage();
        }
        return emptyPage;
    }

    public View getNetworkPage() {
        if (networkPage == null) {
            initNetworkPage();
        }
        return networkPage;
    }

    public ImageView getErrorImg() {
        if (errorPage == null) {
            initErrorPage();
        }
        return errorImg;
    }

    public ImageView getEmptyImg() {
        if (emptyPage == null) {
            initEmptyPage();
        }
        return emptyImg;
    }

    public ImageView getNetworkImg() {
        if (networkPage == null) {
            initNetworkPage();
        }
        return networkImg;
    }

    public TextView getLoadingText() {
        if (loadingPage == null) {
            initLoadingPage();
        }
        return loadingText;
    }

    public TextView getErrorText() {
        if (errorPage == null) {
            initErrorPage();
        }
        return errorText;
    }

    public TextView getEmptyText() {
        if (emptyPage == null) {
            initEmptyPage();
        }
        return emptyText;
    }

    public TextView getNetworkText() {
        if (networkPage == null) {
            initNetworkPage();
        }
        return networkText;
    }

    public View getErrorReloadBtn() {
        if (errorPage == null) {
            initErrorPage();
        }
        return errorReloadBtn;
    }

    public View getNetworkReloadBtn() {
        if (networkPage == null) {
            initNetworkPage();
        }
        return networkReloadBtn;
    }

    public TextView getEmptyReloadBtn() {
        if (emptyPage == null) {
            initEmptyPage();
        }
        return emptyReloadBtn;
    }

    public LoadingLayout setEmptyReloadBtnText(String text) {
        if (TextUtils.isEmpty(text)){
            getEmptyReloadBtn().setVisibility(GONE);
        }else {
            emptyReloadBtn.setVisibility(VISIBLE);
            getEmptyReloadBtn().setText(text);
        }
        return this;
    }

    @IntDef({Success, Empty, Error, No_Network, Loading})
    public @interface Flavour {

    }

    public interface OnReloadListener {

        void onReload(View v);
    }

    /**
     * 获取全局配置的class
     *
     * @return
     */
    public static Config getConfig() {

        return mConfig;
    }

    /**
     * 全局配置的Class，对所有使用到的地方有效
     */
    public static class Config {

        String emptyStr = "暂无数据";
        String errorStr = "加载失败，点击屏幕重试";
        String netwrokStr = "请检查网络是否正常后，点击屏幕重试";
        String reloadBtnStr = "刷 新";
        int progressBgId;
        int progressIndeterminateId;
        int emptyImgId = R.mipmap.no_data;
        int errorImgId = R.mipmap.no_data;
        int networkImgId = R.mipmap.no_network;
        int reloadBtnId;
        int tipTextSize = 16;
        int buttonTextSize = 17;
        int tipTextColor = R.color.loading_text;
        int buttonTextColor;
        int buttonWidth = -1;
        int buttonHeight = -1;
        int buttonDrawableLeft = -1;
        int loadingLayoutId = R.layout.widget_loading_page;
        View loadingView = null;
        int backgroundColor = R.color.global_background;

        public Config setErrorText(@NonNull String text) {

            errorStr = text;
            return mConfig;
        }

        public Config setEmptyText(@NonNull String text) {

            emptyStr = text;
            return mConfig;
        }

        public Config setNoNetworkText(@NonNull String text) {

            netwrokStr = text;
            return mConfig;
        }

        public Config setReloadButtonText(@NonNull String text) {

            reloadBtnStr = text;
            return mConfig;
        }

        /**
         * 设置所有提示文本的字体大小
         *
         * @param sp
         * @return
         */
        public Config setAllTipTextSize(int sp) {

            tipTextSize = sp;
            return mConfig;
        }

        /**
         * 设置所有提示文本的字体颜色
         *
         * @param color
         * @return
         */
        public Config setAllTipTextColor(@ColorRes int color) {

            tipTextColor = color;
            return mConfig;
        }

        public Config setReloadButtonTextSize(int sp) {

            buttonTextSize = sp;
            return mConfig;
        }

        public Config setReloadButtonTextColor(@ColorRes int color) {

            buttonTextColor = color;
            return mConfig;
        }

        public Config setReloadButtonBackgroundResource(@DrawableRes int id) {

            reloadBtnId = id;
            return mConfig;
        }

        public Config setReloadButtonDrawableLeftResource(@DrawableRes int id) {

            buttonDrawableLeft = id;
            return mConfig;
        }

        public Config setReloadButtonWidthAndHeight(int width_dp, int height_dp) {

            buttonWidth = width_dp;
            buttonHeight = height_dp;
            return mConfig;
        }

        public Config setErrorImage(@DrawableRes int id) {

            errorImgId = id;
            return mConfig;
        }

        public Config setEmptyImage(@DrawableRes int id) {

            emptyImgId = id;
            return this;
        }

        public Config setNoNetworkImage(@DrawableRes int id) {

            networkImgId = id;
            return mConfig;
        }

        public Config setLoadingPageLayout(@LayoutRes int id) {

            loadingLayoutId = id;
            return mConfig;
        }

        public Config setLoadingPageView(View view) {

            this.loadingView = view;
            return mConfig;
        }

        public Config setAllPageBackgroundColor(@ColorRes int color) {

            backgroundColor = color;
            return mConfig;
        }

        public Config setProgressBg(int id) {
            progressBgId = id;
            return mConfig;
        }

        ;

        public Config setProgressIndeterminate(@DrawableRes int id) {
            progressIndeterminateId = id;
            return mConfig;
        }

        ;
    }
}
