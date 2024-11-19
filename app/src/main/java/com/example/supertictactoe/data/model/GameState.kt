package com.example.supertictactoe.data.model

data class GameStateSuper(
    val data: MutableList<MutableList<Int>> = MutableList(9) { MutableList(9) { 0 } },
    var IsNextX: Boolean = true,
    val playersNum: Int = 0,
    val prevStep: Int = 10,
    val nextField: MutableList<Int> = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8),
    val winPosition: MutableList<Int> = MutableList(9) { 0 },
    val winner: Int = 0
)

data class GameStateSuperRoom(
    val data: MutableList<MutableList<Int>> = MutableList(9) { MutableList(9) { 0 } },
    var IsNextX: Boolean = true,
    val prevStep: Int = 10,
    val nextField: MutableList<Int> = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8),
    val winPosition: MutableList<Int> = MutableList(9) { 0 },
    val winner: Int = 0
)

data class GameStateSimple(
    var data: MutableList<Int> = MutableList(9) { 0 },
    var IsNextX: Boolean = true,
    val winner: Int = 0,
    val playersNum: Int = 0
)
