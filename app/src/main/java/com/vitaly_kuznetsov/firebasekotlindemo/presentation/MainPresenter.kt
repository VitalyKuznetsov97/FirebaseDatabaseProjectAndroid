package com.vitaly_kuznetsov.firebasekotlindemo.presentation

import android.util.Log
import com.vitaly_kuznetsov.firebasekotlindemo.data.ExceptionBundle
import com.vitaly_kuznetsov.firebasekotlindemo.data.FireBaseController
import com.vitaly_kuznetsov.firebasekotlindemo.data.InternetConnection
import io.reactivex.disposables.Disposable

class MainPresenter(private val view: IMainView) {

    val TAG = "FIREBASE_LOG"
    private var disposable : Disposable? = null
    private val fireBaseController = FireBaseController()

    init {
        checkConnection()
    }

    fun intentHandled(msg : Any) {if (msg is String) Log.i(TAG, msg)}

    fun onSendClicked() {
        val list = view.getFields()

        if (list.component1() == "" || list.component2() == "") {
            onError(ExceptionBundle(ExceptionBundle.Reason.EMPTY_FIELDS))
            return
        }

        view.clearFields()
        checkConnection(list)
    }

    fun onReconnectClicked() {
        checkConnection()
    }

    private fun checkConnection(list: List<String> = listOf()){
        val internetConnection = InternetConnection()
        disposable = internetConnection.hasInternetConnection()
            .subscribe{hasInternet ->
                if (hasInternet){
                    view.toggleReconnect(false)
                    fireBaseController.connect(this)
                    if (list.isNotEmpty()) sendMessage(list)
                }
                else{
                    view.toggleReconnect(true)
                    onError(ExceptionBundle(ExceptionBundle.Reason.NETWORK_UNAVAILABLE))
                }
            }
    }

    fun onMessageReceived(message : String?){
        if (message != null){
            view.showDataBaseContents(message)

            if (!fireBaseController.started) fireBaseController.startService(view.getContext()).subscribe()
        }
        else view.showDataBaseContents("Empty")
        disposable?.dispose()
    }

    private fun sendMessage(list: List<String>) {
        fireBaseController.sendMessage(list.component1(), list.component2())
        disposable?.dispose()
    }

    fun onError(error : ExceptionBundle) {
        view.showError(error)
        disposable?.dispose()
    }
}