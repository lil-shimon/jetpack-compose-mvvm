package com.example.jetpack_compose_mvvm

import android.app.Application

class TodoApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}