package bohnanza.model

interface Move {
    fun validate(): Boolean
    fun execute(): List<Card>
}