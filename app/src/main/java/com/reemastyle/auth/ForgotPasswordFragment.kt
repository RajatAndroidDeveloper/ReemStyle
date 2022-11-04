package com.reemastyle.auth

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.JsonObject
import com.reemastyle.MainActivity
import com.reemastyle.R
import com.reemastyle.preferences.Preferences
import com.reemastyle.preferences.clearValue
import com.reemastyle.util.Constants
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.fragment_forgot_password.img_password


class ForgotPasswordFragment : Fragment() {
    lateinit var viewModel: AuthViewModel
    private var passwordVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
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
                et_confirm_password.transformationMethod = PasswordTransformationMethod.getInstance();
                img_password.setImageResource(R.drawable.ic_eye_icon)
                passwordVisible = false
            }else{
                et_confirm_password.transformationMethod = HideReturnsTransformationMethod.getInstance();
                img_password.setImageResource(R.drawable.ic_eye_disabled)
                passwordVisible = true
            }
        }

        btn_save.setOnClickListener {
            if(et_new_password.text.toString().trim().isEmpty()){
                Utils.showSnackBar(getString(R.string.please_enter_password),btn_save)
                return@setOnClickListener
            }
            if(et_confirm_password.text.toString().trim().isEmpty()){
                Utils.showSnackBar(getString(R.string.please_enter_password),btn_save)
                return@setOnClickListener
            }
            if(et_confirm_password.text.toString().trim() != et_new_password.text.toString().trim()){
                Utils.showSnackBar(getString(R.string.please_enter_valid_password),btn_save)
                return@setOnClickListener
            }
            viewModel?.forgotPasswordEmailRequest(createChangePasswordRequest())
        }
    }

    private fun createChangePasswordRequest(): JsonObject {
        var jsonObject = JsonObject()
        jsonObject.addProperty("action","Reset_password")
        jsonObject.addProperty("email",Preferences.prefs?.getString("email",""))
        jsonObject.addProperty("pwd",et_new_password.text.toString().trim())
        jsonObject.addProperty("repwd",et_confirm_password.text.toString().trim())
        return jsonObject
    }

    private fun attachObservers() {
        viewModel.forgotPasswordRequestResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            Utils.showSnackBar(it.message?:"Password has been updated successfully.", btn_save)
            if(it.status == true) {
                Preferences.prefs?.clearValue(Constants.USER_INFO)
                Preferences.prefs?.clearValue("email")
                Preferences.prefs?.clearValue("from")
                startActivity(Intent(requireActivity() ,MainActivity::class.java))
                requireActivity().finish()
            }
        })

        viewModel.apiError.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                Utils.showSnackBar(it, btn_save)
                Utils.showLoading(false, requireActivity())
            }
        })

        viewModel.unauthorized.observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), btn_save)
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
        fun newInstance() = ForgotPasswordFragment()
    }
}