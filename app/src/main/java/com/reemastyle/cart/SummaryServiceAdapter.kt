package com.reemastyle.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.reemastyle.R
import com.reemastyle.model.cart.ItemsItem
import com.reemastyle.preferences.Preferences
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.summary_booking_service_item.view.*
import kotlinx.android.synthetic.main.summary_booking_service_item.view.txt_booking_type_val
import kotlinx.android.synthetic.main.summary_booking_service_item.view.txt_date_value
import kotlinx.android.synthetic.main.summary_booking_service_item.view.txt_day
import kotlinx.android.synthetic.main.summary_booking_service_item.view.txt_quant_val
import kotlinx.android.synthetic.main.summary_booking_service_item.view.txt_service_name
import kotlinx.android.synthetic.main.summary_booking_service_item.view.txt_time_value

class SummaryServiceAdapter(private var context: Context, private var cartList: ArrayList<ItemsItem>) : RecyclerView.Adapter<SummaryServiceAdapter.MyViewHolder>() {
    inner class MyViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.summary_booking_service_item, parent, false) as ConstraintLayout
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(cartList[position].categoryName!!.trim() == "Leg front" || cartList[position].categoryName!!.trim() == "Hand front" ||cartList[position].categoryName!!.trim() == "Hand front and back" || cartList[position].categoryName!! == "Leg front and back" || cartList[position].categoryName!!.trim() == "الجبهة الساق" || cartList[position].categoryName!!.trim() == "اليد الأمامية"
            || cartList[position].categoryName!! == "اليد الأمامية والخلفية" || cartList[position].categoryName!!.trim() == "أمامي وخلفي الساق" ){

            holder.layout.txt_service_name.text = "${context.getString(R.string.heena)}, " + cartList[position].categoryName
            holder.layout.txt_date_value.text = Utils.getFormattedDateForCart(cartList[position].orderDate.toString())
            holder.layout.txt_quant_val.text = cartList[position].qty
            holder.layout.txt_booking_type_val.text = cartList[position].addresstype
            var price = ((cartList[position].subtotal?:"0.,0").toDouble()/((cartList[position].qty?:"0.0").toDouble()))
            holder.layout.txt_price.text = "${context.getString(R.string.currency_value)} $price"
        }else {
            holder.layout.txt_service_name.text = cartList[position].subcategoryName + ", " + cartList[position].servicename
            holder.layout.txt_date_value.text = Utils.getFormattedDateForCart(cartList[position].orderDate.toString())
            holder.layout.txt_quant_val.text = cartList[position].serviceQty
            holder.layout.txt_booking_type_val.text = cartList[position].addresstype
            holder.layout.txt_price.text = "${context.getString(R.string.currency_value)} ${cartList[position].serviceprice}"
        }
        if (Preferences.prefs?.getString("Language","en") == "ar" && (cartList[position].orderSlotTime?:"").contains("AM")){
            holder.layout.txt_time_value.text  = (cartList[position].orderSlotTime?:"").replace("AM", context.getString(R.string.am_text))
        }else if (Preferences.prefs?.getString("Language","en") == "ar" && (cartList[position].orderSlotTime?:"").contains("PM")){
            holder.layout.txt_time_value.text  = (cartList[position].orderSlotTime?:"").replace("PM", context.getString(R.string.pm_text))
        }else{
            holder.layout.txt_time_value.text  = (cartList[position].orderSlotTime?:"")
        }
        if(cartList[position].addresstype == "home"){
            holder.layout.txt_booking_type_val.text = context.getString(R.string.home)
        }else{
            holder.layout.txt_booking_type_val.text = context.getString(R.string.shop)
        }
        holder.layout.txt_day.text = Utils.getFormattedDayOnly(cartList[position].orderDate.toString())
    }

    override fun getItemCount(): Int {
        return cartList.size
    }
}