package bohnanza.model

object GameHistory {
    private val games = mutableListOf<Game>()
    private val turns = mutableListOf<Turn>()

    fun add(game: Game) {
        games.add(game)
    }

    fun addTurn(turn: Turn) {
        turns.add(turn)
    }

    fun getGamesBy(player: Player): List<Game> {
        return games.filter { it.players.contains(player) }
    }

    fun getAllGames(): List<Game> {
        return games.toList()
    }

    fun getTurnsBy(player: Player): List<Turn> {
        return turns.filter { it.player.id == player.id }
    }

    fun getAllTurns(): List<Turn> {
        return turns.toList()
    }

    fun clearTurns() {
        turns.clear()
    }
}