package bohnanza.model

class Trade(
    val initiatorId: Int,
    val partnerId: Int,
    val offeredCards: MutableList<Card> = mutableListOf(),
    val requestedCards: MutableList<Card> = mutableListOf(),
    var accepted: Boolean = false,
    var proposed: Boolean = false
) {
    fun propose() {
        require(offeredCards.isNotEmpty() || requestedCards.isNotEmpty()) {
            "Нельзя предложить пустую сделку"
        }
        require(!proposed) { "Сделка уже предложена" }
        proposed = true
    }

    fun accept() {
        require(proposed) { "Сделка ещё не была предложена" }
        accepted = true
    }

    fun reject() {
        proposed = false
        accepted = false
        offeredCards.clear()
        requestedCards.clear()
    }
}
