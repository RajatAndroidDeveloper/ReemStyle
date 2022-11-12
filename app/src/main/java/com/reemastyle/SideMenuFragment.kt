package com.reemastyle

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.reemastyle.util.Constants
import com.reemastyle.util.LocaleHelper
import com.reemastyle.util.Utils
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_side_menu.*
import kotlinx.android.synthetic.main.login_dialog_layout.*
import java.util.prefs.Preferences

class SideMenuFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_side_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as HomeActivity).toolbar.visibility = View.GONE
        clickListeners()

        if(com.reemastyle.preferences.Preferences.prefs?.getString("Language","en") == "en"){
            view_english.visibility = View.VISIBLE
            view_arabic.visibility = View.GONE
            txt_arabic.setTextColor(ContextCompat.getColor(requireActivity(),R.color.text_dark_grey))
            txt_english.setTextColor(ContextCompat.getColor(requireActivity(),R.color.white))
        }else{
            view_arabic.visibility = View.VISIBLE
            view_english.visibility = View.GONE
            txt_english.setTextColor(ContextCompat.getColor(requireActivity(),R.color.text_dark_grey))
            txt_arabic.setTextColor(ContextCompat.getColor(requireActivity(),R.color.white))
        }
    }

    private fun clickListeners() {
        txt_notification.setOnClickListener {
            findNavController().navigate(R.id.action_sideMenuFragment_to_notificationsFragment)
        }

        txt_contact.setOnClickListener {
            val u: Uri = Uri.parse("tel:" + "97450787829")
            val i = Intent(Intent.ACTION_DIAL, u)
            try {
                startActivity(i)
            } catch (s: SecurityException) {
                Toast.makeText(requireActivity(), "An error occurred", Toast.LENGTH_LONG).show()
            }
        }

        txt_booking.setOnClickListener {
            if(Utils.getUserData() == null){
                showLoginDialog(requireActivity())
            }else {
                findNavController().navigate(R.id.action_sideMenuFragment_to_bookingsFragment)
            }
        }

        txt_english.setOnClickListener {
            view_english.visibility = View.VISIBLE
            view_arabic.visibility = View.GONE
            txt_arabic.setTextColor(ContextCompat.getColor(requireActivity(),R.color.text_dark_grey))
            txt_english.setTextColor(ContextCompat.getColor(requireActivity(),R.color.white))
            LocaleHelper.setLocale(requireActivity(),"en")
            LocaleHelper.restartApp(requireActivity())
        }

        txt_arabic.setOnClickListener {
            view_arabic.visibility = View.VISIBLE
            view_english.visibility = View.GONE
            txt_english.setTextColor(ContextCompat.getColor(requireActivity(),R.color.text_dark_grey))
            txt_arabic.setTextColor(ContextCompat.getColor(requireActivity(),R.color.white))
            LocaleHelper.setLocale(requireActivity(),"ar")
            LocaleHelper.restartApp(requireActivity())
        }

        img_back.setOnClickListener {
            (requireActivity() as HomeActivity).onBackPressed()
        }

        txt_gallery.setOnClickListener {
            findNavController().navigate(R.id.action_sideMenuFragment_to_galleryFragment)
        }

        txt_cart.setOnClickListener {
            if(Utils.getUserData() == null){
                showLoginDialog(requireActivity())
            }else {
                (requireActivity() as HomeActivity).bottom_menu.visibility = View.GONE
                findNavController().navigate(R.id.action_sideMenuFragment_to_cartFragment)
            }
        }

        txt_address.setOnClickListener {
            if(Utils.getUserData() == null){
                showLoginDialog(requireActivity())
            }else {
                findNavController().navigate(R.id.action_sideMenuFragment_to_selectLocationFragment)
            }
        }

        txt_profile.setOnClickListener {
            if(Utils.getUserData() == null){
                showLoginDialog(requireActivity())
            }else {
                findNavController().navigate(R.id.action_sideMenuFragment_to_profileFragment)
            }
        }

        txt_logout.setOnClickListener {
            showLogoutDialog(requireActivity())
        }
    }

    lateinit var loginDialog: Dialog
    private fun showLoginDialog(context: Context){
        loginDialog = Dialog(context, R.style.DialogAnimation)
        loginDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        loginDialog.setContentView(R.layout.login_dialog_layout)
        loginDialog.setCancelable(true)

        loginDialog.btnCancel.setOnClickListener {
            loginDialog.dismiss()
        }

        loginDialog.btnLogin.setOnClickListener {
            startActivity(Intent(requireActivity(),MainActivity::class.java))
            (requireActivity() as HomeActivity).finish()
        }

        if (!loginDialog.isShowing) {
            loginDialog.show()
        }
    }

    lateinit var logoutDialog: Dialog
    private fun showLogoutDialog(context: Context){
        logoutDialog = Dialog(context, R.style.DialogAnimation)
        logoutDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        logoutDialog.setContentView(R.layout.login_dialog_layout)
        logoutDialog.setCancelable(true)

        logoutDialog.btnLogin.text  = getString(R.string.logout)
        logoutDialog.txtDescription.text  = getString(R.string.are_you_sure_you_want_logout)

        logoutDialog.btnCancel.setOnClickListener {
            logoutDialog.dismiss()
        }

        logoutDialog.btnLogin.setOnClickListener {
          Utils.logoutUser(requireActivity())
        }

        if (!logoutDialog.isShowing) {
            logoutDialog.show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SideMenuFragment()
    }
}