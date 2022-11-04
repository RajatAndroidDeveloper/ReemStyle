package com.reemastyle.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reemastyle.R
import com.reemastyle.model.home.PackagesItem
import com.reemastyle.util.Utils

class TrendingPagerAdapter(var packageList: ArrayList<PackagesItem>, var context: Context, private var packageItemClicked: PackageItemClicked) : RecyclerView.Adapter<TrendingPagerAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingPagerAdapter.ViewHolder {
        val view: View = mInflater.inflate(R.layout.trending_pager_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_package_value.text = packageList[position].discount+" "+context.getString(R.string.off)
        holder.txt_salon_name.text = packageList[position].packname
        if(!packageList[position].image.isNullOrEmpty()){
           Glide.with(context).load(packageList[position].image).into(holder.img_package)
        }
        holder.txt_description.text = packageList[position].discription
        holder.txt_price.text  = "QAR${packageList[position].price}"
        holder.txt_offerPrice.text = Utils.getFormatlistPrice("QAR "+""+packageList[position].oldprice)
        holder.contentlayout.setOnClickListener {
            packageItemClicked.onPackageItemClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return packageList.size
    }

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var txt_salon_name: TextView
        var txt_package_value: TextView
        var txt_price: TextView
        var txt_offerPrice: TextView
        var txt_description: TextView
        var img_package: ImageView
        var contentlayout: CardView

        init {
            txt_salon_name = itemView.findViewById(R.id.txt_salon_name)
            txt_price = itemView.findViewById(R.id.txt_price)
            txt_offerPrice = itemView.findViewById(R.id.txt_offerPrice)
            img_package = itemView.findViewById(R.id.img_package)
            txt_package_value = itemView.findViewById(R.id.txt_package_value)
            txt_description = itemView.findViewById(R.id.txt_description)
            contentlayout = itemView.findViewById(R.id.contentlayout)
        }
    }
}

interface PackageItemClicked{
    fun onPackageItemClicked(position: Int)
}