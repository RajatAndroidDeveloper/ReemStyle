package com.reemastyle.address

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.JsonObject
import com.reemastyle.HomeActivity
import com.reemastyle.R
import com.reemastyle.model.zones.ZonesItem
import com.reemastyle.util.Constants
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_address.*


class AddressFragment : Fragment() {
    lateinit var viewModel: AddressViewModel
    private var fusedLocationClient: FusedLocationProviderClient? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        findCurrentLocation()

        clickListeners()
        viewModel = ViewModelProviders.of(this).get(AddressViewModel::class.java)
        attachObservers()

        var jsonObject = JsonObject()
        jsonObject.addProperty("action", "getzones")
        viewModel?.getAllZones(jsonObject)

        if(Constants.PLACE_DATA != null){
            et_street.setText(Constants.PLACE_DATA?.name?:"")
            latitude = Constants.PLACE_DATA?.latLng?.latitude?:0.0
            longitude = Constants.PLACE_DATA?.latLng?.longitude?:0.0
        }else if(Constants.ADDRESS != ""){
            et_street.setText(Constants.ADDRESS)
            latitude = Constants.LATITUDE
            longitude = Constants.LONGITUDE
        }
    }

    private fun clickListeners() {
        btn_save.setOnClickListener {
            if (selectedZone.isNullOrEmpty()) {
                Utils.showSnackBar(getString(R.string.please_add_zone), et_zones)
                return@setOnClickListener
            }
            if (et_street.text.toString().trim().isNullOrEmpty()) {
                Utils.showSnackBar(getString(R.string.please_add_street), et_street)
                return@setOnClickListener
            }
            if (et_building.text.toString().trim().isNullOrEmpty()) {
                Utils.showSnackBar(getString(R.string.please_add_building), et_building)
                return@setOnClickListener
            }
            if (et_mobile.text.toString().trim().isNullOrEmpty()) {
                Utils.showSnackBar(getString(R.string.please_add_mobile), et_mobile)
                return@setOnClickListener
            }
            viewModel?.addAddress(createAddAddressRequest())
            //findNavController().navigate(R.id.action_addressFragment_to_cartFragment)
        }
    }

    private var selectedZone = ""
//    var addressTypeList = arrayOf("Home", "Office")
//    private var selectedAddressType = ""
//    private fun createAddressTypeSpinner() {
//        val aa =
//            ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, addressTypeList)
//        aa.setDropDownViewResource(R.layout.custom_spinner_layout)
//        et_addressType!!.adapter = aa
//
//        et_addressType.onItemSelectedListener = this
//    }

//    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
//        selectedAddressType = addressTypeList[position]
//    }
//
//    override fun onNothingSelected(arg0: AdapterView<*>) {
//
//    }

    private fun createAddAddressRequest(): JsonObject {
        var jsonObject = JsonObject()
        jsonObject.addProperty("action", "add_address")
        jsonObject.addProperty("userid", Utils.getUserData()?.id)
        jsonObject.addProperty("zone", selectedZone)
        jsonObject.addProperty("street", et_street.text.toString().trim())
        jsonObject.addProperty("building", et_building.text.toString().trim())
        jsonObject.addProperty("floor", et_flooer.text.toString().trim())
        jsonObject.addProperty("flat", et_villaNumber.text.toString().trim())
        jsonObject.addProperty("mobile", et_mobile.text.toString().trim())
        jsonObject.addProperty("type", et_addressType.text.toString().trim())
        jsonObject.addProperty("landline", et_landline_number.text.toString().trim())
        jsonObject.addProperty("instructions", et_instructions.text.toString().trim())
        jsonObject.addProperty("latitude", latitude)
        jsonObject.addProperty("longitude", longitude)
        return jsonObject
    }

    override fun onResume() {
        super.onResume()

        if ((requireActivity() as HomeActivity).getForegroundFragment() is AddressFragment) {
            (requireActivity() as HomeActivity).toolbar.visibility = View.VISIBLE
            (requireActivity() as HomeActivity).bottom_menu.visibility = View.GONE
            (requireActivity() as HomeActivity).et_search.visibility = View.GONE
        }
    }

    private fun findCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful && task.result != null) {
                var lastLocation = task.result
                latitude = lastLocation.latitude
                longitude = lastLocation.longitude
            } else {
                Log.w("Select_Location_frag", "getLastLocation:exception", task.exception)
            }
        }
    }

    private var zonesList: ArrayList<ZonesItem> = ArrayList<ZonesItem>()
    private fun attachObservers() {
        viewModel.addressResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == false) {
                Utils.showSnackBar(it?.message ?:getString(R.string.please_try_ahain), btn_save)
            } else {
                Utils.showSnackBar(it?.message ?: "", btn_save)
                Constants.COMING_FROM = "address_added"
                (requireActivity() as HomeActivity).onBackPressed()
            }
        })
        viewModel.zonesResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == false) {
                Utils.showSnackBar(it?.message ?:getString(R.string.please_try_ahain), btn_save)
            } else {
                if (!it?.zones.isNullOrEmpty()) {
                    zonesList.clear()
                    zonesList = it?.zones as ArrayList<ZonesItem>
                    setUpZonestSpinner(zonesList)
                }
            }
        })

        viewModel.apiError.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                Utils.showSnackBar(it, btn_save)
                Utils.showLoading(false, requireActivity())
            }
        })

        viewModel.unauthorized.observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), btn_save)
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

    private fun setUpZonestSpinner(zonesList: java.util.ArrayList<ZonesItem>) {
        val aa = ArrayAdapter(requireActivity(), R.layout.custom_spinner_layout, zonesList)
        aa.setDropDownViewResource(R.layout.custom_spinner_layout)
        et_zones.adapter = aa
        et_zones.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedZone = zonesList[position].id ?: ""
                Log.e("selectedZone" , selectedZone)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddressFragment()
    }
}