package com.example.supertictactoe.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.supertictactoe.R
import com.example.supertictactoe.databinding.FragmentSimpleTicTacToeBinding
import com.example.supertictactoe.utils.GameRules
import com.example.supertictactoe.utils.UiConfiguration

class OfflineSimple : DialogFragment() {

    private lateinit var binding: FragmentSimpleTicTacToeBinding
    private lateinit var buttonArr: List<Button>
    private val gameRules = GameRules()
    private var arrSimple: MutableList<Int> = MutableList(9) { 0 }
    private val uiConfiguration = UiConfiguration()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSimpleTicTacToeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bComeBackSimple.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        initialization()
    }

    private fun initialization() {
        buttonArr = listOf(
            binding.button1, binding.button2, binding.button3,
            binding.button4, binding.button5, binding.button6,
            binding.button7, binding.button8, binding.button9
        )
        setupButtonListeners()
        resetGame()
    }

    private fun setupButtonListeners() {
        for ((index, button) in buttonArr.withIndex()) {
            button.setOnClickListener {
                updateBoard(index, button)
            }
        }
    }

    private fun updateBoard(index: Int, button: Button) {
        if (arrSimple[index] == 0) {
            val currentPlayer = if (getStep(arrSimple)) 1 else 2
            arrSimple[index] = currentPlayer
            button.text = if (getStep(arrSimple)) "X" else "O"
            updateUI(arrSimple)
            win(gameRules.checkWin(currentPlayer, arrSimple), currentPlayer)
        }
    }

    private fun win(isWin: Boolean, player: Int) {
        val winner = if (player == 1) "X" else "O"
        if (isWin) {
            binding.TicTacToeText.text = "Winner $winner"
        }
    }

    private fun updateUI(boardState: MutableList<Int>) {
        for ((index, button) in buttonArr.withIndex()) {
            when (boardState[index]) {
                1 -> button.text = "X"
                2 -> button.text = "O"
                else -> uiConfiguration.setBackgroundButtonsSimple(requireContext(), button)
            }
        }
    }

    private fun resetGame() {
        gameRules.resetBoardSimple()
        buttonArr.forEach { uiConfiguration.setBackgroundButtonsSimple(requireContext(), it) }
    }

    private fun resetUi() {
        buttonArr.forEach { it.text = "" }
        initialization()
    }
//
//    private fun replaceFragment(fragment: DialogFragment) {
//        if (!parentFragmentManager.isStateSaved) {
//            parentFragmentManager.beginTransaction()
//                .replace(binding.fragmentContainerView3.id, fragment)
//                .commit()
//        }
//    }

    private fun removeFragment() {
        val fragment = parentFragmentManager.findFragmentById(R.id.fragmentConteinerSuper)
        if (fragment != null && !parentFragmentManager.isStateSaved) {
            parentFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
        }
    }

    private fun getStep(list: MutableList<Int>): Boolean {
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
        return when (y) {
            0, 3, 5, 6, 9, 12 -> true
            else -> false
        }
    }
}
