package com.vitaly_kuznetsov.firebasekotlindemo.data

import android.app.Application

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(LifecycleHandler())
    }
}