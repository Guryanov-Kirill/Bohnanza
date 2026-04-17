package bohnanza.model

class BeanField (
    val fieldIndex: Int,
    var beantype: BeanType,
    var count: Int
) {
    fun plant(card: Card) {
        TODO("посадка боба")
    }

    fun harvest(): Int {
        TODO("расчет монет и очистка поля")
    }

    fun isEmpty(): Boolean {
        TODO()
    }

    fun canAccept(card: Card): Boolean {
        TODO("Проверка: можно ли посадить эту карту на это поле")
    }
}