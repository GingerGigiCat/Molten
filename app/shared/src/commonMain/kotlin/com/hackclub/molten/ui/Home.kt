package com.hackclub.molten.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hackclub.molten.MoltenTheme
import com.hackclub.molten.theming.WavyShape

@Composable
fun HomePage() {
    val cardColors = CardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.onSurface,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        disabledContentColor = MaterialTheme.colorScheme.onSurface
    )

    Row(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxHeight().weight(1 / 3f)) {
            Card(Modifier.fillMaxSize().padding(15.dp), shape = WavyShape(), colors = cardColors) {
                SelectionContainer {
                    Text(
                        "Meow this is a panel. maybe a volcano that fills up as you complete time could go here",
                        Modifier.padding(10.dp)
                    )
                }
            }
        }
        Column(Modifier.fillMaxHeight().weight(2 / 3f)) {
            Card(Modifier.fillMaxSize().weight(1f).padding(15.dp), shape = WavyShape(), colors = cardColors) {
                SelectionContainer {
                    Text(
                        "This is also a panel, perhaps some stats or things about other users could go here. a leaderboard?",
                        Modifier.padding(10.dp)
                    )
                }
            }
            Card(Modifier.fillMaxSize().weight(1f).padding(15.dp), shape = WavyShape(), colors = cardColors) {
                SelectionContainer {
                    Text(
                        "This is a different panel, maybe a small project list could go here",
                        Modifier.padding(10.dp)
                    )
                }
            }
        }
    }
}