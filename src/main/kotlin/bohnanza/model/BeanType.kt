package bohnanza.model

enum class BeanType(val totalCards: Int, val coinValue: List<Int>) {
    COFFEE_BEAN(24, listOf(4, 7, 10, 12)),
    WAX_BEAN(22, listOf(4, 7, 9, 11)),
    BLUE_BEAN(20, listOf(4, 6, 8, 10)),
    CHILI_BEAN(18, listOf(3, 6, 8, 9)),
    STINK_BEAN(16, listOf(3, 5, 7, 8)),
    GREEN_BEAN(14, listOf(3, 5, 6, 7)),
    SOY_BEAN(12, listOf(2, 4, 6, 7)),
    BLACK_EYED_BEAN(10, listOf(2, 4, 5, 6)),
    RED_BEAN(8, listOf(2, 3, 4, 5)),
    GARDEN_BEAN(6, listOf(2, 3)),
    COCOA_BEAN(4, listOf(2, 3, 4)),
}