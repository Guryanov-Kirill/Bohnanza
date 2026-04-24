package bohnanza.model

class BeanField (
    val fieldIndex: Int,
    var beanType: BeanType? = null,
    var firstCard: Card? = null,
    var count: Int = 0
) {
    fun plant(card: Card) {
        if (canAccept(card)) {
            if (isEmpty()){
                firstCard = card
                beanType = card.beanType
            }
            count++
        }
    }

    fun harvest(): Int {
        var coins = firstCard?.getCoinsFor(count) ?: 0
        beanType = null
        firstCard = null
        count = 0
        return coins
    }

    fun isEmpty(): Boolean {
        return count == 0
    }

    fun canAccept(card: Card): Boolean {
        return isEmpty() || beanType == card.beanType
    }
}