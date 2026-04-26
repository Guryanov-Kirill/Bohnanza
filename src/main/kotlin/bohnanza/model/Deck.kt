package bohnanza.model

class Deck(
    private var cards: MutableList<Card> = mutableListOf(),
    private var discardPile: MutableList<Card> = mutableListOf(),
    var reshuffleCount: Int = 0
) {
    fun shuffle() {
        cards.shuffle()
    }

    fun draw(n: Int): List<Card> {
        if (cards.size >= n) {
            val takenCards = cards.take(n)
            repeat(takenCards.size) { cards.removeAt(0) }
            return takenCards
        } else if (!isThirdCycle()){
            reshuffle()
            return draw(n)
        } else {
            val copy = cards.toList()
            cards.clear()
            return copy
        }
    }

    fun discard(card: Card) {
        discardPile.add(card)
    }

    fun reshuffle() {
        repeat(discardPile.size){
            cards.add(discardPile[0])
            discardPile.removeAt(0)
        }
        cards.shuffle()
        reshuffleCount++
    }

    fun isThirdCycle(): Boolean {
        return reshuffleCount >= 3
    }
}
