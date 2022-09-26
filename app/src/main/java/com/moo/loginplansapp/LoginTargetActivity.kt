package com.moo.loginplansapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.moo.loginplansapp.databinding.ActivityLoginBinding

class LoginTargetActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    companion object {
        private const val NAME_REQUEST_CODE = "requestCode"
        fun intentStart(context: Context, requestCode: Int) {
            context.startActivity(Intent(
                context,
                LoginTargetActivity::class.java
            ).apply {
                putExtra(NAME_REQUEST_CODE, requestCode)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val requestCode = intent.getIntExtra(NAME_REQUEST_CODE, 0)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener {
            UserManager.setUserLoginStatus(true)
            LoginManager.jumpToRealPage(this, requestCode)
            finish()
        }
    }
}