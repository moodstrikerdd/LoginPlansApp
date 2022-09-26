package com.moo.loginplansapp

import android.content.Context

object UserManager {
    private const val USER_MANAGER = "userManager"
    private const val USER_LOGIN_STATUS = "userLoginStatus"

    fun isUserLogin(): Boolean {
        val sp = MApp.application!!.getSharedPreferences(USER_MANAGER, Context.MODE_PRIVATE);
        return sp.getBoolean(USER_LOGIN_STATUS, false)
    }

    fun setUserLoginStatus(login: Boolean) {
        val sp = MApp.application!!.getSharedPreferences(USER_MANAGER, Context.MODE_PRIVATE);
        val edit = sp.edit()
        edit.putBoolean(USER_LOGIN_STATUS, login)
        edit.apply()
    }
}