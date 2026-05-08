package bohnanza.model

class PlantMove (
    val player: Player,
    val card: Card,
    val fieldIndex: Int
): Move {
    private fun fieldIndexCheck(index: Int): Boolean {
        if (index == 2) {
            return player.hasExtraField
        } else if (index in 0 .. 1) {return true}
        return false
    }

    override fun validate(): Boolean {
        if (!fieldIndexCheck(fieldIndex)) return false
        return player.beanField[fieldIndex].canAccept(card)
    }

    override fun execute(): List<Card> {
        if (validate()) {
            player.beanField[fieldIndex].plant(card)
        }
        return emptyList()
    }
}