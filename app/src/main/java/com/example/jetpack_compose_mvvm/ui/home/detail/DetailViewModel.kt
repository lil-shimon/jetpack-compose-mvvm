package com.example.jetpack_compose_mvvm.ui.home.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jetpack_compose_mvvm.Graph
import com.example.jetpack_compose_mvvm.data.Todo
import com.example.jetpack_compose_mvvm.data.TodoDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class DetailViewModel(
    private val todoDataSource: TodoDataSource = Graph.todoRepo,
    private val id: Long
) : ViewModel() {
    private val todoText = MutableStateFlow("")
    private val todoTime = MutableStateFlow("")
    private val selectedId = MutableStateFlow(-1L)

    private val _state = MutableStateFlow(DetailViewState())
    val state: StateFlow<DetailViewState>
        get() = _state

    init {
        viewModelScope.launch {
            combine(todoText, todoTime, selectedId) { text, time, selectedId ->
                DetailViewState(text, time, selectedId)
            }.collect {
                _state.value = it
            }
        }
    }

    init {
        viewModelScope.launch {
            todoDataSource.selectAll.collect { todo ->
                todo.find {
                    it.id == selectedId.value
                }.also {
                    selectedId.value = it?.id ?: -1
                    if (selectedId.value != -1L) {
                        todoText.value = it?.todo ?: ""
                        todoTime.value = it?.time ?: ""
                    }
                }
            }
        }
    }

    fun onTextChange(newText: String) {
        todoText.value = newText
    }

    fun onTimeChange(newTime: String) {
        todoTime.value = newTime
    }

    fun insert(todo: Todo) = viewModelScope.launch {
        todoDataSource.insertTodo(todo)
    }
}

data class DetailViewState(
    val todo: String = "",
    val time: String = "",
    val selectedId: Long = -1L
)

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val id: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(id = id) as T
        } else {
            throw IllegalArgumentException("Unknown")
        }
    }
}