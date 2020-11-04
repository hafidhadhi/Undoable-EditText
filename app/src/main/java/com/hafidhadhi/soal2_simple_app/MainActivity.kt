package com.hafidhadhi.soal2_simple_app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    View.OnClickListener {

    private val REVERSED_INPUT_TEXT = ".EXTRA_REVERSED_INPUT_TEXT"

    private var revInputTxt: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) {
            revInputTxt = savedInstanceState.getString(REVERSED_INPUT_TEXT, "")
        }
        initialProcess()
    }

    private fun initialProcess() {
        label.text = getString(R.string.output, revInputTxt)
        reverseBtn.setOnClickListener(this)
        undoRedoBtn.setOnClickListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(REVERSED_INPUT_TEXT, revInputTxt)
    }

    override fun onClick(v: View) {
        when (v) {
            reverseBtn -> {
                label.apply {
                    val currentInput = input.text.toString()
                    val revCurrentInput = currentInput.reversed()
                    text = getString(R.string.output, revCurrentInput)
                    revInputTxt = revCurrentInput
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