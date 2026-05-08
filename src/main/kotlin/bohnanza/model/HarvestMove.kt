package bohnanza.model

class HarvestMove (
    val fieldIndex: Int,
    val player: Player,
): Move {
    override fun validate(): Boolean {
        return !player.beanField[fieldIndex].isEmpty()
    }

    override fun execute(): List<Card> {
        if (validate()) {
            val coinsEarned = player.beanField[fieldIndex].coinsFromField()
            val cards = player.beanField[fieldIndex].cardsFromField().toMutableList()
            val coinsForPlayer = cards.take(coinsEarned)
            val discardCards = cards.drop(coinsEarned)
            player.coinCards.addAll(coinsForPlayer)
            return discardCards
        }
        return emptyList()
    }
}

