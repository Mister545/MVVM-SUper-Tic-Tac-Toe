package com.example.supertictactoe.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.supertictactoe.R
import com.example.supertictactoe.databinding.FragmentSimpleTicTacToeBinding
import com.example.supertictactoe.utils.UiConfiguration
import com.exemple.ticktacktoe.DialogHelper.DialogHelper

class SimpleTicTacToe : DialogFragment() {

    private val viewModel: SimpleTicTacToeViewModel by viewModels()

    private lateinit var binding: FragmentSimpleTicTacToeBinding
    private lateinit var buttonArr: List<Button>
    private val uiConfiguration = UiConfiguration()
    private val dialogHelper by lazy { DialogHelper(requireActivity() as MainActivity) }
    private val data = arguments?.getString("type")
    private val code = arguments?.getString("code")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSimpleTicTacToeBinding.inflate(inflater, container, false)

        buttonArr = arrayListOf(
            binding.button1, binding.button2, binding.button3,
            binding.button4, binding.button5, binding.button6,
            binding.button7, binding.button8, binding.button9,
        )


        Log.d("ooo", "data type: $data")

        Log.d("ooo", "codeeeeeeeee = ${code}")
        Log.d("ooo", "typeeeeeeeee = ${data}")

        viewModel.startGame(data, code)

        setupButtonListeners()
        updateUi()

        binding.bComeBackSimple.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            removeFragment()
        }
        return binding.root
    }

    private fun updateUi() {
        viewModel.gameState.observe(this) { gameState ->
            disableEnoughButtons(gameState.data)
            gameState.data.forEachIndexed { index, i ->
                buttonArr[index].text = (if (i == 1) "X" else if (i == 2) "O" else "")
            }
            binding.isNext.visibility = View.VISIBLE
            binding.isNext.text = if (gameState.IsNextX) "next X" else "next O"
        }

        winListener()

        viewModel.codeRoom.observe(this) {
            if (viewModel.typeGame.value == "create simple" || viewModel.typeGame.value == "sign in simple") {
                binding.codeRoom.visibility = View.VISIBLE
                binding.codeRoom.text = "code ${it.takeLast(6)}"
            }
        }
    }

    private fun winListener() {
        viewModel.winner.observe(this) {
            if (it == 1)
            dialogHelper.createWinDialog("Win X", this)
            else if (it == 2)
                dialogHelper.createWinDialog("Win O", this)
            else if (it == 3)
                dialogHelper.createWinDialog("Draw", this)
        }
    }

    private fun disableEnoughButtons(data: MutableList<Int>) {
        data.forEachIndexed { index, i ->
            if (i != 0)
                buttonArr[index].isClickable = false
        }
    }

    private fun setupButtonListeners() {
        for ((index, button) in buttonArr.withIndex()) {
            uiConfiguration.setBackgroundButtonsSimple(requireContext(), button)

            button.setOnClickListener {
                viewModel.setBoardStep(index)
            }
        }
    }

    private fun removeFragment() {
        val fragment = parentFragmentManager.findFragmentById(R.id.fragmentConteinerSuper)
        if (fragment != null && !parentFragmentManager.isStateSaved) {
            parentFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
        }
    }
}

