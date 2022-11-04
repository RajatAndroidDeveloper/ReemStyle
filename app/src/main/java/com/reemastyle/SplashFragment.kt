package com.reemastyle

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.reemastyle.preferences.Preferences
import com.reemastyle.preferences.getValue
import com.reemastyle.util.Constants
import com.reemastyle.util.LocaleHelper
import com.reemastyle.util.Utils

class SplashFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        LocaleHelper.setLocale(requireActivity(), Preferences?.prefs?.getString("Language","en")?:"en");
        setUpData()
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    private fun setUpData() {
        object : CountDownTimer(2000, 1000) {
            override fun onFinish() {
                if(Utils?.getUserData()== null || Preferences.prefs?.getValue(Constants.USER_INFO,"").isNullOrEmpty()){
                    if(Preferences.prefs?.getString("LanguageFirst","") == "Done"){
                        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                    }else {
                        findNavController().navigate(R.id.action_splashFragment_to_selectLanguageFragment)
                    }
                }else {
                    startActivity(Intent(requireActivity(), HomeActivity::class.java))
                    requireActivity().finish()
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                //nothing to fo here
            }
        }.start()
    }

    companion object {
        fun newInstance(param1: String, param2: String) = SplashFragment()
    }
}