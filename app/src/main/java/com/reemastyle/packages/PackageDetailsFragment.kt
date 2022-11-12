package com.reemastyle.packages

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
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.common.PickType
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.bumptech.glide.Glide
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.reemastyle.HomeActivity
import com.reemastyle.MainActivity
import com.reemastyle.R
import com.reemastyle.cart.AddToCartModel
import com.reemastyle.model.packagedetail.Packagedata
import com.reemastyle.model.packagedetail.SlotsItem
import com.reemastyle.model.packagedetail.Storeaddress
import com.reemastyle.model.packagedetail.Useraddress
import com.reemastyle.preferences.Preferences
import com.reemastyle.service.ServiceDetailFragment
import com.reemastyle.util.Constants
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_book_heena.*
import kotlinx.android.synthetic.main.fragment_package_details.*
import kotlinx.android.synthetic.main.fragment_package_details.btn_book_now
import kotlinx.android.synthetic.main.fragment_package_details.clAddFriendAddress
import kotlinx.android.synthetic.main.fragment_package_details.clAddressHome
import kotlinx.android.synthetic.main.fragment_package_details.clAddressShop
import kotlinx.android.synthetic.main.fragment_package_details.cl_select_date
import kotlinx.android.synthetic.main.fragment_package_details.img_add
import kotlinx.android.synthetic.main.fragment_package_details.img_home_selected
import kotlinx.android.synthetic.main.fragment_package_details.img_minus
import kotlinx.android.synthetic.main.fragment_package_details.img_shop_selected
import kotlinx.android.synthetic.main.fragment_package_details.rv_time_slots
import kotlinx.android.synthetic.main.fragment_package_details.txt_date
import kotlinx.android.synthetic.main.fragment_package_details.txt_quantity
import kotlinx.android.synthetic.main.fragment_package_details.txt_total
import kotlinx.android.synthetic.main.login_dialog_layout.*
import java.util.*

class PackageDetailsFragment : Fragment(),
    com.reemastyle.packages.TimeSlotSelected {
    private lateinit var timeSlotAdapter: SlotAdapter
    private var slotList: ArrayList<SlotsItem> = ArrayList<SlotsItem>()
    lateinit var viewModel: PackageViewModel
    private var quantity = 0
    lateinit var packageDetails: Packagedata
    private var selectedTimeSlot: String = ""
    private var selectedDate: String = ""
    private var addressType: String = "home"
    private var addressId: String = ""
    private var homeAddress: Useraddress? = null
    private var storeAddress: Storeaddress? = null
    lateinit var addToCartModel: AddToCartModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(PackageViewModel::class.java)
        addToCartModel = ViewModelProviders.of(this).get(AddToCartModel::class.java)


        var jsonObject = JsonObject()
        jsonObject.addProperty("action", "package_detail")
        jsonObject.addProperty("userid", Utils.getUserData()?.id)
        jsonObject.addProperty("package_id", Constants.SELECTED_PACKAGE_ID)
        if(Preferences.prefs?.getString("Language","en") == "ar"){
            jsonObject.addProperty("lang","AR")
        }else{
            jsonObject.addProperty("lang","EN")
        }
        viewModel?.getPackageDetail(jsonObject)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_package_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as HomeActivity).bottom_menu.visibility = View.GONE
        (requireActivity() as HomeActivity).toolbar.visibility = View.VISIBLE
        (requireActivity() as HomeActivity).et_search.visibility = View.GONE

        clickListeners()
        getCurrentDate()

        attachObservers()

        if(quantity != 0){
            txt_quantity.text  = quantity.toString()
            selectedTime = ""
            selectedTimeSlot = ""
            selectedDate = ""
            getCurrentDate()
            txt_total.text = "${getString(R.string.currency_value)} $totalAmount"
        }
    }

    private fun clickListeners() {
       // clAddressHome.performClick()
        clAddressHome.setOnClickListener {
            if (homeAddress != null) {
                img_home_selected.setImageResource(R.drawable.ic_tick_pink)
                img_shop_selected.setImageResource(R.drawable.ic_light_pink_tick)
                addressType = "home"
                addressId = homeAddress?.id ?: "0"
            } else {
                Utils.showSnackBar(getString(R.string.please_add_address), clAddressHome)
            }
        }

        clAddFriendAddress.setOnClickListener {
            addressType == "home"
            Constants .COMING_FROM = "cart"
            findNavController().navigate(R.id.action_packageDetailsFragment_to_selectLocationFragment)
        }

        clAddressShop.setOnClickListener {
            if (storeAddress != null) {
                img_shop_selected.setImageResource(R.drawable.ic_tick_pink)
                img_home_selected.setImageResource(R.drawable.ic_light_pink_tick)
                addressType = "shop"
                addressId = storeAddress?.id ?: "0"
            } else {
                Utils.showSnackBar(
                    getString(R.string.please_ask_admin_to_provide_store_address),
                    clAddressHome
                )
            }
        }

        btn_book_now.setOnClickListener {
            if (Utils.getUserData() == null) {
                showLoginDialog(requireActivity())
            } else {
                if (quantity == 0) {
                    Utils.showSnackBar(getString(R.string.please_add_service), btn_book_now)
                    return@setOnClickListener
                }
                if (selectedDate == "") {
                    Utils.showSnackBar(getString(R.string.please_select_order_date), btn_book_now)
                    return@setOnClickListener
                }
                if (selectedTimeSlot == "") {
                    Utils.showSnackBar(
                        getString(R.string.please_select_order_time_slot),
                        btn_book_now
                    )
                    return@setOnClickListener
                }
                if(compareDateAndTime() ==  -1){
                    Utils.showSnackBar(getString(R.string.please_select_timeslot_and_date_again),btn_book_now)
                    return@setOnClickListener
                }
                if (addressType == "") {
                    Utils.showSnackBar(getString(R.string.please_select_address_type), btn_book_now)
                    return@setOnClickListener
                }
                addToCartModel.addToCart(createAddtoCartJsonRequest())
            }
        }

        cl_select_date.setOnClickListener {
            openDatePicker()
        }

        img_add.setOnClickListener {
            if (packageDetails != null) {
                quantity += 1
                txt_quantity.text = quantity.toString()
                calculatePrice(packageDetails)
            }
        }

        img_minus.setOnClickListener {
            if (packageDetails != null) {
                if (quantity > 0) {
                    quantity -= 1
                    txt_quantity.text = quantity.toString()
                    calculatePrice(packageDetails)
                }
            }
        }
    }

    private fun createAddtoCartJsonRequest(): JsonObject {
        var jsonObject = JsonObject()
        jsonObject.addProperty("action","AddtoCart")
        jsonObject.addProperty("userid",Utils.getUserData()?.id)
        jsonObject.addProperty("category_id",packageDetails.packservice?.catID)
        jsonObject.addProperty("subcategory_id",packageDetails.packservice?.subcatID)
        jsonObject.addProperty("order_slot_id",selectedTimeSlot)
        jsonObject.addProperty("addresstype",addressType)
        jsonObject.addProperty("order_date",selectedDate)
        var jsonArray = JsonArray()
        var jsonObject1 = JsonObject()
        jsonObject1.addProperty("service_price",packageDetails.price)
        jsonObject1.addProperty("id",packageDetails.packservice?.id)
        jsonObject1.addProperty("quantity",quantity)
        jsonArray.add(jsonObject1)
        jsonObject.add("services",jsonArray)
        return jsonObject
    }

    private var totalAmount: Double = 0.0
    private fun calculatePrice(packageDetails: Packagedata) {
        totalAmount = 0.0
        totalAmount = ((packageDetails.price ?: "0.0").toDouble() * quantity.toDouble())
        txt_total.text = "${getString(R.string.currency_value)} $totalAmount"
    }

    private fun setUpTimeSlotAdapter(slotListData: ArrayList<SlotsItem>) {
        var layoutManager = GridLayoutManager(requireActivity(), 3)
        rv_time_slots.layoutManager = layoutManager
        timeSlotAdapter = SlotAdapter(requireActivity(), this, slotListData)
        rv_time_slots.adapter = timeSlotAdapter
    }

    private fun attachObservers() {
        viewModel.packageDetailsResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == false) {
                Utils.showSnackBar(it.message?:getString(R.string.please_try_ahain), img_add)
            } else {
                try {
                    setUpPackageDetailsData(it?.packagedata)
                    if (it?.slots?.isNullOrEmpty() == false) {
                        slotList = it?.slots as ArrayList<SlotsItem>
                        setUpTimeSlotAdapter(slotList)
                    }
                    homeAddress = it?.useraddress
                    storeAddress = it?.storeaddress
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        })

        addToCartModel.addToCartResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == false) {
                Utils.showSnackBar(it.message?:getString(R.string.please_try_ahain), img_add)
            } else {
                Utils.showSnackBar(it.message?:getString(R.string.data_added_ion_cart), img_add)
                findNavController().navigate(R.id.cartFragment)
            }
        })

        viewModel.apiError.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                Utils.showSnackBar(it, img_add)
                Utils.showLoading(false, requireActivity())
            }
        })

        viewModel.unauthorized.observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), img_add)
                    Utils.logoutUser(requireActivity())
                }
            }
        })

        viewModel.isLoading.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                try {
                    Utils.showLoading(it, requireActivity())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
        addToCartModel.apiError.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                Utils.showSnackBar(it, img_add)
                Utils.showLoading(false, requireActivity())
            }
        })

        addToCartModel.unauthorized.observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), img_add)
                    Utils.logoutUser(requireActivity())
                }
            }
        })

        addToCartModel.isLoading.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                try {
                    Utils.showLoading(it, requireActivity())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun setUpPackageDetailsData(packagedata: Packagedata?) {
        try {
            if (packagedata != null) {
                packageDetails = packagedata
                txt_title.text = packagedata?.packname
                txt_package_name.text = packagedata?.discription
                if (!packagedata?.image.isNullOrEmpty()) {
                    Glide.with(requireActivity()).load(packagedata?.image).into(img_package)
                }
                txt_package_price.text = "QAR ${packagedata?.price}"
                txt_offerpackage_price.text =
                    Utils.getFormatlistPrice("QAR" + " " + packagedata?.oldprice)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PackageDetailsFragment()
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
        Log.e("date_value", singleDay.shortDateString)
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

    lateinit var loginDialog: Dialog
    private fun showLoginDialog(context: Context) {
        loginDialog = Dialog(context, R.style.DialogAnimation)
        loginDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        loginDialog.setContentView(R.layout.login_dialog_layout)
        loginDialog.setCancelable(true)

        loginDialog.btnCancel.setOnClickListener {
            loginDialog.dismiss()
        }

        loginDialog.btnLogin.setOnClickListener {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            (requireActivity() as HomeActivity).finish()
        }

        if (!loginDialog.isShowing) {
            loginDialog.show()
        }
    }

    private var selectedTime = "00:00:00"
    override fun onTimeSlotSelected(position: Int) {
        selectedTimeSlot = slotList[position].id!!
        selectedTime = slotList[position].time?:""
    }

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