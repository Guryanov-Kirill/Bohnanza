package bohnanza.model

enum class TurnPhase {
    PLANT_MANDATORY,   // Cажаем первую карту из руки(по желаннию вторую)
    DRAW_AND_TRADE,    // Открываем 2 карты из колоды, торгуем
    DRAW_FROM_DECK     // Берём 3 карты из колоды
}
