package com.reemastyle.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.JsonObject
import com.reemastyle.R
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.change_password_fragment.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ChangePasswordFragment : Fragment() {
    lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.change_password_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListeners()

        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        attachObservers()
    }

    private fun attachObservers() {
        viewModel.changePasswordResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == true) {
                Utils.logoutUser(requireActivity())
            } else {
                Utils.showSnackBar(it.message ?: "Please try again later", et_old_password)
            }
        })

        viewModel.apiError.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                Utils.showSnackBar(it, et_old_password)
                Utils.showLoading(false, requireActivity())
            }
        })

        viewModel.unauthorized.observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), et_old_password)
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

    private fun clickListeners() {
        btn_save.setOnClickListener {
            if(et_old_password.text.toString().trim().isNullOrBlank()){
                Utils.showSnackBar(getString(R.string.please_enter_password),et_old_password)
                return@setOnClickListener
            }
            if(et_new_password.text.toString().trim().isNullOrBlank()){
                Utils.showSnackBar(getString(R.string.please_enter_new_password),et_old_password)
                return@setOnClickListener
            }
            if(et_confirm_password.text.toString().trim().isNullOrBlank()){
                Utils.showSnackBar(getString(R.string.please_confirm_password),et_old_password)
                return@setOnClickListener
            }
            if(et_confirm_password.text.toString().trim() != et_new_password.text.toString().trim()){
                Utils.showSnackBar(getString(R.string.please_check_new_and_confirm_password),et_old_password)
                return@setOnClickListener
            }
            viewModel?.changePassword(createChangePasswordRequest())
        }
    }

    private fun createChangePasswordRequest(): JsonObject {
        var jsonObject = JsonObject()
        jsonObject.addProperty("action","change_password")
        jsonObject.addProperty("user_id",Utils.getUserData()?.id)
        jsonObject.addProperty("old_pass",et_old_password.text.toString().trim())
        jsonObject.addProperty("new_pass",et_new_password.text.toString().trim())
        return jsonObject
    }
}