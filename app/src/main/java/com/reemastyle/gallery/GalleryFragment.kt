package com.reemastyle.gallery

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.reemastyle.R
import com.reemastyle.model.gallery.GalleryItem
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.large_gallery_view_dialog.*

class GalleryFragment: Fragment(), OnGalleryItemClick, OnGallerySmallItemClick {
    lateinit var viewModel: GalleryViewModel
    private var galleryList: ArrayList<GalleryItem> = ArrayList<GalleryItem>()
    lateinit var galleryAdapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpGallerAdapter(galleryList)
        viewModel = ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        attachObservers()
        var jsonObject = JsonObject()
        jsonObject.addProperty("action","gallery")
        viewModel?.getGalleryData(jsonObject)
    }



    private fun attachObservers() {
        viewModel.galleryResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == false) {
                Utils.showSnackBar(getString(R.string.please_try_ahain), rv_gallery)
            } else {
                if(it?.gallery?.isNullOrEmpty() == false){
                    galleryList = it?.gallery as ArrayList<GalleryItem> /* = java.util.ArrayList<com.reemastyle.model.gallery.GalleryItem> */
                    setUpGallerAdapter(galleryList)
                }
            }
        })

        viewModel.apiError.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                Utils.showSnackBar(it, rv_gallery)
                Utils.showLoading(false, requireActivity())
            }
        })

        viewModel.unauthorized.observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), rv_gallery)
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

    private fun setUpGallerAdapter(galleryList: java.util.ArrayList<GalleryItem>) {
        var layoutManager = GridLayoutManager(requireActivity(), 3)
        rv_gallery.layoutManager = layoutManager
        galleryAdapter = GalleryAdapter(galleryList,requireActivity(),this,"screen")
        rv_gallery.adapter = galleryAdapter
    }

    override fun onGalleryItemClick(position: Int) {
        positionvalue = position
        showLargeGalleryViewDialog(requireActivity(),galleryList[position].image)
    }

    private lateinit var largeImageViewDialog: Dialog
    private var galleryImageView: ImageView ?= null
    private var positionvalue = 0
    private fun showLargeGalleryViewDialog(context: Context, image: String?){
        largeImageViewDialog = Dialog(context, R.style.DialogAnimation)
        largeImageViewDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        largeImageViewDialog.setContentView(R.layout.large_gallery_view_dialog)
        largeImageViewDialog.setCancelable(true)

        galleryImageView = largeImageViewDialog.img_gallery_view
        largeImageViewDialog.btn_cancel.setOnClickListener {
            largeImageViewDialog.dismiss()
        }

        largeImageViewDialog.img_next.setOnClickListener {
            if(positionvalue!=galleryList.size-1){
                positionvalue += 1
                if(!galleryList[positionvalue].image.isNullOrEmpty()){
                    Glide.with(requireActivity()).load(galleryList[positionvalue].image).into(galleryImageView!!)
                }
            }
        }

        largeImageViewDialog.img_previous.setOnClickListener {
            if(positionvalue!=0){
                positionvalue -= 1
                if(!galleryList[positionvalue].image.isNullOrEmpty()){
                    Glide.with(requireActivity()).load(galleryList[positionvalue].image).into(galleryImageView!!)
                }
            }
        }

        if(!image.isNullOrEmpty()){
            Glide.with(requireActivity()).load(image).into(galleryImageView!!)
        }

        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        largeImageViewDialog.rv_gallery_img.layoutManager = layoutManager
        var galleryAdapter = GallerySmallAdapter(galleryList,context,this)
        largeImageViewDialog.rv_gallery_img.adapter = galleryAdapter

        if (!largeImageViewDialog.isShowing) {
            largeImageViewDialog.show()
        }
    }

    override fun onGallerySmallItemClick(position: Int) {
        positionvalue = position
        if(!galleryList[position].image.isNullOrEmpty()){
            Glide.with(requireActivity()).load(galleryList[position].image).into(galleryImageView!!)
        }
    }

}