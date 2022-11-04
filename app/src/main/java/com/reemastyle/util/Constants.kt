package com.reemastyle.util

import java.util.regex.Pattern

object Constants {
    var PHONE_NUMBER = ""
    var COUNTRY_CODE = ""
    var LOGIN_TYPE = ""
    var EMAIL = ""
    var COMING_FROM = ""
    var SELECTED_CATEGORY = "0"
    var SELECTED_CATEGORY_NAME = ""
    var SELECTED_CATEGORY_IMAGE = ""
    var SELECTED_SUB_CATEGORY_NAME = ""
    var SELECTED_SUB_CATEGORY = 0
    var SELECTED_PACKAGE_ID = "0"
    const val MAP_API_KEY = "AIzaSyCv0uIHXE3xqWIPIxQOiBbp0g3Ll658LjY"
    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    //API Actions
    const val ACTION_LOGIN = "login_user"
    const val ACTION_REGISTER = "register_user"

    const val BASE_URL = "https://reem.wearecherry.net/"
    const val PROFILE_IMAGE_BASE_PATH = "https://reem.wearecherry.net/images/profile/"
    const val ACCESS_TOKEN = "access_token"
    const val USER_INFO = "user_info"
    const val USER_ID = "user_id"
    const val SIGN_IN_BY_PHONE_NUMBER = "PHONE_NUMBER"

    const val SOMETHING_WENT_WRONG = "Something went wrong please try again"
    const val FAILURE_TIME_OUT_ERROR = "Time Out"
    const val FAILURE_SOMETHING_WENT_WRONG = "Failure Something went wrong"
    const val FAILURE_SERVER_NOT_RESPONDING = "Oops! server not responding"
    const val FAILURE_INTERNET_CONNECTION = "Internet connection error"
}