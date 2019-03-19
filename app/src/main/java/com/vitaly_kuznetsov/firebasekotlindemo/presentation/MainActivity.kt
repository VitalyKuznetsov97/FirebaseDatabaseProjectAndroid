package com.vitaly_kuznetsov.firebasekotlindemo.presentation

import android.content.Context
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.vitaly_kuznetsov.firebasekotlindemo.R
import com.vitaly_kuznetsov.firebasekotlindemo.data.ExceptionBundle
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), IMainView {

    private val presenter : MainPresenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {presenter.onSendClicked()}
        reconnect.setOnClickListener {presenter.onReconnectClicked()}
        left.setOnFocusChangeListener { _, _ -> checkError() }
        right.setOnFocusChangeListener { _, _ -> checkError() }

        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.get(key)
                if (value != null) presenter.intentHandled(value)
            }
        }
    }

    private fun checkError() {
        val checkError = ExceptionBundle(ExceptionBundle.Reason.EMPTY_FIELDS)
        if (error_text.text == checkError.extras.getString(checkError.ERROR_STRING)) error_text.text = ""
    }

    override fun getFields() = listOf(left.text.toString(), right.text.toString())

    override fun clearFields() {
        left.setText("")
        right.setText("")
    }

    override fun showDataBaseContents(contents : String) {
        firebase_contents.text = contents
        error_text.text = ""
    }

    override fun showError(error: ExceptionBundle) {
        error_text.text = error.extras.getString(error.ERROR_STRING)
    }

    override fun toggleReconnect(toggle: Boolean) {
        button.isClickable = !toggle
        if (toggle) reconnect.visibility = VISIBLE
        else{
            reconnect.visibility = GONE
            error_text.text = ""
        }
    }

    override fun getContext(): Context {
        return this
    }
}
