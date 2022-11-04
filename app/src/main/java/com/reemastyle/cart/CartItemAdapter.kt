package com.reemastyle.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.reemastyle.R
import com.reemastyle.model.cart.ItemsItem
import com.reemastyle.model.cart.SavedCartResponse
import com.reemastyle.preferences.Preferences
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.cart_adapter_item_layout.view.*


class CartItemAdapter(
    private var context: Context,
    private var cartItemClicked: CartItemClicked,
    private var cartData: ArrayList<ItemsItem>
) : RecyclerView.Adapter<CartItemAdapter.MyViewHolder>() {
    inner class MyViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.cart_adapter_item_layout, parent, false) as ConstraintLayout
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (Preferences.prefs?.getString("Language","en") == "ar" && (cartData[position].orderSlotTime?:"").contains("AM")){
            holder.layout.txt_time_value.text  = (cartData[position].orderSlotTime?:"").replace("AM", context.getString(R.string.am_text))
        }else if (Preferences.prefs?.getString("Language","en") == "ar" && (cartData[position].orderSlotTime?:"").contains("PM")){
            holder.layout.txt_time_value.text  = (cartData[position].orderSlotTime?:"").replace("PM", context.getString(R.string.pm_text))
        }

        var homeServicePrice: Double  = 0.0
        if(cartData[position].categoryName!!.trim() == "Leg front" || cartData[position].categoryName!!.trim() == "Hand front" ||cartData[position].categoryName!!.trim() == "Hand front and back" || cartData[position].categoryName!! == "Leg front and back"
            || cartData[position].categoryName!!.trim() == "الجبهة الساق" || cartData[position].categoryName!!.trim() == "اليد الأمامية"
            || cartData[position].categoryName!! == "اليد الأمامية والخلفية" || cartData[position].categoryName!!.trim() == "أمامي وخلفي الساق" ){
            holder.layout.txt_service_name.text = "${context.getString(R.string.heena)}, ${cartData[position].categoryName}"
            var price = (cartData[position].subtotal?:"0.0").toDouble()/(cartData[position].qty?:"0").toDouble()
            holder.layout.txt_service_price.text = context.getString(R.string.currency_value)+price
            holder.layout.txt_subtotal.text  = "${context.getString(R.string.currency_value)} ${cartData[position].subtotal}"
            holder.layout.txt_total.text  = "${context.getString(R.string.currency_value)} ${cartData[position].totalAmount}"
            holder.layout.txt_service_fee.text = "${context.getString(R.string.currency_value)} " + cartData[position].homeservice
            holder.layout.txt_quant_val.text  = "${cartData[position].qty}"
        }else {
            holder.layout.txt_service_fee.text = context.getString(R.string.currency_value) + cartData[position].homeservice
            homeServicePrice = (cartData[position].homeservice?:"0.0").toDouble()
            holder.layout.txt_service_name.text =
                cartData[position].subcategoryName + ", " + cartData[position].servicename
            holder.layout.txt_service_price.text = context.getString(R.string.currency_value)+cartData[position].serviceprice
            holder.layout.txt_subtotal.text = context.getString(R.string.currency_value)+(((cartData[position].serviceprice ?: "0.0").toDouble()) * ((cartData[position].serviceQty ?: "0.0").toDouble()))
            holder.layout.txt_total.text = context.getString(R.string.currency_value)+((((cartData[position].serviceprice ?: "0.0").toDouble()) * ((cartData[position].serviceQty ?: "0.0").toDouble()))+homeServicePrice)
            holder.layout.txt_quant_val.text  = cartData[position].serviceQty
        }

        holder.layout.txt_date_value.text = Utils.getFormattedDateForCart(cartData[position].orderDate.toString())
        holder.layout.txt_day.text = Utils.getFormattedDayOnly(cartData[position].orderDate.toString())
        if(cartData[position].addresstype == "home"){
            holder.layout.txt_booking_type_val.text = context.getString(R.string.home)
        }else{
            holder.layout.txt_booking_type_val.text = context.getString(R.string.shop)
        }
        holder.layout.img_option_menu.setOnClickListener {
            cartItemClicked.onCartItemClicked(position,holder.layout.img_option_menu,"menu")
        }
    }

    override fun getItemCount(): Int {
        return cartData.size
    }
}

interface CartItemClicked{
    fun onCartItemClicked(position:Int,type:String)
    fun onCartItemClicked(position: Int,view: View,type:String)
}