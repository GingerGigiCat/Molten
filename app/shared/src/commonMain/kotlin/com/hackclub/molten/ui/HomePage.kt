package com.hackclub.molten.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hackclub.molten.theming.WavyShape
import com.hackclub.molten.theming.accentCardColors
import moltenplatform.app.shared.generated.resources.Res
import moltenplatform.app.shared.generated.resources.molten_progress_volcano
import org.jetbrains.compose.resources.painterResource

@Composable
@Preview
fun HomePage() {
    val cardColors = CardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.onSurface,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        disabledContentColor = MaterialTheme.colorScheme.onSurface
    )
    val completedTime by remember { mutableStateOf(23) }
    val totalTime = 55

    Row(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxHeight()) {
            Card(Modifier.height(IntrinsicSize.Min).padding(15.dp).width(IntrinsicSize.Min), shape = WavyShape(), colors = cardColors) {
                Column(Modifier.fillMaxSize()) {
                    SelectionContainer {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Card(modifier = Modifier.weight(1f)) {}
                            Card(
                                modifier = Modifier.fillMaxHeight(0.4f) // THIS IS THE PERCENTAGE OF THEIR PROGRESS
                                    .fillMaxWidth(0.25f).padding(top = 20.dp, bottom = 20.dp),
                                colors = accentCardColors
                            ) {}
                        }
                        Image(
                            painterResource(Res.drawable.molten_progress_volcano),
                            "Volcano Image",
                            modifier = Modifier
                                .padding(20.dp).fillMaxHeight()
                        )
                    }
                    Text("${completedTime}h/${totalTime}h")
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