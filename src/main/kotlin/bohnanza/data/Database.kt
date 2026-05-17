package bohnanza.data

import bohnanza.model.Game
import bohnanza.model.GameStatus
import java.sql.Connection
import java.sql.DriverManager

object Database {
    private const val URL = "jdbc:sqlite:bohnanza.db"

    private fun connect(): Connection {
        return DriverManager.getConnection(URL)
    }

    fun init() {
        val conn = connect()

        // Таблица игроков
        conn.createStatement().execute("""
            CREATE TABLE IF NOT EXISTS players (
                id      INTEGER PRIMARY KEY,
                name    TEXT    NOT NULL
            )
        """)

        // Таблица партий
        conn.createStatement().execute("""
            CREATE TABLE IF NOT EXISTS games (
                id      INTEGER PRIMARY KEY AUTOINCREMENT,
                status  TEXT    NOT NULL,
                date    TEXT    NOT NULL
            )
        """)

        // Таблица «кто участвовал в партии и сколько монет набрал»
        conn.createStatement().execute("""
            CREATE TABLE IF NOT EXISTS game_players (
                game_id    INTEGER NOT NULL,
                player_id  INTEGER NOT NULL,
                coins      INTEGER NOT NULL,
                is_winner  INTEGER NOT NULL DEFAULT 0,
                PRIMARY KEY (game_id, player_id)
            )
        """)

        // Таблица ходов
        conn.createStatement().execute("""
            CREATE TABLE IF NOT EXISTS turns (
                id         INTEGER PRIMARY KEY AUTOINCREMENT,
                game_id    INTEGER NOT NULL,
                player_id  INTEGER NOT NULL,
                phase      TEXT    NOT NULL
            )
        """)

        conn.close()
    }

    // Сохранить законченную партию в базу
    fun saveGame(game: Game) {
        if (game.status != GameStatus.FINISHED) return

        val conn = connect()

        // Сохраняем саму партию
        val gameStmt = conn.prepareStatement(
            "INSERT INTO games (status, date) VALUES (?, datetime('now'))"
        )
        gameStmt.setString(1, game.status.name)
        gameStmt.executeUpdate()

        // Узнаём id только что добавленной партии
        val gameId = conn.createStatement()
            .executeQuery("SELECT last_insert_rowid()")
            .getInt(1)

        val winners = game.getWinner().map { it.id }

        // Сохраняем игроков и их результаты
        for (player in game.players) {

            // Добавляем игрока (если он уже есть — ничего не происходит)
            val playerStmt = conn.prepareStatement(
                "INSERT OR IGNORE INTO players (id, name) VALUES (?, ?)"
            )
            playerStmt.setInt(1, player.id)
            playerStmt.setString(2, player.name)
            playerStmt.executeUpdate()

            // Результат игрока в этой партии
            val resultStmt = conn.prepareStatement(
                """INSERT INTO game_players (game_id, player_id, coins, is_winner)
                   VALUES (?, ?, ?, ?)"""
            )
            resultStmt.setInt(1, gameId)
            resultStmt.setInt(2, player.id)
            resultStmt.setInt(3, player.coins())
            resultStmt.setInt(4, if (player.id in winners) 1 else 0)
            resultStmt.executeUpdate()
        }

        // Сохраняем ходы из GameHistory
        for (turn in bohnanza.model.GameHistory.getAllTurns()) {
            val turnStmt = conn.prepareStatement(
                """INSERT INTO turns (game_id, player_id, phase)
                   VALUES (?, ?, ?)"""
            )
            turnStmt.setInt(1, gameId)
            turnStmt.setInt(2, turn.player.id)
            turnStmt.setString(3, turn.phase.name)
            turnStmt.executeUpdate()
        }

        conn.close()
    }

    // Загрузить статистику игрока по имени
    fun getPlayerStats(playerName: String): String {
        val conn = connect()

        val stmt = conn.prepareStatement("""
            SELECT 
                p.name,
                COUNT(gp.game_id)               AS total_games,
                SUM(gp.is_winner)               AS total_wins,
                AVG(gp.coins)                   AS avg_coins
            FROM players p
            JOIN game_players gp ON p.id = gp.player_id
            WHERE p.name = ?
            GROUP BY p.id
        """)
        stmt.setString(1, playerName)

        val rs = stmt.executeQuery()

        val result = if (rs.next()) {
            """
            Игрок:       ${rs.getString("name")}
            Партий:      ${rs.getInt("total_games")}
            Побед:       ${rs.getInt("total_wins")}
            Среднее монет: ${"%.1f".format(rs.getDouble("avg_coins"))}
            """.trimIndent()
        } else {
            "Игрок '$playerName' не найден в базе данных"
        }

        conn.close()
        return result
    }
}
