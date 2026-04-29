package bohnanza.viewmodel

import androidx.compose.runtime.*
import bohnanza.model.*

class GameViewModel {
    var game by mutableStateOf<Game?>(null)
        private set

    var currentTurn by mutableStateOf<Turn?>(null)
        private set

    fun startGame(playerNames: List<String>) {
        val players = playerNames.mapIndexed { index, playerName -> Player(index, playerName) }
        val newGame = Game(players, Deck())
        newGame.startGame()
        game = newGame
        currentTurn = game?.currentTurn
    }

    fun nextTurn() {
        game?.nextTurn()
        currentTurn = game?.currentTurn
    }

    fun plantCard(fieldIndex: Int, card: Card, forced: Boolean) {
        currentTurn?.plantCard(fieldIndex, card, forced)
    }

    fun harvestField(fieldIndex:Int): List<Card> {
        return currentTurn?.harvestField(fieldIndex) ?: emptyList()
    }
}