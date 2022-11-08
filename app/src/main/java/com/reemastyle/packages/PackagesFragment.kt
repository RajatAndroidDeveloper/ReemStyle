package com.reemastyle.packages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.reemastyle.HomeActivity
import com.reemastyle.R
import com.reemastyle.model.packages.PackagesItem
import com.reemastyle.preferences.Preferences
import com.reemastyle.util.Constants
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_packages.*

class PackagesFragment : Fragment(), PackageItemClicked {

    private lateinit var packagesAdapter: PackagesAdapter
    lateinit var viewModel: PackageViewModel
    private var packageList: ArrayList<PackagesItem> = ArrayList<PackagesItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_packages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as HomeActivity).toolbar.visibility = View.VISIBLE
        (requireActivity() as HomeActivity).bottom_menu.visibility = View.VISIBLE
        setUpPackageAdapter(packageList)


        viewModel = ViewModelProviders.of(this).get(PackageViewModel::class.java)
        attachObservers()
        var jsonObject = JsonObject()
        jsonObject.addProperty("action","packageslist")
        if(Preferences.prefs?.getString("Language","en") == "ar"){
            jsonObject.addProperty("lang","AR")
        }else{
            jsonObject.addProperty("lang","EN")
        }
        viewModel?.getPackageData(jsonObject)
    }

    private fun attachObservers() {
        viewModel.packageResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == false) {
                Utils.showSnackBar(it.message?:getString(R.string.please_try_ahain), rv_packages)
            } else {
                if(it?.packages?.isNullOrEmpty() == false){
                    packageList =  it?.packages  as ArrayList<PackagesItem> /* = java.util.ArrayList<com.reemastyle.model.packages.PackagesItem> */
                    setUpPackageAdapter(packageList)
                }
            }
        })

        viewModel.apiError.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                Utils.showSnackBar(it, rv_packages)
                Utils.showLoading(false, requireActivity())
            }
        })

        viewModel.unauthorized.observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), rv_packages)
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
    }

    private fun setUpPackageAdapter(packagesList: ArrayList<PackagesItem>) {
        var layoutManager = LinearLayoutManager(requireActivity())
        rv_packages.layoutManager = layoutManager
        packagesAdapter = PackagesAdapter(requireActivity(),this,packagesList)
        rv_packages.adapter = packagesAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = PackagesFragment()
    }

    override fun onPackageItemClicked(position: Int) {
        Constants.SELECTED_PACKAGE_ID = packageList[position].id?:"0"
        findNavController().navigate(R.id.action_packagesFragment_to_packageDetailsFragment)
    }
}