package com.reemastyle.cart

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.reemastyle.HomeActivity
import com.reemastyle.R
import com.reemastyle.model.cart.ItemsItem
import com.reemastyle.model.cart.SavedCartResponse
import com.reemastyle.preferences.Preferences
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.cancel_booking_dialog_layout.*
import kotlinx.android.synthetic.main.custom_popup_menu.view.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_cart.*


class CartFragment : Fragment(), CartItemClicked {
    private lateinit var cartItemAdapter: CartItemAdapter
    private lateinit var cancelDialog: Dialog
    lateinit var viewModel : AddToCartModel
    private var cartList: ArrayList<ItemsItem> = ArrayList<ItemsItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as HomeActivity).toolbar.visibility = View.VISIBLE
        (requireActivity() as HomeActivity).et_search.visibility = View.GONE
        (requireActivity() as HomeActivity).bottom_menu.visibility = View.GONE

        clickListeners()
        viewModel = ViewModelProviders.of(this)[AddToCartModel::class.java]
        attachObservers()

        viewModel?.getCartData(createGetCartDataRequest())
    }

    private fun createGetCartDataRequest(): JsonObject {
        var jsonObject = JsonObject()
        jsonObject.addProperty("action","ViewCart")
        jsonObject.addProperty("userid",Utils.getUserData()?.id)
        if(Preferences.prefs?.getString("Language","en") == "ar"){
            jsonObject.addProperty("lang","AR")
        }else{
            jsonObject.addProperty("lang","EN")
        }
        return jsonObject
    }

    private fun clickListeners() {
        btn_book_now.setOnClickListener {
            if(totalCartAmount < 100.0){
                showPricingDialogh(requireActivity())
                return@setOnClickListener
            }
            findNavController().navigate(R.id.action_cartFragment_to_summaryFragment)
        }
    }

    private fun setUpCartAdapter(cartData: ArrayList<ItemsItem>) {
        var layoutManager = LinearLayoutManager(requireActivity())
        rv_cart.layoutManager = layoutManager
        cartItemAdapter = CartItemAdapter(requireActivity(),this,cartData)
        rv_cart.adapter = cartItemAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = CartFragment()
    }

    override fun onCartItemClicked(position: Int,type:String) {
        //do nothing
    }

    override fun onCartItemClicked(position: Int, view: View, type: String) {
        showPopupWindow(view,position)
    }

    private fun showPopupWindow(view: View,position: Int) {
        val inflater = view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.custom_popup_menu_for_cart, null)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT
        val focusable = true
        val popupWindow = PopupWindow(popupView, width, height, focusable)
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupWindow.setIsLaidOutInScreen(true)
        }

        popupView.txt_reschedule.setOnClickListener {
            showCancellationConfirmationDialog(requireActivity(),position)
            popupWindow.dismiss()
        }

        popupView.txt_cancel_booking.setOnClickListener {
            showCancellationConfirmationDialog(requireActivity(),position)
            popupWindow.dismiss()
        }
    }

    private var deletedPosition = 0
    private fun showCancellationConfirmationDialog(context:Context,position: Int){
            cancelDialog = Dialog(context, R.style.DialogAnimation)
            cancelDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            cancelDialog.setContentView(R.layout.cancel_booking_dialog_layout)
            cancelDialog.setCancelable(true)

            cancelDialog.btn_cancel.setOnClickListener {
                deletedPosition = position
                var jsonObject = JsonObject()
                jsonObject.addProperty("action","deletecart")
                jsonObject.addProperty("userid",Utils.getUserData()?.id)
                jsonObject.addProperty("cart_id",cartList[position].cartID)
                viewModel?.deleteCartItem(jsonObject)
            }

            cancelDialog.btn_stay.setOnClickListener {
                cancelDialog.dismiss()
            }

            if (!cancelDialog.isShowing) {
                cancelDialog.show()
            }
    }

    private fun attachObservers() {
        viewModel.savedCartResponse.observe(viewLifecycleOwner, Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == false) {
                Utils.showSnackBar(it?.message?:getString(R.string.please_try_ahain), rv_cart)
            } else {
                Utils.showSnackBar(it?.message ?: "", rv_cart)
                cartList = it?.items as ArrayList<ItemsItem> /* = java.util.ArrayList<com.reemastyle.model.cart.ItemsItem> */
                for(i in 0 until cartList.size){
                    if(cartList[i].subtotal == ""){
                        cartList[i].subtotal == "0"
                    }
                    if(cartList[i].homeservice == ""){
                        cartList[i].homeservice == "0"
                    }
                }
                setUpCartAdapter(cartList)
                calculateTotalAmount(cartList)
            }
        })

        viewModel.deleteCartResponse.observe(viewLifecycleOwner,Observer{
            Utils.showLoading(false, requireActivity())
            if(cancelDialog.isShowing)
                cancelDialog.dismiss()
            if(it.status == false){
                Utils.showSnackBar(it?.message ?:getString(R.string.please_try_ahain), rv_cart)
            }else{
                Utils.showSnackBar(it?.message ?: "", rv_cart)
                if(cartList.size >0) {
                    cartList.removeAt(deletedPosition)
                    for (i in 0 until cartList.size) {
                        if (cartList[i].subtotal == "") {
                            cartList[i].subtotal == "0"
                        }
                        if (cartList[i].homeservice == "") {
                            cartList[i].homeservice == "0"
                        }
                    }
                    calculateTotalAmount(cartList)
                }
                cartItemAdapter?.notifyDataSetChanged()
            }
        })

        viewModel.apiError.observe(requireActivity(), Observer {
            it?.let {
                Utils.showSnackBar(it, btn_book_now)
                Utils.showLoading(false, requireActivity())
            }
        })

        viewModel.unauthorized.observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), rv_cart)
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

    private var totalCartAmount = 0.0
    private fun calculateTotalAmount(cartListData: ArrayList<ItemsItem>) {
        if(cartListData.size>0) {
            var totalAmount = 0.0
            for (i in 0 until cartListData.size) {
                if (cartListData[i].subtotal == "") cartListData[i].subtotal = "0.0"
                if (cartListData[i].homeservice == "") cartListData[i].homeservice = "0.0"
                totalAmount += if (cartList[i].categoryName!!.trim() == "Leg front" || cartList[i].categoryName!!.trim() == "Hand front" || cartList[i].categoryName!!.trim() == "Hand front and back" || cartList[i].categoryName!!.trim() == "Leg front and back" || cartList[i].categoryName!!.trim() == "الجبهة الساق" || cartList[i].categoryName!!.trim() == "اليد الأمامية"
                    || cartList[i].categoryName!!.trim() == "اليد الأمامية والخلفية" || cartList[i].categoryName!!.trim() == "أمامي وخلفي الساق"
                ) {
                    ((cartListData[i].subtotal ?: "0.0").toDouble())
                } else {
                    (((cartListData[i].serviceprice
                        ?: "0.0").toDouble()) * ((cartListData[i].serviceQty ?: "0.0").toDouble()))
                }
                totalAmount += ((cartListData[i].homeservice ?: "0.0").toDouble())
            }

            totalCartAmount = totalAmount
            txt_total.text = "${getString(R.string.currency_value)} $totalAmount"
        }
    }

    private lateinit var pricingDialog: Dialog
    private fun showPricingDialogh(context: Context){
        pricingDialog = Dialog(context, R.style.DialogAnimation)
        pricingDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        pricingDialog.setContentView(R.layout.cancel_booking_dialog_layout)
        pricingDialog.setCancelable(true)

        pricingDialog.txt_title.text = getString(R.string.add_more_items)
        pricingDialog.txt_cancel_description.text = getString(R.string.price_should_be_more)
        pricingDialog.btn_cancel.text = getString(R.string.okay)
        pricingDialog.btn_stay.visibility = View.GONE

        pricingDialog.btn_cancel.setOnClickListener {
            pricingDialog.dismiss()
        }

        pricingDialog.btn_stay.setOnClickListener {
            pricingDialog.dismiss()
        }

        if (!pricingDialog.isShowing) {
            pricingDialog.show()
        }
    }
}