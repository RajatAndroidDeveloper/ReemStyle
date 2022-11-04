package com.reemastyle.home
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reemastyle.R
import com.reemastyle.model.home.OffersItem

class OfferPagerAdapter internal constructor(
    var offers: ArrayList<OffersItem>,var context: Context) :
    RecyclerView.Adapter<OfferPagerAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.offer_pager_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_offer.text = offers[position].title

        if(offers[position].offerType == "percentage") {
            holder.txt_offer_value.text =
                offers[position].offerValue + "% " + context.getString(R.string.off)
        }else{
            holder.txt_offer_value.text = offers[position].offerValue + " " + context.getString(R.string.off)
        }
        if(!offers[position].image.isNullOrEmpty()){
            Glide.with(context).load(offers[position].image).into(holder.img_offer)
        }
    }

    override fun getItemCount(): Int {
        return offers.size
    }

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var txt_offer: TextView
        var txt_offer_value: TextView
        var img_offer: ImageView

        init {
            txt_offer = itemView.findViewById(R.id.txt_offer)
            img_offer = itemView.findViewById(R.id.img_offer)
            txt_offer_value = itemView.findViewById(R.id.txt_offer_value)
        }
    }

}

