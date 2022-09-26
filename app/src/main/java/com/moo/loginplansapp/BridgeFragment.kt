package com.moo.loginplansapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class BridgeFragment : Fragment() {
    companion object {
        private const val FRAGMENT_TAG = "fragment_tag"
        private const val REQUEST_CODE = 100011
        fun loginToJump(activity: FragmentActivity, callback: () -> Unit) {
            if (UserManager.isUserLogin()) {
                callback.invoke()
                return
            }
            var fragment = activity.supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
            if (fragment == null) {
                fragment = BridgeFragment()
                fragment.callback = callback
                activity
                    .supportFragmentManager
                    .beginTransaction()
                    .add(fragment, FRAGMENT_TAG)
                    .commitNowAllowingStateLoss()
            } else {
                (fragment as BridgeFragment).callback = callback
                fragment.login()
            }

        }
    }

    var callback: (() -> Unit)? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        login()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun login() {
        startActivityForResult(
            Intent(
                context,
                LoginActivity::class.java
            ),
            REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (callback != null) {
            if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                callback?.invoke()
            }
            callback = null
        }
    }
}