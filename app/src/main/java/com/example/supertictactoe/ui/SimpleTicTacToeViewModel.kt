package com.example.supertictactoe.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.supertictactoe.data.model.GameStateSimple
import com.example.supertictactoe.repository.FirebaseRepository
import com.example.supertictactoe.utils.GameRules
import com.example.supertictactoe.utils.Servers
import kotlin.math.sign

class SimpleTicTacToeViewModel : ViewModel() {
    private val firebaseService = FirebaseRepository()
    var gameState = MutableLiveData<GameStateSimple>()
    val server = Servers()
    val gameRules = GameRules()
    var codeRoom = MutableLiveData<String>()
    var winner = MutableLiveData<Int>()
    var typeGame = MutableLiveData<String>()

    fun startGame(type: String?, code: String?) {

        if (type.isNullOrEmpty()) {
            startOnlineGame()
        } else if (type == "create simple") {
            typeGame.value = type.toString()
            codeRoom.value = server.createRoomWithCode(type)
            listenerState()
        } else if (code != null) {
            typeGame.value = type.toString()
            codeRoom.value = "RoomsSimple/${code}"
            listenerState()
        }
    }

    private fun listenerState() {

        firebaseService.getGameStateSimple(codeRoom.value!!) { myState ->
            gameState.value = myState
            winner.value = gameRules.checkWin(myState.data)
        }
    }

    private fun startOnlineGame() {
        server.findGameSimple { code ->
            if (code == "0") {
                codeRoom.value = server.createGameSimple()
                listenerState()
            } else {
                codeRoom.value = code
                listenerState()
            }
        }
    }

    fun setBoardStep(index: Int) {
        if (typeGame.value != "create simple" && typeGame.value != "sign in simple") {
            stepInOnlineGame(index)
        } else {
            stepInRoom(index)
        }
    }

    private fun stepInOnlineGame(index: Int) {
        firebaseService.getListGamesSimple { gameList ->
            val myState = gameList[codeRoom.value!!.takeLast(6).toInt()]

            val step = if (myState!!.IsNextX) 1 else 2
            val updatedData = gameState.value!!.data.toMutableList()
            updatedData[index] = step

            gameState.value!!.IsNextX = !myState.IsNextX
            gameState.value = gameState.value?.copy(data = updatedData)
            firebaseService.setGameState(codeRoom.value!!, gameState.value!!)
        }
    }

    private fun stepInRoom(index: Int) {
        firebaseService.getListRoomsSimple { gameList ->
            val myState = gameList[codeRoom.value!!.takeLast(6).toInt()]

            val step = if (myState!!.IsNextX) 1 else 2
            val updatedData = gameState.value!!.data.toMutableList()
            updatedData[index] = step

            gameState.value!!.IsNextX = !myState.IsNextX
            gameState.value = gameState.value?.copy(data = updatedData)
            firebaseService.setGameState(codeRoom.value!!, gameState.value!!)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ooo", "cleared")

    }
}