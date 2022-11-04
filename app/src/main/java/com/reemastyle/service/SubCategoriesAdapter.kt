package com.reemastyle.service

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
import com.reemastyle.model.subcategory.PackagesItem
import kotlinx.android.synthetic.main.sub_categories_adapter_item_layout.view.*

class SubCategoriesAdapter (private var context: Context, private var subCategoryItemClicked: SubCategoryItemClicked,private var subCategoryList: ArrayList<PackagesItem>) : RecyclerView.Adapter<SubCategoriesAdapter.MyViewHolder>() {
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

        holder.layout.txt_subCategory_name.text  = subCategoryList[position].catName
        holder.layout.txt_total_services.text  = subCategoryList[position].servicecount.toString()+" "+context.getString(R.string.services)
        if(!subCategoryList[position].image.isNullOrEmpty()){
            Glide.with(context).load(subCategoryList[position].image).into(holder.layout.img_sub_Category)
        }
        holder.layout.setOnClickListener {
            selected = position
            notifyDataSetChanged()
            subCategoryItemClicked.onSubCategoryClicked(subCategoryList[position].id?.toInt()!!,position)
        }
    }

    override fun getItemCount(): Int {
        return subCategoryList.size
    }
}

interface SubCategoryItemClicked{
    fun onSubCategoryClicked(position:Int, catePosition: Int)
}