package com.reemastyle.bookings

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.reemastyle.R
import com.reemastyle.model.history.ItemsItem
import com.reemastyle.preferences.Preferences
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.bookings_adapter_item_layout.view.*
import kotlinx.android.synthetic.main.bookings_adapter_item_layout.view.img_option_menu
import kotlinx.android.synthetic.main.bookings_adapter_item_layout.view.txt_booking_type_val
import kotlinx.android.synthetic.main.bookings_adapter_item_layout.view.txt_date_value
import kotlinx.android.synthetic.main.bookings_adapter_item_layout.view.txt_quant_val
import kotlinx.android.synthetic.main.bookings_adapter_item_layout.view.txt_service_fee
import kotlinx.android.synthetic.main.bookings_adapter_item_layout.view.txt_service_name
import kotlinx.android.synthetic.main.bookings_adapter_item_layout.view.txt_service_price
import kotlinx.android.synthetic.main.bookings_adapter_item_layout.view.txt_subtotal
import kotlinx.android.synthetic.main.bookings_adapter_item_layout.view.txt_time_value
import kotlinx.android.synthetic.main.bookings_adapter_item_layout.view.txt_total

class BookingsAdapter(
    private var context: Context,
    private var bookingItemClicked: BookingItemClicked,
    private var bookings: ArrayList<ItemsItem>
) : RecyclerView.Adapter<BookingsAdapter.MyViewHolder>() {
    inner class MyViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.bookings_adapter_item_layout, parent, false) as ConstraintLayout
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.layout.txt_date.text = Utils.getFormattedDateValue(bookings[position].date ?: "")
        holder.layout.txt_booking_type.text = (bookings[position].itemstatus ?: "")
        if (Preferences.prefs?.getString("Language","en") == "ar" && (bookings[position].orderSlotTime?:"").contains("AM")){
            holder.layout.txt_time_value.text  = (bookings[position].orderSlotTime?:"").replace("AM", context.getString(R.string.am_text))
        }else if (Preferences.prefs?.getString("Language","en") == "ar" && (bookings[position].orderSlotTime?:"").contains("PM")){
            holder.layout.txt_time_value.text  = (bookings[position].orderSlotTime?:"").replace("PM", context.getString(R.string.pm_text))
        }
        holder.layout.txt_day.text = Utils.getFormattedDayOnly(bookings[position].orderDate.toString())

        if(bookings[position].addresstype == "home"){
            holder.layout.txt_booking_type_val.text = context.getString(R.string.home)
        }else{
            holder.layout.txt_booking_type_val.text = context.getString(R.string.shop)
        }
        holder.layout.txt_date_value.text = Utils.getFormattedDateForCart(bookings[position].orderDate ?: "")

        if(bookings[position].categoryName!!.trim() == "Leg front" || bookings[position].categoryName!!.trim() == "Hand front" ||bookings[position].categoryName!!.trim() == "Hand front and back" || bookings[position].categoryName!! == "Leg front and back"
            || bookings[position].categoryName!!.trim() == "الجبهة الساق" || bookings[position].categoryName!!.trim() == "اليد الأمامية"
            || bookings[position].categoryName!! == "اليد الأمامية والخلفية" || bookings[position].categoryName!!.trim() == "أمامي وخلفي الساق" ){
            holder.layout.txt_service_name.text = "${context.getString(R.string.heena)}, ${bookings[position].categoryName}"
            var price = (bookings[position].subtotal?:"0.0").toDouble()/(bookings[position].qty?:"0").toDouble()

            holder.layout.txt_service_price.text = context.getString(R.string.currency_value)+price
            holder.layout.txt_subtotal.text  = "${context.getString(R.string.currency_value)} ${bookings[position].subtotal}"
            holder.layout.txt_total.text  = "${context.getString(R.string.currency_value)} ${bookings[position].totalAmount}"
            holder.layout.txt_service_fee.text = "${context.getString(R.string.currency_value)} " + bookings[position].homeservice
            holder.layout.txt_quant_val.text  = "${bookings[position].qty}"
        }else {
            holder.layout.txt_service_name.text =
                "${bookings[position].subcategoryName}, ${bookings[position].servicename}"
            holder.layout.txt_service_price.text = "${context.getString(R.string.currency_value)} ${bookings[position].servicePrice}"
            var subTotal = ((bookings[position].servicePrice
                ?: "0").toDouble() * (bookings[position].serviceQty ?: "0").toDouble())
            holder.layout.txt_subtotal.text = "${context.getString(R.string.currency_value)} $subTotal"
            holder.layout.txt_service_fee.text = "${context.getString(R.string.currency_value)} ${bookings[position].servicePrice}"
            holder.layout.txt_total.text =
                "${context.getString(R.string.currency_value)} ${(subTotal + (bookings[position].servicePrice ?: "0").toDouble())}"
            holder.layout.txt_quant_val.text = "${bookings[position].serviceQty}"
        }
        holder.layout.img_option_menu.setOnClickListener {
            bookingItemClicked.onBookingClicked(position,"dialog",holder.layout.img_option_menu)
        }
    }

    override fun getItemCount(): Int {
        return bookings.size
    }
}

interface BookingItemClicked{
    fun onBookingClicked(position:Int, type:String,imageView:ImageView)
}