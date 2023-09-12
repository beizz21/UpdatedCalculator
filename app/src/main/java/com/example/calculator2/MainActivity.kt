package com.example.calculator2
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Button
import kotlin.math.*

class MainActivity : AppCompatActivity() {


    val starting_input = "0"
    var firstNumber = starting_input //first number in calculation
    var secondNumber = starting_input //second number in calculation
    var operatorPressed = false // shows when operator is pressed
    var operator = "" // shows which operator is pressed

    //allows calculator to remember number in case of clicking '=' multiple times
    var equalsNumber = ""

    //accounts for edge case described in inScribe
    //If the user presses =, and then clicks a new number, acts as 'clear' action.
    var equalsPressed = false

    private var isPortraitMode = true

    fun toggleOrientation() {
        if (isPortraitMode) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        isPortraitMode = !isPortraitMode
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rotateButton = findViewById<Button>(R.id.rotateButton)
        rotateButton.setOnClickListener {
            toggleOrientation()
        }

        setContentView(R.layout.activity_main)

        val displayText: TextView =
            findViewById(R.id.input) //calculator display text variable
        displayText.text = starting_input //default number shown on calculator (0)

        //buttons 0-9
        val numberButtonIds = arrayOf(
            R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3,
            R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7,
            R.id.button_8, R.id.button_9, R.id.button_sin, R.id.button_cos,
            R.id.button_tan, R.id.button_log, R.id.button_ln
        )

        fun executeCalculation() { //given the firstNumber, operator, and secondNumber, do math and set number on screen. // result of calculation becomes firstNumber
            var result = ""

            if (equalsNumber != starting_input) {
                secondNumber = equalsNumber
            }

            if (operator == "/") {
                result = (firstNumber.toFloat() / secondNumber.toFloat()).toString()
            }

            if (operator == "*") {
                result = (firstNumber.toFloat() * secondNumber.toFloat()).toString()
            }

            if (operator == "+") {
                result = (firstNumber.toFloat() + secondNumber.toFloat()).toString()
            }

            if (operator == "-") {
                result = (firstNumber.toFloat() - secondNumber.toFloat()).toString()
            }

            //converts number back to integer if possible, 7.0 to 7

            if (result.endsWith(".0")) {
                result = result.removeSuffix(".0")
            }


            fun onTrigFunctionClick(view: View) {
                if (!operatorPressed) {
                    val trigFunction = (view as Button).text.toString()
                    val inputNumber = if (equalsPressed) equalsNumber.toDouble()
                    else firstNumber.toDouble()
                    val result = when (trigFunction) {
                        "sin" -> sin(inputNumber)
                        "cos" -> cos(inputNumber)
                        "tan" -> tan(inputNumber)
                        else -> throw IllegalArgumentException("Invalid trigonometric function")
                    }
                    firstNumber = result.toString()
                    displayText.text = firstNumber
                    equalsPressed = true
                }

                fun onLogFunctionClick(view: View) {
                    if (!operatorPressed) {
                        val logFunction = (view as Button).text.toString()
                        val inputNumber =
                            if (equalsPressed) equalsNumber.toDouble() else firstNumber.toDouble()
                        val result = when (logFunction) {
                            "log" -> log10(inputNumber)
                            "ln" -> ln(inputNumber)
                            else -> throw IllegalArgumentException("Invalid logarithmic function")
                        }
                        firstNumber = result.toString()
                        displayText.text = firstNumber
                        equalsPressed = true
                    }
                }


                //update values
                secondNumber = starting_input
                firstNumber = result
                displayText.text = result
                operatorPressed = false
            }

            //array of the operator ids iterates over to ensure all are not pressed.
            val operatorIds = arrayOf(
                R.id.button_division,
                R.id.button_multiply,
                R.id.button_subtraction,
                R.id.button_addition
            )

            fun resetOperators() { //resets press state of operators for clear button or when another operator is pressed
                for (button in operatorIds) {
                    val buttonObj: Button = findViewById(button)
                    buttonObj.isPressed = false
                }
            }

            //iterate over number buttons, setup click listeners
            for (buttonId in numberButtonIds) {
                val numberButton: Button = findViewById(buttonId)



                numberButton.setOnClickListener {
                    val clickedNumber =
                        resources.getResourceEntryName(buttonId).removeSuffix("Button")


                    var stringValue = "0"

                    if (clickedNumber == "button_1") {
                        stringValue = "1"
                    }
                    if (clickedNumber == "button_2") {
                        stringValue = "2"
                    }
                    if (clickedNumber == "button_3") {
                        stringValue = "3"
                    }
                    if (clickedNumber == "button_4") {
                        stringValue = "4"
                    }
                    if (clickedNumber == "button_5") {
                        stringValue = "5"
                    }
                    if (clickedNumber == "button_6") {
                        stringValue = "6"
                    }
                    if (clickedNumber == "button_7") {
                        stringValue = "7"
                    }
                    if (clickedNumber == "button_8") {
                        stringValue = "8"
                    }
                    if (clickedNumber == "button_9") {
                        stringValue = "9"
                    }
                    if (clickedNumber == "button_0") {
                        stringValue = "0"
                    }



                    if (equalsPressed) {

                        secondNumber = starting_input
                        operator = ""
                        operatorPressed = false

                        firstNumber = stringValue
                        displayText.text = firstNumber

                        equalsPressed = false
                    } else {
                        if (operatorPressed) {

                            if (secondNumber == starting_input && stringValue != "0") {

                                displayText.text = stringValue
                                secondNumber = stringValue
                            } else { // the numbers are in, so add them
                                val currentText =
                                    displayText.text.toString() //current text displayed
                                displayText.text = "$currentText$stringValue"
                                secondNumber += stringValue

                            }
                        } else { // if operator is not pressed, add to first number

                            if (firstNumber == starting_input && stringValue != "0") {

                                displayText.text = stringValue
                                firstNumber = stringValue
                            } else { //already numbers in, just add them

                                val currentText =
                                    displayText.text.toString() //current text displayed
                                displayText.text = "$currentText$stringValue"
                                firstNumber += stringValue
                            }
                        }
                    }
                }
            }

            //clear button
            val clearButton: Button = findViewById(R.id.button_clear)
            clearButton.setOnClickListener {

                //make everything default

                firstNumber = starting_input
                secondNumber = starting_input
                equalsNumber = starting_input
                operator = ""
                operatorPressed = false
                equalsPressed = false
                displayText.text = starting_input
                resetOperators()
            }

            //decimal button
            val decimalButton: Button = findViewById(R.id.button_dot)
            decimalButton.setOnClickListener {

                equalsNumber = starting_input

                /* In case this is already defined */
                if (equalsPressed) {
                    secondNumber = starting_input
                    operator = ""
                    operatorPressed = false

                    firstNumber = "."
                    displayText.text = firstNumber

                    equalsPressed = false
                }

                if (operatorPressed) {
                    if ('.' !in secondNumber) { // makes sure number doesn't already have a decimal
                        val currentText = displayText.text.toString() //current text displayed

                        if (secondNumber == starting_input) {
                            displayText.text = "."
                        } else {
                            displayText.text = "$currentText."
                        }

                        secondNumber += "."
                    }
                } else { //1st number
                    if ('.' !in firstNumber) { // don't want multiple decimals
                        firstNumber += "."

                        val currentText = displayText.text.toString() //current text displayed
                        displayText.text = "$currentText."
                    }
                }
            }


            // +/- button
            val signButton: Button = findViewById(R.id.button_sign)
            signButton.setOnClickListener {

                if (operatorPressed) {

                    if ('-' !in secondNumber) { // Enters a "-" in front of the number

                        secondNumber = "-$secondNumber"
                        displayText.text = secondNumber
                    } else {
                        secondNumber = secondNumber.replace(
                            "-",
                            ""
                        ) // replaces minus with nothing to display the sign change
                        displayText.text = secondNumber
                    }
                } else { //1st num

                    if ('-' !in firstNumber) {

                        firstNumber = "-$firstNumber"
                        displayText.text = firstNumber
                    } else {
                        firstNumber = firstNumber.replace("-", "")
                        displayText.text = firstNumber
                    }
                }
            }

            // % button
            val percentButton: Button = findViewById(R.id.button_percentage)
            percentButton.setOnClickListener {

                if (operatorPressed) {

                    secondNumber = (secondNumber.toFloat() / 100).toString()
                    displayText.text = secondNumber

                } else { //1st number
                    firstNumber = (firstNumber.toFloat() / 100).toString()
                    displayText.text = firstNumber
                }
            }

            // '/' - division button
            val divisionButton: Button = findViewById(R.id.button_division)

            divisionButton.setOnClickListener {

                equalsNumber = starting_input

                if (equalsPressed) { //edge case

                    equalsPressed = false
                    secondNumber = starting_input
                    equalsNumber = ""
                }

                if (operatorPressed) { //execute calculation
                    executeCalculation()
                }
                operatorPressed = true // set to true
                operator = "/"
            }

            // * - multiply button
            val multiplyButton: Button = findViewById(R.id.button_multiply)
            multiplyButton.setOnClickListener {
                equalsNumber = starting_input
                if (equalsPressed) { //edge case
                    equalsPressed = false
                    secondNumber = starting_input
                }

                if (operatorPressed) {

                    executeCalculation()
                }

                operatorPressed = true  // set to true
                operator = "*"
            }

            // '-' - subtract button
            val subtractButton: Button = findViewById(R.id.button_subtraction)
            subtractButton.setOnClickListener {

                equalsNumber = starting_input

                if (equalsPressed) {

                    equalsPressed = false
                    secondNumber = starting_input
                }

                if (operatorPressed) {

                    executeCalculation()
                }

                operatorPressed = true
                operator = "-"
            }

            // '+' - addition button
            val addButton: Button = findViewById(R.id.button_addition)
            addButton.setOnClickListener {

                equalsNumber = starting_input

                if (equalsPressed) {

                    equalsPressed = false
                    secondNumber = starting_input
                }

                if (operatorPressed) {

                    executeCalculation()
                }

                operatorPressed = true
                operator = "+"
            }

            // = button
            val equalsButton: Button = findViewById(R.id.button_equals)

            equalsButton.setOnClickListener {
                if (equalsNumber == starting_input) {

                    equalsNumber = secondNumber
                }

                if (operatorPressed && secondNumber == starting_input) { //account for if equals pressed when 1 number inputted

                    secondNumber = firstNumber
                    executeCalculation()
                    operatorPressed = true
                } else {
                    executeCalculation()
                }


                equalsPressed = true
            }
        }
    }
}