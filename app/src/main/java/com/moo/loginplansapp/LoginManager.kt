package com.moo.loginplansapp

import android.content.Context

object LoginManager {
    const val LOGIN_REQUEST_CODE_BUSINESS = 10010

    fun jumpToRealPage(context: Context, requestCode: Int) {
        if (requestCode != 0) {
            when (requestCode) {
                LOGIN_REQUEST_CODE_BUSINESS -> {
                    BusinessActivity.intentStart(context)
                }
                //...其他code
            }
        }
    }

}