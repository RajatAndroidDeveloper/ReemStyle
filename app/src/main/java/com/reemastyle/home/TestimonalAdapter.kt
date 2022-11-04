package com.reemastyle.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.reemastyle.R
import com.reemastyle.model.home.TestimonialsItem

class TestimonalAdapter(var testimonialsList : ArrayList<TestimonialsItem>, var context : Context)  : RecyclerView.Adapter<TestimonalAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    data class Card(val id: Int)

    private val items = mutableListOf<Card>().apply {
        repeat(4) { add(Card(it)) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.testimonal_pager_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = testimonialsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_message.text =testimonialsList[position].review
        holder.txt_author.text = "-"+testimonialsList[position].clientname
    }

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var txt_message: TextView
        var txt_author: TextView

        init {
            txt_message = itemView.findViewById(R.id.txt_message)
            txt_author = itemView.findViewById(R.id.txt_author)
        }
    }
}