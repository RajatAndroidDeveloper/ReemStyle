package com.reemastyle.packages

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reemastyle.R
import com.reemastyle.model.packages.PackagesItem
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.package_adapter_layout.view.*

class PackagesAdapter(
    private var context: Context,
    private var packageItemClicked: PackageItemClicked,
    private var packageList: ArrayList<PackagesItem>
) : RecyclerView.Adapter<PackagesAdapter.MyViewHolder>() {
    inner class MyViewHolder(val layout: CardView) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.package_adapter_layout, parent, false) as CardView
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (!packageList[position].image.isNullOrEmpty()) {
            Glide.with(context).load(packageList[position].image).into(holder.layout.img_offer)
        }
        holder.layout.txt_salon_name.text = packageList[position].packname
        holder.layout.txt_description.text = packageList[position].discription
        holder.layout.txt_price.text = "QAR" + packageList[position].price
        holder.layout.txt_offerPrice.text =Utils.getFormatlistPrice("QAR" + packageList[position].oldprice)
        holder.layout.txt_offer_value.text = packageList[position].discount + " " + context.getString(R.string.off)
        holder.layout.setOnClickListener {
            packageItemClicked.onPackageItemClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return packageList.size
    }
}

interface PackageItemClicked {
    fun onPackageItemClicked(position: Int)
}