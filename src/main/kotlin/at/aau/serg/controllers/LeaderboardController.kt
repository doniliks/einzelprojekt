package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {

    @GetMapping
    fun getLeaderboard(
        @RequestParam(required = false) rank: Int?
    ): List<GameResult> {
        val result: List<GameResult> = gameResultService.getGameResults().sortedWith(compareBy<GameResult>({ -it.score }).thenBy { it.timeInSeconds })
        if(rank == null) {
            return result
        }

        if (rank < 1 || rank > result.size) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
        return result.subList(maxOf(0, rank - 4), minOf(rank + 3, result.size));

    }


}