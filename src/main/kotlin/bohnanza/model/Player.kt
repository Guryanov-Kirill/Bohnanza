package bohnanza.model

class Player(
    val id: Int,
    val name: String,
    var coins: Int = 0,
    val handCards: MutableList<Card> = mutableListOf(),
    var hasExtraField: Boolean = false,
) {
    val beanField: List<BeanField> = listOf(
        BeanField(0),
        BeanField(1),
        BeanField(2)
    )

    fun addCoins(n: Int) {
        coins += n
    }

    fun buyExtraField(): Boolean {
        if (coins > 2 && !hasExtraField) {
            coins -= 3
            hasExtraField = true
            return true
        }
        return false
    }

    fun getHandSize(): Int {
        return handCards.size
    }
}
