package com.reemastyle.profile

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.model.MediaFile
import com.reemastyle.HomeActivity
import com.reemastyle.R
import com.reemastyle.model.login.LoginResponse
import com.reemastyle.model.profile.ProfileData
import com.reemastyle.preferences.Preferences
import com.reemastyle.preferences.getValue
import com.reemastyle.preferences.saveValue
import com.reemastyle.util.Constants
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_update_profile.*
import kotlinx.android.synthetic.main.fragment_update_profile.img_profile1
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class UpdateProfileFragment : Fragment() {
    lateinit var viewModel: ProfileViewModel
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_profile, container, false)
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
        return jsonObject
    }

    private fun clickListeners() {
        img_back.setOnClickListener {
            (requireActivity() as HomeActivity).onBackPressed()
        }

        img_profile1.setOnClickListener {
            Utils.callImagePickerIntent(requireActivity())
        }

        img_edit.setOnClickListener {
            Utils.callImagePickerIntent(requireActivity())
        }

        btn_update.setOnClickListener {
            var imageRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile!!)
            var nameRequestBody = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                txt_name.text.toString().trim()
            )
            var emailRequestBody = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                txt_email.text.toString().trim()
            )
            var mobileRequestBody = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                txt_mobile.text.toString().trim()
            )
            var countryCodeRequestBody = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                countryCodePicker.selectedCountryCodeWithPlus
            )
            var idRequestBody = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                Utils.getUserData()?.id?.toString() ?: "0"
            )
            var actionRequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), "update_profile")
            viewModel?.updateProfile(
                imageRequestBody,
                nameRequestBody,
                emailRequestBody,
                countryCodeRequestBody,
                mobileRequestBody,
                idRequestBody,
                actionRequestBody
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            when (requestCode) {
                Utils.REQUEST_IMAGE -> {
                    val mediaFiles: List<MediaFile>? =
                        data!!.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES)
                    val parcelFileDescriptor = requireActivity().contentResolver.openFileDescriptor(
                        mediaFiles!![0].uri,
                        "r",
                        null
                    )
                    parcelFileDescriptor?.let {
                        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                        imageFile = File(
                            requireActivity().cacheDir,
                            Utils.getFileName(mediaFiles[0].uri, requireActivity())
                        )
                        val outputStream = FileOutputStream(imageFile)
                        IOUtils.copy(inputStream, outputStream)
                        img_profile1.setImageURI(Uri.parse(imageFile.toString()))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun attachObservers() {
        viewModel.profileResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == true) {
                setUpProfileData(it.profileData)
            } else {
                Utils.showSnackBar(it.message ?: getString(R.string.please_try_ahain), img_back)
            }
        })

        viewModel.updateProfileResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == true) {
                Utils.showSnackBar(it.message ?: getString(R.string.data_updated_successfully), img_back)
//                var jsonData = Preferences.prefs?.getValue(Constants.USER_INFO,"")
//                var loginResponse: LoginResponse = Gson().fromJson(jsonData, LoginResponse::class.java)
//
//                loginResponse?.userdata?.profImage = it?.profileData?.profImage
//                loginResponse?.userdata?.name = it?.profileData?.name
//                loginResponse?.userdata?.email = it?.profileData?.email
//                loginResponse?.userdata?.ext = it?.profileData?.ext
//                loginResponse?.userdata?.phone = it?.profileData?.phone

                Preferences.prefs?.saveValue(Constants.USER_INFO,Gson().toJson(it))
                requireActivity().onBackPressed()
            } else {
                Utils.showSnackBar(it.message ?: getString(R.string.please_try_ahain), img_back)
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
                    Utils.showSnackBar(getString(R.string.your_session_is_out), img_back)
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

    private fun setUpProfileData(profileData: ProfileData?) {
        txt_name.setText(profileData?.name ?: "")
        txt_email.setText(profileData?.email ?: "")
        txt_mobile.setText("${profileData?.phone}")
        countryCodePicker.setDefaultCountryUsingPhoneCode((profileData?.ext ?: "+91").replace("+", "").toInt()
        )
        if (!profileData?.profImage.isNullOrEmpty())
            Glide.with(requireActivity()).load(profileData?.profImage)
                .placeholder(R.drawable.ic_dummy_profile).into(img_profile1)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}