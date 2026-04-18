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
        TODO("Установить accepted = true и запустить передачу карт")
    }

    fun reject() {
        TODO("Установить accepted = false и очистить списки предложений")
    }
}
