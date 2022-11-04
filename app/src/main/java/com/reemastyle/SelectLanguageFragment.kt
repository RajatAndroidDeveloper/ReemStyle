package com.reemastyle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.reemastyle.preferences.Preferences
import com.reemastyle.preferences.saveValue
import com.reemastyle.util.LocaleHelper
import kotlinx.android.synthetic.main.fragment_select_language.*

class SelectLanguageFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListeners()
    }

    private fun clickListeners() {
        btn_continue.setOnClickListener {
            Preferences.prefs?.saveValue("LanguageFirst","Done")
            findNavController().navigate(R.id.action_selectLanguageFragment_to_loginFragment)
        }

        btn_english.setOnClickListener {
            btn_english.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_tick_pink),null,null,null)
            btn_arabic.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_light_pink_tick),null,null,null)
            LocaleHelper.setLocale(requireActivity(), "en");
        }

        btn_arabic.setOnClickListener {
            btn_arabic.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_tick_pink),null,null,null)
            btn_english.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_light_pink_tick),null,null,null)
            LocaleHelper.setLocale(requireActivity(), "ar");
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SelectLanguageFragment()
    }
}