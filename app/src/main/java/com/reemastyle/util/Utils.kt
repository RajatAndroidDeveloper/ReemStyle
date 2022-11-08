package com.reemastyle.util

import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.config.Configurations
import com.reemastyle.MainActivity
import com.reemastyle.R
import com.reemastyle.model.login.LoginResponse
import com.reemastyle.model.login.Userdata
import com.reemastyle.preferences.Preferences
import com.reemastyle.preferences.clearValue
import com.reemastyle.preferences.getValue
import com.reemastyle.util.Constants.EMAIL_ADDRESS_PATTERN
import java.text.SimpleDateFormat
import java.util.*


object Utils {
    const val REQUEST_IMAGE = 1

    fun callImagePickerIntent(requireActivity: FragmentActivity) {
        val intent: Intent = Intent(requireActivity, FilePickerActivity::class.java)
        intent.putExtra(
            FilePickerActivity.CONFIGS, Configurations.Builder()
                .setCheckPermission(true)
                .setShowFiles(false)
                .setShowImages(true)
                .setShowAudios(false)
                .setShowVideos(false)
                .setIgnoreNoMedia(false)
                .enableVideoCapture(false)
                .enableImageCapture(true)
                .setIgnoreHiddenFile(false)
                .setSingleChoiceMode(true)
                .setMaxSelection(1)
                .build())
        requireActivity.startActivityForResult(intent, REQUEST_IMAGE)
    }

    fun getFileName(fileUri: Uri, context: Context): String {
        var name = ""
        val returnCursor = context.contentResolver.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }

    fun showLoading(show: Boolean?,ctx: Context) {
        if (show!!) showProgress(ctx) else hideProgress()
    }

    fun hideProgress() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
    }

    private var mProgressDialog: Dialog? = null
    fun showProgress(ctx: Context) {
        if (mProgressDialog == null) {
            mProgressDialog = Dialog(ctx, android.R.style.Theme_Translucent)
            mProgressDialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            mProgressDialog!!.setContentView(R.layout.loader_half__layout)
            mProgressDialog!!.setCancelable(false)
        }
        try {
            mProgressDialog!!.show()
        }
        catch ( e: Exception) {
            Log.e("ErrorDialog:",e.message!!)
        }
    }

    fun showSnackBar(message: String, content: View) {
        this.let {
            Snackbar.make(content, message, Snackbar.LENGTH_LONG).show()
        }
    }

    fun getUserData(): Userdata? {
        var jsonData = Preferences.prefs?.getValue(Constants.USER_INFO,"")
        return if(jsonData.isNullOrEmpty()){
            null
        }else {
            var loginResponse: LoginResponse = Gson().fromJson(jsonData, LoginResponse::class.java)
            loginResponse.userdata?:null
        }
    }

    fun isValidString(str: String): Boolean{
        return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
    }

    fun View.hideKeyBoard() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    fun logoutUser(requireActivity: FragmentActivity) {
        Preferences?.prefs?.clearValue(Constants.USER_INFO)
        Preferences?.prefs?.clearValue(Constants.USER_ID)
        Preferences?.prefs?.clearValue(Constants.ACCESS_TOKEN)
        setConstantValuesAsEmpty()
        requireActivity.startActivity(Intent(requireActivity, MainActivity::class.java))
        requireActivity.finish()
    }

    fun setConstantValuesAsEmpty(){
        Constants.COMING_FROM = ""
        Constants.SELECTED_CATEGORY = "0"
        Constants.SELECTED_CATEGORY_NAME = ""
        Constants.SELECTED_CATEGORY_IMAGE = ""
        Constants.SELECTED_SUB_CATEGORY_NAME = ""
        Constants.SELECTED_SUB_CATEGORY = 0
        Constants.SELECTED_PACKAGE_ID = "0"
    }

    fun getFormattedTimeValue(dateCal: String): String {
        var cal = ""
        try {
            val dateFormat1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale(Preferences.prefs?.getString("Language","en")))
            val dateFormat2 = SimpleDateFormat("EEEE, dd MMM yyyy", Locale(Preferences.prefs?.getString("Language","en")))
            val date = dateFormat1.parse(dateCal)
            cal = dateFormat2.format(date)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cal
    }

    fun getFormattedCurrentDateValue(dateCal: String): String {
        var cal = ""
        try {
            val dateFormat1 = SimpleDateFormat("yyyy/MM/dd", Locale.US)
            val dateFormat2 = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date = dateFormat1.parse(dateCal)
            cal = dateFormat2.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cal
    }

    fun getFormattedDateValue(dateCal: String): String {
        var cal = ""
        try {
            val dateFormat1 = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.US)
            val dateFormat2 = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date = dateFormat1.parse(dateCal)
            cal = dateFormat2.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cal
    }

    fun getFormattedDayOnly(dateCal: String): String {
        var cal = ""
        try {
            var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale(Preferences.prefs?.getString("Language","en")))
            var date = sdf.parse(dateCal)
            val dateFormat2 = SimpleDateFormat("EEEE", Locale(Preferences.prefs?.getString("Language","en")))
            cal = dateFormat2.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cal
    }

    fun getFormattedDateForCart(dateCal: String): String {
        var cal = ""
        try {
            var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale(Preferences.prefs?.getString("Language","en")))
            var date = sdf.parse(dateCal)
            val dateFormat2 = SimpleDateFormat("dd MMM",
                Locale(Preferences.prefs?.getString("Language","en"))
            )
            cal = dateFormat2.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cal
    }

    fun getFormattedTimeForCart(dateCal: String): String {
        var cal = ""
        try {
            var sdf = SimpleDateFormat("H:mm")
            var date = sdf.parse(dateCal)
            val dateFormat2 = SimpleDateFormat("kk:mm:ss", Locale(Preferences.prefs?.getString("Language","en")))
            cal = dateFormat2.format(date)
            Log.e("Asasassas",cal+"Ss")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cal
    }

    fun getTimeIn24HoursFormat(time: String): String{
        var cal = ""
        try{
            val date12Format = SimpleDateFormat("hh:mm a")
            val date24Format = SimpleDateFormat("HH:mm")
            cal = date24Format.format(date12Format.parse(time))
            Log.e("asaasasa",cal+"sd")
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
        return cal
    }

    fun getFormatlistPrice(price: String): SpannableString? {
        val formatListPrice = SpannableString(price)
        formatListPrice.setSpan(
            StrikethroughSpan(),
            0,
            price.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        return formatListPrice
    }


     fun compareDateTimeForSlots(date: String) : Int{
        var result = 0
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val parsed = sdf.parse(date)
            val now = Date(System.currentTimeMillis()) // 2016-03-10 22:06:10
            Log.e("comparision result",(parsed.compareTo(now)).toString())
            result = parsed.compareTo(now)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return result
    }

    @TargetApi(Build.VERSION_CODES.N)
     fun updateResources(context: Context, language: String): Context? {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

     fun updateResourcesLegacy(context: Context, language: String): Context? {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        configuration.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale)
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}