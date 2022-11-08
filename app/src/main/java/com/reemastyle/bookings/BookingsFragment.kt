package com.reemastyle.bookings

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.reemastyle.HomeActivity
import com.reemastyle.R
import com.reemastyle.model.history.ItemsItem
import com.reemastyle.preferences.Preferences
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.cancel_booking_dialog_layout.*
import kotlinx.android.synthetic.main.custom_popup_menu.view.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_bookings.*

class BookingsFragment : Fragment(), BookingItemClicked {
    private lateinit var bookingAdapter: BookingsAdapter
    private lateinit var viewModel: BookingViewModel
    private var bookingList: ArrayList<ItemsItem> = ArrayList<ItemsItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bookings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as HomeActivity).et_search.visibility = View.GONE

        viewModel = ViewModelProviders.of(this).get(BookingViewModel::class.java)
        attachObservers()

        callBookingHistioryAPI()
    }

    private fun callBookingHistioryAPI() {
        var jsonObject = JsonObject()
        jsonObject.addProperty("action", "OrderHistory")
        jsonObject.addProperty("user_id",Utils.getUserData()?.id)
        if(Preferences.prefs?.getString("Language","en") == "ar"){
            jsonObject.addProperty("lang","AR")
        }else{
            jsonObject.addProperty("lang","EN")
        }
        viewModel?.getAllBookings(jsonObject)
    }

    private fun setUpBookingsAdapter(bookings: ArrayList<ItemsItem>) {
        var layoutManager = LinearLayoutManager(requireActivity())
        rv_bookings.layoutManager = layoutManager
        bookingAdapter = BookingsAdapter(requireActivity(),this,bookings)
        rv_bookings.adapter = bookingAdapter
    }

    override fun onResume() {
        super.onResume()

        if((requireActivity() as HomeActivity).getForegroundFragment() is BookingsFragment){
            (requireActivity() as HomeActivity).toolbar.visibility  = View.VISIBLE
            (requireActivity() as HomeActivity).bottom_menu.visibility = View.VISIBLE
            (requireActivity() as HomeActivity).et_search.visibility = View.GONE
        }
    }


    private fun attachObservers() {
        viewModel.bookingHistoryResponse.observe(requireActivity(), Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == false) {
                Utils.showSnackBar(it?.message ?:getString(R.string.please_try_ahain), rv_bookings)
            } else {
                if(it?.items?.isNullOrEmpty() == false){
                    bookingList = it.items as ArrayList<ItemsItem> /* = java.util.ArrayList<com.reemastyle.model.history.OrdersItem> */
                    for(i in 0 until bookingList.size){
                        if(bookingList[i].subtotal == "") bookingList[i].subtotal = "0.0"
                        if(bookingList[i].homeservice == "") bookingList[i].homeservice = "0.0"
                    }
                    setUpBookingsAdapter(bookingList)
                }
            }
        })

        viewModel.bookingCancelResponse.observe(requireActivity(), Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == false) {
                Utils.showSnackBar(it?.message ?:getString(R.string.please_try_ahain), rv_bookings)
            } else {
                Utils.showSnackBar(it?.message ?:"", rv_bookings)
                callBookingHistioryAPI()
            }
        })

        viewModel.apiError.observe(requireActivity(), Observer {
            it?.let {
                Utils.showSnackBar(it, rv_bookings)
                Utils.showLoading(false, requireActivity())
            }
        })

        viewModel.unauthorized.observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), rv_bookings)
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

    companion object {
        @JvmStatic
        fun newInstance() = BookingsFragment()
    }

    override fun onBookingClicked(position: Int, type:String,imageView: ImageView) {
        if(type == "dialog"){
            showPopupWindow(imageView,position)
        }
    }

    private fun showPopupWindow(view: View,position: Int) {
        val inflater = view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.custom_popup_menu, null)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT
        val focusable = true
        val popupWindow = PopupWindow(popupView, width, height, focusable)
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupWindow.setIsLaidOutInScreen(true)
        }

        popupView.txt_reschedule.setOnClickListener {
            popupWindow.dismiss()
            (requireActivity() as HomeActivity).bottom_menu.visibility = View.GONE
            var bundle  = bundleOf("bookingData" to Gson().toJson(bookingList[position]),"from" to "booking")
            findNavController().navigate(R.id.action_bookingsFragment_to_rescheduleBooking,bundle)
        }

        popupView.txt_cancel_booking.setOnClickListener {
            showCancellationConfirmationDialog(requireActivity(),position)
            popupWindow.dismiss()
        }
    }

    private var deletedPosition = 0
    lateinit var cancelDialog: Dialog
    private fun showCancellationConfirmationDialog(context:Context,position: Int){
        cancelDialog = Dialog(context, R.style.DialogAnimation)
        cancelDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        cancelDialog.setContentView(R.layout.cancel_booking_dialog_layout)
        cancelDialog.setCancelable(true)

        cancelDialog.btn_cancel.setOnClickListener {
            deletedPosition = position
            var jsonObject = JsonObject()
            jsonObject.addProperty("action","cancelitem")
            jsonObject.addProperty("userid",Utils.getUserData()?.id)
            jsonObject.addProperty("cart_id",bookingList[position].cartID)
            viewModel?.cancelBooking(jsonObject)
            cancelDialog.dismiss()
        }

        cancelDialog.btn_stay.setOnClickListener {
            cancelDialog.dismiss()
        }

        if (!cancelDialog.isShowing) {
            cancelDialog.show()
        }
    }

}