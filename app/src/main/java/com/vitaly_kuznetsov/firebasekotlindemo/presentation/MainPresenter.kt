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
        val valueText = view.getValue()
        val num = valueText.toIntOrNull()

        if (valueText == "") {
            onError(ExceptionBundle(ExceptionBundle.Reason.EMPTY_FIELDS))
            return
        }

        view.clearFields()
        checkConnection(valueText)
    }

    fun onReconnectClicked() {
        checkConnection()
    }

    private fun checkConnection(valueText: String = ""){
        val internetConnection = InternetConnection()
        disposable = internetConnection.hasInternetConnection()
            .subscribe{hasInternet ->
                if (hasInternet){
                    view.toggleReconnect(false)
                    fireBaseController.connect(this)
                    if (valueText != "") sendMessage(valueText)
                }
                else{
                    view.toggleReconnect(true)
                    onError(ExceptionBundle(ExceptionBundle.Reason.NETWORK_UNAVAILABLE))
                }
            }
    }

    fun onMessageReceived(message : String?){
        if (message != null){
            try {
                view.showDataBaseContents(transform(message))
            }
            catch (e : ExceptionBundle){
                onError(e)
            }

            if (!fireBaseController.started) fireBaseController.startService(view.getContext()).subscribe()
        }
        else view.showDataBaseContents(listOf("Empty"))
        disposable?.dispose()
    }

    private fun sendMessage(message: String) {
        fireBaseController.sendMessage(message)
        disposable?.dispose()
    }

    fun onError(error : ExceptionBundle) {
        view.showError(error)
        disposable?.dispose()
    }
}