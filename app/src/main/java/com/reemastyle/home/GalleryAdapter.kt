package com.reemastyle.home

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reemastyle.HomeActivity
import com.reemastyle.R
import com.reemastyle.model.home.GalleryItem
import kotlin.math.roundToInt

class GalleryAdapter internal constructor(
    var gallery: ArrayList<GalleryItem>, var context: Context, private var homeActivity: HomeActivity
) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.item_gallery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(!gallery[position].image.isNullOrEmpty()){
            Glide.with(context).load(gallery[position].image).into(holder.img_offer)
        }
    }

    override fun getItemCount(): Int {
        return gallery.size
    }

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var img_offer: ImageView

        init {
            img_offer = itemView.findViewById(R.id.img_offer)
        }
    }
}

