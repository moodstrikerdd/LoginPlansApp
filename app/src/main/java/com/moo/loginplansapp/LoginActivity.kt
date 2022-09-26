package com.moo.loginplansapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.moo.apt.login.JudgeLogin
import com.moo.lib.hook.hook.AndLogin
import com.moo.loginplansapp.databinding.ActivityLoginBinding

@com.moo.apt.login.LoginActivity
class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    companion object {
        private const val NAME_REQUEST_CODE = "requestCode"
        fun intentStart(context: Context, requestCode: Int) {
            context.startActivity(
                Intent(context, LoginActivity::class.java).apply {
                    putExtra(NAME_REQUEST_CODE, requestCode)
                }
            )
        }

        // 该方法用于返回是否登录
        @JudgeLogin
        @JvmStatic
        fun checkLogin(): Boolean {
            return UserManager.isUserLogin()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener {
            UserManager.setUserLoginStatus(true)
            setResult(RESULT_OK)
            val targetIntent = intent.getParcelableExtra<Intent>(AndLogin.TARGET_ACTIVITY_NAME)
            if (targetIntent != null) {
                startActivity(targetIntent)
            }
            finish()
        }
    }
}