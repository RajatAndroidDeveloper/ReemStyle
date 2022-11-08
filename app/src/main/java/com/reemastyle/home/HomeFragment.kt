package com.reemastyle.home

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.reemastyle.HomeActivity
import com.reemastyle.R
import com.reemastyle.model.CategoryModel
import com.reemastyle.model.home.*
import com.reemastyle.preferences.Preferences
import com.reemastyle.util.Constants
import com.reemastyle.util.Constants.SELECTED_CATEGORY_IMAGE
import com.reemastyle.util.Constants.SELECTED_CATEGORY_NAME
import com.reemastyle.util.Utils
import com.reemastyle.util.ZoomOutPageTransformer
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_packages.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.lang.Math.abs
import java.util.*

@RuntimePermissions
class HomeFragment : Fragment(), CategoryItemClicked, PackageItemClicked {
    private var categoryList: ArrayList<CategoryModel> = ArrayList<CategoryModel>()
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var offerPagerAdapter: OfferPagerAdapter
    private lateinit var trendingViewPagerAdapter: TrendingPagerAdapter
    private lateinit var testimonalsAdapter: TestimonalAdapter
    private var hasNextPage: Boolean = true
    lateinit var viewModel: HomeViewModel
    private var offers: ArrayList<OffersItem> = ArrayList<OffersItem>()
    private var categoriesList: ArrayList<CategoriesItem> = ArrayList<CategoriesItem>()
    private var packageList: ArrayList<PackagesItem> = ArrayList<PackagesItem>()
    private var testimonialsList: ArrayList<TestimonialsItem> = ArrayList<TestimonialsItem>()
    private var galleryList: ArrayList<GalleryItem> = ArrayList<GalleryItem>()
    private lateinit var galleryAdapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewPager(offers)
        createCategoryList()
        setUpTrendingViewPager(packageList)
        setUpTestimonalsAdapter(testimonialsList)
        setUpCategoryAdapter(categoriesList)
        setUpGalleryAdapter(galleryList)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        attachObservers()

        viewModel?.getHomeData(createHomeDataRequest())
        viewModel?.getAllAddress(createGetAddressRequest())

        txt_view_more.setOnClickListener {
            (requireActivity() as HomeActivity).et_search.visibility = View.GONE
            findNavController().navigate(R.id.action_homeFragment2_to_galleryFragment)
        }

        updateMapWithLocationWithPermissionCheck()
    }

    private fun createGetAddressRequest(): JsonObject {
        var jsonObject = JsonObject()
        jsonObject.addProperty("action", "get_address")
        jsonObject.addProperty("userid", Utils.getUserData()?.id)
        if(Preferences.prefs?.getString("Language","en") == "ar"){
            jsonObject.addProperty("lang","AR")
        }else{
            jsonObject.addProperty("lang","EN")
        }
        return jsonObject
    }

    private fun createHomeDataRequest(): JsonObject {
        var jsonObject = JsonObject()
        jsonObject.addProperty("action", "homescreen")
        if(Preferences.prefs?.getString("Language","en") == "ar"){
            jsonObject.addProperty("lang","AR")
        }else{
            jsonObject.addProperty("lang","EN")
        }
        return jsonObject
    }

    private fun createCategoryList() {
        categoryList.clear()
        categoryList.add(
            CategoryModel(
                R.drawable.hair_removal_icon,
                getString(R.string.hair_cut),
                1
            )
        )
        categoryList.add(
            CategoryModel(
                R.drawable.hairdresser_icon,
                getString(R.string.hair_color),
                2
            )
        )
        categoryList.add(CategoryModel(R.drawable.massage_icon, getString(R.string.head_spa), 3))
        categoryList.add(CategoryModel(R.drawable.blow_dry_icon, getString(R.string.blow_dry), 4))
        categoryList.add(
            CategoryModel(
                R.drawable.hair_follicle_moisturising_icon,
                getString(R.string.hair_henna),
                5
            )
        )
        categoryList.add(
            CategoryModel(
                R.drawable.hair_treatment_icon,
                getString(R.string.hair_treatment),
                6
            )
        )
        categoryList.add(CategoryModel(R.drawable.kids_icon, getString(R.string.hair_kids), 7))
        categoryList.add(CategoryModel(R.drawable.more_icon, getString(R.string.more), 8))
    }

    private fun setUpTestimonalsAdapter(testimonialsList: ArrayList<TestimonialsItem>) {
        testimonalsAdapter = TestimonalAdapter(testimonialsList, requireActivity())
        testimonal_slider.adapter = testimonalsAdapter

        val zoomOutPageTransformer = ZoomOutPageTransformer()
        testimonal_slider.setPageTransformer { page, position ->
            zoomOutPageTransformer.transformPage(page, position)
        }

        testimonal_indicator.attachTo(testimonal_slider)
    }

    private fun setUpTrendingViewPager(packageList: ArrayList<PackagesItem>) {
        var layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
        trendingViewPagerAdapter = TrendingPagerAdapter(packageList, requireActivity(),this)
        trending_slider.layoutManager = layoutManager
        trending_slider.adapter = trendingViewPagerAdapter
    }

    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 1000
    val PERIOD_MS: Long = 4000

    @SuppressLint("SuspiciousIndentation")
    private fun setUpViewPager(offers: ArrayList<OffersItem>) {
        offerPagerAdapter = OfferPagerAdapter(offers, requireActivity())
        offer_slider.adapter = offerPagerAdapter

        val zoomOutPageTransformer = ZoomOutPageTransformer()
        offer_slider.setPageTransformer { page, position ->
            zoomOutPageTransformer.transformPage(page, position)
        }

        offer_indicator.attachTo(offer_slider)
        offer_indicator.attachTo(offer_slider)

        try {
            val handler = Handler()
            val update = Runnable {
                if (currentPage == offers.size) {
                    currentPage = 0
                }
                if(offer_slider != null)
                    if(currentPage == offers.size){
                        offer_slider.setCurrentItem(0, true)
                    }else {
                        offer_slider.setCurrentItem(currentPage++, true)
                    }
            }

            Timer().schedule(object : TimerTask() {
                // task to be scheduled
                override fun run() {
                    handler.post(update)
                }
            }, 3500, 3500)
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
    }

    private fun setUpCategoryAdapter(categoriesList: ArrayList<CategoriesItem>) {
        var layoutManager = GridLayoutManager(requireActivity(), 4)
        categoriesAdapter = CategoriesAdapter(requireActivity(), this, categoriesList, "home")
        rv_categories.layoutManager = layoutManager
        rv_categories.adapter = categoriesAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onResume() {
        super.onResume()

        if ((requireActivity() as HomeActivity).getForegroundFragment() is HomeFragment) {
            (requireActivity() as HomeActivity).toolbar.visibility = View.VISIBLE
            (requireActivity() as HomeActivity).bottom_menu.visibility = View.VISIBLE
            (requireActivity() as HomeActivity).et_search.visibility = View.VISIBLE
        }
    }

    override fun onCategoryClicked(cate_id: String,position: Int) {
        if (cate_id == "0") {
            findNavController().navigate(R.id.action_homeFragment2_to_categoriesFragment)
        } else {
            Constants.SELECTED_CATEGORY = cate_id
            SELECTED_CATEGORY_NAME = categoriesList[position].catName?:""
            SELECTED_CATEGORY_IMAGE = categoriesList[position].image?:""
            findNavController().navigate(R.id.action_homeFragment2_to_subcategoryFragment)
        }
    }

    private fun setUpGalleryAdapter(galleryList: ArrayList<GalleryItem>) {
        galleryAdapter = GalleryAdapter(galleryList, requireActivity(), requireActivity() as HomeActivity)
        rvGalleryViewPager.adapter = galleryAdapter

        val zoomOutPageTransformer = ZoomOutPageTransformer()
        rvGalleryViewPager.setPageTransformer { page, position ->
            zoomOutPageTransformer.transformPage(page, position)
        }

        rvGalleryViewPager.offscreenPageLimit = 1

        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            // Next line scales the item's height. You can remove it if you don't want this effect
            page.scaleY = 1 - (0.25f * abs(position))
            // If you want a fading effect uncomment the next line:
            // page.alpha = 0.25f + (1 - abs(position))
        }
        rvGalleryViewPager.setPageTransformer(pageTransformer)

//        val itemDecoration = HorizontalMarginItemDecoration(
//            requireActivity(),
//            R.dimen.viewpager_current_item_horizontal_margin
//        )
//        rvGalleryViewPager.addItemDecoration(itemDecoration)
    }

    private fun attachObservers() {
        viewModel.homeResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == false) {
                Utils.showSnackBar(getString(R.string.please_try_ahain), rv_categories)
            } else {
                setUpHomeData(it)
            }
        })

        viewModel.getAddressResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == false) {
                //Utils.showSnackBar(getString(R.string.please_try_ahain), rv_categories)
            } else {
                (requireActivity() as HomeActivity).txt_location.text = it?.addresslist?.street
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
                try {
                    Utils.showLoading(it, requireActivity())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun setUpHomeData(it: HomeResponse?) {
        if (it?.offers?.isNullOrEmpty() == false) {
            offers = it?.offers as ArrayList<OffersItem>
            setUpViewPager(it?.offers)
        }

        if (it?.categories?.isNullOrEmpty() == false) {
            categoriesList = it?.categories as ArrayList<CategoriesItem> /* = java.util.ArrayList<com.reemastyle.model.home.CategoriesItem> */
            if (!categoriesList.contains(CategoriesItem(null, getString(R.string.more), "0"))) {
                categoriesList.add(CategoriesItem(null, getString(R.string.more), "0"))
            }
            setUpCategoryAdapter(it?.categories)
        }

        if (it?.packages?.isNullOrEmpty() == false) {
            packageList =
                it?.packages as ArrayList<PackagesItem> /* = java.util.ArrayList<com.reemastyle.model.home.PackagesItem> */
            setUpTrendingViewPager(packageList)
        }

        if (it?.testimonials?.isNullOrEmpty() == false) {
            testimonialsList =
                it?.testimonials as ArrayList<TestimonialsItem> /* = java.util.ArrayList<com.reemastyle.model.home.TestimonialsItem> */
            setUpTestimonalsAdapter(testimonialsList)
        }

        if (it?.gallery?.isNullOrEmpty() == false) {
            galleryList = it?.gallery as ArrayList<GalleryItem> /* = java.util.ArrayList<com.reemastyle.model.home.GalleryItem> */
            setUpGalleryAdapter(galleryList)
        }
    }

    override fun onPackageItemClicked(position: Int) {
        Constants.SELECTED_PACKAGE_ID = packageList[position].id?:"0"
        findNavController().navigate(R.id.action_homeFragment2_to_packageDetailsFragment)
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    fun updateMapWithLocation(){
        findCurrentLocation()
    }

    private fun findCurrentLocation() {
        Log.e("asass","cccc")
    }
}