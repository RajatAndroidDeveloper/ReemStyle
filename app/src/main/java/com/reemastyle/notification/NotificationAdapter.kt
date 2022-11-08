package com.reemastyle.notification

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.reemastyle.R
import com.reemastyle.model.notification.NotiItem
import com.reemastyle.service.AddressAdapter
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.notification_adapter_item_layout.view.*

class NotificationAdapter (private var context: Context, private var notificationList: ArrayList<NotiItem>) : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {
    inner class MyViewHolder(val layout: CardView) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.notification_adapter_item_layout, parent, false) as CardView
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.layout.txtTitle.text = notificationList[position].title
        holder.layout.txtMessage.text = notificationList[position].message
        holder.layout.txtDate.text = Utils.getFormattedTimeValue(notificationList[position].date?:"")
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }
}

interface AddressItemClicked{
    fun onAddressItemClicked(position:Int)
}