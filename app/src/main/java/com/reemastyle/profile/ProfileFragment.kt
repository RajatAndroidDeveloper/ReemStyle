package com.reemastyle.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.reemastyle.HomeActivity
import com.reemastyle.R
import com.reemastyle.model.profile.ProfileData
import com.reemastyle.preferences.Preferences
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {
    lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListeners()

        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        attachObservers()

        viewModel?.getProfile(createGetProfileRequest())
    }

    private fun createGetProfileRequest(): JsonObject {
        var jsonObject = JsonObject()
        jsonObject.addProperty("action", "view_profile")
        jsonObject.addProperty("user_id", Utils.getUserData()?.id)
        if(Preferences.prefs?.getString("Language","en") == "ar"){
            jsonObject.addProperty("lang","AR")
        }else{
            jsonObject.addProperty("lang","EN")
        }
        return jsonObject
    }

    private fun clickListeners() {
        img_back.setOnClickListener {
            (requireActivity() as HomeActivity).onBackPressed()
        }

        btn_edit.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_updateProfileFragment)
        }

        btn_change_password.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
        }
    }

    private fun attachObservers() {
        viewModel.profileResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == true) {
                setUpProfileData(it.profileData)
            } else {
                Utils.showSnackBar(it.message ?: "Please try again later", img_back)
            }
        })

        viewModel.apiError.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                Utils.showSnackBar(it, img_back)
                Utils.showLoading(false, requireActivity())
            }
        })

        viewModel.unauthorized.observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), img_back)
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

    private fun setUpProfileData(profileData: ProfileData?) {
        txt_name.text = profileData?.name ?: ""
        txt_email.text = profileData?.email ?: ""
        txt_mobile.text = "${profileData?.ext}-${profileData?.phone}"
        if (!profileData?.profImage.isNullOrEmpty())
            Glide.with(requireActivity()).load(profileData?.profImage).placeholder(R.drawable.ic_dummy_profile).into(img_profile)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}