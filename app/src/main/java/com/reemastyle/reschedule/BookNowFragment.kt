package com.reemastyle.reschedule

import android.app.Service
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.reemastyle.R
import com.reemastyle.model.services.ServicesItem
import com.reemastyle.service.AddressAdapter
import com.reemastyle.service.AddressItemClicked
import com.reemastyle.service.ServiceAdapter
import com.reemastyle.service.ServiceItemClicked
import kotlinx.android.synthetic.main.fragment_book_now.*

class BookNowFragment : Fragment(), ServiceItemClicked, AddressItemClicked {
    private var serviceList: ArrayList<ServicesItem> = ArrayList<ServicesItem>()
    private lateinit var serviceAdapter: ServiceAdapter
    private lateinit var addressAdapter: AddressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_now, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpServiceAdapter()
        setUpAddressAdapter()
    }

    private fun setUpServiceAdapter() {
        var layoutManager = LinearLayoutManager(requireActivity())
        rv_services.layoutManager = layoutManager
        serviceAdapter = ServiceAdapter(requireActivity(),this,serviceList)
        rv_services.adapter = serviceAdapter
    }

    private fun setUpAddressAdapter() {
        var layoutManager = LinearLayoutManager(requireActivity())
        rv_addresses.layoutManager = layoutManager
        addressAdapter = AddressAdapter(requireActivity(),this)
        rv_addresses.adapter = addressAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = BookNowFragment()
    }

    override fun onServiceClicked(position: Int,type:String) {
        TODO("Not yet implemented")
    }

    override fun onAddressItemClicked(position: Int) {
        TODO("Not yet implemented")
    }


}