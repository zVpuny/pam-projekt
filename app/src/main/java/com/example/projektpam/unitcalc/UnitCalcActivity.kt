package com.example.projektpam.unitcalc

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doOnTextChanged
import com.example.projektpam.R
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

class UnitCalcActivity : AppCompatActivity() {
    lateinit var volumeUnitsAdapter: ArrayAdapter<CharSequence>
    lateinit var weightUnitsAdapter: ArrayAdapter<CharSequence>
    lateinit var input: EditText
    lateinit var resultTextView: TextView
    lateinit var startUnitSpinner: Spinner
    lateinit var endUnitSpinner: Spinner
    var chosenVolume = true
    val decimalFormat = DecimalFormat("0.###")
    private val weightUnitsInGrams =
        arrayOf(
            BigDecimal("1"),
            BigDecimal("10"),
            BigDecimal("1000"),
            BigDecimal("28.3495"),
            BigDecimal("253.592")
        )
    private val volumeUnitsInGrams =
        arrayOf(
            BigDecimal("1000"),
            BigDecimal("1"),
            BigDecimal("3785.4117"),
            BigDecimal("29.5735"),
            BigDecimal("15"),
            BigDecimal("5"),
            BigDecimal("250")
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unit_calc)
        this.supportActionBar?.title = getString(R.string.unit_calc_title)
        this.supportActionBar?.setBackgroundDrawable(AppCompatResources.getDrawable(this,R.color.primaryLightColor))
        this.supportActionBar?.elevation = 0f

        resultTextView = findViewById(R.id.resultTextView)
        input = findViewById(R.id.inputNumber)


        volumeUnitsAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.volume_units_array,
            android.R.layout.simple_spinner_item
        )

        weightUnitsAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.weight_units_array,
            android.R.layout.simple_spinner_item
        )

        volumeUnitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        weightUnitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val categorySpinner: Spinner = findViewById(R.id.categorySpinner)

        startUnitSpinner = findViewById(R.id.startUnitSpinner)
        endUnitSpinner = findViewById(R.id.endUnitSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.unit_categories,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = it
        }

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    startUnitSpinner.adapter = volumeUnitsAdapter
                    endUnitSpinner.adapter = volumeUnitsAdapter
                    chosenVolume = true
                } else {
                    startUnitSpinner.adapter = weightUnitsAdapter
                    endUnitSpinner.adapter = weightUnitsAdapter
                    chosenVolume = false
                }
            }
        }

        startUnitSpinner.onItemSelectedListener = onUnitItemSelectedListener
        endUnitSpinner.onItemSelectedListener = onUnitItemSelectedListener
        input.doOnTextChanged { _, _, _, _ ->
            run {
                resultTextView.text = decimalFormat.format(
                    calculateUnits(
                        startUnitSpinner.selectedItemPosition,
                        endUnitSpinner.selectedItemPosition
                    )
                )
            }
        }


    }

    val onUnitItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            resultTextView.text = decimalFormat.format(
                calculateUnits(
                    startUnitSpinner.selectedItemPosition,
                    endUnitSpinner.selectedItemPosition
                )
            )
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            resultTextView.text = ""
        }
    }

    fun calculateUnits(startPos: Int, endPos: Int): BigDecimal {
        if (input.text.isNotEmpty()) {
            val result: BigDecimal
            if (chosenVolume) {
/*
                result = volumeUnitsInGrams[startPos] *
                        input.text.toString().toBigDecimal() /
                        volumeUnitsInGrams[endPos]
*/
                result = volumeUnitsInGrams[startPos].multiply(input.text.toString().toBigDecimal()).divide(volumeUnitsInGrams[endPos],3,RoundingMode.HALF_UP)

            } else {
                result = weightUnitsInGrams[startPos].multiply(
                        input.text.toString().toBigDecimal()).divide(
                        weightUnitsInGrams[endPos],3,RoundingMode.HALF_UP)
            }
            return result
        } else {
            return BigDecimal(0)
        }
    }


}