package com.moo.loginplansapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.moo.loginplansapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initClick()
    }

    private fun initClick() {
        binding.btnForResult.setOnClickListener {
            if (UserManager.isUserLogin()) {
                BusinessActivity.intentStart(this)
            } else {
                startActivityForResult(
                    Intent(this, LoginActivity::class.java),
                    LoginManager.LOGIN_REQUEST_CODE_BUSINESS
                )
            }
        }

        binding.btnLoginTarget.setOnClickListener {
            if (UserManager.isUserLogin()) {
                BusinessActivity.intentStart(this)
            } else {
                LoginTargetActivity.intentStart(
                    this,
                    LoginManager.LOGIN_REQUEST_CODE_BUSINESS
                )
            }
        }

        binding.btnFragment.setOnClickListener {
            BridgeFragment.loginToJump(this) {
                BusinessActivity.intentStart(this)
            }
        }


        binding.btnHook.setOnClickListener {
            BusinessActivity.intentStart(this)
        }

        binding.btnLoginOut.setOnClickListener {
            UserManager.setUserLoginStatus(false)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LoginManager.LOGIN_REQUEST_CODE_BUSINESS
            && resultCode == Activity.RESULT_OK
        ) {
            BusinessActivity.intentStart(this)
        }
    }

}