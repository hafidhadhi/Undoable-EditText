package com.hafidhadhi.soal2_simple_app

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText

class UndoableEditText(ctx: Context, attrs: AttributeSet?) : AppCompatEditText(ctx, attrs) {

    private val INPUT_HISTORY_EXTRA_KEY = ".EXTRA_INPUT_HISTORY"
    private val VIEW_STATE_EXTRA_KEY = ".EXTRA_VIEW_STATE"

    private var inputHistory = arrayListOf<String>()
    private val imm = ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    init {
        isSingleLine = true
    }

    fun undoRedo() {
        Log.d(this::class.java.simpleName, inputHistory.toList().toString())
        if (inputHistory.size > 1) {
            val currentInput = inputHistory[inputHistory.size - 1]
            val prevInput = inputHistory[inputHistory.size - 2]
            val newInput =
                if (text.toString() == currentInput) prevInput else currentInput
            setText(newInput)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val bundle = state as Bundle
        val viewState = bundle.getParcelable<SavedState>(VIEW_STATE_EXTRA_KEY)
        inputHistory = bundle.getStringArrayList(INPUT_HISTORY_EXTRA_KEY) as ArrayList<String>
        super.onRestoreInstanceState(viewState)
    }

    override fun onSaveInstanceState(): Parcelable? {
        return Bundle().apply {
            putParcelable(VIEW_STATE_EXTRA_KEY, super.onSaveInstanceState())
            putStringArrayList(INPUT_HISTORY_EXTRA_KEY, inputHistory)
        }
    }

    private fun ArrayList<String>.specialAdd(value: String) {
        if (size > 1) removeAt(0)
        add(value)
    }
}