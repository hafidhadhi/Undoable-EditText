package com.hafidhadhi.soal2_simple_app

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText

class UndoableEditText(ctx: Context, attrs: AttributeSet?) : AppCompatEditText(ctx, attrs) {

    private val INPUT_HISTORY_EXTRA_KEY = ".EXTRA_INPUT_HISTORY"
    private val VIEW_STATE_EXTRA_KEY = ".EXTRA_VIEW_STATE"
    private val UNDO_REDO_STATE_KEY = ".EXTRA_UNDO_REDO_STATE"

    private val imm = ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    private var inputHistory = arrayListOf<String>()
    private var undoRedoState = true
    private var pressedTime = 0L
    private val undoRedoHandler = Handler(Looper.getMainLooper())
    private var newText = ""
    private val runnableSetText = Runnable { setText(newText) }

    /**
     * must single-lined because multiline text view doesn't have ime action on soft keyboard
     * which is used as condition to save current inputted text
     * @see com.hafidhadhi.soal2_simple_app.UndoableEditText.onEditorAction
     */
    init {
        isSingleLine = true
    }

    /**Undo in first click, Redo in second click, Undo in third click, and so on*/
    fun undoRedo() {
        Log.d(this::class.java.simpleName, inputHistory.toList().toString())
        if (inputHistory.size > 1) {
            val currentInput = inputHistory.last()
            val prevInput = inputHistory.first()
            val newInput =
                if (undoRedoState) prevInput else currentInput
            undoRedoState = !undoRedoState
            setText(newInput)
        }
    }

    /** Undo with single click, Redo with double click*/
    fun undoRedo2() {
        Log.d(this::class.java.simpleName, inputHistory.toList().toString() + pressedTime)
        if (inputHistory.size > 1) {
            val currentInput = inputHistory.last()
            val prevInput = inputHistory.first()
            newText =
                if (pressedTime + 300 > System.currentTimeMillis()) currentInput else prevInput
            pressedTime = System.currentTimeMillis()
            undoRedoHandler.removeCallbacks(runnableSetText)
            undoRedoHandler.postDelayed(runnableSetText, 300)
        }
    }

    /**
    this edit text only store inputted value when user click on ime action
     */
    override fun onEditorAction(actionCode: Int) {
        super.onEditorAction(actionCode)
        clearFocus()
        imm.hideSoftInputFromWindow(windowToken, 0)
        inputHistory.saveInput(text.toString())
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val bundle = state as Bundle
        val viewState = bundle.getParcelable<SavedState>(VIEW_STATE_EXTRA_KEY)
        inputHistory = bundle.getStringArrayList(INPUT_HISTORY_EXTRA_KEY) as ArrayList<String>
        undoRedoState = bundle.getBoolean(UNDO_REDO_STATE_KEY)
        super.onRestoreInstanceState(viewState)
    }

    override fun onSaveInstanceState(): Parcelable? {
        return Bundle().apply {
            putParcelable(VIEW_STATE_EXTRA_KEY, super.onSaveInstanceState())
            putStringArrayList(INPUT_HISTORY_EXTRA_KEY, inputHistory)
            putBoolean(UNDO_REDO_STATE_KEY, undoRedoState)
        }
    }

    fun ArrayList<String>.saveInput(value: String) {
        if (size > 1) removeAt(0)
        add(value)
    }
}