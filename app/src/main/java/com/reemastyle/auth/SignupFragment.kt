package com.reemastyle.auth

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
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.reemastyle.R
import com.reemastyle.preferences.Preferences
import com.reemastyle.preferences.saveValue
import com.reemastyle.util.Constants
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.fragment_signup.*
import java.util.regex.Pattern

class SignupFragment : Fragment() {
    lateinit var viewModel: AuthViewModel
    private var passwordVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListeners()
        viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        attachObservers()
    }

    private fun clickListeners() {
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

        btn_signup.setOnClickListener {
            if(et_name.text.toString().trim().isEmpty()){
                Utils.showSnackBar(getString(R.string.please_enter_name),et_name)
                return@setOnClickListener
            }
            if(et_email.text.toString().trim().isEmpty()){
                Utils.showSnackBar(getString(R.string.please_enter_email),et_email)
                return@setOnClickListener
            }
            if(!Utils.isValidString(et_email.text.toString().trim())){
                Utils.showSnackBar(getString(R.string.please_enter_a_valid_email),et_email)
                return@setOnClickListener
            }
            if(et_mobile.text.toString().trim().isEmpty()){
                Utils.showSnackBar(getString(R.string.please_enter_mobile),et_mobile)
                return@setOnClickListener
            }
            if(countryCodePicker.selectedCountryCode.toString().isEmpty()){
                Utils.showSnackBar(getString(R.string.please_enter_country_code),countryCodePicker)
                return@setOnClickListener
            }
            if(et_password.text.toString().trim().isEmpty()){
                Utils.showSnackBar(getString(R.string.please_enter_password),et_password)
                return@setOnClickListener
            }

            viewModel?.registerUser(createHashMap())
        }

        txt_already_have_account.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }

    private fun createHashMap(): JsonObject {
        var jsonObject = JsonObject()
        jsonObject.addProperty("action","register_user")
        jsonObject.addProperty("name",et_name.text.toString().trim())
        jsonObject.addProperty("email",et_email.text.toString().trim())
        jsonObject.addProperty("password",et_password.text.toString().trim())
        jsonObject.addProperty("phone",et_mobile.text.toString().trim())
        jsonObject.addProperty("device","android")
        jsonObject.addProperty("firebase_token",Preferences.prefs?.getString("fcm_token",""))
        jsonObject.addProperty("ext",countryCodePicker.defaultCountryCodeWithPlus.toString().trim())
        return jsonObject
    }

    private fun attachObservers() {
        viewModel.registerResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            Utils.showSnackBar(it.message?:"Otp has been sent on your email address.", et_email)
            if(it.status == true) {
                Preferences.prefs?.saveValue(Constants.USER_INFO, Gson().toJson(it))
                Preferences.prefs?.saveValue("email", et_email.text.toString().trim())
                Preferences.prefs?.saveValue("from", "signup")
                findNavController()?.navigate(R.id.action_signupFragment_to_verifyOtpFragment)
            }
        })

        viewModel.apiError.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                Utils.showSnackBar(it, et_email)
                Utils.showLoading(false, requireActivity())
            }
        })

        viewModel.unauthorized.observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), et_email)
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

    companion object {
        @JvmStatic
        fun newInstance() = SignupFragment()
    }
}