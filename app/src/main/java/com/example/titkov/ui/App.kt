package com.example.titkov.ui

import android.app.Application
import android.content.Context
import com.example.titkov.di.AppComponent
import com.example.titkov.di.DaggerAppComponent

class App : Application() {
    val appComponent by lazy {
        DaggerAppComponent.factory()
            .create(this)
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> applicationContext.appComponent
    }