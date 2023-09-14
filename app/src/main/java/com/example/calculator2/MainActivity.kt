package com.example.calculator2
import android.R
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(com.example.calculator2.R.layout.activity_main)

        val displayNumber: TextView =
            findViewById(com.example.calculator2.R.id.input) //calculator display text variable
        displayNumber.text = starting_input //default number shown on calculator (0)

        //buttons 0-9
        val numberButtonIds = arrayOf(
            com.example.calculator2.R.id.button_0, com.example.calculator2.R.id.button_1, com.example.calculator2.R.id.button_2, com.example.calculator2.R.id.button_3,
            com.example.calculator2.R.id.button_4, com.example.calculator2.R.id.button_5, com.example.calculator2.R.id.button_6, com.example.calculator2.R.id.button_7,
            com.example.calculator2.R.id.button_8, com.example.calculator2.R.id.button_9
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


                //update values
                secondNumber = starting_input
                firstNumber = result
                displayNumber.text = result
                operatorPressed = false
            }

            //array of the operator ids iterates over to ensure all are not pressed.
            val operatorIds = arrayOf(
                com.example.calculator2.R.id.button_division,
                com.example.calculator2.R.id.button_multiply,
                com.example.calculator2.R.id.button_subtraction,
                com.example.calculator2.R.id.button_addition
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
                        displayNumber.text = firstNumber

                        equalsPressed = false
                    } else {
                        if (operatorPressed) {

                            if (secondNumber == starting_input && stringValue != "0") {

                                displayNumber.text = stringValue
                                secondNumber = stringValue
                            } else { // the numbers are in, so add them
                                val currentText =
                                    displayNumber.text.toString() //current text displayed
                                displayNumber.text = "$currentText$stringValue"
                                secondNumber += stringValue

                            }
                        } else { // if operator is not pressed, add to first number

                            if (firstNumber == starting_input && stringValue != "0") {

                                displayNumber.text = stringValue
                                firstNumber = stringValue
                            } else { //already numbers in, just add them

                                val currentText =
                                    displayNumber.text.toString() //current text displayed
                                displayNumber.text = "$currentText$stringValue"
                                firstNumber += stringValue
                            }
                        }
                    }
                }
            }

            //clear button
            val clearButton: Button = findViewById(com.example.calculator2.R.id.button_clear)
            clearButton.setOnClickListener {

                //make everything default

                firstNumber = starting_input
                secondNumber = starting_input
                equalsNumber = starting_input
                operator = ""
                operatorPressed = false
                equalsPressed = false
                displayNumber.text = starting_input
                resetOperators()
            }

            //decimal button
            val decimalButton: Button = findViewById(com.example.calculator2.R.id.button_dot)
            decimalButton.setOnClickListener {

                equalsNumber = starting_input

                /* In case this is already defined */
                if (equalsPressed) {
                    secondNumber = starting_input
                    operator = ""
                    operatorPressed = false

                    firstNumber = "."
                    displayNumber.text = firstNumber

                    equalsPressed = false
                }

                if (operatorPressed) {
                    if ('.' !in secondNumber) { // makes sure number doesn't already have a decimal
                        val currentText = displayNumber.text.toString() //current text displayed

                        if (secondNumber == starting_input) {
                            displayNumber.text = "."
                        } else {
                            displayNumber.text = "$currentText."
                        }

                        secondNumber += "."
                    }
                } else { //1st number
                    if ('.' !in firstNumber) { // don't want multiple decimals
                        firstNumber += "."

                        val currentText = displayNumber.text.toString() //current text displayed
                        displayNumber.text = "$currentText."
                    }
                }
            }


            // +/- button
            val signButton: Button = findViewById(com.example.calculator2.R.id.button_sign)
            signButton.setOnClickListener {

                if (operatorPressed) {

                    if ('-' !in secondNumber) { // Enters a "-" in front of the number

                        secondNumber = "-$secondNumber"
                        displayNumber.text = secondNumber
                    } else {
                        secondNumber = secondNumber.replace(
                            "-",
                            ""
                        ) // replaces minus with nothing to display the sign change
                        displayNumber.text = secondNumber
                    }
                } else { //1st num

                    if ('-' !in firstNumber) {

                        firstNumber = "-$firstNumber"
                        displayNumber.text = firstNumber
                    } else {
                        firstNumber = firstNumber.replace("-", "")
                        displayNumber.text = firstNumber
                    }
                }
            }

            // % button
            val percentButton: Button = findViewById(com.example.calculator2.R.id.button_percentage)
            percentButton.setOnClickListener {

                if (operatorPressed) {

                    secondNumber = (secondNumber.toFloat() / 100).toString()
                    displayNumber.text = secondNumber

                } else { //1st number
                    firstNumber = (firstNumber.toFloat() / 100).toString()
                    displayNumber.text = firstNumber
                }
            }

            // '/' - division button
            val divisionButton: Button = findViewById(com.example.calculator2.R.id.button_division)

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
            val multiplyButton: Button = findViewById(com.example.calculator2.R.id.button_multiply)
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
            val subtractButton: Button = findViewById(com.example.calculator2.R.id.button_subtraction)
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
            val addButton: Button = findViewById(com.example.calculator2.R.id.button_addition)
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
            val equalsButton: Button = findViewById(com.example.calculator2.R.id.button_equals)

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


        // if phone is rotated into landscape mode...
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(com.example.calculator2.R.layout.landscape)

            val displayNumber: TextView =
                findViewById(com.example.calculator2.R.id.calculatorText) //calculator display text variable
            displayNumber.text = starting_input //default number shown on calculator (0)

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


                //update values
                secondNumber = starting_input
                firstNumber = result
                displayNumber.text = result
                operatorPressed = false
            }

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
                        displayNumber.text = firstNumber

                        equalsPressed = false
                    } else {
                        if (operatorPressed) {

                            if (secondNumber == starting_input && stringValue != "0") {

                                displayNumber.text = stringValue
                                secondNumber = stringValue
                            } else { // the numbers are in, so add them
                                val currentText =
                                    displayNumber.text.toString() //current text displayed
                                displayNumber.text = "$currentText$stringValue"
                                secondNumber += stringValue

                            }
                        } else { // if operator is not pressed, add to first number

                            if (firstNumber == starting_input && stringValue != "0") {

                                displayNumber.text = stringValue
                                firstNumber = stringValue
                            } else { //already numbers in, just add them

                                val currentText =
                                    displayNumber.text.toString() //current text displayed
                                displayNumber.text = "$currentText$stringValue"
                                firstNumber += stringValue
                            }
                        }
                    }
                }
            }

            //clear button
            val clearButton: Button = findViewById(com.example.calculator2.R.id.button_clear)
            clearButton.setOnClickListener {

                //make everything default

                firstNumber = starting_input
                secondNumber = starting_input
                equalsNumber = starting_input
                operator = ""
                operatorPressed = false
                equalsPressed = false
                displayNumber.text = starting_input
                resetOperators()
            }

            //decimal button
            val decimalButton: Button = findViewById(com.example.calculator2.R.id.button_dot)
            decimalButton.setOnClickListener {

                equalsNumber = starting_input

                /* In case this is already defined */
                if (equalsPressed) {
                    secondNumber = starting_input
                    operator = ""
                    operatorPressed = false

                    firstNumber = "."
                    displayNumber.text = firstNumber

                    equalsPressed = false
                }

                if (operatorPressed) {
                    if ('.' !in secondNumber) { // makes sure number doesn't already have a decimal
                        val currentText = displayNumber.text.toString() //current text displayed

                        if (secondNumber == starting_input) {
                            displayNumber.text = "."
                        } else {
                            displayNumber.text = "$currentText."
                        }

                        secondNumber += "."
                    }
                } else { //1st number
                    if ('.' !in firstNumber) { // don't want multiple decimals
                        firstNumber += "."

                        val currentText = displayNumber.text.toString() //current text displayed
                        displayNumber.text = "$currentText."
                    }
                }
            }


            // +/- button
            val signButton: Button = findViewById(com.example.calculator2.R.id.button_sign)
            signButton.setOnClickListener {

                if (operatorPressed) {

                    if ('-' !in secondNumber) { // Enters a "-" in front of the number

                        secondNumber = "-$secondNumber"
                        displayNumber.text = secondNumber
                    } else {
                        secondNumber = secondNumber.replace(
                            "-",
                            ""
                        ) // replaces minus with nothing to display the sign change
                        displayNumber.text = secondNumber
                    }
                } else { //1st num

                    if ('-' !in firstNumber) {

                        firstNumber = "-$firstNumber"
                        displayNumber.text = firstNumber
                    } else {
                        firstNumber = firstNumber.replace("-", "")
                        displayNumber.text = firstNumber
                    }
                }
            }

            // % button
            val percentButton: Button = findViewById(com.example.calculator2.R.id.button_percentage)
            percentButton.setOnClickListener {

                if (operatorPressed) {

                    secondNumber = (secondNumber.toFloat() / 100).toString()
                    displayNumber.text = secondNumber

                } else { //1st number
                    firstNumber = (firstNumber.toFloat() / 100).toString()
                    displayNumber.text = firstNumber
                }
            }

            // '/' - division button
            val divisionButton: Button = findViewById(com.example.calculator2.R.id.button_division)

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
            val multiplyButton: Button = findViewById(com.example.calculator2.R.id.button_multiply)
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
            val subtractButton: Button = findViewById(com.example.calculator2.R.id.button_subtraction)
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
            val addButton: Button = findViewById(com.example.calculator2.R.id.button_addition)
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
            val equalsButton: Button = findViewById(com.example.calculator2.R.id.button_equals)

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



            //sin button
            val sinButton: Button = findViewById(com.example.calculator2.R.id.button_sin)
            sinButton.setOnClickListener {
                Log.d("press","sin button pressed.")

                if (operatorPressed && secondNumber != starting_input) {
                    secondNumber = (sin(secondNumber.toFloat()).toString())

                    if (secondNumber.endsWith(".0")) {
                        secondNumber = secondNumber.removeSuffix(".0")
                    }

                    displayNumber.text = secondNumber
                } else { //1st number
                    firstNumber = (sin(firstNumber.toFloat()).toString())

                    if (firstNumber.endsWith(".0")) { //convert to int
                        firstNumber = firstNumber.removeSuffix(".0")
                    }
                    displayNumber.text = firstNumber
                }
            }

            //cos button
            val cosButton: Button = findViewById(com.example.calculator2.R.id.button_cos)
            cosButton.setOnClickListener {
                Log.d("press","cos button pressed.")

                if (operatorPressed && secondNumber != starting_input) {
                    secondNumber = (cos(secondNumber.toFloat()).toString())

                    if (secondNumber.endsWith(".0")) { //convert to int
                        secondNumber = secondNumber.removeSuffix(".0")
                    }
                    displayNumber.text = secondNumber

                } else { //1st number
                    firstNumber = (cos(firstNumber.toFloat()).toString())

                    if (firstNumber.endsWith(".0")) { //convert to int
                        firstNumber = firstNumber.removeSuffix(".0")
                    }
                    displayNumber.text = firstNumber
                }
            }

            //tan button
            val tanButton: Button = findViewById(com.example.calculator2.R.id.button_tan)
            tanButton.setOnClickListener {
                Log.d("press","tan button pressed.")

                if (operatorPressed && secondNumber != starting_input) {
                    secondNumber = (tan(secondNumber.toFloat()).toString())

                    if (secondNumber.endsWith(".0")) { //convert to int
                        secondNumber = secondNumber.removeSuffix(".0")
                    }

                    displayNumber.text = secondNumber

                } else { //1st number
                    firstNumber = (tan(firstNumber.toFloat()).toString())

                    if (firstNumber.endsWith(".0")) { //convert to int
                        firstNumber = firstNumber.removeSuffix(".0")
                    }
                    displayNumber.text = firstNumber

                }
            }

            //Log 10 button
            val log10Button: Button = findViewById(com.example.calculator2.R.id.button_log)
            log10Button.setOnClickListener {
                Log.d("press","Log 10 button pressed.")

                if (operatorPressed && secondNumber != starting_input) {
                    secondNumber = (log10(secondNumber.toFloat()).toString())

                    if (secondNumber.endsWith(".0")) { //convert to int
                        secondNumber = secondNumber.removeSuffix(".0")
                    }

                    displayNumber.text = secondNumber

                } else { //1st number
                    firstNumber = (log10(firstNumber.toFloat()).toString())

                    if (firstNumber.endsWith(".0")) { //convert to int
                        firstNumber = firstNumber.removeSuffix(".0")
                    }
                    displayNumber.text = firstNumber
                }
            }

            //ln button
            val lnButton: Button = findViewById(com.example.calculator2.R.id.button_ln)
            lnButton.setOnClickListener {
                Log.d("button","ln button pressed.")

                if (operatorPressed && secondNumber != starting_input) {
                    secondNumber = ln(secondNumber.toFloat()).toString()

                    if (secondNumber.endsWith(".0")) { //convert to int
                        secondNumber = secondNumber.removeSuffix(".0")
                    }
                    displayNumber.text = secondNumber

                } else { //1st number
                    firstNumber = ln(firstNumber.toFloat()).toString()

                    if (firstNumber.endsWith(".0")) { //convert to int
                        firstNumber = firstNumber.removeSuffix(".0")

                    }
                    displayNumber.text = firstNumber
                }
            }
        }
    }
}
