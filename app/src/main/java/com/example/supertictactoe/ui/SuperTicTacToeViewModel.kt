package com.example.supertictactoe.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.supertictactoe.data.model.GameStateSuper
import com.example.supertictactoe.repository.FirebaseRepository
import com.example.supertictactoe.utils.GameRules
import com.example.supertictactoe.utils.Servers

class SuperTicTacToeViewModel : ViewModel() {

    private val firebaseService = FirebaseRepository()
    var gameState = MutableLiveData<GameStateSuper>()
    private val server = Servers()
    val gameRules = GameRules()
    var codeRoom = MutableLiveData<String>()
    var winnerBoards = MutableLiveData<MutableList<Int>>()
    var winner = MutableLiveData<Int>()
    val nextField = MutableLiveData<MutableList<Int>>()
    var typeGame = MutableLiveData<String>()

    fun startGame(type: String?, code: String?) {
        if (type.isNullOrEmpty()) {
            startOnlineGame()
        } else if (type == "create super") {
            typeGame.value = type.toString()
            codeRoom.value = server.createRoomWithCode(type)
            listenerState()
        } else if (code != null) {
            typeGame.value = type.toString()
            codeRoom.value = "/${code}"
            listenerState()
        }
    }

    private fun startOnlineGame() {
        server.findGameSuper { code ->
            if (code == "0") {
                codeRoom.value = server.createGameSuper()
                listenerState()
            } else {
                codeRoom.value = code
                listenerState()
            }
        }
    }

    private fun listenerState() {
        firebaseService.getGameStateSuper(codeRoom.value!!) { myState ->
            gameState.value = myState

            nextField.value =
                myState.nextField
            winnerBoards.value = gameRules.checkWinSuper(myState.data).value
            winner.value = gameRules.checkWin(winnerBoards.value!!)
        }
    }

    fun setBoardStep(index1: Int, index2: Int) {
        if (typeGame.value != "create super" && typeGame.value != "sign in super") {
            stepInOnlineGame(index1, index2)
        } else {
            stepInRoom(index1, index2)
        }
    }

    private fun stepInRoom(index1: Int, index2: Int) {
        firebaseService.getListGamesSuperRooms { gameList ->
            val myState = gameList[codeRoom.value!!.takeLast(6).toInt()]

            val step = if (myState!!.IsNextX) 1 else 2
            val updatedData = gameState.value!!.data.toMutableList()
            updatedData[index1][index2] = step

            gameState.value =
                gameState.value?.copy(data = updatedData, IsNextX = !myState.IsNextX)
            winnerBoards.value = gameRules.checkWinSuper(updatedData).value

            gameState.value =
                gameState.value?.copy(nextField = makeNextField(index2))

            Log.d("ooo", "1111111111 game State = ${myState} " +
                    "updatedData = ${updatedData} " +
                    "winnerBoards = ${gameRules.checkWinSuper(updatedData).value}")

            firebaseService.setGameState(codeRoom.value!!, gameState.value!!)
            listenerState()
        }
    }

    private fun stepInOnlineGame(index1: Int, index2: Int) {
        firebaseService.getListGamesSuper { gameList ->
            val myState = gameList[codeRoom.value!!.takeLast(6).toInt()]
            val step = if (myState!!.IsNextX) 1 else 2
            val updatedData = gameState.value!!.data.toMutableList()
            updatedData[index1][index2] = step

            gameState.value =
                gameState.value?.copy(data = updatedData, IsNextX = !myState.IsNextX)
            winnerBoards.value = gameRules.checkWinSuper(updatedData).value

            gameState.value =
                gameState.value?.copy(nextField = makeNextField(index2))

            Log.d("ooo", "222222222 game State = ${myState} " +
                    "updatedData = ${updatedData} " +
                    "winnerBoards = ${gameRules.checkWinSuper(updatedData).value}")

            firebaseService.setGameState(codeRoom.value!!, gameState.value!!)
            listenerState()
        }
    }

    private fun prevStepInWinListSuper(nextBoard: Int): Boolean {
        winnerBoards.value!!.forEachIndexed { index, i ->
            return i != 0 && index == nextBoard
        }
        return false
    }

    private fun makeNextField(nextBoard: Int): MutableList<Int> {
        val arr = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8)
        val empty = -1

        return if (winnerBoards.value!![nextBoard] != 0) {
            arr.forEachIndexed { index, i ->
                if (winnerBoards.value!![index] != 0) {
                    arr[index] = empty
                }
            }
            arr.filter { it != -1 }.toMutableList()
        } else {
            mutableListOf(nextBoard).filter { it != -1 }.toMutableList()
        }
    }
}
