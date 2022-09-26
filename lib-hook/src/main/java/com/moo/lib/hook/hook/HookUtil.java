package com.moo.lib.hook.hook;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.moo.lib.hook.Constant;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;


public class HookUtil {
    private static final String UTILS_PATH = "com.moo.apt.AndLoginUtils";
    private static Class<?> loginActivityClazz;
    private static final List<String> REQUIRE_LOGIN_NAMES = new ArrayList<>();

    @SuppressLint({"PrivateApi", "DiscouragedPrivateApi"})
    public static void HookAms(Context context) {
        try {
            Field singletonField;
            Class<?> iActivityManagerClass;
            // 1，获取Instrumentation中调用startActivity(,intent,)方法的对象
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // 10.0以上是ActivityTaskManager中的IActivityTaskManagerSingleton
                Class<?> activityTaskManagerClass = Class.forName("android.app.ActivityTaskManager");
                singletonField = activityTaskManagerClass.getDeclaredField("IActivityTaskManagerSingleton");
                iActivityManagerClass = Class.forName("android.app.IActivityTaskManager");
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 8.0,9.0在ActivityManager类中IActivityManagerSingleton
                Class<?> activityManagerClass = ActivityManager.class;
                singletonField = activityManagerClass.getDeclaredField("IActivityManagerSingleton");
                iActivityManagerClass = Class.forName("android.app.IActivityManager");
            } else {
                // 8.0以下在ActivityManagerNative类中 gDefault
                Class<?> activityManagerNative = Class.forName("android.app.ActivityManagerNative");
                singletonField = activityManagerNative.getDeclaredField("gDefault");
                iActivityManagerClass = Class.forName("android.app.IActivityManager");
            }
            singletonField.setAccessible(true);
            Object singleton = singletonField.get(null);

            // 2，获取Singleton中的mInstance，也就是要代理的对象
            Class<?> singletonClass = Class.forName("android.util.Singleton");
            Field mInstanceField = singletonClass.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);
            /* Object mInstance = mInstanceField.get(singleton); */
            Method getMethod = singletonClass.getDeclaredMethod("get");
            Object mInstance = getMethod.invoke(singleton);
            if (mInstance == null) {
                return;
            }
            // 3，对IActivityManager进行动态代理
            Object proxyInstance = Proxy.newProxyInstance(context.getClassLoader(), new Class[]{iActivityManagerClass},
                    (proxy, method, args) -> {
                        if (method.getName().equals("startActivity")) {
                            if (!isLogin()) {
                                int pos = 0;
                                for (; pos < args.length; pos++) {
                                    if (args[pos] instanceof Intent) {
                                        break;
                                    }
                                }
                                Intent originIntent = (Intent) args[pos];
                                if (originIntent.getComponent() != null) {
                                    String activityName = originIntent.getComponent().getClassName();
                                    if (isRequireLogin(activityName)) {
                                        if (getLoginActivity() != null) {
                                            Intent intent = new Intent(context, getLoginActivity());
                                            intent.putExtra(Constant.HOOK_AMS_EXTRA_NAME, originIntent);
                                            args[pos] = intent;
                                        }
                                    }
                                }
                            }
                        }
                        return method.invoke(mInstance, args);
                    });
            // 4，把代理赋值给IActivityManager类型的mInstance对象
            mInstanceField.set(singleton, proxyInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 该activity是否需要登录
     *
     */
    private static boolean isRequireLogin(String activityName) {
        if (REQUIRE_LOGIN_NAMES.size() == 0) {
            // 反射调用apt生成的方法
            try {
                Class<?> loginUtils = Class.forName(UTILS_PATH);
                Method getRequireLoginListMethod = loginUtils.getDeclaredMethod("getRequireLoginList");
                getRequireLoginListMethod.setAccessible(true);
                REQUIRE_LOGIN_NAMES.addAll((List<String>) getRequireLoginListMethod.invoke(null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return REQUIRE_LOGIN_NAMES.contains(activityName);
    }

    /**
     * 获取登录activity
     *
     */
    private static Class<?> getLoginActivity() {
        if (loginActivityClazz == null) {
            try {
                Class<?> loginUtils = Class.forName(UTILS_PATH);
                Method getLoginActivityMethod = loginUtils.getDeclaredMethod("getLoginActivity");
                getLoginActivityMethod.setAccessible(true);
                String loginActivity = (String) getLoginActivityMethod.invoke(null);
                loginActivityClazz = Class.forName(loginActivity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return loginActivityClazz;
    }

    /**
     * 是否已经登录
     *
     */
    private static boolean isLogin() {
        try {
            Class<?> loginUtils = Class.forName(UTILS_PATH);
            Method getJudgeLoginMethod = loginUtils.getDeclaredMethod("getJudgeLoginMethod");
            getJudgeLoginMethod.setAccessible(true);
            String methodPath = (String) getJudgeLoginMethod.invoke(null);
            String[] split = methodPath.split("#");
            if (split.length == 2) {
                String methodPkg = split[0];
                String methodName = split[1];
                Class<?> methodInClazz = Class.forName(methodPkg);
                Method methodNameMethod = methodInClazz.getDeclaredMethod(methodName);
                methodNameMethod.setAccessible(true);
                return (boolean) methodNameMethod.invoke(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
