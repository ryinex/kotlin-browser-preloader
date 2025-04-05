package com.ryinex.kotlin.js

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.russhwolf.settings.Settings
import com.russhwolf.settings.serialization.decodeValue
import com.russhwolf.settings.serialization.encodeValue
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

private val settings = Settings()

@Serializable
private data class Todo(val text: String, val done: Boolean, val id: String)

private class Repository() {
    fun get(): List<Todo> = settings.decodeValue(serializer = ListSerializer(Todo.serializer()), "todos", listOf())

    @OptIn(ExperimentalUuidApi::class)
    fun insert(todo: String): List<Todo> {
        val todo = Todo(todo, false, Uuid.random().toString())
        val todos = get() + todo
        settings.encodeValue(serializer = ListSerializer(Todo.serializer()), "todos", todos)
        return todos
    }

    fun update(todo: Todo): List<Todo> {
        val todos = get().toMutableList()
        val index = todos.indexOfFirst { it.id == todo.id }
        todos[index] = todo
        settings.encodeValue(serializer = ListSerializer(Todo.serializer()), "todos", todos)
        return todos
    }

    fun delete(todo: Todo): List<Todo> {
        val todos = get().toMutableList()
        todos.remove(todo)
        settings.encodeValue(serializer = ListSerializer(Todo.serializer()), "todos", todos)
        return todos
    }
}

private val repository = Repository()

@Composable
internal fun TODOs() {
    var todos by remember { mutableStateOf<List<Todo>>(repository.get()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TODOInsertRow(modifier = Modifier.fillMaxWidth()) { todos = repository.insert(it) }

        HorizontalDivider()

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(todos, key = { index, item -> item.id }) { index, todo ->
                TODOItem(
                    modifier = Modifier.fillMaxWidth(),
                    index = index,
                    todo = todo,
                    onEdit = { todos = repository.update(todo.copy(text = it)) },
                    onDelete = { todos = repository.delete(todo) },
                    onComplete = { todos = repository.update(todo.copy(done = it)) }
                )
            }
        }
    }
}

@Composable
private fun TODOInsertRow(modifier: Modifier = Modifier, onInsert: (String) -> Unit) {
    var value by remember { mutableStateOf("") }
    val isInsertAvailable = remember(value) { mutableStateOf(value.isNotBlank()) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SupportIconButton(
            painter = rememberVectorPainter(Icons.Filled.Add),
            enabled = isInsertAvailable.value,
            onClick = {
                onInsert(value)
                value = ""
            }
        )

        AppTextField(
            modifier = Modifier.weight(1f),
            value = value,
            onValueChange = { value = it },
            hint = "Todo input",
            keyboardAction = KeyboardActions { if (isInsertAvailable.value) onInsert(value) }

        )
    }
}

@Composable
private fun TODOItem(
    modifier: Modifier = Modifier,
    index: Int,
    todo: Todo,
    onEdit: (String) -> Unit,
    onComplete: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    var value by remember { mutableStateOf(todo.text) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SupportIconButton(
            painter = rememberVectorPainter(Icons.Filled.Add),
            enabled = value != todo.text,
            onClick = { onEdit(value) }
        )

        SupportIconButton(painter = rememberVectorPainter(Icons.Filled.Delete), enabled = true, onClick = onDelete)

        SupportIconButton(
            painter = rememberVectorPainter(if (!todo.done) Icons.Filled.Done else Icons.Filled.Clear),
            enabled = true,
            onClick = { onComplete(!todo.done) }
        )

        Text(text = "${index + 1}. ", style = LocalTextStyle.current)

        key(todo) {
            AppTextField(
                modifier = Modifier.weight(1f),
                value = value,
                onValueChange = { value = it },
                hint = "Todo",
                textStyle = if (todo.done) {
                    LocalTextStyle.current.copy(textDecoration = TextDecoration.LineThrough)
                } else {
                    LocalTextStyle.current
                },
                keyboardAction = KeyboardActions { if (value != todo.text) onEdit(value) }
            )
        }
    }
}

@Composable
private fun SupportIconButton(painter: Painter, enabled: Boolean, onClick: () -> Unit) {
    IconButton(
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(painter = painter, contentDescription = null)
    }
}

@Composable
private fun AppTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = LocalTextStyle.current,
    hint: String,
    keyboardAction: KeyboardActions = KeyboardActions.Default
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        keyboardActions = keyboardAction,
        textStyle = textStyle.copy(color = LocalContentColor.current),
        cursorBrush = SolidColor(LocalContentColor.current),
    ) {
        if (value.isBlank()) {
            Text(
                text = hint,
                style = textStyle,
                color = LocalContentColor.current.copy(alpha = 0.5f)
            )
        }
        it()
    }
}