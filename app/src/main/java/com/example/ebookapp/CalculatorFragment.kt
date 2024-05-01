package com.example.ebookapp
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.navdrawerscratch.R
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.Exception

class CalculatorFragment : Fragment() {

    private lateinit var editText: EditText
    private lateinit var resultTextView: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calculator, container, false)

        editText = view.findViewById(R.id.editText)
        resultTextView = view.findViewById(R.id.resultTextView)

        setupInputChangeListener(view)

        val buttonIds = arrayOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
            R.id.buttonDot, R.id.buttonAdd, R.id.buttonSubtract, R.id.buttonMultiply,
            R.id.buttonDivide, R.id.buttonBr1, R.id.buttonBr2, R.id.backspace,
            R.id.AC, R.id.buttonEquals
        )

        for (buttonId in buttonIds) {
            val button = view.findViewById<Button>(buttonId)
            // Set button background colors
            when (buttonId) {
                in arrayOf(
                    R.id.buttonDot, R.id.button0, R.id.button1, R.id.button2, R.id.button3,
                    R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9
                ) -> {
                    setButtonBackground(button, R.color.navi)
                }
                in arrayOf(
                    R.id.buttonEquals, R.id.buttonAdd, R.id.buttonSubtract,
                    R.id.buttonMultiply, R.id.buttonDivide
                ) -> {
                    setButtonBackground(button, R.color.yellow)
                }
                in arrayOf(R.id.backspace, R.id.AC) -> {
                    setButtonBackground(button, R.color.red)
                }
            }
            button.setOnClickListener { onButtonClick(it) }
        }

        view.findViewById<Button>(R.id.buttonEquals).setOnClickListener { onEqualsButtonClick() }

        return view
    }

    private fun setupInputChangeListener(view: View) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val expression = s.toString()
                try {
                    val result = evaluateExpression(expression)
                    resultTextView.setText(result.toString())
                } catch (e: Exception) {

                    resultTextView.text.clear()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun onButtonClick(view: View) {
        val button = view as Button
        val buttonText = button.text.toString()
        val currentText = editText.text.toString()

        when (buttonText) {
            "AC" -> editText.setText("")
            "C" -> {
                if (currentText.isNotEmpty()) {
                    editText.setText(currentText.dropLast(1))
                }
            }
            else -> editText.setText(currentText + buttonText)
        }
    }

    private fun onEqualsButtonClick() {
        val currentText = editText.text.toString()
        try {
            val result = evaluateExpression(currentText)
            val resultInt = result.toInt() // Convert result to integer
            resultTextView.text = Editable.Factory.getInstance().newEditable(resultInt.toString())
        } catch (e: Exception) {
            editText.setText("Error: ${e.message}")
        }
    }

    private fun evaluateExpression(expression: String): Int {
        return ExpressionBuilder(expression).build().evaluate().toInt()
    }

    private fun setButtonBackground(button: Button, colorId: Int) {
        val color = ContextCompat.getColor(requireContext(), colorId)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_button)
        drawable?.setColorFilter(color, PorterDuff.Mode.SRC)
        button.background = drawable
    }
}
