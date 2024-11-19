package com.example.supertictactoe.utils

import android.util.Log
import com.example.supertictactoe.data.model.GameStateSimple
import com.example.supertictactoe.data.model.GameStateSuper
import com.example.supertictactoe.data.model.GameStateSuperRoom
import com.example.supertictactoe.repository.FirebaseRepository


class Servers {

    private val firebaseService = FirebaseRepository()

    fun createRoomWithCode(type: String): String {
        if (type == "create simple") {
            val codeRoom = createCodeRoom()

            firebaseService.setGameState(
                "RoomsSimple/${codeRoom}",
                GameStateSimple()
            )
            return "RoomsSimple/${codeRoom}"
        } else if (type == "create super") {
            val codeRoom = createCodeRoom()

            firebaseService.setGameState(
                "/${codeRoom}",
                GameStateSuperRoom()
            )
            return "/${codeRoom}"
        }
        return ""
    }

    private fun createCodeRoom(): Int {
        return (100000..999999).random()
    }

    private fun checkCodeInBd(code: Int, callback: (Boolean) -> Unit) {
        firebaseService.getListGamesSimple { listGames ->
            var size = 0
            listGames.forEach {
                if (it.key == code)
                    callback(true)
                if (size == listGames.size)
                    callback(false)
                size++
            }
        }
    }

    fun createGameSimple(): String {
        val codeRoom = createCodeRoom()

        firebaseService.setGameState(
            "Simple/${codeRoom}",
            GameStateSimple(playersNum = 1)
        )
        return "Simple/${codeRoom}"
    }

    fun createGameSuper(): String {
        val codeRoom = createCodeRoom()

        firebaseService.setGameState(
            "Super/${codeRoom}",
            GameStateSuper(playersNum = 1)
        )
        return "Super/${codeRoom}"
    }

    fun findGame(code: Int, callback: (String) -> Unit) {
        firebaseService.getListGamesSuper { superGames ->
            if (superGames.containsKey(code)) {
                callback("Super")
                return@getListGamesSuper
            }

            firebaseService.getListGamesSimple { simpleGames ->
                if (simpleGames.containsKey(code)) {
                    callback("Simple")
                } else {
                    callback("Not Found")
                }
            }
        }
        firebaseService.getListGamesSuperRooms { superGames ->
            if (superGames.containsKey(code)) {
                callback("SuperR")
                return@getListGamesSuperRooms
            }

            firebaseService.getListRoomsSimple { simpleGames ->
                if (simpleGames.containsKey(code)) {
                    callback("SimpleR")
                } else {
                    callback("Not Found")
                }
            }
        }
    }

    fun findGameSimple(callback: (String) -> Unit) {
        firebaseService.getListGamesSimple { listGameState ->
            Log.d("ooo", "list games size = ${listGameState.size}")

            // Прапорець для визначення, чи знайшли гру
            var gameFound = false

            if (listGameState.containsKey(0) && listGameState.size == 1) {
                callback("0")
                gameFound = true
            } else {
                for (game in listGameState) {
                    if (game.value.playersNum < 2) {
                        Log.d("ooo", "spins = ${game.key}")
                        firebaseService.setGameState(
                            "Simple/${game.key}",
                            GameStateSimple(playersNum = 2)
                        )
                        callback("Simple/${game.key}")
                        gameFound = true
                        break
                    }
                }
            }

            // Якщо не знайшли гру, викликаємо callback з 0
            if (!gameFound) {
                callback("0")
            }
        }
    }

    fun findGameSuper(callback: (String) -> Unit) {
        firebaseService.getListGamesSuper { listGameState ->
            var gameFound = false

            if (listGameState.containsKey(0)) {
                callback("0")
                gameFound = true
            } else {
                // Проходимо по всіх іграх
                for (game in listGameState) {
                    if (game.value.playersNum < 2) {
                        firebaseService.setGameState(
                            "Super/${game.key}",
                            GameStateSuper(playersNum = 2)
                        )
                        callback("Super/${game.key}")
                        gameFound = true
                        break // Якщо знайшли гру, виходимо з циклу
                    }
                }
            }

            // Якщо не знайшли жодної гри, викликаємо callback з 0
            if (!gameFound) {
                callback("0")
            }
        }
    }
}