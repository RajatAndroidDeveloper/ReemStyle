package com.reemastyle

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonObject
import com.reemastyle.bookings.BookingsFragment
import com.reemastyle.cart.CartFragment
import com.reemastyle.home.HomeFragment
import com.reemastyle.home.HomeViewModel
import com.reemastyle.packages.PackageDetailsFragment
import com.reemastyle.profile.ProfileFragment
import com.reemastyle.service.ServiceDetailFragment
import com.reemastyle.service.SubCategoryFragment
import com.reemastyle.util.Constants
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.login_dialog_layout.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.util.*

@RuntimePermissions
class HomeActivity : AppCompatActivity() {
    private var navController: NavController? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navController = findNavController(R.id.nav_host_fragment)
        bottom_menu.setupWithNavController(navController!!);

        if(Utils.getUserData()?.profImage?.isNullOrEmpty() == false){
            Log.e("xsddddsdsdsd",Utils.getUserData()?.profImage?:"wdd")
            Glide.with(this).load(Utils.getUserData()?.profImage).placeholder(R.drawable.ic_dummy_profile).into(img_profile)
        }
        onClickListeners()
        //updateMapWithLocationWithPermissionCheck()

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        attachObservers()

       et_search.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
           if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
               var jsonObject = JsonObject()
               jsonObject.addProperty("action","searchbycat")
               jsonObject.addProperty("search_key",et_search.text.toString().trim())

               if(et_search.text.toString().trim().isNotBlank()){
                   viewModel?.searchCategory(jsonObject)
               }
           }
           false
       })
    }

    private fun onClickListeners() {
        img_menu.setOnClickListener {
            toolbar.visibility = View.GONE
            bottom_menu.visibility = View.GONE
            navController?.navigate(R.id.sideMenuFragment)
        }

        img_profile.setOnClickListener {
            if (Utils.getUserData() == null) {
                showLoginDialog(this)
            } else {
                toolbar.visibility = View.GONE
                bottom_menu.visibility = View.GONE
                navController?.navigate(R.id.profileFragment)
            }
        }

        img_cart.setOnClickListener {
           bottom_menu.visibility = View.GONE
            navController?.navigate(R.id.cartFragment)
        }

        bottom_menu.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }


    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    navController?.navigate(R.id.homeFragment2)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_book_now -> {
                    bottom_menu.visibility = View.GONE
                    navController?.navigate(R.id.packagesFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_bookings -> {
                    if(Utils.getUserData() == null){
                        showLoginDialog(this)
                    }else {
                        navController?.navigate(R.id.bookingsFragment)
                    }
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    toolbar.visibility = View.GONE
                    bottom_menu.visibility = View.GONE
                    if(Utils.getUserData() == null){
                        showLoginDialog(this)
                    }else {
                        navController?.navigate(R.id.profileFragment)
                    }
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
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
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        if (!loginDialog.isShowing) {
            loginDialog.show()
        }
    }

    fun updateProfilePicture(){
        if(Utils.getUserData()?.profImage?.isNullOrEmpty() == false){
            Log.e("xsddddsdsdsd",Utils.getUserData()?.profImage?:"wdd")
            Glide.with(this).load(Utils.getUserData()?.profImage).placeholder(R.drawable.ic_dummy_profile).into(img_profile)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if(getForegroundFragment() is HomeFragment || getForegroundFragment() is SubCategoryFragment){
            toolbar.visibility  = View.VISIBLE
            bottom_menu.visibility = View.VISIBLE
            et_search.visibility = View.VISIBLE
        }
        if(getForegroundFragment() is BookingsFragment){
            toolbar.visibility  = View.VISIBLE
            bottom_menu.visibility = View.VISIBLE
            et_search.visibility = View.GONE
        }
        if(getForegroundFragment() is CartFragment){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        if(getForegroundFragment() is ServiceDetailFragment){
            toolbar.visibility  = View.VISIBLE
            bottom_menu.visibility = View.VISIBLE
            et_search.visibility = View.GONE
        }
        if(getForegroundFragment() is ProfileFragment){
            toolbar.visibility  = View.GONE
            bottom_menu.visibility = View.GONE
        }
    }

    fun getForegroundFragment(): Fragment? {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        return navHostFragment?.childFragmentManager?.fragments?.get(0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var fragment = getForegroundFragment()
        fragment?.onActivityResult(requestCode,resultCode,data)
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    fun updateMapWithLocation(){
        findCurrentLocation()
    }

    private fun findCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
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
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                var lastLocation = task.result
                getCurrentAddress(lastLocation?.latitude?:0.0,lastLocation?.longitude?:0.0)
            } else {
                Log.w("Select_Location_frag", "getLastLocation:exception", task.exception)
            }
        }
    }


    private fun getCurrentAddress(latitude: Double?, longitude: Double?): String {
        var address = ""
        try {
            var geocoder = Geocoder(this, Locale.getDefault())
            var addresses: List<Address> = geocoder.getFromLocation(latitude!!, longitude!!, 1)!!
            address = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            txt_location.text  = address
            Log.e("asasasas","address$address")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return address
    }

    private fun attachObservers() {
        viewModel.searchCategoryResponse.observe(this, androidx.lifecycle.Observer {
            Utils.showLoading(false, this)
            if (it.status == false) {
                Utils.showSnackBar(getString(R.string.please_try_ahain), et_search)
            } else {
                if(!it?.searchData.isNullOrEmpty()) {
                    Constants.SELECTED_CATEGORY = it?.searchData!![0]!!.id!!
                    Constants.SELECTED_CATEGORY_IMAGE = it?.searchData!![0]!!.image!!
                    Constants.SELECTED_CATEGORY_NAME = it?.searchData!![0]!!.catName!!
                    navController?.navigate(R.id.action_homeFragment2_to_subcategoryFragment)
                }
            }
        })

        viewModel.apiError.observe(this, androidx.lifecycle.Observer {
            it?.let {
                Utils.showSnackBar(it, et_search)
                Utils.showLoading(false, this)
            }
        })

        viewModel.unauthorized.observe(this, androidx.lifecycle.Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), et_search)
                    Utils.logoutUser(this)
                }
            }
        })

        viewModel.isLoading.observe(this, androidx.lifecycle.Observer {
            it?.let {
                try {
                    Utils.showLoading(it, this)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

}


