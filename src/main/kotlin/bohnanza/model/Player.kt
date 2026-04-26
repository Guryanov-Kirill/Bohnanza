package bohnanza.model

class Player(
    val id: Int,
    val name: String,
    val handCards: MutableList<Card> = mutableListOf(),
    var hasExtraField: Boolean = false,
    val coinCards: MutableList<Card> = mutableListOf(),
) {
    val beanField: List<BeanField> = listOf(
        BeanField(0),
        BeanField(1),
        BeanField(2)
    )

    fun addCoins(cards: List<Card>) {
        coinCards.addAll(cards)
    }

    fun buyExtraField(): List<Card> {
        if (coins() > 2 && !hasExtraField) {
            val pay = coinCards.take(3)
            for (card in pay) {coinCards.remove(card)}
            hasExtraField = true
            return pay
        }
        throw InsufficientFundsException("Недостаточно монет для покупки третьего поля. Нужно: 3, у вас: ${coins()}")
    }

    fun getHandSize(): Int {
        return handCards.size
    }

    fun coins(): Int {
        return coinCards.size
    }
}
