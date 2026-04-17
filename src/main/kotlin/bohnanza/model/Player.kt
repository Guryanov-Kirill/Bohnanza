package bohnanza.model

class Player(
    val id: Int,
    val name: String,
    var coins: Int = 0,
    val handCards: MutableList<Card> = mutableListOf(),
    var hasExtraField: Boolean = false
) {
    // Список грядок игрока
    val beanFields: List<BeanField> = TODO()

    fun addCoins(n: Int) {
        TODO("Прибавление монет")
    }

    fun buyExtraField(): Boolean {
        TODO("Покупка дополнитльного поля")
    }

    fun getHandSize(): Int {
        TODO("Количество карт в руке")
    }
}
