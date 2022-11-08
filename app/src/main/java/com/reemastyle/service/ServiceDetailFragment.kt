package com.reemastyle.service

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.common.PickType
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.bumptech.glide.util.Util
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.reemastyle.HomeActivity
import com.reemastyle.MainActivity
import com.reemastyle.R
import com.reemastyle.cart.AddToCartModel
import com.reemastyle.cart.AddToCartRequest
import com.reemastyle.cart.ServiceModel
import com.reemastyle.model.services.ServicesItem
import com.reemastyle.model.services.SlotsItem
import com.reemastyle.model.services.Storeaddress
import com.reemastyle.model.services.Useraddress
import com.reemastyle.preferences.Preferences
import com.reemastyle.util.Constants
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.cancel_booking_dialog_layout.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_address.*
import kotlinx.android.synthetic.main.fragment_service_deatil.*
import kotlinx.android.synthetic.main.login_dialog_layout.*
import java.util.*

class ServiceDetailFragment : Fragment(), ServiceItemClicked, TimeSlotSelected, AddressItemClicked {
    lateinit var viewModel: ServiceViewModel
    private lateinit var serviceAdapter: ServiceAdapter
    private lateinit var timeSlotsAdapter: TimeSlotsAdapter
    private lateinit var addressAdapter: AddressAdapter
    private var slotList: ArrayList<SlotsItem> = ArrayList<SlotsItem>()
    private var servicesList: ArrayList<ServicesItem> = ArrayList<ServicesItem>()
    var addToCartRequest = AddToCartRequest()
    var selectedServiceList: ArrayList<ServiceModel> = ArrayList<ServiceModel>()
    var homeAddress: Useraddress? = null
    var storeAddress: Storeaddress? = null
    lateinit var addToCartModel: AddToCartModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(requireActivity())[ServiceViewModel::class.java]
        addToCartModel = ViewModelProviders.of(requireActivity())[AddToCartModel::class.java]

        var jsonObject = JsonObject()
        jsonObject.addProperty("action", "getservicesbysubcat")
        jsonObject.addProperty("subcatID", Constants.SELECTED_SUB_CATEGORY)
        jsonObject.addProperty("userid", Utils.getUserData()?.id)
        if(Preferences.prefs?.getString("Language","en") == "ar"){
            jsonObject.addProperty("lang","AR")
        }else{
            jsonObject.addProperty("lang","EN")
        }
        viewModel.getServices(jsonObject)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_service_deatil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpServiceAdapter(servicesList)
        Constants.COMING_FROM = ""
        clickListeners()
        getCurrentDate()

        attachObservers()
    }

    private fun clickListeners() {
        clAddFriendAddress.setOnClickListener {
            addToCartRequest.addresstype = "home"
            findNavController().navigate(R.id.action_serviceDetailFragment_to_addressFragment)
        }

        cl_select_date.setOnClickListener {
            openDatePicker()
        }

        btn_book_now.setOnClickListener {
            if(Utils.getUserData() == null){
                showLoginDialog(requireActivity())
                return@setOnClickListener
            }
            if (addToCartRequest.totalAmount == "0" || addToCartRequest.totalAmount == "0.0") {
                Utils.showSnackBar(getString(R.string.please_add_service), btn_book_now)
                return@setOnClickListener
            }
            if (addToCartRequest.services.size == 0) {
                Utils.showSnackBar(getString(R.string.please_add_service), btn_book_now)
                return@setOnClickListener
            }
            if (addToCartRequest.categoryId == "0") {
                Utils.showSnackBar(getString(R.string.please_go_back_and_try_again), btn_book_now)
                return@setOnClickListener
            }
            if (addToCartRequest.subcategory_id == "0") {
                Utils.showSnackBar(getString(R.string.please_go_back_and_try_again), btn_book_now)
                return@setOnClickListener
            }
            if (addToCartRequest.order_date == "") {
                Utils.showSnackBar(getString(R.string.please_select_order_date), btn_book_now)
                return@setOnClickListener
            }
            if (addToCartRequest.order_slot_id == "0") {
                Utils.showSnackBar(getString(R.string.please_select_order_time_slot), btn_book_now)
                return@setOnClickListener
            }

            if (compareDateAndTime() == -1) {
                Utils.showSnackBar(getString(R.string.please_select_timeslot_and_date_again), btn_book_now)
                return@setOnClickListener
            }

            if (addToCartRequest.addresstype == "0") {
                Utils.showSnackBar(getString(R.string.please_select_address_type), btn_book_now)
                return@setOnClickListener
            }

            addToCartModel?.addToCart(createAddToCartRequest())
        }

        clAddressHome.setOnClickListener {
            if (homeAddress != null) {
                img_home_selected.setImageResource(R.drawable.ic_tick_pink)
                img_shop_selected.setImageResource(R.drawable.ic_light_pink_tick)
                addToCartRequest.addresstype = "home"
                addToCartRequest.addressId = homeAddress?.id ?: "0"
            } else {
                Constants.COMING_FROM = "service_details"
                findNavController().navigate(R.id.action_serviceDetailFragment_to_selectLocationFragment)
            }
        }

        clAddressShop.setOnClickListener {
            if (storeAddress != null) {
                img_shop_selected.setImageResource(R.drawable.ic_tick_pink)
                img_home_selected.setImageResource(R.drawable.ic_light_pink_tick)
                addToCartRequest.addresstype = "shop"
                addToCartRequest.addressId = storeAddress?.id ?: "0"
            } else {
                Utils.showSnackBar(
                    getString(R.string.please_ask_admin_to_provide_store_address),
                    clAddressHome
                )
            }
        }
    }

    private fun createAddToCartRequest(): JsonObject {
        var cartObject = JsonObject()
        cartObject.addProperty("action", addToCartRequest.action)
        cartObject.addProperty("userid", addToCartRequest.userid)
        cartObject.addProperty("category_id", addToCartRequest.categoryId)
        cartObject.addProperty("subcategory_id", addToCartRequest.subcategory_id)
        cartObject.addProperty("order_slot_id", addToCartRequest.order_slot_id)
        cartObject.addProperty("addresstype", addToCartRequest.addresstype)
        cartObject.addProperty("totalAmount", addToCartRequest.totalAmount)
        cartObject.addProperty("order_date", addToCartRequest.order_date)

        var serviceArray = JsonArray()
        for (i in 0 until addToCartRequest.services.size) {
            var jsonObject = JsonObject()
            jsonObject.addProperty("id", addToCartRequest.services[i].id)
            jsonObject.addProperty("quantity", addToCartRequest.services[i].quantity)
            jsonObject.addProperty("service_price", addToCartRequest.services[i].price)
            serviceArray.add(jsonObject)
        }
        cartObject.add("services", serviceArray)
        return cartObject
    }

    private fun setUpAddressAdapter() {
        var layoutManager = LinearLayoutManager(requireActivity())
        rv_addresses.layoutManager = layoutManager
        addressAdapter = AddressAdapter(requireActivity(), this)
        rv_addresses.adapter = addressAdapter
    }

    private fun setUpTimeSlotAdapter(slotListData: ArrayList<SlotsItem>) {
        var layoutManager = GridLayoutManager(requireActivity(), 3)
        rv_time_slots.layoutManager = layoutManager
        timeSlotsAdapter = TimeSlotsAdapter(requireActivity(), this, slotListData)
        rv_time_slots.adapter = timeSlotsAdapter
    }

    private fun setUpServiceAdapter(serviceList: ArrayList<ServicesItem>) {
        var layoutManager = LinearLayoutManager(requireActivity())
        rv_services.layoutManager = layoutManager
        serviceAdapter = ServiceAdapter(requireActivity(), this, serviceList)
        rv_services.adapter = serviceAdapter
    }

    override fun onResume() {
        super.onResume()

        if ((requireActivity() as HomeActivity).getForegroundFragment() is ServiceDetailFragment) {
            (requireActivity() as HomeActivity).toolbar.visibility = View.VISIBLE
            (requireActivity() as HomeActivity).bottom_menu.visibility = View.GONE
            (requireActivity() as HomeActivity).et_search.visibility = View.GONE
        }
    }

    private fun attachObservers() {
        try {
            viewModel.servicesResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                Utils.showLoading(false, requireActivity())
                if (it.status == false) {
                    Utils.showSnackBar(it?.message?:getString(R.string.please_try_ahain), rv_time_slots)
                } else {
                    storeAddress = it?.storeaddress
                    homeAddress = it?.useraddress
                    if (homeAddress != null) {
                        txt_home_address_value.text =
                            homeAddress?.street + ", " + homeAddress?.zone + ", " + homeAddress?.building
                    }
                    if (storeAddress != null) {
                        txt_shop_address_value.text =
                            storeAddress?.street + ", " + storeAddress?.zone + ", " + storeAddress?.building
                    }
                    if (it?.services?.isNullOrEmpty() == false) {
                        servicesList =
                            it?.services as ArrayList<ServicesItem> /* = java.util.ArrayList<com.reemastyle.model.services.ServicesItem> */
                        setUpServiceAdapter(servicesList)

                        txt_service.text = servicesList[0].subcatName
                    }

                    if (it?.slots?.isNullOrEmpty() == false) {
                        slotList = it?.slots as ArrayList<SlotsItem>
                        setUpTimeSlotAdapter(slotList)
                    }
                }
            })

            viewModel.apiError.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    Utils.showSnackBar(it, rv_time_slots)
                    Utils.showLoading(false, requireActivity())
                }
            })

            viewModel.unauthorized.observe(viewLifecycleOwner, Observer {
                it?.let {
                    if (it) {
                        Utils.showSnackBar(getString(R.string.your_session_is_out), rv_time_slots)
                        Utils.logoutUser(requireActivity())
                    }
                }
            })

            viewModel.isLoading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    Utils.showLoading(it, requireActivity())
                }
            })

            addToCartModel.addToCartResponse.observe(
                viewLifecycleOwner,
                androidx.lifecycle.Observer {
                    Utils.showLoading(false, requireActivity())
                    if (it.status == false) {
                        Utils.showSnackBar(getString(R.string.please_try_ahain), rv_time_slots)
                    } else {
                        Utils.showSnackBar(
                            it.message ?: getString(R.string.added_to_cart_successfully),
                            rv_time_slots
                        )
                        findNavController()?.navigate(R.id.action_serviceDetailFragment_to_cartFragment)
//                startActivity(Intent(viewLifecycleOwner, HomeActivity::class.java))
//                (viewLifecycleOwner as HomeActivity).finish()
                    }
                })

            addToCartModel.apiError.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    Utils.showSnackBar(it, rv_time_slots)
                    Utils.showLoading(false, requireActivity())
                }
            })

            addToCartModel.unauthorized.observe(viewLifecycleOwner, Observer {
                it?.let {
                    if (it) {
                        Utils.showSnackBar(getString(R.string.your_session_is_out), rv_time_slots)
                        Utils.logoutUser(requireActivity())
                    }
                }
            })

            addToCartModel.isLoading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    Utils.showLoading(it, requireActivity())
                }
            })
        } catch (e: java.lang.Exception){
            e.printStackTrace()
        }
    }

    private fun setUpDataForAddress(storeAddress: Storeaddress?) {
        var storeAddressValue = ""
        if (storeAddress?.flat?.isNotEmpty() == true) {
            storeAddressValue = "#${storeAddress?.flat}"
        }
        if (storeAddress?.building?.isNotEmpty() == true) {
            storeAddressValue = storeAddressValue + ", " + "Building${storeAddress?.building}"
        }
        if (storeAddress?.street?.isNotEmpty() == true) {
            storeAddressValue = storeAddressValue + ", " + "${storeAddress?.street}"
        }
        txt_address_shop.text = storeAddressValue
    }

    private fun setUpDataForHomeAddress(useraddress: Useraddress?) {
        var storeAddressValue = ""
        if (useraddress?.flat?.isNotEmpty() == true) {
            storeAddressValue = "#${useraddress?.flat}"
        }
        if (useraddress?.building?.isNotEmpty() == true) {
            storeAddressValue = storeAddressValue + ", " + "Building${useraddress?.building}"
        }
        if (useraddress?.street?.isNotEmpty() == true) {
            storeAddressValue = storeAddressValue + ", " + "${useraddress?.street}"
        }
        txt_address_home.text = storeAddressValue
    }

    companion object {
        @JvmStatic
        fun newInstance() = ServiceDetailFragment()
        const val PICKER_TAG = "PrimeDatePickerBottomSheet"
    }

    private fun checkForIdInServices(id: String): Int {
        for (i in 0 until selectedServiceList.size) {
            if (selectedServiceList[i].id == id)
               return i
        }
        return -1
    }

    override fun onServiceClicked(position: Int, type: String) {
        if (type == "add") {
            servicesList[position].quantity = (servicesList[position].quantity ?: 0) + 1
            addToCartRequest.categoryId = servicesList[position].catID ?: "0"
            addToCartRequest.subcategory_id = servicesList[position].subcatID ?: "0"

            var serviceModel = ServiceModel(
                servicesList[position].id ?: "0",
                (servicesList[position].quantity ?: 0).toString(),
                (servicesList[position].price ?: "0.0").toDouble()
            )

            if (checkForIdInServices(servicesList[position].id ?: "0") != -1 ) {
                selectedServiceList[checkForIdInServices(servicesList[position].id ?: "0")] =
                    serviceModel
            } else {
                selectedServiceList.add(serviceModel)
            }

            calculatePrice(selectedServiceList)
            addToCartRequest.services = selectedServiceList
        } else {
            if ((servicesList[position].quantity ?: 0) > 0) {
                servicesList[position].quantity = (servicesList[position].quantity ?: 0) - 1
                var serviceModel = ServiceModel(
                    servicesList[position].id ?: "0",
                    (servicesList[position].quantity ?: 0).toString(),
                    (servicesList[position].price ?: "0.0").toDouble()
                )
                if (checkForIdInServices(servicesList[position].id ?: "0") != -1 ) {
                    selectedServiceList[checkForIdInServices(servicesList[position].id ?: "0")] =
                        serviceModel
                } else {
                    selectedServiceList.add(serviceModel)
                }
                calculatePrice(selectedServiceList)
                addToCartRequest.services = selectedServiceList
            }
        }
        serviceAdapter?.notifyItemChanged(position)
        Log.e(
            "selected_service_list",
            Gson().toJson(selectedServiceList) + "-------->>>>>>>" + Gson().toJson(addToCartRequest)
        )
    }

    private fun calculatePrice(serviceList: ArrayList<ServiceModel>) {
        var subTotal = 0.0
        for (i in 0 until serviceList.size) {
            subTotal += ((serviceList[i].price)*(serviceList[i].quantity).toDouble())
        }
        txt_total.text = "${getString(R.string.currency_value)} $subTotal"
        addToCartRequest.totalAmount = subTotal.toString()
    }

    override fun onTimeSlotSelected(position: Int) {
        addToCartRequest.order_slot_id = slotList[position].id?:"0"
        selectedTime = slotList[position].time?:""
    }

    override fun onAddressItemClicked(position: Int) {

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
        datePicker?.show(childFragmentManager, PICKER_TAG)
        datePicker?.setOnDismissListener { datePicker = null }
    }

    private fun getCurrentDate() {
        val calendarType = getCalendarType()
        val locale = getLocale(calendarType)
        val today = CalendarFactory.newInstance(calendarType, locale)
        var date = Utils.getFormattedCurrentDateValue(today.shortDateString)
        txt_date.text = date
        addToCartRequest.order_date = date
    }

    private val singleDayPickCallback = SingleDayPickCallback { singleDay ->
        var date = Utils.getFormattedCurrentDateValue(singleDay.shortDateString)
        Log.e("date_value", singleDay.longDateString)
        txt_date.text = date
        addToCartRequest.order_date = date
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

    lateinit var loginDialog: Dialog
    private fun showLoginDialog(context: Context){
        loginDialog = Dialog(context, R.style.DialogAnimation)
        loginDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        loginDialog.setContentView(R.layout.login_dialog_layout)
        loginDialog.setCancelable(true)

        loginDialog.btnCancel.setOnClickListener {
            loginDialog.dismiss()
        }

        loginDialog.btnLogin.setOnClickListener {
            startActivity(Intent( context,MainActivity::class.java))
            requireActivity().finish()
        }

        if (!loginDialog.isShowing) {
            loginDialog.show()
        }
    }

    private var selectedTime = "00:00:00"
    private fun compareDateAndTime(): Int{
        selectedTime = Utils.getTimeIn24HoursFormat(selectedTime)
        var date = "${addToCartRequest.order_date} $selectedTime"
        return Utils.compareDateTimeForSlots(date)
    }
}