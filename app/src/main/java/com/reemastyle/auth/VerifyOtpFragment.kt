package com.reemastyle.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.gson.JsonObject
import com.reemastyle.R
import com.reemastyle.preferences.Preferences
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.fragment_verify_otp.*

class VerifyOtpFragment : Fragment() {
    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verify_otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListeners()
        viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        attachObservers()
    }

    private fun clickListeners() {
        btn_save.setOnClickListener {
            if (txt_otp.otp.isEmpty()) {
                Utils.showSnackBar(getString(R.string.please_enter_otp), txt_otp)
                return@setOnClickListener
            }
            if (txt_otp.otp.length < 4) {
                Utils.showSnackBar(getString(R.string.please_enter_otp), txt_otp)
                return@setOnClickListener
            }
            viewModel?.verifyOtp(createJsonMap())
        }

        txt_resend_otp.setOnClickListener {
            viewModel?.resendOtp(createJsonForResendOtp())
        }
    }

    private fun createJsonForResendOtp(): JsonObject {
        var jsonObject = JsonObject()
        if(Preferences.prefs?.getString("from","") == "forgot_password"){
            jsonObject.addProperty("action", "resendresetpass")
            jsonObject.addProperty("email", Preferences.prefs?.getString("email",""))
        }else {
            jsonObject.addProperty("email", Utils.getUserData()?.email ?: "")
            jsonObject.addProperty("action", "resendaccount")
        }
        return jsonObject
    }

    private fun createJsonMap(): JsonObject {
        var jsonObject = JsonObject()
        jsonObject.addProperty("code", txt_otp.otp.toString().trim())
        if(Preferences.prefs?.getString("from","") == "forgot_password"){
            jsonObject.addProperty("action", "verifyresetpass")
            jsonObject.addProperty("email", Preferences.prefs?.getString("email",""))
        }else {
            jsonObject.addProperty("email", Utils.getUserData()?.email ?: "")
            jsonObject.addProperty("action", "verifyregister")
        }
        return jsonObject
    }

    private fun attachObservers() {
        viewModel.verifyOtpResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if(it.status == true) {
                Utils.showSnackBar(it.message ?: getString(R.string.otp_sent_successfully), txt_otp)
                if(Preferences.prefs?.getString("from","") == "forgot_password"){
                    findNavController().navigate(R.id.action_verifyOtpFragment_to_forgotPasswordFragment)
                }else {
                    findNavController().navigate(R.id.action_verifyOtpFragment_to_loginFragment)
                }
            }else{
                Utils.showSnackBar(it.message ?: getString(R.string.enter_otp_sent_successfully), txt_otp)
            }
        })

        viewModel.resendOtpResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            Utils.showSnackBar(it.message ?: getString(R.string.otp_sent_successfully), txt_otp)
        })

        viewModel.apiError.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                Utils.showSnackBar(it, txt_otp)
                Utils.showLoading(false, requireActivity())
            }
        })

        viewModel.unauthorized.observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), txt_otp)
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
        fun newInstance() = VerifyOtpFragment()
    }
}