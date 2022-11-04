package com.reemastyle.reschedule

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.reemastyle.R
import com.reemastyle.model.slots.SlotsItem
import com.reemastyle.service.TimeSlotSelected
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.time_slots_adapter_layout.view.*

class RescheduleTimeSlotAdapter (private var context: Context, private var timeSlotSelected: TimeSlotSelected, private var slotList: ArrayList<SlotsItem>) : RecyclerView.Adapter<RescheduleTimeSlotAdapter.MyViewHolder>() {
    inner class MyViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout)
    private var selected  = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.time_slots_adapter_layout, parent, false) as ConstraintLayout
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return slotList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.layout.txt_time.text  = slotList[position].time

        if(selected == position){
            holder.layout.txt_time.background = context.getDrawable(R.drawable.ic_app_color_time_slot_bg)
            holder.layout.txt_time.setTextColor(ContextCompat.getColor(context, R.color.white))
        }else{
            holder.layout.txt_time.background = context.getDrawable(R.drawable.ic_time_slot_grey_bg)
            holder.layout.txt_time.setTextColor(ContextCompat.getColor(context, R.color.black))
        }

        if(slotList[position].status == "0"){
            holder.layout.txt_time.background = context.getDrawable(R.drawable.ic_time_slot_grey_bg)
            holder.layout.txt_time.setTextColor(ContextCompat.getColor(context, R.color.black))
            holder.layout.isEnabled = false
            holder.layout.isClickable = false
        }

        holder.layout.setOnClickListener{
            if(slotList[position].status == "1"){
                timeSlotSelected.onTimeSlotSelected(position)
                selected = position
                notifyDataSetChanged()
            }else{
                Utils.showSnackBar(context.getString(R.string.this_slot_is_not_available),holder.layout)
            }
        }
    }
}

interface TimeSlotSelected {
    fun onTimeSlotSelected(position: Int)
}