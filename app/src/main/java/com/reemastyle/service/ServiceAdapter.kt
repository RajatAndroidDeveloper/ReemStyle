package com.reemastyle.service

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.reemastyle.R
import com.reemastyle.model.services.ServicesItem
import kotlinx.android.synthetic.main.service_adapter_item_layout.view.*

class ServiceAdapter (private var context: Context, private var serviceItemClicked: ServiceItemClicked,private var serviceList: ArrayList<ServicesItem>) : RecyclerView.Adapter<ServiceAdapter.MyViewHolder>() {
    inner class MyViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.service_adapter_item_layout, parent, false) as ConstraintLayout
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.layout.txt_service_size.text = (serviceList[position].name?:"").capitalize()
        holder.layout.txt_price.text = context.getString(R.string.currency_value)+serviceList[position].price
        holder.layout.txtDescription.text = (serviceList[position].discription?:"").capitalize()
        if((serviceList[position].quantity ?: 0) < 10){
            holder.layout.txt_quantity.text  = (serviceList[position].quantity?:0).toString()
        }else{
            holder.layout.txt_quantity.text  = (serviceList[position].quantity?:0).toString()
        }

        holder.layout.img_add.setOnClickListener {
            serviceItemClicked.onServiceClicked(position,"add")
        }

        holder.layout.img_minus.setOnClickListener {
            serviceItemClicked.onServiceClicked(position,"minus")
        }
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }
}

interface ServiceItemClicked{
    fun onServiceClicked(position:Int,type:String)
}