package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 10, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 20, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard(null)

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(third, res[0])
        assertEquals(second, res[1])
        assertEquals(first, res[2])
    }

    @Test
    fun test_getLeaderboard_sameScore_SortedByLowerTime() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 20, 10.0)
        val third = GameResult(3, "third", 20, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard(null)

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(second, res[0])
        assertEquals(third, res[1])
        assertEquals(first, res[2])
    }

    @Test
    fun test_getLeaderboard_additionalRankValue_longlist() {
        val leaderboard = (1..8).map {
            GameResult(
                it.toLong(),
                "player$it",
                (10 - it),
                it.toDouble()
            )
        }
        whenever(mockedService.getGameResults()).thenReturn(leaderboard)

        val res: List<GameResult> = controller.getLeaderboard(4)
        verify(mockedService).getGameResults()

        assertEquals(7, res.size)
        assertEquals(leaderboard.subList(0, 7), res)

    }

    @Test
    fun test_getLeaderboard_additionalRankValue_shortlist() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 20, 10.0)
        val third = GameResult(3, "third", 20, 15.0)

        val leaderboard = listOf(third, second, first)
        val expectedLeaderboard = listOf(second, third, first)


        whenever(mockedService.getGameResults()).thenReturn(leaderboard)

        val res: List<GameResult> = controller.getLeaderboard(3)
        verify(mockedService).getGameResults()

        assertEquals(3, res.size)
        assertEquals(expectedLeaderboard, res)
    }

    @Test
    fun test_getLeaderboard_additionalRankValue_oneValue() {
        val first = GameResult(1, "first", 20, 20.0)

        val leaderboard = listOf(first)

        whenever(mockedService.getGameResults()).thenReturn(leaderboard)

        val res: List<GameResult> = controller.getLeaderboard(1)
        verify(mockedService).getGameResults()

        assertEquals(1, res.size)
        assertEquals(leaderboard, res)
    }

    @Test
    fun test_getLeaderboard_invalidValues_exception() {
        val first = GameResult(1, "first", 20, 20.0)

        val leaderboard = listOf(first)

        whenever(mockedService.getGameResults()).thenReturn(leaderboard)

        var exception = assertThrows<ResponseStatusException> {
            controller.getLeaderboard(-10)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)

        exception = assertThrows<ResponseStatusException> {
            controller.getLeaderboard(1000)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
    }
}