package bohnanza.model

class PlayerRating(
    val player: Player,
    val totalGames: Int,
    val totalWins: Int,
    val averageCoins: Double,
    val ratingScore: Double,
) {
    companion object {
        fun calculate(player: Player, history: GameHistory): PlayerRating {
            val games = history.getGamesBy(player)
            val totalGames = games.size
            val totalWins = games.count { game ->
                game.status == GameStatus.FINISHED &&
                        game.getWinner().any { it.id == player.id }
            }
            val averageCoins = player.coins().toDouble()
            val ratingScore = totalWins * 10.0 + averageCoins
            return PlayerRating(player, totalGames, totalWins, averageCoins, ratingScore)
        }
    }
}