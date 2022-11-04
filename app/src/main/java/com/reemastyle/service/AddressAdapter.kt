package com.reemastyle.service

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.reemastyle.R

class AddressAdapter (private var context: Context, private var addressItemClicked: AddressItemClicked) : RecyclerView.Adapter<AddressAdapter.MyViewHolder>() {
    inner class MyViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.address_adapter_item_layout, parent, false) as ConstraintLayout
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 2
    }
}

interface AddressItemClicked{
    fun onAddressItemClicked(position:Int)
}