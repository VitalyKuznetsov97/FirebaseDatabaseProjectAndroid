package com.vitaly_kuznetsov.firebasekotlindemo.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.vitaly_kuznetsov.firebasekotlindemo.R
import com.vitaly_kuznetsov.firebasekotlindemo.presentation.MainPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class FireBaseController {

    var started = false
    val TAG = "FIREBASE_LOG"

    fun sendMessage (reference : String, message : String) {
        val dbReference = FirebaseDatabase.getInstance().getReference(reference)
        dbReference.setValue(message)
    }

    fun connect (presenter : MainPresenter) {
        val dbReference = FirebaseDatabase.getInstance().reference

        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.value.toString()
                presenter.onMessageReceived(value)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                presenter.onError(ExceptionBundle(ExceptionBundle.Reason.FIREBASE_UNAVAILABLE))
            }
        })
    }

    fun startService(context : Context) : Observable<String>{
        started = true
        return Observable.just("Started Service")
            .doOnNext { onStartService(context) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun onStartService(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = context.getString(R.string.default_notification_channel_id)
            val channelName = context.getString(R.string.default_notification_channel_name)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_LOW)
            )
        }

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                Log.d(TAG, token.toString())
            })

        FirebaseMessaging.getInstance().subscribeToTopic("FIREBASE_DATA_CHANGE")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscribtion Failed"
                }
                Log.d(TAG, msg)
            }
    }

}