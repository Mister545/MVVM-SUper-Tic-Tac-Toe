package com.example.supertictactoe.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.supertictactoe.R

class UiConfiguration{

    fun setBackgroundButtonsSuper(context: Context, button: Button) {
        button.setBackgroundResource(R.drawable.button_background_tic_tac_toe)

        val params = button.layoutParams
        params.width = dpToPx(context, 30)
        params.height = dpToPx(context, 30)
        button.layoutParams = params

        button.setTextColor(ContextCompat.getColor(context, R.color.black))
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, spToPx(context, 20f))
        button.setTypeface(null, android.graphics.Typeface.BOLD)
    }

    fun setBackgroundButtonsSimple(context: Context, button: Button){
        button.setBackgroundResource(R.drawable.button_background_tic_tac_toe_simple)

        val params = button.layoutParams
        params.width = 100.dpToPx()
        params.height = 100.dpToPx()
        button.layoutParams = params

        button.setTextColor(ContextCompat.getColor(context, R.color.black))
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, spToPx(context, 75f))
        button.setTypeface(null, android.graphics.Typeface.BOLD)
    }

    fun setStrokeOnButtonOn(button: Button, context: Context) {
        val background = button.background as? GradientDrawable
        if (background != null) {
            val strokeWidth = context.resources.getDimensionPixelSize(R.dimen.new_stroke_width)
            val strokeColor = ContextCompat.getColor(context, R.color.black)
            background.setStroke(strokeWidth, strokeColor)
        }
    }
    fun setStrokeOnButtonOff(button: Button, context: Context) {
        val background = button.background as? GradientDrawable
        if (background != null) {
            val strokeWidth = context.resources.getDimensionPixelSize(R.dimen.new_stroke_width)
            val strokeColor = ContextCompat.getColor(context, R.color.stroke_buttons)
            background.setStroke(strokeWidth, strokeColor)
        }
    }

    private fun spToPx(context: Context, sp: Float): Float {
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return sp * scaledDensity
    }
    private fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }
    private fun dpToPx(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }
}