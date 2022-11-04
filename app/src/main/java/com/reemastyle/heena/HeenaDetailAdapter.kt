package com.reemastyle.heena

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.reemastyle.R
import kotlinx.android.synthetic.main.heena_detail_adapter_item_layout.view.*

class HeenaDetailAdapter (
    private var context: Context,private var legDataList: ArrayList<Int>,private var onHeenaDetailClickedListener: HeenaDetailClickedListener
) : RecyclerView.Adapter<HeenaDetailAdapter.MyViewHolder>() {
    inner class MyViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.heena_detail_adapter_item_layout, parent, false) as ConstraintLayout
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.layout.ic_image.setImageResource(legDataList[position])
        holder.layout.setOnClickListener {
            onHeenaDetailClickedListener.onHeenaDetailClickedListener(position)
        }
    }

    override fun getItemCount(): Int {
        return legDataList.size
    }
}

interface HeenaDetailClickedListener{
    fun onHeenaDetailClickedListener(position: Int)
}