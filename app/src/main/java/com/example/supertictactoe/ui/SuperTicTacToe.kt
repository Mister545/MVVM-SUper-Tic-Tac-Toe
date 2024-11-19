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
import com.example.supertictactoe.databinding.FragmentSuperTicTacToeBinding
import com.example.supertictactoe.utils.GameRules
import com.example.supertictactoe.utils.UiConfiguration

class SuperTicTacToe : DialogFragment() {

    private lateinit var binding: FragmentSuperTicTacToeBinding
    private lateinit var buttonArrWithArr: List<List<Button>>
    private lateinit var buttonArrAll: List<Button>
    private val uiConfiguration = UiConfiguration()

    private val viewModel: SuperTicTacToeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuperTicTacToeBinding.inflate(inflater, container, false)

        val data = arguments?.getString("type")
        val code = arguments?.getString("code")
        Log.d("ooo", "data type: $data")

        viewModel.startGame(data, code)
        updateUi()
        setupButtons()
        setStyleOnAllButtons()
        setupButtonListeners()

        binding.bComeBackSuper.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
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

    private fun setStyleOnAllButtons() {
        buttonArrWithArr.forEach { listButtons ->
            listButtons.forEach { button ->
                uiConfiguration.setBackgroundButtonsSuper(
                    requireContext(), button
                )
            }
        }
    }

    private fun updateUi() {
        viewModel.gameState.observe(this) { gameState ->

            gameState.data.forEachIndexed { index, ints ->
                ints.forEachIndexed { index2, i ->
                    if (i == 1)
                        buttonArrWithArr[index][index2].text = "X"
                    else if (i == 2)
                        buttonArrWithArr[index][index2].text = "O"
                    else
                        buttonArrWithArr[index][index2].text = ""
                }
            }

            binding.textIsNextX.text = if (gameState.IsNextX) "next X" else "next O"
            setStoke(gameState.nextField, gameState.data)

            setNextField(gameState.nextField)

            disableButtonsIsNotNull()

        }

        viewModel.winner.observe(this) {
            updateWinnerUi(it)
        }

        viewModel.codeRoom.observe(this) {
            if (viewModel.typeGame.value == "create super" || viewModel.typeGame.value == "sign in super"){
                binding.codeRoom.visibility = View.VISIBLE
                binding.codeRoom.text = "code ${it.takeLast(6)}"
            }
        }
    }

    private fun updateWinnerUi(i: Int) {
        if (i == 1)
            binding.TextWin.text = "Win X"
        if (i == 2)
            binding.TextWin.text = "Win O"
        if (i == 3)
            binding.TextWin.text = "Draw"
    }

    private fun setupButtonListeners() {
        buttonArrWithArr.forEachIndexed { index1, buttons ->
            buttons.forEachIndexed { index2, button ->

                uiConfiguration.setBackgroundButtonsSuper(requireContext(), button)

                button.setOnClickListener {
                    viewModel.setBoardStep(index1, index2)
                }
            }
        }
    }

    private fun setNextField(nextField: MutableList<Int>){

        buttonArrAll.forEachIndexed { index2, button ->
            button.isClickable = false
        }

//        Log.d("ooo", "nextField2222 $nextField")

        nextField.forEachIndexed { index, i ->
            buttonArrWithArr[i].forEachIndexed { index2, button ->
                button.isClickable = true
            }
        }
    }

    private fun setStoke(nextField: MutableList<Int>, data: MutableList<MutableList<Int>>) {
        for (i in data.indices) {
            for (j in data[i].indices) {
                uiConfiguration.setStrokeOnButtonOff(
                    buttonArrWithArr[i][j],
                    requireContext()
                )
            }
        }

        for (j in nextField) {
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

    private fun disableButtonsIsNotNull() {
        buttonArrAll.forEachIndexed { indexButton, button ->
            if (button.text != ""){
                button.isClickable = false
            }
        }
    }
}



