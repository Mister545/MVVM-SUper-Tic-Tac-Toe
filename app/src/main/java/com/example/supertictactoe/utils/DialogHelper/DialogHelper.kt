package com.exemple.ticktacktoe.DialogHelper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.supertictactoe.databinding.RoomDialogBinding
import com.example.supertictactoe.databinding.WinDialogBinding
import com.example.supertictactoe.ui.MainActivity
import com.example.supertictactoe.ui.SimpleTicTacToe
import com.example.supertictactoe.ui.SuperTicTacToe
import com.example.supertictactoe.utils.Servers

class DialogHelper(private val mainActivity: MainActivity) {
    private val servers = Servers()
    private val bundle = Bundle()

    fun createRoomDialog() {
        val builder = AlertDialog.Builder(mainActivity)
        val binding = RoomDialogBinding.inflate(mainActivity.layoutInflater)
        val view = binding.root
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()

        binding.bCreateRoomSimple.setOnClickListener {
            bundle.putString("type", "create simple")
            val fragment = SimpleTicTacToe().apply { arguments = bundle }

            mainActivity.binding.fragmentConteinerSuper.visibility = View.VISIBLE
            mainActivity.binding.mainConteiner.visibility = View.GONE

            replaceFragment(fragment)
            dialog.dismiss()
        }

        binding.bCreateRoomSuper.setOnClickListener {
            bundle.putString("type", "create super")
            val fragment = SuperTicTacToe().apply { arguments = bundle }

            mainActivity.binding.fragmentConteinerSuper.visibility = View.VISIBLE
            mainActivity.binding.mainConteiner.visibility = View.GONE

            replaceFragment(fragment)
            dialog.dismiss()
        }

        binding.bSignInRoom.setOnClickListener {
            val code = binding.eCodeRoom.text.toString()
            if (code.isNotEmpty()) {
                servers.findGame(code.toInt()) { result ->
                    Log.d("ooo", "result = $result")

                    val fragment = when (result) {
                        "SimpleR" -> {
                            bundle.putString("type", "sign in simple")
                            bundle.putString("code", code)
                            SimpleTicTacToe().apply { arguments = bundle }
                        }
                        "SuperR" -> {
                            bundle.putString("type", "sign in super")
                            bundle.putString("code", code)
                            SuperTicTacToe().apply { arguments = bundle }
                        }
                        else -> null
                    }

                    if (fragment != null) {
                        mainActivity.binding.fragmentConteinerSuper.visibility = View.VISIBLE
                        mainActivity.binding.mainConteiner.visibility = View.GONE
                        replaceFragment(fragment)
                        dialog.dismiss()
                    } else {
                        binding.textView3.text = "Wrong code"
                        binding.textView3.visibility = View.VISIBLE
                    }
                }
            } else {
                binding.textView3.text = "Wrong code"
                binding.textView3.visibility = View.VISIBLE
            }
        }
    }

    fun createWinDialog(text: String, fragment: DialogFragment) {
        val createDialog = AlertDialog.Builder(mainActivity)
        val binding = WinDialogBinding.inflate(mainActivity.layoutInflater)
        val view = binding.root
        createDialog.setView(view)

        val dialog = createDialog.create()
        dialog.show()

        binding.tResult.text = text
        binding.bMenu.setOnClickListener {
            mainActivity.binding.mainConteiner.visibility = View.VISIBLE
            mainActivity.binding.fragmentConteinerSuper.visibility = View.GONE
            dialog.dismiss()
        }
        binding.bRestart.setOnClickListener {
            replaceFragment(fragment)
            dialog.dismiss()
        }
    }

    private fun replaceFragment(fragment: DialogFragment) {
        val fragmentManager = mainActivity.supportFragmentManager

        if (!fragmentManager.isStateSaved) {
            fragmentManager.beginTransaction().apply {
                replace(mainActivity.binding.fragmentConteinerSuper.id, fragment)
                commit()
            }
        } else {
            fragmentManager.beginTransaction().apply {
                replace(mainActivity.binding.fragmentConteinerSuper.id, fragment)
                commitAllowingStateLoss()
            }
        }
    }
}
