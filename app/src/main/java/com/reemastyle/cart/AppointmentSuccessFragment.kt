package com.reemastyle.cart

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.reemastyle.HomeActivity
import com.reemastyle.R
import kotlinx.android.synthetic.main.fragment_appointment_success.*

class AppointmentSuccessFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_appointment_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClickListener()
    }

    private fun onClickListener() {
        btn_continue_booking.setOnClickListener {
            startActivity(Intent(requireActivity(), HomeActivity::class.java))
            requireActivity().finish()
        }

        btn_bookings.setOnClickListener {
            startActivity(Intent(requireActivity(), HomeActivity::class.java))
            requireActivity().finish()
        }
    }
}