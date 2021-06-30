package com.dat.bankorder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ListMessageAdapter(
    private val context: Context,
    private val dataSource: MutableList<SmsModels>
) : BaseAdapter() {
    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int = dataSource.size
    override fun getItem(position: Int): Any = dataSource[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.message_item, parent, false)
        val item = dataSource[position]
        val tvAddress = rowView.findViewById(R.id.tv_address) as TextView
        val tvMessage = rowView.findViewById(R.id.tv_message) as TextView
        val tvTime = rowView.findViewById(R.id.tv_time) as TextView
        tvAddress.text = item.address
        tvMessage.text = item.msg
        tvTime.text = item.time
        return rowView
    }
}