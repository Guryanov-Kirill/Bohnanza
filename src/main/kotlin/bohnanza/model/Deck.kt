package bohnanza.model

class Deck(
    private var cards: MutableList<Card> = mutableListOf(),
    private var discardPile: MutableList<Card> = mutableListOf(),
    var reshuffleCount: Int = 0
) {
    fun shuffle() {
        TODO("Перемешать список карт")
    }

    fun draw(n: Int): List<Card> {
        TODO("Выдать n карт")
    }

    fun discard(card: Card) {
        TODO("Положить карту в сброс")
    }

    fun reshuffle() {
        TODO("Перемешиваем карты и кладём в колоду")
    }

    fun isThirdCycle(): Boolean {
        TODO("Проверяет круг игры")
    }
}
