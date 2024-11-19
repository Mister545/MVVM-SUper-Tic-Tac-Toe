package com.example.supertictactoe.utils

import androidx.lifecycle.MutableLiveData
import com.example.supertictactoe.databinding.FragmentSuperTicTacToeBinding

class GameRules {

    private var arrSimple: MutableList<Int> = MutableList(9) { 0 }
    private var winListSuper: MutableList<Int> = MutableList(9) { 0 }

    fun getWinListSuper(): MutableList<Int> {
        return winListSuper
    }

    fun checkWinSuper(
        boardState: MutableList<MutableList<Int>>,
        binding: FragmentSuperTicTacToeBinding
    ) {
        boardState.forEachIndexed { indexI, i ->
            when {
                checkWin(1, i) -> winListSuper[indexI] = 1
                checkWin(2, i) -> winListSuper[indexI] = 2
                isDraw(i) -> winListSuper[indexI] = 3
            }

            when {
                checkWin(1, winListSuper) -> binding.TextWin.text = "Win X"
                checkWin(2, winListSuper) -> binding.TextWin.text = "Win O"
                isDraw(winListSuper) -> binding.TextWin.text = "Draw"
            }
        }
    }

    fun checkWinSuper(boardState: MutableList<MutableList<Int>>): MutableLiveData<MutableList<Int>> {
        val listWonBoards: MutableLiveData<MutableList<Int>> = MutableLiveData(MutableList(9) { 0 })
        val updatedList = MutableList(9) { 0 }

        boardState.forEachIndexed { indexI, i ->
            when {
                checkWin(1, i) -> updatedList[indexI] = 1
                checkWin(2, i) -> updatedList[indexI] = 2
                isDraw(i) -> updatedList[indexI] = 3
            }
        }

        listWonBoards.value = updatedList
        return listWonBoards
    }

    fun checkRightPlace(place: Int): Boolean {
        return if (place == 10)
            false
        else
            winListSuper[place] == 1 || winListSuper[place] == 2 || winListSuper[place] == 3
    }

    fun resetBoardSimple() {
        arrSimple.fill(0)
    }

    fun checkWin(player: Int, array: MutableList<Int>): Boolean {
        val winConditions = arrayOf(
            intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
        )
        return winConditions.any { condition ->
            condition.all { array[it] == player }
        }
    }

    fun checkWin(array: MutableList<Int>): Int {
        val winConditions = arrayOf(
            intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
        )

        // Перевіряємо умови для гравця 1
        if (winConditions.any { condition -> condition.all { array[it] == 1 } }) {
            return 1
        }

        // Перевіряємо умови для гравця 2
        if (winConditions.any { condition -> condition.all { array[it] == 2 } }) {
            return 2
        }

        // Повертаємо 0, якщо немає переможця або 3 якщо нічия
        return if (isDraw(array)) 3 else 0
    }

    private fun isDraw(array: MutableList<Int>): Boolean {
        return array.all { it != 0 }
    }
}

