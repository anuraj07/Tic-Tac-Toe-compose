package com.deep.tic_tac_toe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {
    var state by mutableStateOf(GameState())
    val boardItems: MutableMap<Int, BoardCellValue> = mutableMapOf(
        1 to BoardCellValue.NONE,
        2 to BoardCellValue.NONE,
        3 to BoardCellValue.NONE,
        4 to BoardCellValue.NONE,
        5 to BoardCellValue.NONE,
        6 to BoardCellValue.NONE,
        7 to BoardCellValue.NONE,
        8 to BoardCellValue.NONE,
        9 to BoardCellValue.NONE,
    )

    private fun addValueToBoard(cellNo: Int) {
        if (boardItems[cellNo] != BoardCellValue.NONE) {
            return
        }
        if (state.currentTurn == BoardCellValue.CIRCLE) {
            boardItems[cellNo] = BoardCellValue.CIRCLE
            state = if (checkForVictory(BoardCellValue.CIRCLE)) {
                state.copy(
                    hintText = "Player 'O' Won",
                    playerCircleCount = state.playerCircleCount + 1,
                    currentTurn = BoardCellValue.NONE,
                    hasWon = true
                )
            } else if (hasBoardFull()) {
                state.copy(
                    hintText = "Game Draw",
                    drawCount = state.drawCount + 1
                )
            } else {
                state.copy(
                    hintText = "Player 'X' turn",
                    currentTurn = BoardCellValue.CROSS
                )
            }
        } else if (state.currentTurn == BoardCellValue.CROSS) {
            boardItems[cellNo] = BoardCellValue.CROSS
            state = if (checkForVictory(BoardCellValue.CROSS)) {
                state.copy(
                    hintText = "Player 'X' Won",
                    playerCrossCount = state.playerCrossCount + 1,
                    currentTurn = BoardCellValue.NONE,
                    hasWon = true
                )
            } else if (hasBoardFull()) {
                state.copy(
                    hintText = "Game Draw",
                    drawCount = state.drawCount + 1
                )
            } else {
                state.copy(
                    hintText = "Player 'O' turn",
                    currentTurn = BoardCellValue.CIRCLE
                )
            }
        }
    }

    private fun checkForVictory(boardCellValue: BoardCellValue): Boolean {
        when {
            //horizontal victory
            boardItems[1] == boardCellValue && boardItems[2] == boardCellValue && boardItems[3] == boardCellValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL1)
                return true
            }
            boardItems[4] == boardCellValue && boardItems[5] == boardCellValue && boardItems[6] == boardCellValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL2)
                return true
            }
            boardItems[7] == boardCellValue && boardItems[8] == boardCellValue && boardItems[9] == boardCellValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL3)
                return true
            }
            //vertical victory

            boardItems[1] == boardCellValue && boardItems[4] == boardCellValue && boardItems[7] == boardCellValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL1)
                return true
            }
            boardItems[2] == boardCellValue && boardItems[5] == boardCellValue && boardItems[8] == boardCellValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL2)
                return true
            }
            boardItems[3] == boardCellValue && boardItems[6] == boardCellValue && boardItems[9] == boardCellValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL3)
                return true
            }
            //diagonal victory
            boardItems[1] == boardCellValue && boardItems[5] == boardCellValue && boardItems[9] == boardCellValue -> {
                state = state.copy(victoryType = VictoryType.DIAGONAL1)
                return true
            }
            boardItems[3] == boardCellValue && boardItems[5] == boardCellValue && boardItems[7] == boardCellValue -> {
                state = state.copy(victoryType = VictoryType.DIAGONAL2)
                return true
            }
            else ->  return false
        }
    }

    private fun hasBoardFull(): Boolean {
        if (boardItems.containsValue(BoardCellValue.NONE)) return false
        return true
    }

    fun onAction(actions: UserActions) {
        when (actions) {
            is UserActions.BoardTapped -> {
                addValueToBoard(actions.cellNo)
            }
            UserActions.PlayAgainButtonClicked -> {
                gameReSet()
            }
        }
    }

    private fun gameReSet() {
        boardItems.forEach { (i, _) ->
            boardItems[i] = BoardCellValue.NONE
        }
        state = state.copy(
            hintText = "Player 'O' turn",
            currentTurn = BoardCellValue.CIRCLE,
            victoryType = VictoryType.NONE,
            hasWon = false
        )
    }
}