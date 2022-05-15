package com.example.jetpack_compose_mvvm

import android.content.Context
import com.example.jetpack_compose_mvvm.data.TodoDataSource
import com.example.jetpack_compose_mvvm.data.room.TodoDatabase

object Graph {

    lateinit var database: TodoDatabase
        private set

    val todoRepo by lazy {
        TodoDataSource(database.todoDao())
    }

    fun provide(context: Context) {
        database = TodoDatabase.getDatabase(context)
    }
}