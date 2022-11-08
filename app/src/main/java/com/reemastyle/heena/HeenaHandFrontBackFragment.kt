package com.reemastyle.heena

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.reemastyle.R
import com.reemastyle.model.heenadetail.HeenaDetailResponse
import com.reemastyle.model.heenadetail.HeenaSectionItem
import com.reemastyle.preferences.Preferences
import com.reemastyle.service.ServiceViewModel
import com.reemastyle.util.Constants
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.fragment_heena_detail.*
import kotlinx.android.synthetic.main.fragment_update_profile.*

class HeenaHandFrontBackFragment : Fragment() {
    lateinit var viewModel: ServiceViewModel
    private var heenaDetailList: ArrayList<HeenaSectionItem> = ArrayList<HeenaSectionItem>()
    private var selectedHeena: HeenaSectionItem?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_heena_hand_front_back_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ServiceViewModel::class.java)
        attachObservers()
        var jsonObject = JsonObject()
        jsonObject.addProperty("action", "get_heena_categories")
        jsonObject.addProperty("id", Constants.SELECTED_SUB_CATEGORY)
        if(Preferences.prefs?.getString("Language","en") == "ar"){
            jsonObject.addProperty("lang","AR")
        }else{
            jsonObject.addProperty("lang","EN")
        }
        viewModel.getHeenaDetails(jsonObject)

        changeDataAccordingToCategory()
        onSetClickListeners()
    }

    private var clickedPosition = 0
    private lateinit var heenaDetailResponse: HeenaDetailResponse
    private fun onSetClickListeners() {
        btn_book_now.setOnClickListener{
            if(selectedHeena ==  null){
                Utils.showSnackBar(getString(R.string.please_select_any_heena), btn_book_now)
                return@setOnClickListener
            }else{
                var bundle = Bundle()
                bundle.putString("heena_data", Gson().toJson(selectedHeena))
                bundle.putString("heena_details", Gson().toJson(heenaDetailResponse))
                findNavController().navigate(R.id.action_heenaHandFrontBackFragment_to_bookHeenaFragment, bundle)
            }
        }
        img_1.setOnClickListener {
            clickedPosition = 1
            selectedHeena = heenaDetailList[clickedPosition-1]
            changeImageResources()
        }
        img_2.setOnClickListener {
            clickedPosition = 2
            selectedHeena = heenaDetailList[clickedPosition-1]
            changeImageResources()
        }
        img_3.setOnClickListener {
            clickedPosition = 3
            selectedHeena = heenaDetailList[clickedPosition-1]
            changeImageResources()
        }
        img_4.setOnClickListener {
            clickedPosition = 4
            selectedHeena = heenaDetailList[clickedPosition-1]
            changeImageResources()
        }
        img_5.setOnClickListener {
            clickedPosition = 5
            selectedHeena = heenaDetailList[clickedPosition-1]
            changeImageResources()
        }
        img_6.setOnClickListener {
            clickedPosition = 6
            selectedHeena = heenaDetailList[clickedPosition-1]
            changeImageResources()
        }
        img_7.setOnClickListener {
            clickedPosition = 7
            selectedHeena = heenaDetailList[clickedPosition-1]
            changeImageResources()
        }
        img_8.setOnClickListener {
            clickedPosition = 8
            selectedHeena = heenaDetailList[clickedPosition-1]
            changeImageResources()
        }
        img_9.setOnClickListener {
            clickedPosition = 9
            selectedHeena = heenaDetailList[clickedPosition-1]
            changeImageResources()
        }
        img_10.setOnClickListener {
            clickedPosition = 10
            selectedHeena = heenaDetailList[clickedPosition-2]
            changeImageResources()
        }
    }

    private fun changeImageResources() {
        if (Constants.SELECTED_SUB_CATEGORY_NAME.trim() == "Leg front" || Constants.SELECTED_SUB_CATEGORY_NAME.trim() == "الجبهة الساق") {
            when (clickedPosition) {
                10 -> {
                    hidePricingData(ll9,ll2,ll3,ll4,ll1,ll6,ll7,ll8,ll5,
                        txtPrice9,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice1)
                    img_1.setImageResource(R.drawable.leg_selected_13)
                    img_2.setImageResource(R.drawable.leg_selected_12)
                    img_3.setImageResource(R.drawable.leg_selected_11)
                    img_4.setImageResource(R.drawable.leg_selected_10)
                    img_5.setImageResource(R.drawable.leg_selected_09)
                    img_6.setImageResource(R.drawable.leg_selected_08)
                    img_7.setImageResource(R.drawable.leg_selected_07)
                    img_8.setImageResource(R.drawable.leg_selected_06)
                    img_9.setImageResource(R.drawable.leg_selected_05)
                    img_10.setImageResource(R.drawable.leg_selected_03)
                }
                9 -> {
                    hidePricingData(ll9,ll2,ll3,ll4,ll1,ll6,ll7,ll8,ll5,
                        txtPrice9,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice1)
                    img_1.setImageResource(R.drawable.leg_selected_13)
                    img_2.setImageResource(R.drawable.leg_selected_12)
                    img_3.setImageResource(R.drawable.leg_selected_11)
                    img_4.setImageResource(R.drawable.leg_selected_10)
                    img_5.setImageResource(R.drawable.leg_selected_09)
                    img_6.setImageResource(R.drawable.leg_selected_08)
                    img_7.setImageResource(R.drawable.leg_selected_07)
                    img_8.setImageResource(R.drawable.leg_selected_06)
                    img_9.setImageResource(R.drawable.leg_selected_05)
                    img_10.setImageResource(R.drawable.leg_10)
                }
                8 -> {
                    hidePricingData(ll8,ll2,ll3,ll4,ll1,ll6,ll7,ll5,ll9,
                        txtPrice8,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice1,txtPrice9)
                    img_1.setImageResource(R.drawable.leg_selected_13)
                    img_2.setImageResource(R.drawable.leg_selected_12)
                    img_3.setImageResource(R.drawable.leg_selected_11)
                    img_4.setImageResource(R.drawable.leg_selected_10)
                    img_5.setImageResource(R.drawable.leg_selected_09)
                    img_6.setImageResource(R.drawable.leg_selected_08)
                    img_7.setImageResource(R.drawable.leg_selected_07)
                    img_8.setImageResource(R.drawable.leg_selected_06)
                    img_9.setImageResource(R.drawable.leg_9)
                    img_10.setImageResource(R.drawable.leg_10)
                }
                7 -> {
                    hidePricingData(ll7,ll2,ll3,ll4,ll1,ll6,ll5,ll8,ll9,
                        txtPrice7,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice1,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.leg_selected_13)
                    img_2.setImageResource(R.drawable.leg_selected_12)
                    img_3.setImageResource(R.drawable.leg_selected_11)
                    img_4.setImageResource(R.drawable.leg_selected_10)
                    img_5.setImageResource(R.drawable.leg_selected_09)
                    img_6.setImageResource(R.drawable.leg_selected_08)
                    img_7.setImageResource(R.drawable.leg_selected_07)
                    img_8.setImageResource(R.drawable.leg_8)
                    img_9.setImageResource(R.drawable.leg_9)
                    img_10.setImageResource(R.drawable.leg_10)
                }
                6 -> {
                    hidePricingData(ll6,ll2,ll3,ll4,ll5,ll1,ll7,ll8,ll9,
                        txtPrice6,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice1,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.leg_selected_13)
                    img_2.setImageResource(R.drawable.leg_selected_12)
                    img_3.setImageResource(R.drawable.leg_selected_11)
                    img_4.setImageResource(R.drawable.leg_selected_10)
                    img_5.setImageResource(R.drawable.leg_selected_09)
                    img_6.setImageResource(R.drawable.leg_selected_08)
                    img_7.setImageResource(R.drawable.leg_7)
                    img_8.setImageResource(R.drawable.leg_8)
                    img_9.setImageResource(R.drawable.leg_9)
                    img_10.setImageResource(R.drawable.leg_10)
                }
                5 -> {
                    hidePricingData(ll5,ll2,ll3,ll4,ll1,ll6,ll7,ll8,ll9,
                        txtPrice5,txtPrice2,txtPrice3,txtPrice4,txtPrice1,txtPrice6,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.leg_selected_13)
                    img_2.setImageResource(R.drawable.leg_selected_12)
                    img_3.setImageResource(R.drawable.leg_selected_11)
                    img_4.setImageResource(R.drawable.leg_selected_10)
                    img_5.setImageResource(R.drawable.leg_selected_09)
                    img_6.setImageResource(R.drawable.leg_6)
                    img_7.setImageResource(R.drawable.leg_7)
                    img_8.setImageResource(R.drawable.leg_8)
                    img_9.setImageResource(R.drawable.leg_9)
                    img_10.setImageResource(R.drawable.leg_10)
                }
                4 -> {
                    hidePricingData(ll4,ll2,ll3,ll1,ll5,ll6,ll7,ll8,ll9,
                        txtPrice4,txtPrice2,txtPrice3,txtPrice1,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.leg_selected_13)
                    img_2.setImageResource(R.drawable.leg_selected_12)
                    img_3.setImageResource(R.drawable.leg_selected_11)
                    img_4.setImageResource(R.drawable.leg_selected_10)
                    img_5.setImageResource(R.drawable.leg_5)
                    img_6.setImageResource(R.drawable.leg_6)
                    img_7.setImageResource(R.drawable.leg_7)
                    img_8.setImageResource(R.drawable.leg_8)
                    img_9.setImageResource(R.drawable.leg_9)
                    img_10.setImageResource(R.drawable.leg_10)
                }
                3 -> {
                    hidePricingData(ll3,ll2,ll1,ll4,ll5,ll6,ll7,ll8,ll9,
                        txtPrice3,txtPrice2,txtPrice1,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.leg_selected_13)
                    img_2.setImageResource(R.drawable.leg_selected_12)
                    img_3.setImageResource(R.drawable.leg_selected_11)
                    img_4.setImageResource(R.drawable.leg_4)
                    img_5.setImageResource(R.drawable.leg_5)
                    img_6.setImageResource(R.drawable.leg_6)
                    img_7.setImageResource(R.drawable.leg_7)
                    img_8.setImageResource(R.drawable.leg_8)
                    img_9.setImageResource(R.drawable.leg_9)
                    img_10.setImageResource(R.drawable.leg_10)
                }
                2 -> {
                    hidePricingData(ll2,ll1,ll3,ll4,ll5,ll6,ll7,ll8,ll9,
                        txtPrice2,txtPrice1,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.leg_selected_13)
                    img_2.setImageResource(R.drawable.leg_selected_12)
                    img_3.setImageResource(R.drawable.leg_3)
                    img_4.setImageResource(R.drawable.leg_4)
                    img_5.setImageResource(R.drawable.leg_5)
                    img_6.setImageResource(R.drawable.leg_6)
                    img_7.setImageResource(R.drawable.leg_7)
                    img_8.setImageResource(R.drawable.leg_8)
                    img_9.setImageResource(R.drawable.leg_9)
                    img_10.setImageResource(R.drawable.leg_10)
                }
                1 -> {
                    hidePricingData(ll1,ll2,ll3,ll4,ll5,ll6,ll7,ll8,ll9,
                        txtPrice1,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.leg_selected_13)
                    img_2.setImageResource(R.drawable.leg_2)
                    img_3.setImageResource(R.drawable.leg_3)
                    img_4.setImageResource(R.drawable.leg_4)
                    img_5.setImageResource(R.drawable.leg_5)
                    img_6.setImageResource(R.drawable.leg_6)
                    img_7.setImageResource(R.drawable.leg_7)
                    img_8.setImageResource(R.drawable.leg_8)
                    img_9.setImageResource(R.drawable.leg_9)
                    img_10.setImageResource(R.drawable.leg_10)
                }
            }
        }else if (Constants.SELECTED_SUB_CATEGORY_NAME.trim() == "Leg front and back" || Constants.SELECTED_SUB_CATEGORY_NAME.trim() == "اليد الأمامية والخلفية") {
            when (clickedPosition) {
                10 -> {
                    hidePricingData(ll9,ll2,ll3,ll4,ll1,ll6,ll7,ll8,ll5,
                        txtPrice9,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice1)
                    img_1.setImageResource(R.drawable.leg_01_10_selected)
                    img_2.setImageResource(R.drawable.leg_01_09_selected)
                    img_3.setImageResource(R.drawable.leg_01_08_selected)
                    img_4.setImageResource(R.drawable.leg_01_07_selected)
                    img_5.setImageResource(R.drawable.leg_01_06_selected)
                    img_6.setImageResource(R.drawable.leg_01_05_selected)
                    img_7.setImageResource(R.drawable.leg_01_04_selected)
                    img_8.setImageResource(R.drawable.leg_01_03_selected)
                    img_9.setImageResource(R.drawable.leg_01_02_selected)
                    img_10.setImageResource(R.drawable.leg_01_01_selected)
                }
                9 -> {
                    hidePricingData(ll9,ll2,ll3,ll4,ll1,ll6,ll7,ll8,ll5,
                        txtPrice9,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice1)
                    img_1.setImageResource(R.drawable.leg_01_10_selected)
                    img_2.setImageResource(R.drawable.leg_01_09_selected)
                    img_3.setImageResource(R.drawable.leg_01_08_selected)
                    img_4.setImageResource(R.drawable.leg_01_07_selected)
                    img_5.setImageResource(R.drawable.leg_01_06_selected)
                    img_6.setImageResource(R.drawable.leg_01_05_selected)
                    img_7.setImageResource(R.drawable.leg_01_04_selected)
                    img_8.setImageResource(R.drawable.leg_01_03_selected)
                    img_9.setImageResource(R.drawable.leg_01_02_selected)
                    img_10.setImageResource(R.drawable.leg_01_01)
                }
                8 -> {
                    hidePricingData(ll8,ll2,ll3,ll4,ll1,ll6,ll7,ll5,ll9,
                        txtPrice8,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice1,txtPrice9)
                    img_1.setImageResource(R.drawable.leg_01_10_selected)
                    img_2.setImageResource(R.drawable.leg_01_09_selected)
                    img_3.setImageResource(R.drawable.leg_01_08_selected)
                    img_4.setImageResource(R.drawable.leg_01_07_selected)
                    img_5.setImageResource(R.drawable.leg_01_06_selected)
                    img_6.setImageResource(R.drawable.leg_01_05_selected)
                    img_7.setImageResource(R.drawable.leg_01_04_selected)
                    img_8.setImageResource(R.drawable.leg_01_03_selected)
                    img_9.setImageResource(R.drawable.leg_01_02)
                    img_10.setImageResource(R.drawable.leg_01_01)
                }
                7 -> {
                    hidePricingData(ll7,ll2,ll3,ll4,ll1,ll6,ll5,ll8,ll9,
                        txtPrice7,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice1,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.leg_01_10_selected)
                    img_2.setImageResource(R.drawable.leg_01_09_selected)
                    img_3.setImageResource(R.drawable.leg_01_08_selected)
                    img_4.setImageResource(R.drawable.leg_01_07_selected)
                    img_5.setImageResource(R.drawable.leg_01_06_selected)
                    img_6.setImageResource(R.drawable.leg_01_05_selected)
                    img_7.setImageResource(R.drawable.leg_01_04_selected)
                    img_8.setImageResource(R.drawable.leg_01_03)
                    img_9.setImageResource(R.drawable.leg_01_02)
                    img_10.setImageResource(R.drawable.leg_01_01)
                }
                6 -> {
                    hidePricingData(ll6,ll2,ll3,ll4,ll5,ll1,ll7,ll8,ll9,
                        txtPrice6,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice1,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.leg_01_10_selected)
                    img_2.setImageResource(R.drawable.leg_01_09_selected)
                    img_3.setImageResource(R.drawable.leg_01_08_selected)
                    img_4.setImageResource(R.drawable.leg_01_07_selected)
                    img_5.setImageResource(R.drawable.leg_01_06_selected)
                    img_6.setImageResource(R.drawable.leg_01_05_selected)
                    img_7.setImageResource(R.drawable.leg_01_04)
                    img_8.setImageResource(R.drawable.leg_01_03)
                    img_9.setImageResource(R.drawable.leg_01_02)
                    img_10.setImageResource(R.drawable.leg_01_01)
                }
                5 -> {
                    hidePricingData(ll5,ll2,ll3,ll4,ll1,ll6,ll7,ll8,ll9,
                        txtPrice5,txtPrice2,txtPrice3,txtPrice4,txtPrice1,txtPrice6,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.leg_01_10_selected)
                    img_2.setImageResource(R.drawable.leg_01_09_selected)
                    img_3.setImageResource(R.drawable.leg_01_08_selected)
                    img_4.setImageResource(R.drawable.leg_01_07_selected)
                    img_5.setImageResource(R.drawable.leg_01_06_selected)
                    img_6.setImageResource(R.drawable.leg_01_05)
                    img_7.setImageResource(R.drawable.leg_01_04)
                    img_8.setImageResource(R.drawable.leg_01_03)
                    img_9.setImageResource(R.drawable.leg_01_02)
                    img_10.setImageResource(R.drawable.leg_01_01)
                }
                4 -> {
                    hidePricingData(ll4,ll2,ll3,ll1,ll5,ll6,ll7,ll8,ll9,
                        txtPrice4,txtPrice2,txtPrice3,txtPrice1,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.leg_01_10_selected)
                    img_2.setImageResource(R.drawable.leg_01_09_selected)
                    img_3.setImageResource(R.drawable.leg_01_08_selected)
                    img_4.setImageResource(R.drawable.leg_01_07_selected)
                    img_5.setImageResource(R.drawable.leg_01_06)
                    img_6.setImageResource(R.drawable.leg_01_05)
                    img_7.setImageResource(R.drawable.leg_01_04)
                    img_8.setImageResource(R.drawable.leg_01_03)
                    img_9.setImageResource(R.drawable.leg_01_02)
                    img_10.setImageResource(R.drawable.leg_01_01)
                }
                3 -> {
                    hidePricingData(ll3,ll2,ll1,ll4,ll5,ll6,ll7,ll8,ll9,
                        txtPrice3,txtPrice2,txtPrice1,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.leg_01_10_selected)
                    img_2.setImageResource(R.drawable.leg_01_09_selected)
                    img_3.setImageResource(R.drawable.leg_01_08_selected)
                    img_4.setImageResource(R.drawable.leg_01_07)
                    img_5.setImageResource(R.drawable.leg_01_06)
                    img_6.setImageResource(R.drawable.leg_01_05)
                    img_7.setImageResource(R.drawable.leg_01_04)
                    img_8.setImageResource(R.drawable.leg_01_03)
                    img_9.setImageResource(R.drawable.leg_01_02)
                    img_10.setImageResource(R.drawable.leg_01_01)
                }
                2 -> {
                    hidePricingData(ll2,ll1,ll3,ll4,ll5,ll6,ll7,ll8,ll9,
                        txtPrice2,txtPrice1,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.leg_01_10_selected)
                    img_2.setImageResource(R.drawable.leg_01_09_selected)
                    img_3.setImageResource(R.drawable.leg_01_08)
                    img_4.setImageResource(R.drawable.leg_01_07)
                    img_5.setImageResource(R.drawable.leg_01_06)
                    img_6.setImageResource(R.drawable.leg_01_05)
                    img_7.setImageResource(R.drawable.leg_01_04)
                    img_8.setImageResource(R.drawable.leg_01_03)
                    img_9.setImageResource(R.drawable.leg_01_02)
                    img_10.setImageResource(R.drawable.leg_01_01)
                }
                1 -> {
                    hidePricingData(ll1,ll2,ll3,ll4,ll5,ll6,ll7,ll8,ll9,
                        txtPrice1,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.leg_01_10_selected)
                    img_2.setImageResource(R.drawable.leg_01_09)
                    img_3.setImageResource(R.drawable.leg_01_08)
                    img_4.setImageResource(R.drawable.leg_01_07)
                    img_5.setImageResource(R.drawable.leg_01_06)
                    img_6.setImageResource(R.drawable.leg_01_05)
                    img_7.setImageResource(R.drawable.leg_01_04)
                    img_8.setImageResource(R.drawable.leg_01_03)
                    img_9.setImageResource(R.drawable.leg_01_02)
                    img_10.setImageResource(R.drawable.leg_01_01)
                }
            }
        } else if (Constants.SELECTED_SUB_CATEGORY_NAME.trim() == "Hand front" || Constants.SELECTED_SUB_CATEGORY_NAME.trim() == "اليد الأمامية" )  {
            when (clickedPosition) {
                10 -> {
                    hidePricingData(ll9,ll2,ll3,ll4,ll1,ll6,ll7,ll8,ll5,
                        txtPrice9,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice1)
                    img_1.setImageResource(R.drawable.hand_13_selected)
                    img_2.setImageResource(R.drawable.hand_12_selected)
                    img_3.setImageResource(R.drawable.hand_11_selected)
                    img_4.setImageResource(R.drawable.hand_10_selected)
                    img_5.setImageResource(R.drawable.hand_09_selected)
                    img_6.setImageResource(R.drawable.hand_08_selected)
                    img_7.setImageResource(R.drawable.hand_07_selected)
                    img_8.setImageResource(R.drawable.hand_06_selected)
                    img_9.setImageResource(R.drawable.hand_05_selected)
                    img_10.setImageResource(R.drawable.hand_03_selected)
                }
                9 -> {
                    hidePricingData(ll9,ll2,ll3,ll4,ll1,ll6,ll7,ll8,ll5,
                        txtPrice9,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice1)
                    img_1.setImageResource(R.drawable.hand_13_selected)
                    img_2.setImageResource(R.drawable.hand_12_selected)
                    img_3.setImageResource(R.drawable.hand_11_selected)
                    img_4.setImageResource(R.drawable.hand_10_selected)
                    img_5.setImageResource(R.drawable.hand_09_selected)
                    img_6.setImageResource(R.drawable.hand_08_selected)
                    img_7.setImageResource(R.drawable.hand_07_selected)
                    img_8.setImageResource(R.drawable.hand_06_selected)
                    img_9.setImageResource(R.drawable.hand_05_selected)
                    img_10.setImageResource(R.drawable.hand_03)
                }
                8 -> {
                    hidePricingData(ll8,ll2,ll3,ll4,ll1,ll6,ll7,ll5,ll9,
                        txtPrice8,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice1,txtPrice9)
                    img_1.setImageResource(R.drawable.hand_13_selected)
                    img_2.setImageResource(R.drawable.hand_12_selected)
                    img_3.setImageResource(R.drawable.hand_11_selected)
                    img_4.setImageResource(R.drawable.hand_10_selected)
                    img_5.setImageResource(R.drawable.hand_09_selected)
                    img_6.setImageResource(R.drawable.hand_08_selected)
                    img_7.setImageResource(R.drawable.hand_07_selected)
                    img_8.setImageResource(R.drawable.hand_06_selected)
                    img_9.setImageResource(R.drawable.hand_05)
                    img_10.setImageResource(R.drawable.hand_03)
                }
                7 -> {
                    hidePricingData(ll7,ll2,ll3,ll4,ll1,ll6,ll5,ll8,ll9,
                        txtPrice7,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice1,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.hand_13_selected)
                    img_2.setImageResource(R.drawable.hand_12_selected)
                    img_3.setImageResource(R.drawable.hand_11_selected)
                    img_4.setImageResource(R.drawable.hand_10_selected)
                    img_5.setImageResource(R.drawable.hand_09_selected)
                    img_6.setImageResource(R.drawable.hand_08_selected)
                    img_7.setImageResource(R.drawable.hand_07_selected)
                    img_8.setImageResource(R.drawable.hand_06)
                    img_9.setImageResource(R.drawable.hand_05)
                    img_10.setImageResource(R.drawable.hand_03)
                }
                6 -> {
                    hidePricingData(ll6,ll2,ll3,ll4,ll5,ll1,ll7,ll8,ll9,
                        txtPrice6,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice1,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.hand_13_selected)
                    img_2.setImageResource(R.drawable.hand_12_selected)
                    img_3.setImageResource(R.drawable.hand_11_selected)
                    img_4.setImageResource(R.drawable.hand_10_selected)
                    img_5.setImageResource(R.drawable.hand_09_selected)
                    img_6.setImageResource(R.drawable.hand_08_selected)
                    img_7.setImageResource(R.drawable.hand_07)
                    img_8.setImageResource(R.drawable.hand_06)
                    img_9.setImageResource(R.drawable.hand_05)
                    img_10.setImageResource(R.drawable.hand_03)
                }
                5 -> {
                    hidePricingData(ll5,ll2,ll3,ll4,ll1,ll6,ll7,ll8,ll9,
                        txtPrice5,txtPrice2,txtPrice3,txtPrice4,txtPrice1,txtPrice6,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.hand_13_selected)
                    img_2.setImageResource(R.drawable.hand_12_selected)
                    img_3.setImageResource(R.drawable.hand_11_selected)
                    img_4.setImageResource(R.drawable.hand_10_selected)
                    img_5.setImageResource(R.drawable.hand_09_selected)
                    img_6.setImageResource(R.drawable.hand_08)
                    img_7.setImageResource(R.drawable.hand_07)
                    img_8.setImageResource(R.drawable.hand_06)
                    img_9.setImageResource(R.drawable.hand_05)
                    img_10.setImageResource(R.drawable.hand_03)
                }
                4 -> {
                    hidePricingData(ll4,ll2,ll3,ll1,ll5,ll6,ll7,ll8,ll9,
                        txtPrice4,txtPrice2,txtPrice3,txtPrice1,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.hand_13_selected)
                    img_2.setImageResource(R.drawable.hand_12_selected)
                    img_3.setImageResource(R.drawable.hand_11_selected)
                    img_4.setImageResource(R.drawable.hand_10_selected)
                    img_5.setImageResource(R.drawable.hand_09)
                    img_6.setImageResource(R.drawable.hand_08)
                    img_7.setImageResource(R.drawable.hand_07)
                    img_8.setImageResource(R.drawable.hand_06)
                    img_9.setImageResource(R.drawable.hand_05)
                    img_10.setImageResource(R.drawable.hand_03)
                }
                3 -> {
                    hidePricingData(ll3,ll2,ll1,ll4,ll5,ll6,ll7,ll8,ll9,
                        txtPrice3,txtPrice2,txtPrice1,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.hand_13_selected)
                    img_2.setImageResource(R.drawable.hand_12_selected)
                    img_3.setImageResource(R.drawable.hand_11_selected)
                    img_4.setImageResource(R.drawable.hand_10)
                    img_5.setImageResource(R.drawable.hand_09)
                    img_6.setImageResource(R.drawable.hand_08)
                    img_7.setImageResource(R.drawable.hand_07)
                    img_8.setImageResource(R.drawable.hand_06)
                    img_9.setImageResource(R.drawable.hand_05)
                    img_10.setImageResource(R.drawable.hand_03)
                }
                2 -> {
                    hidePricingData(ll2,ll1,ll3,ll4,ll5,ll6,ll7,ll8,ll9,
                        txtPrice2,txtPrice1,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.hand_13_selected)
                    img_2.setImageResource(R.drawable.hand_12_selected)
                    img_3.setImageResource(R.drawable.hand_11)
                    img_4.setImageResource(R.drawable.hand_10)
                    img_5.setImageResource(R.drawable.hand_09)
                    img_6.setImageResource(R.drawable.hand_08)
                    img_7.setImageResource(R.drawable.hand_07)
                    img_8.setImageResource(R.drawable.hand_06)
                    img_9.setImageResource(R.drawable.hand_05)
                    img_10.setImageResource(R.drawable.hand_03)
                }
                1 -> {
                    hidePricingData(ll1,ll2,ll3,ll4,ll5,ll6,ll7,ll8,ll9,
                        txtPrice1,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.hand_13_selected)
                    img_2.setImageResource(R.drawable.hand_12)
                    img_3.setImageResource(R.drawable.hand_11)
                    img_4.setImageResource(R.drawable.hand_10)
                    img_5.setImageResource(R.drawable.hand_09)
                    img_6.setImageResource(R.drawable.hand_08)
                    img_7.setImageResource(R.drawable.hand_07)
                    img_8.setImageResource(R.drawable.hand_06)
                    img_9.setImageResource(R.drawable.hand_05)
                    img_10.setImageResource(R.drawable.hand_03)
                }
            }
        }else if (Constants.SELECTED_SUB_CATEGORY_NAME.trim() == "Hand front and back" || Constants.SELECTED_SUB_CATEGORY_NAME.trim() == "أمامي وخلفي الساق")  {
            when (clickedPosition) {
                10 -> {
                    hidePricingData(ll9,ll2,ll3,ll4,ll1,ll6,ll7,ll8,ll5,
                        txtPrice9,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice1)
                    img_1.setImageResource(R.drawable.h4_13_selected)
                    img_2.setImageResource(R.drawable.h4_12_selected)
                    img_3.setImageResource(R.drawable.h4_11_selected)
                    img_4.setImageResource(R.drawable.h4_10_selected)
                    img_5.setImageResource(R.drawable.h4_09_selected)
                    img_6.setImageResource(R.drawable.h4_08_selected)
                    img_7.setImageResource(R.drawable.h4_07_selected)
                    img_8.setImageResource(R.drawable.h4_06_selected)
                    img_9.setImageResource(R.drawable.h4_05_selected)
                    img_10.setImageResource(R.drawable.h4_03_selected)
                }
                9 -> {
                    hidePricingData(ll9,ll2,ll3,ll4,ll1,ll6,ll7,ll8,ll5,
                        txtPrice9,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice1)
                    img_1.setImageResource(R.drawable.h4_13_selected)
                    img_2.setImageResource(R.drawable.h4_12_selected)
                    img_3.setImageResource(R.drawable.h4_11_selected)
                    img_4.setImageResource(R.drawable.h4_10_selected)
                    img_5.setImageResource(R.drawable.h4_09_selected)
                    img_6.setImageResource(R.drawable.h4_08_selected)
                    img_7.setImageResource(R.drawable.h4_07_selected)
                    img_8.setImageResource(R.drawable.h4_06_selected)
                    img_9.setImageResource(R.drawable.h4_05_selected)
                    img_10.setImageResource(R.drawable.h4_03)
                }
                8 -> {
                    hidePricingData(ll8,ll2,ll3,ll4,ll1,ll6,ll7,ll5,ll9,
                        txtPrice8,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice1,txtPrice9)
                    img_1.setImageResource(R.drawable.h4_13_selected)
                    img_2.setImageResource(R.drawable.h4_12_selected)
                    img_3.setImageResource(R.drawable.h4_11_selected)
                    img_4.setImageResource(R.drawable.h4_10_selected)
                    img_5.setImageResource(R.drawable.h4_09_selected)
                    img_6.setImageResource(R.drawable.h4_08_selected)
                    img_7.setImageResource(R.drawable.h4_07_selected)
                    img_8.setImageResource(R.drawable.h4_06_selected)
                    img_9.setImageResource(R.drawable.h4_05)
                    img_10.setImageResource(R.drawable.h4_03)
                }
                7 -> {
                    hidePricingData(ll7,ll2,ll3,ll4,ll1,ll6,ll5,ll8,ll9,
                        txtPrice7,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice1,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.h4_13_selected)
                    img_2.setImageResource(R.drawable.h4_12_selected)
                    img_3.setImageResource(R.drawable.h4_11_selected)
                    img_4.setImageResource(R.drawable.h4_10_selected)
                    img_5.setImageResource(R.drawable.h4_09_selected)
                    img_6.setImageResource(R.drawable.h4_08_selected)
                    img_7.setImageResource(R.drawable.h4_07_selected)
                    img_8.setImageResource(R.drawable.h4_06)
                    img_9.setImageResource(R.drawable.h4_05)
                    img_10.setImageResource(R.drawable.h4_03)
                }
                6 -> {
                    hidePricingData(ll6,ll2,ll3,ll4,ll5,ll1,ll7,ll8,ll9,
                        txtPrice6,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice1,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.h4_13_selected)
                    img_2.setImageResource(R.drawable.h4_12_selected)
                    img_3.setImageResource(R.drawable.h4_11_selected)
                    img_4.setImageResource(R.drawable.h4_10_selected)
                    img_5.setImageResource(R.drawable.h4_09_selected)
                    img_6.setImageResource(R.drawable.h4_08_selected)
                    img_7.setImageResource(R.drawable.h4_07)
                    img_8.setImageResource(R.drawable.h4_06)
                    img_9.setImageResource(R.drawable.h4_05)
                    img_10.setImageResource(R.drawable.h4_03)
                }
                5 -> {
                    hidePricingData(ll5,ll2,ll3,ll4,ll1,ll6,ll7,ll8,ll9,
                        txtPrice5,txtPrice2,txtPrice3,txtPrice4,txtPrice1,txtPrice6,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.h4_13_selected)
                    img_2.setImageResource(R.drawable.h4_12_selected)
                    img_3.setImageResource(R.drawable.h4_11_selected)
                    img_4.setImageResource(R.drawable.h4_10_selected)
                    img_5.setImageResource(R.drawable.h4_09_selected)
                    img_6.setImageResource(R.drawable.h4_08)
                    img_7.setImageResource(R.drawable.h4_07)
                    img_8.setImageResource(R.drawable.h4_06)
                    img_9.setImageResource(R.drawable.h4_05)
                    img_10.setImageResource(R.drawable.h4_03)
                }
                4 -> {
                    hidePricingData(ll4,ll2,ll3,ll1,ll5,ll6,ll7,ll8,ll9,
                        txtPrice4,txtPrice2,txtPrice3,txtPrice1,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.h4_13_selected)
                    img_2.setImageResource(R.drawable.h4_12_selected)
                    img_3.setImageResource(R.drawable.h4_11_selected)
                    img_4.setImageResource(R.drawable.h4_10_selected)
                    img_5.setImageResource(R.drawable.h4_09)
                    img_6.setImageResource(R.drawable.h4_08)
                    img_7.setImageResource(R.drawable.h4_07)
                    img_8.setImageResource(R.drawable.h4_06)
                    img_9.setImageResource(R.drawable.h4_05)
                    img_10.setImageResource(R.drawable.h4_03)
                }
                3 -> {
                    hidePricingData(ll3,ll2,ll1,ll4,ll5,ll6,ll7,ll8,ll9,
                        txtPrice3,txtPrice2,txtPrice1,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.h4_13_selected)
                    img_2.setImageResource(R.drawable.h4_12_selected)
                    img_3.setImageResource(R.drawable.h4_11_selected)
                    img_4.setImageResource(R.drawable.h4_10)
                    img_5.setImageResource(R.drawable.h4_09)
                    img_6.setImageResource(R.drawable.h4_08)
                    img_7.setImageResource(R.drawable.h4_07)
                    img_8.setImageResource(R.drawable.h4_06)
                    img_9.setImageResource(R.drawable.h4_05)
                    img_10.setImageResource(R.drawable.h4_03)
                }
                2 -> {
                    hidePricingData(ll2,ll1,ll3,ll4,ll5,ll6,ll7,ll8,ll9,
                        txtPrice2,txtPrice1,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.h4_13_selected)
                    img_2.setImageResource(R.drawable.h4_12_selected)
                    img_3.setImageResource(R.drawable.h4_11)
                    img_4.setImageResource(R.drawable.h4_10)
                    img_5.setImageResource(R.drawable.h4_09)
                    img_6.setImageResource(R.drawable.h4_08)
                    img_7.setImageResource(R.drawable.h4_07)
                    img_8.setImageResource(R.drawable.h4_06)
                    img_9.setImageResource(R.drawable.h4_05)
                    img_10.setImageResource(R.drawable.h4_03)
                }
                1 -> {
                    hidePricingData(ll1,ll2,ll3,ll4,ll5,ll6,ll7,ll8,ll9,
                        txtPrice1,txtPrice2,txtPrice3,txtPrice4,txtPrice5,txtPrice6,txtPrice7,txtPrice8,txtPrice9)
                    img_1.setImageResource(R.drawable.h4_13_selected)
                    img_2.setImageResource(R.drawable.h4_12)
                    img_3.setImageResource(R.drawable.h4_11)
                    img_4.setImageResource(R.drawable.h4_10)
                    img_5.setImageResource(R.drawable.h4_09)
                    img_6.setImageResource(R.drawable.h4_08)
                    img_7.setImageResource(R.drawable.h4_07)
                    img_8.setImageResource(R.drawable.h4_06)
                    img_9.setImageResource(R.drawable.h4_05)
                    img_10.setImageResource(R.drawable.h4_03)
                }
            }
        }
    }

    private fun hidePricingData(ll1: LinearLayout, ll2: LinearLayout, ll3: LinearLayout, ll4: LinearLayout, ll5: LinearLayout, ll6: LinearLayout, ll7: LinearLayout, ll8: LinearLayout, ll9: LinearLayout,
                                txt1: TextView, txt2: TextView, txt3: TextView, txt4: TextView, txt5: TextView, txt6: TextView, txt7: TextView, txt8: TextView, txt9: TextView
    ){
        ll1.setBackgroundResource(R.drawable.rounded_gradient_bg)
        ll2.background = null
        ll3.background = null
        ll4.background = null
        ll5.background = null
        ll6.background = null
        ll7.background = null
        ll8.background = null
        ll9.background = null

        txt1.visibility = View.VISIBLE
        if(selectedHeena != null) {
            txt1.text = "${getString(R.string.currency_value)} ${selectedHeena?.price}"
            txt_total.text = "${getString(R.string.currency_value)} ${selectedHeena?.price}"
        } else {
            txt1.text = ""
            txt_total.text = ""
        }
        txt2.visibility = View.GONE
        txt3.visibility = View.GONE
        txt4.visibility = View.GONE
        txt5.visibility = View.GONE
        txt6.visibility = View.GONE
        txt7.visibility = View.GONE
        txt8.visibility = View.GONE
        txt9.visibility = View.GONE
    }

    private fun changeDataAccordingToCategory() {
        if (Constants.SELECTED_SUB_CATEGORY_NAME.trim() == "Leg front" || Constants.SELECTED_SUB_CATEGORY_NAME.trim() == "الجبهة الساق") {
            img_1.setImageResource(R.drawable.leg_1)
            img_2.setImageResource(R.drawable.leg_2)
            img_3.setImageResource(R.drawable.leg_3)
            img_4.setImageResource(R.drawable.leg_4)
            img_5.setImageResource(R.drawable.leg_5)
            img_6.setImageResource(R.drawable.leg_6)
            img_7.setImageResource(R.drawable.leg_7)
            img_8.setImageResource(R.drawable.leg_8)
            img_9.setImageResource(R.drawable.leg_9)
            img_10.setImageResource(R.drawable.leg_10)
        } else if (Constants.SELECTED_SUB_CATEGORY_NAME.trim() == "Leg front and back" || Constants.SELECTED_SUB_CATEGORY_NAME.trim() == "اليد الأمامية والخلفية") {
            img_1.setImageResource(R.drawable.leg_01_10)
            img_2.setImageResource(R.drawable.leg_01_09)
            img_3.setImageResource(R.drawable.leg_01_08)
            img_4.setImageResource(R.drawable.leg_01_07)
            img_5.setImageResource(R.drawable.leg_01_06)
            img_6.setImageResource(R.drawable.leg_01_05)
            img_7.setImageResource(R.drawable.leg_01_04)
            img_8.setImageResource(R.drawable.leg_01_03)
            img_9.setImageResource(R.drawable.leg_01_02)
            img_10.setImageResource(R.drawable.leg_01_01)
        } else if((Constants.SELECTED_SUB_CATEGORY_NAME.trim() == "Hand front") || Constants.SELECTED_SUB_CATEGORY_NAME.trim() == "اليد الأمامية" ) {
            img_1.setImageResource(R.drawable.hand_13)
            img_2.setImageResource(R.drawable.hand_12)
            img_3.setImageResource(R.drawable.hand_11)
            img_4.setImageResource(R.drawable.hand_10)
            img_5.setImageResource(R.drawable.hand_09)
            img_6.setImageResource(R.drawable.hand_08)
            img_7.setImageResource(R.drawable.hand_07)
            img_8.setImageResource(R.drawable.hand_06)
            img_9.setImageResource(R.drawable.hand_05)
            img_10.setImageResource(R.drawable.hand_03)
        } else if((Constants.SELECTED_SUB_CATEGORY_NAME.trim() == "Hand front and back") || Constants.SELECTED_SUB_CATEGORY_NAME.trim() == "أمامي وخلفي الساق" ) {
            img_1.setImageResource(R.drawable.h4_13)
            img_2.setImageResource(R.drawable.h4_12)
            img_3.setImageResource(R.drawable.h4_11)
            img_4.setImageResource(R.drawable.h4_10)
            img_5.setImageResource(R.drawable.h4_09)
            img_6.setImageResource(R.drawable.h4_08)
            img_7.setImageResource(R.drawable.h4_07)
            img_8.setImageResource(R.drawable.h4_06)
            img_9.setImageResource(R.drawable.h4_05)
            img_10.setImageResource(R.drawable.h4_03)
        }
    }

    private fun attachObservers() {
        viewModel.heenaCategoryDetailsResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == true) {
                if(it.heenaSection.isNullOrEmpty()){
                    Utils.showSnackBar( it.message?:getString(R.string.please_try_ahain), img_10)
                }else{
                    heenaDetailList = it.heenaSection as ArrayList<HeenaSectionItem> /* = java.util.ArrayList<com.reemastyle.model.heenadetail.HeenaSectionItem> */
                    heenaDetailResponse = it
                }
            } else {
                Utils.showSnackBar( it?.message?:getString(R.string.please_try_ahain), img_10)
            }
        })

        viewModel.apiError.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                Utils.showSnackBar(it, img_back)
                try {
                    Utils.showLoading(false, requireActivity())
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        })

        viewModel.unauthorized.observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), img_10)
                    Utils.logoutUser(requireActivity())
                }
            }
        })

        viewModel.isLoading.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                try {
                    Utils.showLoading(it, requireActivity())
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        })
    }
}
