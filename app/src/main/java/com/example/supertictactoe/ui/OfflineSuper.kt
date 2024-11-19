package com.example.supertictactoe.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.supertictactoe.R
import com.example.supertictactoe.databinding.FragmentSuperTicTacToeBinding
import com.example.supertictactoe.utils.GameRules
import com.example.supertictactoe.utils.UiConfiguration


class OfflineSuper : DialogFragment() {

    private lateinit var binding: FragmentSuperTicTacToeBinding
    private lateinit var buttonArrWithArr: List<List<Button>>
    private lateinit var buttonArrAll: List<Button>
    private var arrSuper: MutableList<MutableList<Int>> = MutableList(9) { MutableList(9) { 0 } }
    private val gameRules = GameRules()
    var nextField = arrayListOf<Int>()
    var nextBoardM = 10
    var isNextX = true
    private val uiConfiguration = UiConfiguration()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuperTicTacToeBinding.inflate(inflater, container, false)

        setupButtons()
        initialization()

        binding.bComeBackSuper.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)

            removeFragment()
        }
        return binding.root
    }


    private fun setupButtons() {
        buttonArrWithArr = listOf(
            listOf(
                binding.button000000,
                binding.button000101,
                binding.button000202,
                binding.button000303,
                binding.button000404,
                binding.button000505,
                binding.button000606,
                binding.button000707,
                binding.button000808
            ),
            listOf(
                binding.button010009,
                binding.button010110,
                binding.button010211,
                binding.button010312,
                binding.button010413,
                binding.button010514,
                binding.button010615,
                binding.button010716,
                binding.button010817
            ),
            listOf(
                binding.button020018,
                binding.button020119,
                binding.button020220,
                binding.button020321,
                binding.button020422,
                binding.button020523,
                binding.button020624,
                binding.button020725,
                binding.button020826
            ),
            listOf(
                binding.button030027,
                binding.button030128,
                binding.button030229,
                binding.button030330,
                binding.button030431,
                binding.button030532,
                binding.button030633,
                binding.button030734,
                binding.button030835
            ),
            listOf(
                binding.button040036,
                binding.button040137,
                binding.button040238,
                binding.button040339,
                binding.button040440,
                binding.button040541,
                binding.button040642,
                binding.button040743,
                binding.button040844
            ),
            listOf(
                binding.button050045,
                binding.button050146,
                binding.button050247,
                binding.button050348,
                binding.button050449,
                binding.button050550,
                binding.button050651,
                binding.button050752,
                binding.button050853
            ),
            listOf(
                binding.button060054,
                binding.button060155,
                binding.button060256,
                binding.button060357,
                binding.button060458,
                binding.button060559,
                binding.button060660,
                binding.button060761,
                binding.button060862
            ),
            listOf(
                binding.button070063,
                binding.button070164,
                binding.button070265,
                binding.button070366,
                binding.button070467,
                binding.button070568,
                binding.button070669,
                binding.button070770,
                binding.button070871
            ),
            listOf(
                binding.button080072,
                binding.button080173,
                binding.button080274,
                binding.button080375,
                binding.button080476,
                binding.button080577,
                binding.button080678,
                binding.button080779,
                binding.button080880
            )
        )
        buttonArrAll = buttonArrWithArr.flatten()
    }

    private fun initialization() {
        nextField = MutableList(9) { 0 } as ArrayList<Int>
        arrSuper = MutableList(9) { MutableList(9) { 0 } }
        setupFirebaseListenerAndChecker()
        nextBoardM = 10
        resetGame()
    }

    private fun setStoke(nextBoard: Int) {

        for (i in arrSuper.indices) {
            for (j in arrSuper[i].indices) {
                uiConfiguration.setStrokeOnButtonOff(
                    buttonArrWithArr[i][j],
                    requireContext()
                )
            }
        }

        for (j in nextField(nextBoard)) {
            if (j !in buttonArrWithArr.indices) {
                Log.e("Error", "Invalid index: $j")
                continue
            }

            buttonArrWithArr[j].forEachIndexed { _, button ->
                uiConfiguration.setStrokeOnButtonOn(
                    button,
                    requireContext()
                )
            }
        }
    }


    private fun setupButtonListeners(nextBoard: Int) {
        disableAllButtons()
        if (gameRules.checkRightPlace(nextBoard)) {
            disableBoardWinner(nextBoard)
            nextBoardM = 10
            nextField = nextField(10) as ArrayList<Int>
            buttonArrWithArr.forEachIndexed { i, _ -> enableBoardButtons(i) }
        } else if (nextBoard == 10) {
            buttonArrWithArr.forEachIndexed { i, _ -> enableBoardButtons(i) }
        } else {
            enableBoardButtons(nextBoard)
        }
    }

    private fun nextField(nextBoard: Int): MutableList<Int> {
        val fieldInBd: MutableList<Int>
        val arr = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8)
        val empty = -1

        val winListSuper = gameRules.getWinListSuper()
        return if (nextBoard == 10 || prevStepInWinListSuper(nextBoard, winListSuper)) {
            winListSuper.forEachIndexed { index, i ->
                if (i in arr.indices && i != 0) { // Перевірка, чи індекс знаходиться в межах допустимого діапазону
                    arr[index] = empty
                }
            }
            Log.d("ooo", "Updated arr: $arr")
            fieldInBd = arr.filter { it != -1 } as MutableList<Int>
            fieldInBd
        } else {
            mutableListOf(nextBoard)
        }
    }

    private fun prevStepInWinListSuper(nextBoard: Int, winListSuper: MutableList<Int>): Boolean {
        winListSuper.forEachIndexed { index, i ->
            return i != 0 && index == nextBoard
        }
        return false
    }

    private fun disableAllButtons() {

        buttonArrWithArr.flatten().forEach {
            it.isClickable = false
        }
    }

    private fun enableAllButtons() {

        buttonArrWithArr.flatten().forEach {
            it.isClickable = true
            binding.bComeBackSuper.isClickable = true
        }
    }

    private fun disableButtonsIsNotNull(array: MutableList<MutableList<Int>>) {

        buttonArrWithArr.forEachIndexed { indexButton, button ->
            array.forEachIndexed { index, _ ->
                if (array[indexButton][index] != 0)
                    button[index].isClickable = false
            }
        }
    }

    private fun disableBoardWinner(i: Int) {
        buttonArrWithArr[i].forEach { it.isClickable = false }
    }

    private fun enableBoardButtons(boardIndex: Int) {
        buttonArrWithArr[boardIndex].forEachIndexed { index, button ->
            button.isClickable = true
            if (gameRules.checkRightPlace(boardIndex)) {
                Log.d("ooo", "chacking======${gameRules.checkRightPlace(boardIndex)}")
                disableBoardWinner(boardIndex)
                nextBoardM = 10
                nextField = nextField(10) as ArrayList<Int>
            } else {
                button.setOnClickListener {
                    updateBoard(boardIndex, index, button)
                    nextBoardM = index
                    nextField = nextField(index) as ArrayList<Int>
                    setupButtonListeners(nextBoardM)
                    if (gameRules.checkRightPlace(nextBoardM)) {
                        disableBoardWinner(nextBoardM)
                        enableBoardButtons(boardIndex)
                    }
                    setStoke(nextBoardM)
                    setupFirebaseListenerAndChecker()
                    disableButtonsIsNotNull(arrSuper)
                }
            }
        }
    }

    private fun updateBoard(boardIndex: Int, index: Int, button: Button) {
        getBoardState { listBD, step ->
            if (listBD[boardIndex][index] == 0) {
                val currentPlayer = if (!step) 2 else 1
                listBD[boardIndex][index] = currentPlayer
                isNextX = !step
                button.text = if (step) "X" else "O"
                arrSuper = listBD
            }
        }
    }

    private fun getBoardState(callback: (MutableList<MutableList<Int>>, Boolean) -> Unit) {

        val gameStatus = getStepSuper(arrSuper)
        binding.textIsNextX.text =
            if (!getStepSuper(arrSuper)) "next step X" else "next step O"

        callback(arrSuper, gameStatus)
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
            0, 3, 5, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36, 39, 42, 45,
            48, 51, 54, 57, 60, 63, 66, 69, 72, 75, 78, 81, 84, 87, 90, 93, 96, 99, 102, 105, 108,
            111, 114, 117, 120, 123
        )
        return y in validNum
    }

    private fun setupFirebaseListenerAndChecker() {
        updateUI(arrSuper)
        gameRules.checkWinSuper(arrSuper, binding)
        setupButtonListeners(nextBoardM)
        nextField = nextField(nextBoardM) as ArrayList<Int>
        Log.d("ooo", "boardState=========$arrSuper")
        Log.d("ooo", "nextBoard=========$nextBoardM")
    }

    private fun updateUI(boardState: MutableList<MutableList<Int>>) {

        boardState.flatten().forEachIndexed { index, value ->
            val button = buttonArrAll[index]
            when (value) {
                1 -> button.text = "X"
                2 -> button.text = "O"
                else -> button.text = " "
            }
        }
    }

    private fun resetGame() {
        arrSuper
        MutableList(9) { MutableList(9) { 0 } }
        buttonArrAll.forEach { uiConfiguration.setBackgroundButtonsSuper(requireContext(), it) }
    }

    private fun replaceFragment(fragment: DialogFragment) {
        // Перевірка чи активність не знищена і не завершується
        if (!parentFragmentManager.isDestroyed) {
            val fragmentTransaction = parentFragmentManager.beginTransaction()

            // Перевірка чи транзакція безпечна (стан активності не був збережений)
            if (!parentFragmentManager.isStateSaved) {
                fragmentTransaction.replace(binding.fragmentContainerView.id, fragment)
                fragmentTransaction.commit()
            } else {
                // Якщо стан активності був збережений, використовуйте commitAllowingStateLoss
                fragmentTransaction.replace(binding.fragmentContainerView.id, fragment)
                fragmentTransaction.commitAllowingStateLoss()
            }
        } else {
            // Логування або інші дії, якщо транзакція не може бути виконана
            println("Замінити фрагмент неможливо: активність знищена або завершується")
        }
    }

    private fun removeFragment() {
        val fragment = parentFragmentManager.findFragmentById(R.id.fragmentContainerView)
        if (fragment != null && !parentFragmentManager.isStateSaved) {
            parentFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
        }
    }
}

