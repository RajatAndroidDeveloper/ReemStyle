package com.reemastyle.service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.reemastyle.HomeActivity
import com.reemastyle.R
import com.reemastyle.heena.HeenaAdapter
import com.reemastyle.heena.HeenaItemClicked
import com.reemastyle.model.heenacategories.ResultItem
import com.reemastyle.model.subcategory.PackagesItem
import com.reemastyle.preferences.Preferences
import com.reemastyle.util.Constants
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_sub_category.*

class SubCategoryFragment : Fragment(), SubCategoryItemClicked, HeenaItemClicked {
    lateinit var viewModel: ServiceViewModel
    private lateinit var subCategoriesAdapter: SubCategoriesAdapter
    private var subCategoryList: ArrayList<PackagesItem> = ArrayList<PackagesItem>()
    private var heenaList: ArrayList<ResultItem> = ArrayList<ResultItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sub_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSubCategoryAdapter(subCategoryList)

        viewModel = ViewModelProviders.of(this).get(ServiceViewModel::class.java)
        attachObservers()

        txt_sub_category.text = Constants.SELECTED_CATEGORY_NAME
        if (!Constants.SELECTED_CATEGORY_IMAGE.isNullOrEmpty())
            Glide.with(requireActivity()).load(Constants.SELECTED_CATEGORY_IMAGE).into(img_category)

        var jsonObject = JsonObject()
        if (Constants.SELECTED_CATEGORY_NAME == "Heena" || Constants.SELECTED_CATEGORY_NAME == "هينا") {
            jsonObject.addProperty("action", "get_heena_catonly")
            if(Preferences.prefs?.getString("Language","en") == "ar"){
                jsonObject.addProperty("lang","AR")
            }else{
                jsonObject.addProperty("lang","EN")
            }
            viewModel.getHeenaCategories(jsonObject)
        } else {
            jsonObject.addProperty("action", "subcategories")
            if(Preferences.prefs?.getString("Language","en") == "ar"){
                jsonObject.addProperty("lang","AR")
            }else{
                jsonObject.addProperty("lang","EN")
            }
            jsonObject.addProperty("catID", Constants.SELECTED_CATEGORY.toInt())
            viewModel.getSubCategories(jsonObject)
        }
    }

    private fun setUpSubCategoryAdapter(subCategoryList: ArrayList<PackagesItem>) {
        var layoutManager = LinearLayoutManager(requireActivity())
        subCategoriesAdapter = SubCategoriesAdapter(requireActivity(), this, subCategoryList)
        rv_subCategories.layoutManager = layoutManager
        rv_subCategories.adapter = subCategoriesAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = SubCategoryFragment()
    }

    override fun onResume() {
        super.onResume()
        if ((requireActivity() as HomeActivity).getForegroundFragment() is SubCategoryFragment) {
            (requireActivity() as HomeActivity).toolbar.visibility = View.VISIBLE
            (requireActivity() as HomeActivity).bottom_menu.visibility = View.VISIBLE
            (requireActivity() as HomeActivity).et_search.visibility = View.VISIBLE
        }
    }

    override fun onSubCategoryClicked(position: Int, catePosition: Int) {
        if(subCategoryList[catePosition].servicecount == 0){
            Utils.showSnackBar(getString(R.string.no_service_available),txt_sub_category)
        }else {
            (requireActivity() as HomeActivity).toolbar.visibility = View.VISIBLE
            (requireActivity() as HomeActivity).bottom_menu.visibility = View.GONE
            (requireActivity() as HomeActivity).et_search.visibility = View.GONE
            Constants.SELECTED_SUB_CATEGORY = position
            findNavController().navigate(R.id.action_subcategoryFragment_to_serviceDetailFragment)
        }
    }

    private fun attachObservers() {
        viewModel.subCategoriesResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == false) {
                Utils.showSnackBar(it?.message?:getString(R.string.please_try_ahain), rv_subCategories)
            } else {
                if (it?.packages?.isNullOrEmpty() == false) {
                    subCategoryList = it?.packages as ArrayList<PackagesItem> /* = java.util.ArrayList<com.reemastyle.model.home.PackagesItem> */
                    setUpSubCategoryAdapter(subCategoryList)
                }
            }
        })

        viewModel.heenaCategoriesResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == false) {
                Utils.showSnackBar(it?.message?:getString(R.string.please_try_ahain), rv_subCategories)
            } else {
                txt_sub_category.text = Constants.SELECTED_CATEGORY_NAME
                if (!Constants.SELECTED_CATEGORY_IMAGE.isNullOrEmpty())
                    Glide.with(requireActivity()).load(Constants.SELECTED_CATEGORY_IMAGE).into(img_category)

                if(it?.result?.isNullOrEmpty() == false){
                    heenaList = it.result as ArrayList<ResultItem> /* = java.util.ArrayList<com.reemastyle.model.heenacategories.ResultItem> */
                    setUpHeenaAdapter(heenaList)
                }
            }
        })

        viewModel.apiError.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                Utils.showSnackBar(it, rv_subCategories)
                Utils.showLoading(false, requireActivity())
            }
        })

        viewModel.unauthorized.observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), rv_subCategories)
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

    lateinit var heenaAdapter : HeenaAdapter
    private fun setUpHeenaAdapter(heenaList: java.util.ArrayList<ResultItem>) {
        var layoutManager = LinearLayoutManager(requireActivity())
        heenaAdapter = HeenaAdapter(requireActivity(), this, heenaList)
        rv_subCategories.layoutManager = layoutManager
        rv_subCategories.adapter = heenaAdapter
    }

    override fun onHeenaItemClicked(position: Int) {
        (requireActivity() as HomeActivity).toolbar.visibility = View.VISIBLE
        (requireActivity() as HomeActivity).bottom_menu.visibility = View.GONE
        (requireActivity() as HomeActivity).et_search.visibility = View.GONE
        Constants.SELECTED_SUB_CATEGORY = (heenaList[position].id?:"0").toInt()
        Constants.SELECTED_SUB_CATEGORY_NAME = heenaList[position].name?:""
        if((heenaList[position].name?:"").trim() == "Leg front" || (heenaList[position].name?:"").trim() == "الجبهة الساق"){
            findNavController().navigate(R.id.action_subcategoryFragment_to_heenaLegFrontFragment)
        } else if((heenaList[position].name?:"").trim() == "Leg front and back" || (heenaList[position].name?:"").trim() == "اليد الأمامية والخلفية" ){
            findNavController().navigate(R.id.action_subcategoryFragment_to_heenaLegFrontBackFragment)
        }  else if((heenaList[position].name?:"").trim() == "Hand front and back" || (heenaList[position].name?:"").trim() == "أمامي وخلفي الساق"){
            findNavController().navigate(R.id.action_subcategoryFragment_to_heenaHandFrontBackFragment)
        } else {
            findNavController().navigate(R.id.action_subcategoryFragment_to_heenaDetailFragment)
        }
    }
}