package com.reemastyle.reschedule

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.common.PickType
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.reemastyle.HomeActivity
import com.reemastyle.R
import com.reemastyle.cart.AddToCartModel
import com.reemastyle.model.history.ItemsItem
import com.reemastyle.model.history.OptionsItem
import com.reemastyle.model.slots.SlotsItem
import com.reemastyle.service.ServiceDetailFragment
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.fragment_reschedule_booking.*
import kotlinx.android.synthetic.main.fragment_reschedule_booking.clAddressHome
import kotlinx.android.synthetic.main.fragment_reschedule_booking.clAddressShop
import kotlinx.android.synthetic.main.fragment_reschedule_booking.cl_select_date
import kotlinx.android.synthetic.main.fragment_reschedule_booking.img_home_selected
import kotlinx.android.synthetic.main.fragment_reschedule_booking.img_shop_selected
import kotlinx.android.synthetic.main.fragment_reschedule_booking.txt_date
import kotlinx.android.synthetic.main.fragment_reschedule_booking.txt_total
import kotlinx.android.synthetic.main.time_slot_dialog_layout.*
import okio.utf8Size
import java.util.*

class RescheduleBooking : Fragment(), com.reemastyle.service.TimeSlotSelected {
    private var cartItemData: ItemsItem? = null
    private var slotList: ArrayList<SlotsItem> = ArrayList<SlotsItem>()
    private lateinit var timeSlotsAdapter: RescheduleTimeSlotAdapter
    lateinit var viewModel : AddToCartModel
    private var addressType: String  = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reschedule_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBundledData()
        clickListeners()
        viewModel = ViewModelProviders.of(this).get(AddToCartModel::class.java)
        attachObservers()
    }

    private fun getBundledData() {
        if (arguments != null) {
            var data = arguments?.getString("bookingData")
            cartItemData = Gson().fromJson(data, ItemsItem::class.java)
            setUpData(cartItemData)
        }
    }

    private var heenaPrice = 0.0
    private fun setUpData(cartItemData: ItemsItem?) {
        selectedTime = cartItemData?.orderSlotTime?:"00:00:00"
        if(cartItemData?.categoryName!!.trim() == "Leg front" || cartItemData?.categoryName!!.trim() == "Leg front and back"||cartItemData?.categoryName!!.trim() == "Hand front and back"||cartItemData?.categoryName!!.trim() == "Hand front"
            || cartItemData?.categoryName!!.trim() == "الجبهة الساق" || cartItemData?.categoryName!!.trim() == "اليد الأمامية"
            || cartItemData?.categoryName!! == "اليد الأمامية والخلفية" || cartItemData?.categoryName!!.trim() == "أمامي وخلفي الساق" ){

            txt_service_size.text = "${getString(R.string.heena)}, " + cartItemData?.categoryName
            txt_price.text = "${getString(R.string.currency_value)} ${((cartItemData?.subtotal?:"0.0").toDouble())/((cartItemData?.qty?:"0.0").toDouble())}"
            txt_quantity.text = cartItemData?.qty

            txt_total.text = "${getString(R.string.currency_value)} ${cartItemData?.totalAmount}"
            heenaPrice =((cartItemData?.subtotal?:"0.0").toDouble())/((cartItemData?.qty?:"0.0").toDouble())
        }else {
            txt_service_size.text = cartItemData?.subcategoryName + ", " + cartItemData?.servicename
            txt_price.text = "${getString(R.string.currency_value)} ${cartItemData?.servicePrice ?: "0.0"}"
            txt_quantity.text = cartItemData?.serviceQty

            var totalAmount =
                (((cartItemData?.servicePrice ?: "0.0").toDouble()) * ((cartItemData?.serviceQty
                    ?: "0.0").toDouble()))
            txt_total.text = "${getString(R.string.currency_value)} "+(totalAmount + (cartItemData?.homeservice ?: "0.0").toDouble()).toString()
            cartItemData?.totalAmount = (totalAmount + (cartItemData?.homeservice ?: "0.0").toDouble())
        }
        txt_date.text = cartItemData?.orderDate ?: ""
        txt_time.text = cartItemData?.orderSlotTime

        if (cartItemData?.addresstype == "home") {
            img_home_selected.setImageResource(R.drawable.ic_tick_pink)
            img_shop_selected.setImageResource(R.drawable.ic_light_pink_tick)
        } else {
            img_shop_selected.setImageResource(R.drawable.ic_tick_pink)
            img_home_selected.setImageResource(R.drawable.ic_light_pink_tick)
        }
    }

    private fun createGetTimeSlotRequest(): JsonObject {
        var jsonObject = JsonObject()
        jsonObject.addProperty("action","gettimeslots")
        if(cartItemData?.categoryName!!.trim() == "Leg front" || cartItemData?.categoryName!!.trim() == "Leg front and back"||cartItemData?.categoryName!!.trim() == "Hand front and back"||cartItemData?.categoryName!!.trim() == "Hand front"
            || cartItemData?.categoryName!!.trim() == "الجبهة الساق" || cartItemData?.categoryName!!.trim() == "اليد الأمامية"
            || cartItemData?.categoryName!! == "اليد الأمامية والخلفية" || cartItemData?.categoryName!!.trim() == "أمامي وخلفي الساق" ){
            jsonObject.addProperty("subcatID",cartItemData?.categoryId)
            jsonObject.addProperty("type","heena")
        }else{
            jsonObject.addProperty("subcatID",cartItemData?.subcategoryId)
        }
        return jsonObject
    }

    private fun clickListeners() {
        btn_update_booking.setOnClickListener {
            if(compareDateAndTime() == -1){
                Utils.showSnackBar(getString(R.string.please_select_timeslot_and_date_again),btn_update_booking)
                return@setOnClickListener
            }

            if(cartItemData?.categoryName!!.trim() == "Leg front" || cartItemData?.categoryName!!.trim() == "Leg front and back"||cartItemData?.categoryName!!.trim() == "Hand front and back"||cartItemData?.categoryName!!.trim() == "Hand front"){
                viewModel?.updateHeenaItem(createUpdateHeenaRequest())
            }else {
                viewModel?.updateCartItem(createUpdationJsonRequest())
            }
        }

        cl_select_time.setOnClickListener {
            viewModel?.getAllTimeSlots(createGetTimeSlotRequest())
        }

        cl_select_date.setOnClickListener {
            openDatePicker()
        }

        clAddressHome.setOnClickListener {
            cartItemData?.addresstype = "home"
            img_home_selected.setImageResource(R.drawable.ic_tick_pink)
            img_shop_selected.setImageResource(R.drawable.ic_light_pink_tick)
        }

        clAddressShop.setOnClickListener {
            cartItemData?.addresstype = "shop"
            img_shop_selected.setImageResource(R.drawable.ic_tick_pink)
            img_home_selected.setImageResource(R.drawable.ic_light_pink_tick)
        }

        img_add.setOnClickListener {
            if(cartItemData?.categoryName!!.trim() == "Leg front" || cartItemData?.categoryName!!.trim() == "Leg front and back"||cartItemData?.categoryName!!.trim() == "Hand front and back"||cartItemData?.categoryName!!.trim() == "Hand front"
                || cartItemData?.categoryName!!.trim() == "الجبهة الساق" || cartItemData?.categoryName!!.trim() == "اليد الأمامية"
                || cartItemData?.categoryName!! == "اليد الأمامية والخلفية" || cartItemData?.categoryName!!.trim() == "أمامي وخلفي الساق" ){
                cartItemData?.qty = ((cartItemData?.qty ?: "0").toInt() + 1).toString()
                txt_quantity.text = cartItemData?.qty
            }else {
                cartItemData?.serviceQty = ((cartItemData?.serviceQty ?: "0").toInt() + 1).toString()
                txt_quantity.text = cartItemData?.serviceQty
            }
            updateTotalPrice(cartItemData)
        }

        img_minus.setOnClickListener {
            if(cartItemData?.categoryName!!.trim() == "Leg front" || cartItemData?.categoryName!!.trim() == "Leg front and back"||cartItemData?.categoryName!!.trim() == "Hand front and back"||cartItemData?.categoryName!!.trim() == "Hand front"
                || cartItemData?.categoryName!!.trim() == "الجبهة الساق" || cartItemData?.categoryName!!.trim() == "اليد الأمامية"
                || cartItemData?.categoryName!! == "اليد الأمامية والخلفية" || cartItemData?.categoryName!!.trim() == "أمامي وخلفي الساق" ){
                if((cartItemData?.qty?:"0").toInt()>1){
                    cartItemData?.qty = ((cartItemData?.qty?:"0").toInt()-1).toString()
                }
                txt_quantity.text =  cartItemData?.qty
            }else{
                if((cartItemData?.serviceQty?:"0").toInt()>1){
                cartItemData?.serviceQty = ((cartItemData?.serviceQty?:"0").toInt()-1).toString()
            }
            txt_quantity.text =  cartItemData?.serviceQty
            }
            updateTotalPrice(cartItemData)
        }
    }

    private fun createUpdateHeenaRequest(): JsonObject {
        var jsonObject = JsonObject()
        jsonObject.addProperty("action" ,"heenaupdatecart")
        jsonObject.addProperty("cartid" , cartItemData?.cartID)
        jsonObject.addProperty("userid" ,cartItemData?.userid)
        jsonObject.addProperty("category_id" , cartItemData?.categoryId)
        jsonObject.addProperty(  "optionsids" , createOptionIdData(cartItemData?.options))
        jsonObject.addProperty(  "order_slot_id" , cartItemData?.orderSlotId)
        jsonObject.addProperty( "totalAmount" ,cartItemData?.totalAmount)
        jsonObject.addProperty( "addresstype" ,cartItemData?.addresstype)
        jsonObject.addProperty( "order_date", cartItemData?.orderDate)
        jsonObject.addProperty(  "qty", cartItemData?.qty)
        return jsonObject
    }

    private fun createOptionIdData(options: List<OptionsItem?>?): String {
        var optionIds: String  = ""
        for(i in 0 until options!!.size){
            optionIds = if(optionIds == ""){
                options[i]!!.id.toString()
            }else{
                optionIds+","+options[i]!!.id.toString()
            }
        }
        return optionIds
    }

    private fun updateTotalPrice(cartItemData: ItemsItem?){
        if(cartItemData?.categoryName!!.trim() == "Leg front" || cartItemData?.categoryName!!.trim() == "Leg front and back"||cartItemData?.categoryName!!.trim() == "Hand front and back"||cartItemData?.categoryName!!.trim() == "Hand front") {
            var totalAmount = (heenaPrice * ((cartItemData?.qty
                    ?: "0.0").toDouble()))
            txt_total.text =
                "${getString(R.string.currency_value)} " + (totalAmount + (cartItemData?.homeservice ?: "0.0").toDouble()).toString()
            cartItemData?.totalAmount =
                (totalAmount + (cartItemData?.homeservice ?: "0.0").toDouble())
        }else {
            var totalAmount =
                (((cartItemData?.servicePrice ?: "0.0").toDouble()) * ((cartItemData?.serviceQty
                    ?: "0.0").toDouble()))
            txt_total.text =
                "${getString(R.string.currency_value)} " + (totalAmount + (cartItemData?.homeservice ?: "0.0").toDouble()).toString()
            cartItemData?.totalAmount =
                (totalAmount + (cartItemData?.homeservice ?: "0.0").toDouble())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = RescheduleBooking()
    }

    lateinit var timeSlotDialog: Dialog
    private fun showTimeSlotDialog(context: Context, slotList: ArrayList<SlotsItem>) {
        timeSlotDialog = Dialog(context, R.style.DialogAnimation)
        timeSlotDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        timeSlotDialog.setContentView(R.layout.time_slot_dialog_layout)
        timeSlotDialog.setCancelable(true)

        var layoutManager = GridLayoutManager(context,3)
        timeSlotDialog.rvSlots.layoutManager = layoutManager
        timeSlotsAdapter = RescheduleTimeSlotAdapter(context,this,slotList)
        timeSlotDialog.rvSlots.adapter = timeSlotsAdapter

        timeSlotDialog.btnDone.setOnClickListener {
            timeSlotDialog.dismiss()
        }

        if (!timeSlotDialog.isShowing) {
            timeSlotDialog.show()
        }
    }

    override fun onTimeSlotSelected(position: Int) {
        txt_time.text = slotList[position].time
        cartItemData?.orderSlotId = slotList[position].id
        selectedTime = slotList[position].time?:""
    }

    private var datePicker: PrimeDatePicker? = null
    private fun openDatePicker() {
        val calendarType = getCalendarType()
        val locale = getLocale(calendarType)
        val pickType = getPickType()
        val today = CalendarFactory.newInstance(calendarType, locale)

        datePicker = PrimeDatePicker.dialogWith(today).let {
            when (pickType) {
                PickType.SINGLE -> {
                    it.pickSingleDay(singleDayPickCallback)
                }
                else -> null
            }
        }?.let {
            it.build()
        }
        datePicker?.show(childFragmentManager, ServiceDetailFragment.PICKER_TAG)
        datePicker?.setOnDismissListener { datePicker = null }
    }

    private val singleDayPickCallback = SingleDayPickCallback { singleDay ->
        var date = Utils.getFormattedCurrentDateValue(singleDay.shortDateString)
        Log.e("date_value", singleDay.shortDateString)
        txt_date.text = date
        cartItemData?.orderDate = date
    }

    private fun getCalendarType(): CalendarType {
        return CalendarType.CIVIL
    }

    private fun getPickType(): PickType {
        return PickType.SINGLE
    }

    private fun getLocale(calendarType: CalendarType): Locale {
        return Locale.ENGLISH
    }

    private fun createUpdationJsonRequest(): JsonObject{
        var jsonObject = JsonObject()
        jsonObject.addProperty("action" , "updatecart")
        jsonObject.addProperty("cartid",cartItemData?.cartID)
        jsonObject.addProperty( "userid",cartItemData?.userid)
        jsonObject.addProperty("category_id",cartItemData?.categoryId)
        jsonObject.addProperty("subcategory_id",cartItemData?.subcategoryId)
        jsonObject.addProperty("order_slot_id",cartItemData?.orderSlotId)
        jsonObject.addProperty("addresstype",cartItemData?.addresstype)
        jsonObject.addProperty("totalAmount",cartItemData?.totalAmount)
        jsonObject.addProperty("servicesid",cartItemData?.servicesID)
        jsonObject.addProperty("servicesqty",cartItemData?.serviceQty)
        jsonObject.addProperty("service_price",cartItemData?.servicePrice)
        jsonObject.addProperty("order_date",cartItemData?.orderDate)
        return jsonObject
    }

    private fun attachObservers() {
        viewModel.updateCartResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == false) {
                Utils.showSnackBar(it?.message?:getString(R.string.please_try_ahain), clAddressHome)
            } else {
                Utils.showSnackBar(it?.message ?: "", clAddressHome)
                (requireActivity() as HomeActivity).onBackPressed()
            }
        })

        viewModel.timeSlotsResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == false) {
                Utils.showSnackBar(it?.message?:getString(R.string.please_try_ahain), clAddressHome)
            } else {
                if(it?.slots?.isNullOrEmpty() == false){
                    slotList.clear()
                    slotList = it?.slots as ArrayList<SlotsItem>
                    showTimeSlotDialog(requireActivity(),slotList)
                }
            }
        })

        viewModel.apiError.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                Utils.showSnackBar(it, clAddressHome)
                Utils.showLoading(false, requireActivity())
            }
        })

        viewModel.unauthorized.observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), clAddressHome)
                    Utils.logoutUser(requireActivity())
                }
            }
        })

        viewModel.isLoading.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                Utils.showLoading(it, requireActivity())
            }
        })
    }

    private var selectedTime = "00:00:00"
    private fun compareDateAndTime(): Int{
        selectedTime = Utils.getTimeIn24HoursFormat(selectedTime)
//        if(selectedTime.contains("AM") || selectedTime.contains("am")){
//            selectedTime = selectedTime.replace("AM","00")
//            selectedTime = selectedTime.replace("am","00")
//        }
//        if(selectedTime.contains("PM") || selectedTime.contains("pm")){
//            selectedTime = selectedTime.replace("PM","00")
//            selectedTime = selectedTime.replace("pm","00")
//        }
        var date = "${cartItemData?.orderDate} $selectedTime"
        return Utils.compareDateTimeForSlots(date)
    }
}