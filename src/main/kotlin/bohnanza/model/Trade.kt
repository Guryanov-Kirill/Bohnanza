package bohnanza.model

class Trade(
    val initiatorId: Int,
    val partnerId: Int,
    val offeredCards: MutableList<Card> = mutableListOf(),
    val requestedCards: MutableList<Card> = mutableListOf(),
    var accepted: Boolean = false
) {
    fun propose() {
        TODO("Отправить предложение партнеру")
    }

    fun accept() {
        accepted = true
    }

    fun reject() {
        accepted = false
        offeredCards.clear()
        requestedCards.clear()
    }
}
