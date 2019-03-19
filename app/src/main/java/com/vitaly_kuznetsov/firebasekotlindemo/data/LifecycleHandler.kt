package com.vitaly_kuznetsov.firebasekotlindemo.data

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

class LifecycleHandler : Application.ActivityLifecycleCallbacks {

    val TAG = "FIREBASE_LOG"

    companion object {
        var active : Boolean = false
    }

    override fun onActivityPaused(p0: Activity?) {
        active = false
        Log.d(TAG, "Pause")
    }

    override fun onActivityResumed(p0: Activity?) {
        active = true
        Log.d(TAG, "Res")
    }

    override fun onActivityStarted(p0: Activity?) {
        active = true
        Log.d(TAG, "star")
    }

    override fun onActivityDestroyed(p0: Activity?) {
        active = false
        Log.d(TAG, "des")
    }

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {}

    override fun onActivityStopped(p0: Activity?) {
        active = false
        Log.d(TAG, "sto")
    }

    override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
        active = true
        Log.d(TAG, "cre")
    }

}