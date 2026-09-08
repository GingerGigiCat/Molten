package com.hackclub.molten.ui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hackclub.molten.theming.WavyShape

@Composable
fun NavigationButton(text: String, onClick: () -> Unit = {}, modifier: Modifier = Modifier) {
    val buttonHeight = 50.dp
    val buttonPadding = 15.dp
    val buttonTextStyle = MaterialTheme.typography.headlineSmallEmphasized
    Button(
        onClick = onClick,
        shape = WavyShape(),
        modifier = modifier.padding(buttonPadding).height(buttonHeight)
    ) {
        Text(text, style = buttonTextStyle)
    }
}