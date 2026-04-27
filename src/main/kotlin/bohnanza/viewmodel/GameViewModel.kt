package bohnanza.viewmodel

import androidx.compose.runtime.*
import bohnanza.model.*

class GameViewModel {
    var game by mutableStateOf<Game?>(null)
        private set

    var currentTurn by mutableStateOf<Turn?>(null)
        private set

    fun startGame() {
        TODO()
    }
}