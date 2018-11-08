package com.xiaoxi.component;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.StrictMode;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.flattener.ClassicFlattener;
import com.elvishew.xlog.interceptor.BlacklistTagsFilterInterceptor;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.ConsolePrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy;
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;
import com.orhanobut.hawk.Hawk;
import com.xiaoxi.BuildConfig;
import com.xiaoxi.R;

import java.io.File;

/**
 * Created by zhouhui on 2018/4/18.
 */

public class InitializeService extends IntentService {

    private static final String ACTION_INIT = "initApplication";

    public InitializeService() {
        super("InitializeService");
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT.equals(action)) {
                initApplication();
            }
        }
    }

    private void initApplication() {

        //初始化数据存储
        Hawk.init(this).build();

        //初始化日志
        initXlog();

        //初始化错误收集
        initBugly();

        //设置严苛模式
        initStrictMode();
    }

    /**
     * 初始化Xlog
     */
    private void initXlog() {
        LogConfiguration config = new LogConfiguration.Builder()
                .logLevel(BuildConfig.DEBUG ? LogLevel.ALL              // 指定日志级别，低于该级别的日志将不会被打印，默认为 LogLevel.ALL
                        : LogLevel.NONE)
                .tag(getString(R.string.global_tag))                                            // 指定 TAG，默认为 "X-LOG"
                .t()                                                    // 允许打印线程信息，默认禁止
                .st(2)                                                  // 允许打印深度为2的调用栈信息，默认禁止
                .b()                                                    // 允许打印日志边框，默认禁止
                // .jsonFormatter(new MyJsonFormatter())                // 指定 JSON 格式化器，默认为 DefaultJsonFormatter
                // .xmlFormatter(new MyXmlFormatter())                  // 指定 XML 格式化器，默认为 DefaultXmlFormatter
                // .throwableFormatter(new MyThrowableFormatter())      // 指定可抛出异常格式化器，默认为 DefaultThrowableFormatter
                // .threadFormatter(new MyThreadFormatter())            // 指定线程信息格式化器，默认为 DefaultThreadFormatter
                // .stackTraceFormatter(new MyStackTraceFormatter())    // 指定调用栈信息格式化器，默认为 DefaultStackTraceFormatter
                // .borderFormatter(new MyBoardFormatter())             // 指定边框格式化器，默认为 DefaultBorderFormatter
                // .addObjectFormatter(AnyClass.class,                  // 为指定类添加格式化器
                //     new AnyClassObjectFormatter())                   // 默认使用 Object.toString()
                // .addInterceptor(new BlacklistTagsFilterInterceptor(  // 添加黑名单 TAG 过滤器
                //        "blacklist1", "blacklist2", "blacklist3"))
                // .addInterceptor(new WhitelistTagsFilterInterceptor(  // 添加白名单 TAG 过滤器
                //     "whitelist1", "whitelist2", "whitelist3"))
                // .addInterceptor(new MyInterceptor())                 // 添加一个日志拦截器
                .build();

        Printer androidPrinter = new AndroidPrinter();             // 通过 android.util.Log 打印日志的打印器
        Printer consolePrinter = new ConsolePrinter();             // 通过 System.out 打印日志到控制台的打印器
        Printer filePrinter = new FilePrinter                      // 打印日志到文件的打印器
//                .Builder(new File(Environment.getExternalStorageDirectory(), "xlogsample").getPath())
                .Builder("/sdcard/xlog/")                              // 指定保存日志文件的路径
                .fileNameGenerator(new DateFileNameGenerator())        // 指定日志文件名生成器，默认为 ChangelessFileNameGenerator("log")
//                .backupStrategy(new MyBackupStrategy())              // 指定日志文件备份策略，默认为 FileSizeBackupStrategy(1024 * 1024)
//                .cleanStrategy(new FileLastModifiedCleanStrategy(MAX_TIME))     // 指定日志文件清空策略，默认为 NeverCleanStrategy()
                .logFlattener(new ClassicFlattener())                       // 指定日志平铺器，默认为 DefaultFlattener
                .build();

        XLog.init(                                                      // 初始化 XLog
                config,                                                 // 指定日志配置，如果不指定，会默认使用 new LogConfiguration.Builder().build()
                androidPrinter,                                         // 添加任意多的打印器。如果没有添加任何打印器，会默认使用 AndroidPrinter(Android)/ConsolePrinter(java)
                consolePrinter,
                filePrinter);
    }

    /**
     * 初始化buggly
     */
    private void initBugly() {
//        Context context = getApplicationContext();
//        // 获取当前包名
//        String packageName = context.getPackageName();
//        // 获取当前进程名
//        String processName = SystemUtils.getProcessName(android.os.Process.myPid());
//        // 设置是否为上报进程
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
//        strategy.setUploadProcess(processName == null || processName.equals(packageName));
//
//        CrashReport.initCrashReport(context, Constants.BUGLY_APPID, false, strategy);
//
//        //设置版本号
//        try {
//            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//            String versionName = packageInfo.versionName;
//            CrashReport.setAppVersion(getApplicationContext(), versionName);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 严苛模式主要检测两大问题:
     * 1.线程策略，即TreadPolicy，
     * 2.VM策略，即VmPolicy
     */
    private void initStrictMode() {
        /**
         * 线程监控策略
         */
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()//将对当前线程应用该策略
//                    .detectDiskReads()//监控磁盘读
                .detectDiskWrites()//监控磁盘写
                .detectNetwork()//监控网络访问
                .detectAll()//检测当前线程所有函数
                /**
                 * 表示将警告输出到LogCat，你也可以使用其他或增加新的惩罚（penalty）函数，例如使用penaltyDeath()的话，一旦StrictMode消息被写到LogCat后应用就会崩溃。
                 */
                .penaltyLog()
                //penaltyDeath()
                //.penaltyDialog()// 弹出违规提示对话框
                .build());

        /**
         * VM虚拟机监控策略
         */
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()//内存泄露的Activity对象
                .detectLeakedSqlLiteObjects()//内存泄露的SQLite对象
                .detectAll()//内存泄露的其他任何类似可关闭对象
                .penaltyLog()
                //.penaltyDeath()
                .build());

    }

}
