// MainActivity.kt
package com.example.supertictactoe.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.supertictactoe.R
import com.example.supertictactoe.databinding.ActivityMainBinding
import com.example.supertictactoe.repository.FirebaseRepository
import com.exemple.ticktacktoe.DialogHelper.DialogHelper

//import com.exemple.ticktacktoe.DialogHelper.DialogHelper

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val bundle = Bundle()

    //    private val dialogHelper = DialogHelper(this)
    private val firebaseService = FirebaseRepository()
    private var online = false
    private val dialogHelper = DialogHelper(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bRooms.setOnClickListener {
            dialogHelper.createRoomDialog()
        }

        binding.chip.setOnCheckedChangeListener { _, isChecked ->
            online = isChecked
        }


        binding.bPlaySimple.setOnClickListener {
            if (online) {
                binding.fragmentConteinerSuper.visibility = View.VISIBLE
                binding.mainConteiner.visibility = View.GONE
                replaceFragment(SimpleTicTacToe())
            } else {
                binding.fragmentConteinerSuper.visibility = View.VISIBLE
                binding.mainConteiner.visibility = View.GONE
                replaceFragment(OfflineSimple())
            }
        }
        binding.bPlaySuper.setOnClickListener {
            if (online) {
                binding.fragmentConteinerSuper.visibility = View.VISIBLE
                binding.mainConteiner.visibility = View.GONE
                replaceFragment(SuperTicTacToe())
            } else {
                binding.fragmentConteinerSuper.visibility = View.VISIBLE
                binding.mainConteiner.visibility = View.GONE
                replaceFragment(OfflineSuper())
            }
        }
    }

    private fun replaceFragment(fragment: DialogFragment) {
        if (!isFinishing && !supportFragmentManager.isDestroyed) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()

            if (!supportFragmentManager.isStateSaved) {
                fragmentTransaction.replace(binding.fragmentConteinerSuper.id, fragment)
                fragmentTransaction.commit()
            } else {
                fragmentTransaction.replace(binding.fragmentConteinerSuper.id, fragment)
                fragmentTransaction.commitAllowingStateLoss()
            }
        } else {
            println("Замінити фрагмент неможливо: активність знищена або завершується")
        }
    }

    private fun removeFragment() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentConteinerSuper)
        if (fragment != null && !supportFragmentManager.isStateSaved) {
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
        }
    }
}
