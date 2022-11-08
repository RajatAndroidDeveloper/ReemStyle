package com.reemastyle.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.reemastyle.HomeActivity
import com.reemastyle.R
import com.reemastyle.model.cart.ItemsItem
import com.reemastyle.preferences.Preferences
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_summary.*

class SummaryFragment : Fragment() {
    lateinit var viewModel: AddToCartModel
    private var cartList: ArrayList<ItemsItem> = ArrayList<ItemsItem>()
    private lateinit var summaryServiceAdapter: SummaryServiceAdapter
    private var subTotal = 0.0
    private var totalAmount = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as HomeActivity).et_search.visibility = View.GONE
        clickListeners()

        cl_cash_in_hand.performClick()
        viewModel = ViewModelProviders.of(this).get(AddToCartModel::class.java)
        attachObservers()

        viewModel?.getCartData(createGetCartDataRequest())
    }

    private fun createGetCartDataRequest(): JsonObject {
        var jsonObject = JsonObject()
        jsonObject.addProperty("action", "ViewCart")
        jsonObject.addProperty("userid", Utils.getUserData()?.id)
        if(Preferences.prefs?.getString("Language","en") == "ar"){
            jsonObject.addProperty("lang","AR")
        }else{
            jsonObject.addProperty("lang","EN")
        }
        return jsonObject
    }

    private var paymentMethod: String = "cod"
    private fun clickListeners() {
        btn_pay.setOnClickListener {
            callPlaceOrderApi()
        }

        cl_cash_in_hand.setOnClickListener {
            paymentMethod == "cod"
            ic_tick_img.visibility = View.VISIBLE
            ic_tick_pos.visibility = View.INVISIBLE
        }

        cl_card_option.setOnClickListener {
            paymentMethod == "pos"
            ic_tick_img.visibility = View.INVISIBLE
            ic_tick_pos.visibility = View.VISIBLE
        }
    }

    private fun callPlaceOrderApi() {
        var jsonObject = JsonObject()
        jsonObject.addProperty("action", "Place_Order")
        jsonObject.addProperty("user_id", Utils.getUserData()?.id)
        jsonObject.addProperty("user_id", Utils.getUserData()?.id)
        var cartIds = ""
        for (i in 0 until cartList.size) {
            cartIds = if (cartIds == "") {
                cartList[i].cartID ?: ""
            } else {
                cartIds + "," + cartList[i].cartID ?: ""
            }
        }
        jsonObject.addProperty("cartids", cartIds)
        jsonObject.addProperty("subtotal", subTotal.toString())
        jsonObject.addProperty("total", totalAmount.toString())
        jsonObject.addProperty("payment_method", paymentMethod)
        viewModel?.placeOrder(jsonObject)
    }

    private fun setUpSummaryServiceAdapter(cartList: ArrayList<ItemsItem>) {
        var layoutManager = LinearLayoutManager(requireActivity())
        rv_services.layoutManager = layoutManager
        summaryServiceAdapter = SummaryServiceAdapter(requireActivity(), cartList)
        rv_services.adapter = summaryServiceAdapter
    }

    override fun onResume() {
        super.onResume()

        if ((requireActivity() as HomeActivity).getForegroundFragment() is SummaryFragment) {
            (requireActivity() as HomeActivity).toolbar.visibility = View.VISIBLE
            (requireActivity() as HomeActivity).bottom_menu.visibility = View.GONE
            (requireActivity() as HomeActivity).et_search.visibility = View.GONE
        }
    }

    private fun attachObservers() {
        viewModel.savedCartResponse.observe(requireActivity(), Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == false) {
                Utils.showSnackBar(it?.message ?:getString(R.string.please_try_ahain), rv_services)
            } else {
                Utils.showSnackBar(it?.message ?: "", rv_services)
                cartList = it?.items as ArrayList<ItemsItem> /* = java.util.ArrayList<com.reemastyle.model.cart.ItemsItem> */

                for(i in 0 until cartList.size){
                    if (cartList[i].subtotal == ""){
                        cartList[i].subtotal == "0"
                    }
                    if (cartList[i].homeservice == ""){
                        cartList[i].homeservice == "0"
                    }
                }
                setUpSummaryServiceAdapter(cartList)
                calculateTotalAmount(cartList)
            }
        })

        viewModel.placeOrderResponse.observe(requireActivity(), Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == false) {
                Utils.showSnackBar(it?.message ?:getString(R.string.please_try_ahain), rv_services)
            } else {
                Utils.showSnackBar(it?.message ?: "", rv_services)
                (requireActivity() as HomeActivity).toolbar.visibility = View.GONE
                (requireActivity() as HomeActivity).bottom_menu.visibility = View.GONE
                findNavController().navigate(R.id.action_summaryFragment_to_appointmentSuccessFragment)
            }
        })

        viewModel.apiError.observe(requireActivity(), Observer {
            it?.let {
                Utils.showSnackBar(it, rv_services)
                Utils.showLoading(false, requireActivity())
            }
        })

        viewModel.unauthorized.observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), rv_services)
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

    private fun calculateTotalAmount(cartListData: ArrayList<ItemsItem>) {
        var totalAmount = 0.0
        var totalHomeService = 0.0
        for (i in 0 until cartListData.size) {
            if(cartListData[i].subtotal =="") cartListData[i].subtotal = "0.0"
            if(cartListData[i].homeservice =="") cartListData[i].homeservice = "0.0"
            totalAmount += if(cartList[i].categoryName!!.trim() == "Leg front" || cartList[i].categoryName!!.trim() == "Hand front" ||cartList[i].categoryName!!.trim() == "Hand front and back" || cartList[i].categoryName!! == "Leg front and back"|| cartList[i].categoryName!!.trim() == "الجبهة الساق" || cartList[i].categoryName!!.trim() == "اليد الأمامية"
                || cartList[i].categoryName!! == "اليد الأمامية والخلفية" || cartList[i].categoryName!!.trim() == "أمامي وخلفي الساق" ){

                (((cartListData[i].subtotal ?: "0.0").toDouble()))
            }else {
                (((cartListData[i].serviceprice
                    ?: "0.0").toDouble()) * ((cartListData[i].serviceQty ?: "0.0").toDouble()))
            }
            totalHomeService += ((cartListData[i].homeservice ?: "0.0").toDouble())
            totalAmount += ((cartListData[i].homeservice ?: "0.0").toDouble())
        }
        subTotal = totalAmount - totalHomeService
        txt_subtotal.text = "${getString(R.string.currency_value)} $subTotal"
        txt_service_fee.text = "${getString(R.string.currency_value)} $totalHomeService"
        txt_total.text = "${getString(R.string.currency_value)}  $totalAmount"
        txt_total_val.text = "${getString(R.string.currency_value)}  $totalAmount"
    }

    companion object {
        @JvmStatic
        fun newInstance() = SummaryFragment()
    }
}