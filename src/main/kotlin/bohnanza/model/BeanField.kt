package bohnanza.model

class BeanField (
    val fieldIndex: Int,
    var beanType: BeanType? = null,
    val cards: MutableList<Card> = mutableListOf()
) {
    fun plant(card: Card) {
        if (canAccept(card)) {
            if (isEmpty()) beanType = card.beanType
            cards.add(card)
        }
    }

    fun coinsFromField(): Int {
        val coins = cards.firstOrNull()?.getCoinsFor(cards.size) ?: 0
        return coins
    }

    fun cardsFromField(): List<Card> {
        val temp = cards.toList()
        cards.clear()
        beanType = null
        return temp
    }

    fun isEmpty(): Boolean {
        return cards.size == 0
    }

    fun canAccept(card: Card): Boolean {
        return isEmpty() || beanType == card.beanType
    }
}