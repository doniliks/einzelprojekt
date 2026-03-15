package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class GameResultControllerTests {
    private var first = GameResult(1, "first", 20, 20.0)
    private var second = GameResult(2, "second", 20, 10.0)
    private var third = GameResult(3, "third", 20, 15.0)

    private val listOfGameResults = listOf(first, second, third)

    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = GameResultController(mockedService)
    }

    @Test
    fun test_getGameResult_validSearch() {

        whenever(mockedService.getGameResult(1)).thenReturn(first)

        val res = controller.getGameResult(1)
        verify(mockedService).getGameResult(1)
        assertEquals(first, res)
    }


    @Test
    fun test_getGameResult_invalidSearch() {

        whenever(mockedService.getGameResult(1)).thenReturn(null)

        val res = controller.getGameResult(1)
        verify(mockedService).getGameResult(1)
        assertNull(res)
    }

    @Test
    fun test_getAllGameResults_validSearch() {

        whenever(mockedService.getGameResults()).thenReturn(listOfGameResults)

        val res = controller.getAllGameResults()
        verify(mockedService).getGameResults()
        assertEquals(listOfGameResults, res)

    }
    @Test
    fun test_addGameResult() {
        controller.addGameResult(first)
        verify(mockedService).addGameResult(first)
    }

    @Test
    fun test_deleteGameResult() {
        controller.deleteGameResult(1)
        verify(mockedService).deleteGameResult(1)
    }


    }
