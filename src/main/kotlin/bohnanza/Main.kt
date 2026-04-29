package bohnanza

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import bohnanza.viewmodel.GameViewModel
import androidx.compose.runtime.remember
import StartScreen
import GameScreen


@Composable
fun App() {
    val viewModel = remember { GameViewModel() }

    MaterialTheme {
        if (viewModel.game == null) {
            StartScreen(viewModel)
        } else {
            GameScreen(viewModel)
        }
    }
}

fun main()  = application {
    Window(onCloseRequest = ::exitApplication, title = "Bohnanza Game") {
        App()
    }
}