package com.reemastyle.preferences

import android.content.Context

class Preferences {
    companion object {

        public var prefs: EncryptedPreferences? = null
        fun initPreferences(context: Context) {
            prefs = EncryptedPreferences(context)
        }
    }
}