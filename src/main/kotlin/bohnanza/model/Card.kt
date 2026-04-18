package bohnanza.model

data class Card (
    val id: Int, val beanType: BeanType, val coinValue: IntArray
) {
    fun getCoinsFor(count: Int): Int {
        for (i in (coinValue.size - 1 downTo 0)) {
            if (coinValue[i] <= count) {
                return i + 1
            }
        }
        return 0
    }
}

