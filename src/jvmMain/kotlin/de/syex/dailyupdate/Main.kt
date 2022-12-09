package de.syex.dailyupdate

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        title = "Daily Update Generator",
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}
