package com.moo.loginplansapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.moo.apt.login.RequireLogin

@RequireLogin
class BusinessActivity : AppCompatActivity() {
    companion object {
        fun intentStart(context: Context) {
            context.startActivity(Intent(context, BusinessActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business)
    }
}