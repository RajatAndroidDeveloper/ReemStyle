package com.reemastyle

import android.app.Application
import com.reemastyle.preferences.Preferences

class ReemStyleApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Preferences.initPreferences(this)
    }
}