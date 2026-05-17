package bohnanza.model

import bohnanza.data.Database

class Game (
    val players: List<Player>,
    val deck: Deck,
){
    var currentPlayerIndex = 0
    var currentTurn: Turn? = null
    var status: GameStatus = GameStatus.WAITING

    fun getCurrentPlayer(): Player = players[currentPlayerIndex]

    private fun requireInProgress(): Turn {
        require(status == GameStatus.IN_PROGRESS) { "Игра не запущена" }
        return currentTurn ?: throw IllegalStateException("Текущий ход не инициализирован")
    }

    fun startGame() {
        GameHistory.clearTurns()
        deck.startDeckFactory()
        deck.shuffle()
        for (player in players){
            player.handCards.addAll(deck.draw(5))
        }
        status = GameStatus.IN_PROGRESS
        currentTurn = Turn(players[currentPlayerIndex], deck)
    }

    fun nextTurn() {
        val turn = currentTurn ?: throw IllegalStateException("Игра не начата")
        require(turn.phase == TurnPhase.DRAW_FROM_DECK) { "Ход ещё не завершён" }
        GameHistory.addTurn(turn)
        if (deck.isThirdCycle()) {
            endGame()
            return
        }
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size
        currentTurn = Turn(players[currentPlayerIndex], deck)
    }

    fun endGame() {
        for (player in players) {
            for (field in player.beanField) {
                if (!field.isEmpty()) {
                    val coins = field.coinsFromField()
                    val cards = field.cardsFromField().toMutableList()
                    player.addCoins(cards.take(coins))
                    cards.drop(coins).forEach { deck.discard(it) }
                }
            }
        }
        status = GameStatus.FINISHED
        saveGameHistory()
    }

    fun saveGameHistory() {
        GameHistory.add(this)
        Database.saveGame(this)
    }

    fun getWinner(): List<Player> {
        if (status != GameStatus.FINISHED) return emptyList()
        val maxCoins = players.maxOf { it.coins() }
        return players.filter { it.coins() == maxCoins }
    }

    fun getRanking(): List<Player> = players.sortedByDescending { it.coins() }

    // Посадить карту
    fun plantCard(fieldIndex: Int, card: Card, forced: Boolean = true) {
        val turn = requireInProgress()
        require(turn.phase == TurnPhase.PLANT_MANDATORY) { "Сажать карты можно только в фазе PLANT_MANDATORY" }
        turn.plantCard(fieldIndex, card)
    }

    // Собрать урожай
    fun harvestField(fieldIndex: Int) {
        val turn = requireInProgress()
        val discarded = turn.harvestField(fieldIndex)
        discarded.forEach { deck.discard(it) }
    }

    // Открыть две карты из колоды
    fun drawTwoCards() {
        val turn = requireInProgress()
        require(turn.phase == TurnPhase.DRAW_AND_TRADE) { "Открывать карты можно только в фазе DRAW_AND_TRADE" }
        turn.drawTwoCards()
    }

    // Взять три карты в руку
    fun drawThreeCards() {
        val turn = requireInProgress()
        require(turn.phase == TurnPhase.DRAW_FROM_DECK) { "Брать карты из колоды можно только в фазе DRAW_FROM_DECK" }
        turn.drawThreeCards()
    }

    fun plantOpenCard(card: Card, fieldIndex: Int) {
        val turn = requireInProgress()
        require(turn.phase == TurnPhase.DRAW_AND_TRADE) {
            "Сажать открытые карты можно только в фазе DRAW_AND_TRADE"
        }
        turn.plantOpenCard(card, fieldIndex)
    }

    // Перейти к следующей фазе внутри хода
    fun nextPhase() {
        requireInProgress().nextPhase()
    }

    // Предложить сделку
    fun proposeTrade(trade: Trade) {
        val turn = requireInProgress()
        require(turn.phase == TurnPhase.DRAW_AND_TRADE) {
            "Торговать можно только в фазе DRAW_AND_TRADE"
        }
        require(trade.initiatorId == getCurrentPlayer().id) {
            "Предлагать сделку может только текущий игрок"
        }
        trade.propose()
    }

    // Принять сделку и обменять карты
    fun acceptTrade(trade: Trade) {
        requireInProgress()
        val initiator = players.find { it.id == trade.initiatorId }
            ?: throw IllegalStateException("Инициатор сделки не найден")
        val partner = players.find { it.id == trade.partnerId }
            ?: throw IllegalStateException("Партнёр сделки не найден")

        trade.accept()

        // Перекладываем карты
        initiator.handCards.removeAll(trade.offeredCards)
        partner.handCards.addAll(trade.offeredCards)

        partner.handCards.removeAll(trade.requestedCards)
        initiator.handCards.addAll(trade.requestedCards)
    }

    // Отменить сделку
    fun rejectTrade(trade: Trade) {
        requireInProgress()
        trade.reject()
    }

    // Покупка третьего поля
    fun buyExtraField() {
        requireInProgress()
        val player = getCurrentPlayer()
        val paid = player.buyExtraField()   // бросает InsufficientFundsException если денег нет
        paid.forEach { deck.discard(it) }
    }

}

