package com.reemastyle.heena

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reemastyle.R
import com.reemastyle.model.heenacategories.ResultItem
import kotlinx.android.synthetic.main.sub_categories_adapter_item_layout.view.*

class HeenaAdapter(private var context: Context, private var onHeenaItemClicked: HeenaItemClicked, private var heenaList: ArrayList<ResultItem>) : RecyclerView.Adapter<HeenaAdapter.MyViewHolder>() {
    inner class MyViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout)

    private var selected = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.sub_categories_adapter_item_layout, parent, false) as ConstraintLayout
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        if(selected == position){
            holder.layout.img_subCategory.visibility = View.VISIBLE
            holder.layout.txt_subCategory_name.setTextColor(ContextCompat.getColor(context, R.color.black))
            holder.layout.txt_total_services.setTextColor(ContextCompat.getColor(context, R.color.black))
            holder.layout.ic_arrow.setImageResource(R.drawable.ic_down_arrow_white)
        }

        holder.layout.txt_subCategory_name.text  = heenaList[position].name
        holder.layout.txt_total_services.visibility = View.INVISIBLE
        if(!heenaList[position].iMG.isNullOrEmpty()){
            Glide.with(context).load(heenaList[position].iMG).into(holder.layout.img_sub_Category)
        }
        holder.layout.setOnClickListener {
            selected = position
            notifyDataSetChanged()
            onHeenaItemClicked.onHeenaItemClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return heenaList.size
    }
}

interface HeenaItemClicked{
    fun onHeenaItemClicked(position:Int)
}