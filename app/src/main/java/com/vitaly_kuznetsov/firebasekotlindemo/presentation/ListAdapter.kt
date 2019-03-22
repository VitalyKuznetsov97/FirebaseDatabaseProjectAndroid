package com.vitaly_kuznetsov.firebasekotlindemo.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vitaly_kuznetsov.firebasekotlindemo.R

class ListAdapter(private val list: List<String>)
    : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(list[position]) {
            holder.textView?.text = this
        }
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var textView: TextView? = null

        init {
            textView = view.findViewById(R.id.text_holder)
        }
    }
}