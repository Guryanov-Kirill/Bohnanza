package bohnanza.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class CardTest {

    @Test
    fun getCoinsTest() {
        val prices = intArrayOf(3, 5, 7, 8)
        val card = Card(1, BeanType.COFFEE_BEAN, prices)

        // Меньше минимума
        assertEquals(0, card.getCoinsFor(0))
        assertEquals(0, card.getCoinsFor(2))

        // По границам
        assertEquals(1, card.getCoinsFor(3))
        assertEquals(2, card.getCoinsFor(5))
        assertEquals(3, card.getCoinsFor(7))
        assertEquals(4, card.getCoinsFor(8))

        // Между границами
        assertEquals(2, card.getCoinsFor(6))

        // Больше максимума
        assertEquals(4, card.getCoinsFor(10))
    }
}
