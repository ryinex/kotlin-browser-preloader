package com.ryinex.kotlin.js

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeViewport
import com.ryinex.kotlin.js.theme.AppTheme
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        AppTheme {
            Scaffold {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        "Awesome Kotlin <3",
                        style = MaterialTheme.typography.displayLarge.copy(color = MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        "Todo App Example!",
                        style = MaterialTheme.typography.displaySmall.copy(color = MaterialTheme.colorScheme.primary)
                    )

                    TODOs()
                }
            }
        }
    }
}