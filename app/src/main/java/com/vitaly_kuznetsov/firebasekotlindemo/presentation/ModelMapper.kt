package com.vitaly_kuznetsov.firebasekotlindemo.presentation

import android.util.Log
import com.google.gson.Gson
import com.vitaly_kuznetsov.firebasekotlindemo.data.ExceptionBundle
import java.text.SimpleDateFormat
import java.util.*

val TAG = "FIREBASE_LOG"

@Throws(ExceptionBundle::class)
fun transform(message : String) : List<String> {
    val gson = Gson()
    return try {
        Log.d(TAG, message)
        val list = gson.fromJson(message, ContentList::class.java)
        transform(list)
    }
    catch (e : Exception){
        e.printStackTrace()
        throw ExceptionBundle(ExceptionBundle.Reason.WRONG_CONTENTS_FORMAT)
    }
}

private fun transform(list : ContentList) : List<String>{
    val listNew = MutableList(list.contents.size){""}

    for ((i, content) in list.contents.withIndex()) {
        if (content == null) listNew[i] = "Empty field"
        else {
            val date = Date(content.timestamp)
            val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault())
            listNew[i] = """${dateFormat.format(date)}: ${i} = ${content.contentValue.toString()}"""
        }
    }
    return listNew
}

class ContentList {
    internal var contents = ArrayList<Content>()
}

class Content {
    var contentValue: String? = null
    var timestamp: Long = 0
}