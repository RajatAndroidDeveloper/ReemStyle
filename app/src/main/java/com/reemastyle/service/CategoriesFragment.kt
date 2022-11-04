package com.reemastyle.service

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import com.reemastyle.HomeActivity
import com.reemastyle.R
import com.reemastyle.model.categories.AllCategoriesItem
import com.reemastyle.preferences.Preferences
import com.reemastyle.util.Constants.SELECTED_CATEGORY
import com.reemastyle.util.Constants.SELECTED_CATEGORY_IMAGE
import com.reemastyle.util.Constants.SELECTED_CATEGORY_NAME
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_categories.*


class CategoriesFragment : Fragment(), CategoryItemClicked {
    private var categoryList: ArrayList<AllCategoriesItem> = ArrayList<AllCategoriesItem>()
    private lateinit var categoriesAdapter: AllCategoryAdapter
    lateinit var viewModel: ServiceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpCategoryAdapter(categoryList)

        viewModel = ViewModelProviders.of(this).get(ServiceViewModel::class.java)
        attachObservers()
        var jsonObject = JsonObject()
        jsonObject.addProperty("action","categories")
        if(Preferences.prefs?.getString("Language","en") == "ar"){
            jsonObject.addProperty("lang","AR")
        }else{
            jsonObject.addProperty("lang","EN")
        }
        viewModel.getCategories(jsonObject)

        (requireActivity() as HomeActivity).et_search.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s != "") {
                   searchCategory(s.toString())
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun setUpCategoryAdapter(categoriesList: ArrayList<AllCategoriesItem>){
        var layoutManager = GridLayoutManager(requireActivity(),4)
        categoriesAdapter = AllCategoryAdapter(requireActivity(), this, categoriesList, "categories")
        rv_categories.layoutManager = layoutManager
        rv_categories.adapter = categoriesAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = CategoriesFragment()
    }

    override fun onCategoryClicked(position: Int) {
        SELECTED_CATEGORY = categoryList[position].id?:""
        SELECTED_CATEGORY_IMAGE = categoryList[position].image?:""
        SELECTED_CATEGORY_NAME = categoryList[position].catName?:""
        findNavController().navigate(R.id.action_categoriesFragment_to_subcategoryFragment)
    }

    private fun attachObservers() {
        viewModel.categoriesResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if(it.status == false){
                Utils.showSnackBar(getString(R.string.please_try_ahain),rv_categories)
            }else {
                if(it?.allCategories?.isNullOrEmpty() == false){
                    categoryList = it?.allCategories as ArrayList<AllCategoriesItem> /* = java.util.ArrayList<com.reemastyle.model.categories.AllCategoriesItem> */
                    setUpCategoryAdapter(categoryList)
                }
            }
        })

        viewModel.apiError.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                Utils.showSnackBar(it, rv_categories)
                Utils.showLoading(false, requireActivity())
            }
        })

        viewModel.unauthorized.observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), rv_categories)
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

    private fun searchCategory(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<AllCategoriesItem> = ArrayList()

        // running a for loop to compare elements.
        for (item in categoryList) {
            // checking if the entered string matched with any item of our recycler view.
            if ((item.catName?:"").toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
           Utils.showSnackBar(getString(R.string.no_category_found),rv_categories)
        } else {
            categoriesAdapter.filterList(filteredlist)
        }
    }
}