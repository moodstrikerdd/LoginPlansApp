package com.moo.lib.hook.hook;

import android.content.Context;


public class AndLogin {
    public static final String TARGET_ACTIVITY_NAME = "targetActivity";
    private static volatile AndLogin instance;

    private AndLogin() {
    }

    public static AndLogin getInstance() {
        if (instance == null) {
            synchronized (AndLogin.class) {
                if (instance == null) {
                    instance = new AndLogin();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        HookUtil.HookAms(context);
    }

}
