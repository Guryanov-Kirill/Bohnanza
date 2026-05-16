package bohnanza.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows

//  ЮНИТ-ТЕСТЫ

class BeanFieldTest {

    // Новое поле должно быть пустым
    @Test
    fun testNewFieldIsEmpty() {
        val field = BeanField(0)
        assertTrue(field.isEmpty())
    }

    // После посадки карты поле не пустое
    @Test
    fun testFieldNotEmptyAfterPlant() {
        val field = BeanField(0)
        val card = Card(0, BeanType.COFFEE_BEAN)
        field.plant(card)
        assertFalse(field.isEmpty())
    }

    // После посадки первой карты устанавливается тип поля
    @Test
    fun testBeanTypeSetAfterFirstPlant() {
        val field = BeanField(0)
        field.plant(Card(0, BeanType.COFFEE_BEAN))
        assertEquals(BeanType.COFFEE_BEAN, field.beanType)
    }

    // Карта другого типа не должна добавляться в поле
    @Test
    fun testWrongTypeCardNotPlanted() {
        val field = BeanField(0)
        field.plant(Card(0, BeanType.COFFEE_BEAN))
        field.plant(Card(1, BeanType.WAX_BEAN))
        assertEquals(1, field.cards.size)
    }

    // Пустое поле принимает любую карту
    @Test
    fun testEmptyFieldCanAcceptAnyCard() {
        val field = BeanField(0)
        assertTrue(field.canAccept(Card(0, BeanType.RED_BEAN)))
    }

    // Поле не принимает карту другого типа
    @Test
    fun testFieldRejectsWrongType() {
        val field = BeanField(0)
        field.plant(Card(0, BeanType.COFFEE_BEAN))
        assertFalse(field.canAccept(Card(1, BeanType.WAX_BEAN)))
    }

    // После cardsFromField поле снова пустое
    @Test
    fun testCardsFromFieldClearsField() {
        val field = BeanField(0)
        field.plant(Card(0, BeanType.COFFEE_BEAN))
        field.cardsFromField()
        assertTrue(field.isEmpty())
        assertNull(field.beanType)
    }

    // cardsFromField возвращает все карты которые были в поле
    @Test
    fun testCardsFromFieldReturnsAllCards() {
        val field = BeanField(0)
        field.plant(Card(0, BeanType.COFFEE_BEAN))
        field.plant(Card(1, BeanType.COFFEE_BEAN))
        val cards = field.cardsFromField()
        assertEquals(2, cards.size)
    }

    // Пустое поле даёт 0 монет
    @Test
    fun testEmptyFieldGivesZeroCoins() {
        val field = BeanField(0)
        assertEquals(0, field.coinsFromField())
    }
}

class CardTest {

    // При количестве карт ниже порога — 0 монет
    @Test
    fun testZeroCoinsWhenBelowThreshold() {
        // COFFEE_BEAN: нужно минимум 4 карты для 1 монеты
        val card = Card(0, BeanType.COFFEE_BEAN)
        assertEquals(0, card.getCoinsFor(3))
    }

    // Ровно на пороге — 1 монета
    @Test
    fun testOneCoinsAtFirstThreshold() {
        val card = Card(0, BeanType.COFFEE_BEAN)
        assertEquals(1, card.getCoinsFor(4))
    }

    // Очень много карт — максимальные монеты
    @Test
    fun testMaxCoinsForLargeCount() {
        val card = Card(0, BeanType.COFFEE_BEAN)
        assertEquals(4, card.getCoinsFor(100))
    }
}

class DeckTest {

    private lateinit var deck: Deck

    @BeforeEach
    fun setUp() {
        deck = Deck()
        deck.startDeckFactory()
    }

    // draw возвращает нужное количество карт
    @Test
    fun testDrawReturnsCorrectCount() {
        val drawn = deck.draw(5)
        assertEquals(5, drawn.size)
    }

    // После reshuffle счётчик увеличивается на 1
    @Test
    fun testReshuffleIncreasesCounter() {
        deck.discard(Card(0, BeanType.RED_BEAN))
        deck.reshuffle()
        assertEquals(1, deck.reshuffleCount)
    }

    // После 3 перемешиваний isThirdCycle возвращает true
    @Test
    fun testIsThirdCycleAfterThreeReshuffles() {
        repeat(3) {
            deck.discard(Card(it, BeanType.RED_BEAN))
            deck.reshuffle()
        }
        assertTrue(deck.isThirdCycle())
    }

    // Сброшенная карта попадает обратно в колоду после reshuffle
    @Test
    fun testDiscardedCardReturnedAfterReshuffle() {
        // Вытащим все карты из колоды
        val allCards = deck.draw(1000)
        // Сбросим одну карту
        deck.discard(allCards[0])
        deck.reshuffle()
        // Теперь можно снова вытащить 1 карту
        val drawn = deck.draw(1)
        assertEquals(1, drawn.size)
    }
}

class DeckFactoryTest {

    // Фабрика создаёт правильное суммарное число карт
    @Test
    fun testTotalCardCount() {
        val expected = BeanType.entries.sumOf { it.totalCards }
        val actual = DeckFactory().createDeck().size
        assertEquals(expected, actual)
    }

    // Для каждого типа создаётся нужное количество карт
    @Test
    fun testCardCountPerType() {
        val cards = DeckFactory().createDeck()
        for (type in BeanType.entries) {
            val count = cards.count { it.beanType == type }
            assertEquals(type.totalCards, count)
        }
    }
}

class PlayerTest {

    // Новый игрок имеет 0 монет
    @Test
    fun testNewPlayerHasZeroCoins() {
        val player = Player(1, "Alice")
        assertEquals(0, player.coins())
    }

    // addCoins увеличивает количество монет
    @Test
    fun testAddCoinsIncreasesCount() {
        val player = Player(1, "Alice")
        player.addCoins(listOf(Card(0, BeanType.RED_BEAN), Card(1, BeanType.RED_BEAN)))
        assertEquals(2, player.coins())
    }

    // buyExtraField при наличии 3+ монет работает успешно
    @Test
    fun testBuyExtraFieldSuccess() {
        val player = Player(1, "Alice")
        player.addCoins(listOf(Card(0, BeanType.RED_BEAN), Card(1, BeanType.RED_BEAN), Card(2, BeanType.RED_BEAN)))
        player.buyExtraField()
        assertTrue(player.hasExtraField)
        assertEquals(0, player.coins())
    }

    // buyExtraField бросает исключение при нехватке монет
    @Test
    fun testBuyExtraFieldFailsWithNoMoney() {
        val player = Player(1, "Alice")
        assertThrows<InsufficientFundsException> { player.buyExtraField() }
    }

    // buyExtraField бросает исключение если поле уже куплено
    @Test
    fun testBuyExtraFieldFailsIfAlreadyBought() {
        val player = Player(1, "Alice")
        player.addCoins((0..5).map { Card(it, BeanType.RED_BEAN) })
        player.buyExtraField()
        assertThrows<IllegalStateException> { player.buyExtraField() }
    }
}

class TradeTest {

    // После propose флаг proposed становится true
    @Test
    fun testProposeSetsProposeFlag() {
        val trade = Trade(1, 2, mutableListOf(Card(0, BeanType.RED_BEAN)))
        trade.propose()
        assertTrue(trade.proposed)
    }

    // Нельзя предложить пустую сделку
    @Test
    fun testCannotProposeEmptyTrade() {
        val trade = Trade(1, 2)
        assertThrows<IllegalArgumentException> { trade.propose() }
    }

    // После accept флаг accepted становится true
    @Test
    fun testAcceptSetsAcceptedFlag() {
        val trade = Trade(1, 2, mutableListOf(Card(0, BeanType.RED_BEAN)))
        trade.propose()
        trade.accept()
        assertTrue(trade.accepted)
    }

    // reject сбрасывает флаги и очищает карты
    @Test
    fun testRejectClearsTrade() {
        val trade = Trade(1, 2, mutableListOf(Card(0, BeanType.RED_BEAN)))
        trade.propose()
        trade.reject()
        assertFalse(trade.proposed)
        assertTrue(trade.offeredCards.isEmpty())
    }
}

//  ИНТЕГРАЦИОННЫЕ ТЕСТЫ

class TurnAndPlayerTest {

    private lateinit var player: Player
    private lateinit var deck: Deck
    private lateinit var turn: Turn

    @BeforeEach
    fun setUp() {
        player = Player(1, "Alice")
        deck = Deck()
        deck.startDeckFactory()
        turn = Turn(player, deck)
    }

    // После plantCard карта исчезает из руки и появляется на поле
    @Test
    fun testPlantCardMovesCardFromHandToField() {
        val card = Card(0, BeanType.COFFEE_BEAN)
        player.handCards.add(card)
        turn.plantCard(0, card)
        assertFalse(player.handCards.contains(card))
        assertFalse(player.beanField[0].isEmpty())
    }

    // drawTwoCards добавляет ровно 2 карты в openCards
    @Test
    fun testDrawTwoCardsFillsOpenCards() {
        turn.phase = TurnPhase.DRAW_AND_TRADE
        turn.drawTwoCards()
        assertEquals(2, turn.openCards.size)
    }

    // completeTrade перекладывает openCards в руку игрока
    @Test
    fun testCompleteTradeMovesOpenCardsToHand() {
        turn.phase = TurnPhase.DRAW_AND_TRADE
        turn.openCards.add(Card(0, BeanType.RED_BEAN))
        turn.completeTrade()
        assertEquals(1, player.handCards.size)
        assertTrue(turn.openCards.isEmpty())
    }

    // nextPhase без посадки карты бросает исключение
    @Test
    fun testNextPhaseThrowsIfNothingPlanted() {
        player.handCards.add(Card(0, BeanType.COFFEE_BEAN))
        assertThrows<IllegalArgumentException> { turn.nextPhase() }
    }
}

class GameAndPlayersTest {

    private lateinit var game: Game
    private lateinit var p1: Player
    private lateinit var p2: Player

    @BeforeEach
    fun setUp() {
        p1 = Player(1, "Alice")
        p2 = Player(2, "Bob")
        game = Game(listOf(p1, p2), Deck())
        game.startGame()
    }

    // После startGame у каждого игрока по 5 карт
    @Test
    fun testEachPlayerHasFiveCardsAfterStart() {
        assertEquals(5, p1.handCards.size)
        assertEquals(5, p2.handCards.size)
    }

    // После startGame статус игры IN_PROGRESS
    @Test
    fun testGameStatusIsInProgressAfterStart() {
        assertEquals(GameStatus.IN_PROGRESS, game.status)
    }

    // acceptTrade передаёт карты между игроками
    @Test
    fun testAcceptTradeSwapsCards() {
        val offered = Card(500, BeanType.RED_BEAN)
        val requested = Card(501, BeanType.WAX_BEAN)
        p1.handCards.add(offered)
        p2.handCards.add(requested)

        val trade = Trade(p1.id, p2.id, mutableListOf(offered), mutableListOf(requested))
        trade.propose()
        game.acceptTrade(trade)

        assertTrue(p2.handCards.contains(offered))
        assertTrue(p1.handCards.contains(requested))
    }

    // endGame переводит статус в FINISHED
    @Test
    fun testEndGameSetsStatusFinished() {
        game.endGame()
        assertEquals(GameStatus.FINISHED, game.status)
    }
}

//  СИСТЕМНЫЕ ТЕСТЫ

class FullGameSystemTest {

    @BeforeEach
    fun clearHistory() {
        GameHistory.clearTurns()
    }

    // Полный ход одного игрока — все три фазы без ошибок
    @Test
    fun testFullTurnOfOnePlayer() {
        val p1 = Player(1, "Alice")
        val p2 = Player(2, "Bob")
        val game = Game(listOf(p1, p2), Deck())
        game.startGame()

        // Фаза 1: посадить первую карту
        game.plantCard(0, p1.handCards[0])
        game.nextPhase()

        // Фаза 2: открыть карты, перейти дальше
        game.drawTwoCards()
        game.nextPhase()

        // Фаза 3: добрать 3 карты
        game.drawThreeCards()

        // Переход к следующему игроку
        game.nextTurn()
        assertEquals(p2, game.getCurrentPlayer())
    }

    // После завершения игры есть хотя бы один победитель
    @Test
    fun testGameHasWinnerAfterEnd() {
        val p1 = Player(1, "Alice")
        val p2 = Player(2, "Bob")
        val game = Game(listOf(p1, p2), Deck())
        game.startGame()
        game.endGame()

        val winners = game.getWinner()
        assertFalse(winners.isEmpty())
    }

    // Завершённая игра сохраняется в историю
    @Test
    fun testFinishedGameSavedToHistory() {
        val p1 = Player(1, "Alice")
        val p2 = Player(2, "Bob")
        val game = Game(listOf(p1, p2), Deck())
        game.startGame()
        game.endGame()

        assertTrue(GameHistory.getAllGames().contains(game))
    }
}