package com.reemastyle.address

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.reemastyle.HomeActivity
import com.reemastyle.R
import com.reemastyle.util.Constants
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_select_location.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.util.*


@RuntimePermissions
class SelectLocationFragment : Fragment(), OnMapReadyCallback {
    var mapFragment: SupportMapFragment? = null
    var googleMapHome: GoogleMap? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val AUTOCOMPLETE_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as HomeActivity).toolbar.visibility = View.VISIBLE
        (requireActivity() as HomeActivity).et_search.isClickable = true
        (requireActivity() as HomeActivity).et_search.isFocusable = false
        (requireActivity() as HomeActivity).et_search.isFocusableInTouchMode = false
        if (!Places.isInitialized()) {
            Places.initialize(requireActivity(), Constants.MAP_API_KEY, Locale.US);
        }
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        (requireActivity() as HomeActivity).et_search.setText(getString(R.string.search_for_location))

        updateMapWithLocationWithPermissionCheck()

        try {
            findCurrentLocation()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        btn_confirm_location.setOnClickListener {
            (requireActivity() as HomeActivity).toolbar.visibility = View.VISIBLE
            (requireActivity() as HomeActivity).et_search.visibility = View.GONE
            (requireActivity() as HomeActivity).bottom_menu.visibility = View.GONE
            if (placeData != null) {
                Constants.PLACE_DATA = placeData
            }
            Constants.LATITUDE = latitude
            Constants.LONGITUDE = longitude
            Constants.ADDRESS = txt_your_location.text.toString().trim()
            findNavController().navigate(R.id.action_selectLocationFragment_to_addressFragment)
        }

        (requireActivity() as HomeActivity).et_search.setOnClickListener {
            val fields = listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS_COMPONENTS
            )
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(requireActivity())
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

        ic_current_location.setOnClickListener {
            findCurrentLocation()
        }

        if(Constants.COMING_FROM == "address_added"){
            Constants.COMING_FROM = ""
            (requireActivity() as HomeActivity).onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()

        if ((requireActivity() as HomeActivity).getForegroundFragment() is SelectLocationFragment) {
            (requireActivity() as HomeActivity).toolbar.visibility = View.VISIBLE
            (requireActivity() as HomeActivity).bottom_menu.visibility = View.GONE
            (requireActivity() as HomeActivity).et_search.visibility = View.VISIBLE
        }
    }

    @NeedsPermission(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    fun updateMapWithLocation() {
        findCurrentLocation()
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
        fusedLocationClient?.lastLocation
            ?.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    findFullAddress(location?.latitude, location?.longitude)
                    latitude = location?.latitude
                    longitude = location?.longitude
                    Log.e(
                        "asasasas",
                        location?.latitude.toString() + "    vvvvv    " + location?.longitude.toString()
                    )
                    focusOnCurrentLocation(location?.latitude, location?.longitude)

                    Log.e(
                        "Select_Location_frag",
                        "Current location is ${location.longitude} ${location.latitude}"
                    )
                } else {
                    Log.w("Select_Location_frag", "getLastLocation:exception")
                }
            }
    }

    private fun focusOnCurrentLocation(latitude: Double?, longitude: Double?) {
        val target = LatLng(latitude ?: 0.0, longitude ?: 0.0)
        val builder = CameraPosition.Builder()
        builder.zoom(14f)
        builder.target(target)
        (googleMapHome)?.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()))
//        val markerOptions =
//            MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker))
//                .position(
//                    LatLng(latitude ?: 0.0, longitude ?: 0.0)
//                )
//        markerOptions.visible(true)
//        googleMapHome?.addMarker(markerOptions)
    }

    private fun findFullAddress(latitude: Double?, longitude: Double?) {
        try {
            var geocoder = Geocoder(requireActivity() as HomeActivity, Locale.getDefault())
            var addresses: List<Address> = geocoder.getFromLocation(latitude!!, longitude!!, 1)!!
            val address =
                addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            txt_your_location.text = address
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var placeData: Place? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        placeData = Autocomplete.getPlaceFromIntent(data)
                        txt_your_location.text = placeData?.address
                        Log.i(
                            "Select_loc_frag",
                            "Place: ${placeData?.address}, ${placeData?.name}, ${placeData?.id}"
                        )
                        if (placeData?.latLng != null) {
                            focusOnCurrentLocation(
                                placeData?.latLng?.latitude,
                                placeData?.latLng?.longitude
                            )
                        }
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i("Select_loc_frag", status.statusMessage ?: "")
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private var latitude = 0.0
    private var longitude = 0.0
    override fun onMapReady(p0: GoogleMap) {
        googleMapHome = p0
        googleMapHome?.clear()
        googleMapHome?.uiSettings?.isCompassEnabled = false
        googleMapHome?.setOnCameraIdleListener(OnCameraIdleListener { //get latlng at the center by calling
            val midLatLng: LatLng = googleMapHome?.cameraPosition?.target!!
            googleMapHome?.clear()
            if(midLatLng != null){
//                val markerOptions =
//                    MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker))
//                        .position(
//                            LatLng(midLatLng.latitude ?: 0.0, midLatLng.longitude ?: 0.0)
//                        )
//                markerOptions.visible(true)
//                googleMapHome?.addMarker(markerOptions)

                latitude = midLatLng.latitude
                longitude = midLatLng.longitude
                findFullAddress(midLatLng.latitude,midLatLng.longitude)
            }
            Log.e("asasasasasa","hasahjhasas ${midLatLng.latitude}    ${midLatLng.longitude}")
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = SelectLocationFragment()
    }
}
