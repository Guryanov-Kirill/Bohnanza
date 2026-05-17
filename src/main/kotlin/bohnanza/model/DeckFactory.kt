package bohnanza.model

class DeckFactory () {
    fun createDeck(): List<Card> {
        var index = 0
        val cards: MutableList<Card> = mutableListOf()
        for (beantype in BeanType.entries) {
            repeat (beantype.totalCards) {
                cards.add(Card(index, beantype))
                index++
            }
        }
        return cards
    }
}