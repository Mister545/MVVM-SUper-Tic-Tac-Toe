package com.example.supertictactoe.repository

import android.util.Log
import com.example.supertictactoe.data.model.GameStateSimple
import com.example.supertictactoe.data.model.GameStateSuper
import com.example.supertictactoe.data.model.GameStateSuperRoom
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseRepository {
    private val database = FirebaseDatabase.getInstance()

    fun setGameState(codeRoom: String, gameState: GameStateSuper) {
        database.getReference("Server/games$codeRoom").setValue(gameState)
    }

    fun setGameState(codeRoom: String, gameState: GameStateSuperRoom) {
        database.getReference("Server/games$codeRoom").setValue(gameState)
    }

    fun setGameState(codeRoom: String, gameState: GameStateSimple) {
        database.getReference("Server/games$codeRoom").setValue(gameState)
    }

    fun getGameStateSuper(codeRoom: String, callback: (GameStateSuper) -> Unit) {
        val databaseReference = database.getReference("Server/games$codeRoom")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.getValue(GameStateSuper::class.java) == null) {
                    println()
                } else {
                    val gameState = dataSnapshot.getValue(GameStateSuper::class.java)!!
                    callback(gameState)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Помилка зчитування з Firebase: ${error.message}")
            }
        })
    }

    fun getGameStateSimple(codeRoom: String, callback: (GameStateSimple) -> Unit) {
        val databaseReference = database.getReference("Server/games$codeRoom")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.getValue(GameStateSimple::class.java) == null) {
                    println()
                } else {
                    val gameState = dataSnapshot.getValue(GameStateSimple::class.java)!!
                    callback(gameState)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Помилка зчитування з Firebase: ${error.message}")
            }
        })
    }

    fun getListGamesSuper(callback: (Map<Int, GameStateSuper>) -> Unit) {
        val databaseReference = database.getReference("Server/gamesSuper")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataMap = mutableMapOf<Int, GameStateSuper>()

                // Перетворюємо дані з DataSnapshot в Map з Int ключами
                for (snapshot in dataSnapshot.children) {
                    val key = snapshot.key?.toIntOrNull() // Перетворюємо ключ на Int
                    val value =
                        snapshot.getValue(GameStateSuper::class.java) // Конвертуємо значення

                    if (key != null && value != null) {
                        dataMap[key] = value
                    }
                }

                if (dataMap.isNotEmpty()) {
                    callback(dataMap)
                } else {
                    callback(mutableMapOf(0 to GameStateSuper()))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Помилка зчитування з Firebase: ${error.message}")
            }
        })
    }

    fun getListGamesSuperRooms(callback: (Map<Int, GameStateSuper>) -> Unit) {
        val databaseReference = database.getReference("Server/games")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataMap = mutableMapOf<Int, GameStateSuper>()

                // Перетворюємо дані з DataSnapshot в Map з Int ключами
                for (snapshot in dataSnapshot.children) {
                    val key = snapshot.key?.toIntOrNull() // Перетворюємо ключ на Int
                    val value =
                        snapshot.getValue(GameStateSuper::class.java) // Конвертуємо значення

                    if (key != null && value != null) {
                        dataMap[key] = value
                    }
                }

                if (dataMap.isNotEmpty()) {
                    callback(dataMap)
                } else {
                    callback(mutableMapOf(0 to GameStateSuper()))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Помилка зчитування з Firebase: ${error.message}")
            }
        })
    }


    fun getListGamesSimple(callback: (Map<Int, GameStateSimple>) -> Unit) {
        val databaseReference = database.getReference("Server/gamesSimple")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataMap = mutableMapOf<Int, GameStateSimple>()

                // Перетворюємо дані з DataSnapshot в Map з Int ключами
                for (snapshot in dataSnapshot.children) {
                    val key = snapshot.key?.toIntOrNull() // Перетворюємо ключ на Int
                    val value =
                        snapshot.getValue(GameStateSimple::class.java) // Конвертуємо значення

                    if (key != null && value != null) {
                        dataMap[key] = value
                    }
                }

                if (dataMap.isNotEmpty()) {
                    callback(dataMap)
                } else {
                    callback(mutableMapOf(0 to GameStateSimple()))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Помилка зчитування з Firebase: ${error.message}")
            }
        })
    }

    fun getListRoomsSimple(callback: (Map<Int, GameStateSimple>) -> Unit) {
        val databaseReference = database.getReference("Server/gamesRoomsSimple")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataMap = mutableMapOf<Int, GameStateSimple>()

                // Перетворюємо дані з DataSnapshot в Map з Int ключами
                for (snapshot in dataSnapshot.children) {
                    val key = snapshot.key?.toIntOrNull() // Перетворюємо ключ на Int
                    val value =
                        snapshot.getValue(GameStateSimple::class.java) // Конвертуємо значення

                    if (key != null && value != null) {
                        dataMap[key] = value
                    }
                }

                if (dataMap.isNotEmpty()) {
                    callback(dataMap)
                } else {
                    callback(mutableMapOf(0 to GameStateSimple()))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Помилка зчитування з Firebase: ${error.message}")
            }
        })
    }

    fun getPlayersNumSimple2(path: String, callback: (Int) -> Unit) {
        val databaseReference = database.getReference(path)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.getValue(Int::class.java) == null) {
                    println()
                } else {
                    val playerNum = dataSnapshot.getValue(Int::class.java)!!
                    callback(playerNum)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Помилка зчитування з Firebase: ${error.message}")
            }
        })
    }

    fun getPlayerName(path: String, callback: (String) -> Unit) {
        val databaseReference = database.getReference(path)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.getValue(String::class.java) == null) {
                    println()
                } else {
                    val playerName = dataSnapshot.getValue(String::class.java)!!
                    Log.d("ooo", "playerName $playerName")
                    callback(playerName)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Помилка зчитування з Firebase: ${error.message}")
            }
        })
    }

    fun getCodeRoom(path: String, callback: (Int, String) -> Unit) {
        val databaseReference = database.getReference(path)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result =
                    dataSnapshot.value as? Map<String, Map<String, *>>  // Отримуємо карту з типами String
                if (result == null) {
                    callback(0, "0")
                } else {
                    result.forEach { (key, data) ->
                        val serverSimpleName =
                            data.keys.firstOrNull() // Отримуємо перший ключ, який може бути назвою ServerSimple

                        callback(key.toInt(), serverSimpleName?.toString()!!)
                    }
                }
                Log.d("ooo", "codeRoom: ${dataSnapshot.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                println("Помилка зчитування з Firebase: ${error.message}")
            }
        })
    }

    fun getExitCode(path: String, callback: (Int) -> Unit): ValueEventListener {
        val firebaseListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val exitCode = dataSnapshot.getValue(Int::class.java)
                if (exitCode != null) {
                    callback(exitCode)
                } else {
                    // Обробка випадку, коли дані відсутні або є null
                    println("Дані відсутні або є null")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Помилка зчитування з Firebase: ${error.message}")
            }
        }

        val databaseReference = database.getReference(path)
        databaseReference.addValueEventListener(firebaseListener)

        return firebaseListener
    }

    fun getStep(list: MutableList<Int>): Boolean {
        var x = 0
        var o = 0
        var y = 0

        for (value in list) {
            y += value
            if (value == 1) {
                x++
            } else if (value == 2) {
                o++
            }
        }
        val ret = when (y) {
            0 -> true
            3 -> true
            5 -> true
            6 -> true
            9 -> true
            12 -> true
            else -> false
        }
        return ret
    }

    fun getStepSuper(list: MutableList<MutableList<Int>>): Boolean {
        var y = 0
        val arrSuper = List(200) { 0 }.toMutableList()

        var index = 0
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                y += list[i][j]
                arrSuper[index] = y
                index++
            }
        }
        val validNum = setOf(
            0, 3, 5, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36, 39, 42, 45, 48, 51, 54,
            57, 60, 63, 66, 69, 72, 75, 78, 81, 84, 87, 90, 93, 96, 99, 102, 105, 108,
            111, 114, 117, 120, 123
        )
        return y in validNum
    }
}