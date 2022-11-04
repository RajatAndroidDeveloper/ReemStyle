package com.reemastyle.auth

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.util.Util
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.reemastyle.HomeActivity
import com.reemastyle.R
import com.reemastyle.preferences.Preferences
import com.reemastyle.preferences.saveValue
import com.reemastyle.util.Constants.ACTION_LOGIN
import com.reemastyle.util.Constants.USER_ID
import com.reemastyle.util.Constants.USER_INFO
import com.reemastyle.util.Utils
import com.reemastyle.util.Utils.showLoading
import com.reemastyle.util.Utils.showSnackBar
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
    lateinit var viewModel: AuthViewModel
    private var passwordVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListeners()

        viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        attachObservers()
    }

    private fun clickListeners() {
        btn_login.setOnClickListener {
            if(countryCodePicker.selectedCountryCode.isEmpty()){
                Utils.showSnackBar(getString(R.string.please_enter_country_code),countryCodePicker)
                return@setOnClickListener
            }
            if(et_mobile.text.toString().trim().isEmpty()){
                Utils.showSnackBar(getString(R.string.please_enter_mobile),countryCodePicker)
                return@setOnClickListener
            }
            if(et_password.text.toString().trim().isEmpty()){
                Utils.showSnackBar(getString(R.string.please_enter_password),countryCodePicker)
                return@setOnClickListener
            }
            viewModel?.loginUser(createLoginRequest())
        }

        txt_dont_have_account.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        txt_forgot_password.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordEmailRequestFragment)
        }

        txt_guest.setOnClickListener {
            startActivity(Intent(requireActivity(),HomeActivity::class.java))
            requireActivity().finish()
        }

        img_password.setOnClickListener {
            if(passwordVisible){
                et_password.transformationMethod = PasswordTransformationMethod.getInstance();
                img_password.setImageResource(R.drawable.ic_eye_icon)
                passwordVisible = false
            }else{
                et_password.transformationMethod = HideReturnsTransformationMethod.getInstance();
                img_password.setImageResource(R.drawable.ic_eye_disabled)
                passwordVisible = true
            }
        }
    }

    private fun createLoginRequest(): JsonObject {
        var jsonObject  = JsonObject()
        jsonObject.addProperty("email",et_mobile.text.toString().trim())
        jsonObject.addProperty("password",et_password.text.toString().trim())
        jsonObject.addProperty("action",ACTION_LOGIN)
        jsonObject.addProperty("device","android")
        jsonObject.addProperty("firebase_token",Preferences.prefs?.getString("fcm_token",""))
        jsonObject.addProperty("ext",countryCodePicker.selectedCountryCodeWithPlus)
        return jsonObject
    }

    private fun attachObservers() {
        viewModel.loginResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            showLoading(false, requireActivity())
            if(it.status == false){
                Utils.showSnackBar(it.message?:getString(R.string.please_try_ahain),et_email)
            }else {
                Preferences.prefs?.saveValue(USER_INFO, Gson().toJson(it))
                Preferences.prefs?.saveValue(USER_ID, it.userdata?.id)
                startActivity(Intent(requireActivity(), HomeActivity::class.java))
                requireActivity().finish()
            }
        })

        viewModel.apiError.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                showSnackBar(it, et_email)
                showLoading(false, requireActivity())
            }
        })

        viewModel.unauthorized.observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    showSnackBar(getString(R.string.your_session_is_out), et_email)
                    Utils.logoutUser(requireActivity())
                }
            }
        })

        viewModel.isLoading.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                showLoading(it, requireActivity())
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}