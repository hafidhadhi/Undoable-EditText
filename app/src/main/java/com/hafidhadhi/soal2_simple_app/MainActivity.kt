package com.hafidhadhi.soal2_simple_app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    View.OnClickListener {

    private val CURRENT_INPUT_EXTRA_KEY = ".EXTRA_CURRENT_INPUT"

    private var currentInput: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) {
            currentInput = savedInstanceState.getString(CURRENT_INPUT_EXTRA_KEY, "")
        }
        initialProcess()
    }

    private fun initialProcess() {
        label.text = getString(R.string.output, currentInput)
        reverseBtn.setOnClickListener(this)
        undoRedoBtn.setOnClickListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_INPUT_EXTRA_KEY, input.text.toString())
    }

    override fun onClick(v: View) {
        when (v) {
            reverseBtn -> {
                label.apply {
                    val currentInput = input.text.toString()
                    val revCurrentInput = getString(R.string.output, currentInput.reversed())
                    text = revCurrentInput
                }
            }
            undoRedoBtn -> {
                input.undoRedo()
            }
            else -> {
            }
        }
    }
}