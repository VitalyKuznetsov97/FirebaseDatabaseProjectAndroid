package com.vitaly_kuznetsov.firebasekotlindemo.presentation

import android.content.Context
import com.vitaly_kuznetsov.firebasekotlindemo.data.ExceptionBundle

interface IMainView {
    fun getFields() : List<String>
    fun clearFields()
    fun showDataBaseContents(list : List<String>)
    fun showError(error : ExceptionBundle)
    fun toggleReconnect(toggle : Boolean)

    fun getContext() : Context
}