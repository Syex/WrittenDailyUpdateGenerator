package de.syex.dailyupdate

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        title = "Daily Update Generator",
        state = WindowState(size = DpSize(800.dp, 1200.dp), position = WindowPosition(Alignment.Center)),
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}
