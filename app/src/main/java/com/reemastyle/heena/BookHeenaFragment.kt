package com.reemastyle.heena

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
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
import com.reemastyle.model.heenadetail.HeenaDetailResponse
import com.reemastyle.model.heenadetail.HeenaSectionItem
import com.reemastyle.model.heenadetail.SlotsItem
import com.reemastyle.service.ServiceDetailFragment
import com.reemastyle.service.TimeSlotsAdapter
import com.reemastyle.util.Constants
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.fragment_book_heena.*
import kotlinx.android.synthetic.main.fragment_book_heena.btn_book_now
import kotlinx.android.synthetic.main.fragment_book_heena.clAddressHome
import kotlinx.android.synthetic.main.fragment_book_heena.clAddressShop
import kotlinx.android.synthetic.main.fragment_book_heena.cl_select_date
import kotlinx.android.synthetic.main.fragment_book_heena.img_add
import kotlinx.android.synthetic.main.fragment_book_heena.img_home_selected
import kotlinx.android.synthetic.main.fragment_book_heena.img_minus
import kotlinx.android.synthetic.main.fragment_book_heena.img_shop_selected
import kotlinx.android.synthetic.main.fragment_book_heena.rv_time_slots
import kotlinx.android.synthetic.main.fragment_book_heena.txt_date
import kotlinx.android.synthetic.main.fragment_book_heena.txt_quantity
import kotlinx.android.synthetic.main.fragment_book_heena.txt_total
import java.util.*

class BookHeenaFragment: Fragment(), TimeSlotSelected {
    private var selectedQuantity = 0
    private var subtotal = 0.0
    private var selectedSlot = 0
    private var selectedHeena: HeenaSectionItem?= null
    private lateinit var timeSlotAdapter: HeenaSlotAdapter
    private var slotList: ArrayList<SlotsItem> = ArrayList<SlotsItem>()
    private lateinit var mViewModel: HeenaViewModel
    private var selectedAddressType: String = ""
    private var selectedDate: String = ""
    private lateinit var heenaResponse: HeenaDetailResponse


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_heena, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
        mViewModel = ViewModelProviders.of(this).get(HeenaViewModel::class.java)
        attachObservers()
        getCurrentDate()

        if(arguments != null){
            var data = arguments?.getString("heena_data")
            selectedHeena = Gson().fromJson(data,HeenaSectionItem::class.java)
            var heenaDetail = arguments?.getString("heena_details")
            heenaResponse = Gson().fromJson(heenaDetail,HeenaDetailResponse::class.java)
            if(heenaResponse?.slots?.isNullOrEmpty() == false)
                slotList = heenaResponse?.slots as ArrayList<SlotsItem>
            setUpTimeSlotAdapter(slotList)
            setUpData(selectedHeena!!)
        }

        if(Constants.HEENA_RESPONSE != null){
            txt_quantity.text = selectedQuantity.toString()
            selectedDate = ""
            selectedTime = ""
            getCurrentDate()
            selectedSlot = 0
            txt_total.text = getString(R.string.currency_value)+" "+subtotal
        }
        clAddressHome.performClick()
    }

    private fun setUpTimeSlotAdapter(slotListData: ArrayList<SlotsItem>) {
        var layoutManager = GridLayoutManager(requireActivity(), 3)
        rv_time_slots.layoutManager = layoutManager
        timeSlotAdapter = HeenaSlotAdapter(requireActivity(), this, slotListData)
        rv_time_slots.adapter = timeSlotAdapter
    }

    private fun setUpData(selectedHeena: HeenaSectionItem) {
        txt_service_size.text = "${getString(R.string.heena)}, ${selectedHeena?.heenaCatname}"
        txt_price.text = "${getString(R.string.currency_value)} ${selectedHeena?.price}"
    }

    private fun setOnClickListeners() {
        clAddFriendAddress.setOnClickListener {
            selectedAddressType == "home"
            var bundle  = Bundle()
            bundle.putString("from","addToCart")
            Constants.COMING_FROM = "cart"
            Constants.HEENA_RESPONSE = Gson().toJson(heenaResponse)
            findNavController().navigate(R.id.action_bookHeenaFragment_to_selectLocationFragment)
        }

        img_add.setOnClickListener{
            selectedQuantity += 1
            calculatePrice()
            txt_quantity.text  = "$selectedQuantity"
        }

        clAddressHome.setOnClickListener {
            selectedAddressType = "home"
            img_home_selected.setImageResource(R.drawable.ic_tick_pink)
            img_shop_selected.setImageResource(R.drawable.ic_light_pink_tick)
        }

        clAddressShop.setOnClickListener {
            selectedAddressType = "shop"
            img_home_selected.setImageResource(R.drawable.ic_light_pink_tick)
            img_shop_selected.setImageResource(R.drawable.ic_tick_pink)
        }

        img_minus.setOnClickListener {
            if(selectedQuantity>0){
                selectedQuantity -= 1
                calculatePrice()
                txt_quantity.text  = "$selectedQuantity"
            }
        }

        cl_select_date.setOnClickListener {
            openDatePicker()
        }

        btn_book_now.setOnClickListener {
            if(selectedHeena == null){
                Utils.showSnackBar(getString(R.string.please_go_back_and_select_heena_option),btn_book_now)
                return@setOnClickListener
            }

            if(selectedQuantity == 0){
                Utils.showSnackBar(getString(R.string.please_add_service),btn_book_now)
                return@setOnClickListener
            }

            if(selectedDate == ""){
                Utils.showSnackBar(getString(R.string.please_select_order_date),btn_book_now)
                return@setOnClickListener
            }

            if(selectedSlot == 0){
                Utils.showSnackBar(getString(R.string.please_select_order_time_slot),btn_book_now)
                return@setOnClickListener
            }

            if(compareDateAndTime() == -1){
                Utils.showSnackBar(getString(R.string.please_select_timeslot_and_date_again),btn_book_now)
                return@setOnClickListener
            }

            if(selectedAddressType == ""){
                Utils.showSnackBar(getString(R.string.please_select_address_type),btn_book_now)
                return@setOnClickListener
            }

            mViewModel?.addHeenaToCart(createAddHeenaToCartRequest())
        }
    }

    private fun createAddHeenaToCartRequest(): JsonObject {
        var jsonObject = JsonObject()
        jsonObject.addProperty("action","HeenaAddtoCart")
        jsonObject.addProperty("userid",Utils.getUserData()?.id)
        jsonObject.addProperty("category_id",selectedHeena?.heenaCatID)
        jsonObject.addProperty("optionsids",getOptionId(selectedHeena?.optionname))
        jsonObject.addProperty("order_slot_id",selectedSlot)
        jsonObject.addProperty("addresstype",selectedAddressType)
        jsonObject.addProperty("totalAmount",subtotal.toString())
        jsonObject.addProperty("order_date",selectedDate)
        jsonObject.addProperty("qty",selectedQuantity)
        return jsonObject
    }

    private fun getOptionId(optionname: String?): String? {
        var optionIds = ""
        if(optionname == "option 1" || optionname == "الخيار 1"){
            optionIds = "1"
        }
        if(optionname == "option 2" || optionname == "الخيار 2"){
            optionIds =  "1,2"
        }
        if(optionname == "option 3" || optionname == "الخيار 3"){
            optionIds =  "1,2,3"
        }
        if(optionname == "option 4" || optionname == "الخيار41"){
            optionIds =  "1,2,3,4"
        }
        if(optionname == "option 5" || optionname == "الخيار 5"){
            optionIds =  "1,2,3,4,5"
        }
        if(optionname == "option 6" || optionname == "الخيار 6"){
            optionIds =  "1,2,3,4,5,6"
        }
        if(optionname == "option 7" || optionname == "الخيار 7"){
            optionIds =  "1,2,3,4,5,6,7"
        }
        if(optionname == "option 8" || optionname == "الخيار 8"){
            optionIds =  "1,2,3,4,5,6,7,8"
        }
        if(optionname == "option 9" || optionname == "الخيار 9"){
            optionIds =  "1,2,3,4,5,6,7,8,9"
        }
        return optionIds
    }

    private fun attachObservers() {
        mViewModel.addHeenaToCartResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == true) {
                Constants.HEENA_RESPONSE = ""
                findNavController().navigate(R.id.cartFragment)
            } else {
                Utils.showSnackBar( it.message?:getString(R.string.please_try_ahain), img_add)
            }
        })

        mViewModel.apiError.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                Utils.showSnackBar(it, img_add)
                try {
                    Utils.showLoading(false, requireActivity())
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        })

        mViewModel.unauthorized.observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), img_add)
                    Utils.logoutUser(requireActivity())
                }
            }
        })

        mViewModel.isLoading.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                try {
                    Utils.showLoading(it, requireActivity())
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        })
    }

    private fun calculatePrice() {
        subtotal = (selectedHeena?.price?:"0.0").toDouble() * selectedQuantity.toDouble()
        txt_total.text = "${getString(R.string.currency_value)} $subtotal"
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

    private fun getCurrentDate() {
        val calendarType = getCalendarType()
        val locale = getLocale(calendarType)
        val today = CalendarFactory.newInstance(calendarType, locale)
        var date = Utils.getFormattedCurrentDateValue(today.shortDateString)
        txt_date.text = date
        selectedDate = date
    }

    private val singleDayPickCallback = SingleDayPickCallback { singleDay ->
        var date = Utils.getFormattedCurrentDateValue(singleDay.shortDateString)
        Log.e("date_value", singleDay.longDateString)
        txt_date.text = date
        selectedDate = date
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

    override fun onTimeSlotSelected(position: Int) {
        selectedSlot = (slotList[position].id?:"0").toInt()
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
        var date = "${selectedDate} $selectedTime"
        return Utils.compareDateTimeForSlots(date)
    }
}