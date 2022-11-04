package com.reemastyle.service

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reemastyle.R
import com.reemastyle.model.categories.AllCategoriesItem
import kotlinx.android.synthetic.main.categories_adapter_item_layout.view.*

class AllCategoryAdapter(
    private var context: Context,
    private var categoryItemClicked: CategoryItemClicked,
    private var categoryList: ArrayList<AllCategoriesItem>,
    private var type: String
) : RecyclerView.Adapter<AllCategoryAdapter.MyViewHolder>() {
    inner class MyViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.categories_adapter_item_layout, parent, false) as ConstraintLayout
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (!categoryList[position].image.isNullOrEmpty())
            Glide.with(context).load(categoryList[position].image)
                .into(holder.layout.img_category)
        holder.layout.txt_category_title.text = categoryList[position].catName

        holder.layout.setOnClickListener {
            categoryItemClicked.onCategoryClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun filterList(filteredlist: java.util.ArrayList<AllCategoriesItem>) {
        categoryList = filteredlist
        notifyDataSetChanged()
    }
}

interface CategoryItemClicked {
    fun onCategoryClicked(position: Int)
}
