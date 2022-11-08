package com.reemastyle.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.reemastyle.HomeActivity
import com.reemastyle.R
import com.reemastyle.home.HomeViewModel
import com.reemastyle.model.notification.NotiItem
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.fragment_notifications.*

class NotificationsFragment : Fragment() {
    lateinit var viewModel: HomeViewModel
    lateinit var notificationAdapter: NotificationAdapter
    var notificationList: ArrayList<NotiItem> = ArrayList<NotiItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        attachObservers()
        viewModel?.getNotifications(createGetNotificationRequest())

        imgClose.setOnClickListener {
            (requireActivity() as HomeActivity).onBackPressed()
        }
    }

    private fun createGetNotificationRequest(): JsonObject {
        var jsonObject = JsonObject()
        jsonObject.addProperty("action","getallnotifications")
        jsonObject.addProperty("user_id",Utils.getUserData()?.id?:"")
        return jsonObject
    }

    private fun attachObservers() {
        viewModel.notificationsResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            Utils.showLoading(false, requireActivity())
            if (it.status == false) {
                Utils.showSnackBar(it.message?:getString(R.string.please_try_ahain), rvNotifications)
            } else {
                if(!it?.noti.isNullOrEmpty()){
                    notificationList.clear()
                    notificationList = it?.noti as ArrayList<NotiItem>
                    setUpNotificationAdapter(notificationList)
                }

            }
        })

        viewModel.apiError.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                Utils.showSnackBar(it, rvNotifications)
                Utils.showLoading(false, requireActivity())
            }
        })

        viewModel.unauthorized.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let {
                if (it) {
                    Utils.showSnackBar(getString(R.string.your_session_is_out), rvNotifications)
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

    private fun setUpNotificationAdapter(notificationList: java.util.ArrayList<NotiItem>) {
        var layoutManager = LinearLayoutManager(requireActivity())
        rvNotifications.layoutManager = layoutManager
        notificationAdapter = NotificationAdapter(requireActivity(),notificationList)
        rvNotifications.adapter = notificationAdapter
    }
}