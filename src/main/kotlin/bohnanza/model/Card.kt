package bohnanza.model

data class Card (
    val id: Int,
    val beanType: BeanType,
) {
    fun getCoinsFor(count: Int): Int {
        for (i in (beanType.coinValue.size - 1 downTo 0)) {
            if (beanType.coinValue[i] <= count) {
                return i + 1
            }
        }
        return 0
    }
}

