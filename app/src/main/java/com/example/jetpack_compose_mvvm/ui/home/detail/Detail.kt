package com.example.jetpack_compose_mvvm.ui.home.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpack_compose_mvvm.data.Todo

@Composable
fun DetailScreen(
    selectedId: Long,
    onNavigate: () -> Unit
) {
    val viewModel =
        viewModel(
            DetailViewModel::class.java,
            factory = DetailViewModelFactory(selectedId)
        )

    val state by viewModel.state.collectAsState()
    DetailScreenComponent(
        todoText = state.todo,
        onTodoTextChange = { viewModel.onTextChange(it) },
        todoTime = state.time,
        onTodoTimeChange = { viewModel.onTimeChange(it) },
        onNavigate = { onNavigate() },
        onSaveTodo = { viewModel.insert(it) },
        selectedId = state.selectedId
    )
}

@Composable
fun DetailScreenComponent(
    todoText: String,
    onTodoTextChange: (String) -> Unit,
    todoTime: String,
    onTodoTimeChange: (String) -> Unit,
    onNavigate: () -> Unit,
    onSaveTodo: (Todo) -> Unit,
    selectedId: Long
) {

    val isTodoEdit = selectedId == -1L
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            value = todoText,
            onValueChange = { onTodoTextChange(it) },
            label = { Text(text = "Enter Todo") }
        )
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            value = todoTime,
            onValueChange = { onTodoTimeChange(it) },
            label = {
                Text(
                    text = "Enter time"
                )
            })
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = {
            val todo = if (isTodoEdit) Todo(todoText, todoTime, false)
            else Todo(
                todoText,
                todoTime,
                false,
                id = selectedId
            )
            onSaveTodo(todo)
            onNavigate()
        }) {
            val text = if (isTodoEdit) "save todo" else "update todo"
            Text(text = text)
        }
    }
}