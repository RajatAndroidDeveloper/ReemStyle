package com.reemastyle.gallery

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.reemastyle.R
import com.reemastyle.model.gallery.GalleryItem
import kotlinx.android.synthetic.main.item_gallery_adapter.view.*

class GallerySmallAdapter (private var gallery: ArrayList<GalleryItem>, private var context: Context, private var onGallerySmallItemClick: OnGallerySmallItemClick) : RecyclerView.Adapter<GallerySmallAdapter.MyViewHolder>() {
    inner class MyViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery_small_adapter, parent, false) as ConstraintLayout
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(!gallery[position].image.isNullOrEmpty()){
            Log.e("asasaksasas",gallery[position].image+"SDsd")
            Glide.with(context).load(gallery[position].image?:"").apply( RequestOptions().override(300, 100)).placeholder(
                R.drawable.gallery_img).into(holder.layout.img_gallery)
        }

        holder.layout.setOnClickListener {
            onGallerySmallItemClick.onGallerySmallItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return gallery.size
    }
}

interface OnGallerySmallItemClick{
    fun onGallerySmallItemClick(position:Int)
}