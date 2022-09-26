package com.moo.lib.hook.startup

import android.content.Context
import androidx.startup.Initializer
import com.moo.lib.hook.hook.AndLogin

class AndLoginInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        AndLogin.getInstance().init(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}