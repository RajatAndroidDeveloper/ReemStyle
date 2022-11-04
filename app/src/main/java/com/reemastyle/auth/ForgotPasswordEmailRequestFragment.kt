package com.reemastyle.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.util.Util
import com.google.gson.JsonObject
import com.reemastyle.R
import com.reemastyle.preferences.Preferences
import com.reemastyle.preferences.saveValue
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.fragment_forgot_password_email_request.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.et_email
import kotlinx.android.synthetic.main.fragment_signup.*

class ForgotPasswordEmailRequestFragment : Fragment() {
    lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot_password_email_request, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListeners()
        viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        attachObservers()
    }

    private fun clickListeners() {
        btn_save.setOnClickListener {
            if(et_email_address.text.toString().trim().isEmpty()){
                Utils.showSnackBar(getString(R.string.please_enter_email),et_email_address)
                return@setOnClickListener
            }
            if(!Utils.isValidString(et_email_address.text.toString().trim())){
                Utils.showSnackBar(getString(R.string.please_enter_a_valid_email),et_email_address)
                return@setOnClickListener
            }
            viewModel?.forgotPasswordEmailRequest(createRequestJson())
        }
    }

    private fun createRequestJson(): JsonObject {
        var jsonObject = JsonObject()
        jsonObject.addProperty("action","Forget_password")
        jsonObject.addProperty("email",et_email_address.text.toString().trim())
        return jsonObject
    }


    private fun attachObservers() {
        viewModel.forgotPasswordRequestResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
           if(it?.status == true) {
               Utils.showSnackBar(
                   it.message ?: getString(R.string.otp_sent_successfully),
                   et_email_address
               )
               Preferences.prefs?.saveValue("email", et_email_address.text.toString().trim())
               Preferences.prefs?.saveValue("from", "forgot_password")
               findNavController().navigate(R.id.action_forgotPasswordEmailRequestFragment_to_verifyOtpFragment)
           }else{
               Utils.showSnackBar(it.message?:"", et_email_address)
           }
        })

        viewModel.apiError.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                Utils.showSnackBar(it, et_email_address)
                Utils.showLoading(false, requireActivity())
            }
        })

        viewModel.unauthorized.observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), et_email_address)
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
}