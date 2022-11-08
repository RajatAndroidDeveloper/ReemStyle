package com.reemastyle.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reemastyle.R
import com.reemastyle.model.home.CategoriesItem
import kotlinx.android.synthetic.main.categories_adapter_item_layout.view.*

class CategoriesAdapter(
    private var context: Context,
    private var categoryItemClicked: CategoryItemClicked,
    private var categoryList: ArrayList<CategoriesItem>,
    private var type: String
) : RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>() {
    inner class MyViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.categories_adapter_item_layout, parent, false) as ConstraintLayout
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (categoryList[position].id == "0" && type == "home") {
            holder.layout.img_category.background = context.getDrawable(R.drawable.ic_selected_cate)
            holder.layout.img_category.setImageResource(R.drawable.more_icon)
            holder.layout.txt_category_title.text = categoryList[position].catName
            holder.layout.img_category.setPadding(55)
        } else {
            if (!categoryList[position].image.isNullOrEmpty())
                Glide.with(context).load(categoryList[position].image).placeholder(R.drawable.cate_dummy)
                    .into(holder.layout.img_category)
            holder.layout.txt_category_title.text = categoryList[position].catName
        }

        holder.layout.setOnClickListener {
            categoryItemClicked.onCategoryClicked(categoryList[position].id ?:"0",position)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}

interface CategoryItemClicked {
    fun onCategoryClicked(position: String, pos: Int)
}