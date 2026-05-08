package bohnanza.model

class Turn (
    val player: Player,
    val deck: Deck
){
    var phase: TurnPhase = TurnPhase.PLANT_MANDATORY
    val openCards: MutableList<Card> = mutableListOf()
    var plantedThisPhase: Int = 0

    fun plantCard(fieldIndex: Int, card: Card) {
        require(player.handCards.contains(card)) { "Карта не найдена в руке игрока" }

        val move = PlantMove(player, card, fieldIndex)
        require(move.validate()) { "Невозможно посадить карту на это поле" }
        move.execute()
        player.handCards.remove(card)
    }

    fun harvestField(fieldIndex: Int): List<Card> {
        val move = HarvestMove(fieldIndex, player)
        return move.execute()
    }

    fun drawTwoCards() {
        require(openCards.isEmpty()) { "Карты уже были открыты в этом ходу" }
        openCards.addAll(deck.draw(2))
    }

    fun completeTrade() {
        require(phase == TurnPhase.DRAW_AND_TRADE) { "completeTrade можно вызвать только в фазе DRAW_AND_TRADE" }
        player.handCards.addAll(openCards)
        openCards.clear()
    }

    fun drawThreeCards() {
        player.handCards.addAll(deck.draw(3))
    }

    fun plantOpenCard(card: Card, fieldIndex: Int) {
        require(openCards.contains(card)) { "Карта не найдена среди открытых карт" }
        if (plantedThisPhase == 0) {
            require(card == player.handCards[0]) { "Первой нужно сажать первую карту из руки" }
        }
        if (plantedThisPhase == 1) {
            require(card == player.handCards[0]) { "Второй можно сажать только первую карту из руки" }
        }
        val move = PlantMove(player, card, fieldIndex)
        require(move.validate()) { "Невозможно посадить карту на это поле" }
        if (move.validate()) {
            move.execute()
            openCards.remove(card)
        }
    }

    fun nextPhase(){
        require(plantedThisPhase >= 1) { "Необходимо посадить хотя бы одну карту" }
        if (phase == TurnPhase.DRAW_AND_TRADE && openCards.isNotEmpty()) {
            completeTrade()
        }
        phase = when (phase){
            TurnPhase.PLANT_MANDATORY -> TurnPhase.DRAW_AND_TRADE
            TurnPhase.DRAW_AND_TRADE -> TurnPhase.DRAW_FROM_DECK
            TurnPhase.DRAW_FROM_DECK -> TurnPhase.PLANT_MANDATORY
        }
    }
}