package com.hackclub.molten

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hackclub.molten.theming.WavyShape
import com.hackclub.molten.ui.HomePage


@Composable
@Preview
fun App() {
    MoltenTheme(true) {
        Scaffold(modifier = Modifier.fillMaxSize(), containerColor = MaterialTheme.colorScheme.background) {
            var showContent by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .safeContentPadding()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                // Top Bar
                Card(
                    shape = WavyShape(),
                    modifier = Modifier.fillMaxWidth().padding(15.dp),
                    colors = CardColors(
                        containerColor = Color(0xFFFC4C02),
                        contentColor = Color(0xFFFFFBFF),
                        disabledContainerColor = Color(0xFFFC4C02),
                        disabledContentColor = Color(0xFFFFFBFF)
                    )
                ) {
                    val buttonHeight = 50.dp
                    val buttonPadding = 15.dp
                    val buttonTextStyle = MaterialTheme.typography.headlineSmallEmphasized
                    Row {
                        Button(
                            onClick = { showContent = !showContent },
                            shape = WavyShape(),
                            modifier = Modifier.padding(buttonPadding).weight(1f).height(buttonHeight)
                        ) {
                            Text("Home", style = buttonTextStyle)
                        }

                        Button(
                            onClick = { showContent = !showContent },
                            shape = WavyShape(),
                            modifier = Modifier.padding(buttonPadding).weight(1f).requiredHeight(buttonHeight)
                        ) {
                            Text("Projects", style = buttonTextStyle)
                        }

                        Button(
                            onClick = { showContent = !showContent },
                            shape = WavyShape(),
                            modifier = Modifier.padding(buttonPadding).weight(1f).requiredHeight(buttonHeight)
                        ) {
                            Text("Shop", style = buttonTextStyle)
                        }

                        Button(
                            onClick = { showContent = !showContent },
                            shape = WavyShape(),
                            modifier = Modifier.padding(buttonPadding).weight(1f).requiredHeight(buttonHeight)
                        ) {
                            Text("Event", style = buttonTextStyle)
                        }
                    }
                }

                // Main Content
                if (showContent) {
                    HomePage()
                }

            }
        }
    }
}