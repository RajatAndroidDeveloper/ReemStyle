package com.reemastyle.auth

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.reemastyle.HomeActivity
import com.reemastyle.R

class LoginSuccessFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpData()
    }

    private fun setUpData() {
        object : CountDownTimer(2000, 1000) {
            override fun onFinish() {
                startActivity(Intent(requireActivity(), HomeActivity::class.java))
                requireActivity().finish()
            }

            override fun onTick(millisUntilFinished: Long) {
                //nothing to fo here
            }
        }.start()
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginSuccessFragment()
    }
}