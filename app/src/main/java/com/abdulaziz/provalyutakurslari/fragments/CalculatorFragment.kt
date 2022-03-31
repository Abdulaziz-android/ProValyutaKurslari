package com.abdulaziz.provalyutakurslari.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.abdulaziz.provalyutakurslari.R
import com.abdulaziz.provalyutakurslari.adapters.SpinnerAdapter
import com.abdulaziz.provalyutakurslari.databinding.FragmentCalculatorBinding
import com.abdulaziz.provalyutakurslari.db.CurrencyDatabase
import com.abdulaziz.provalyutakurslari.models.Currency
import com.abdulaziz.provalyutakurslari.utils.CurrencyItem
import java.text.DecimalFormat
import kotlin.math.pow

class CalculatorFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentCalculatorBinding
    private lateinit var database: CurrencyDatabase
    private var currencyList: List<Currency>? = null
    private var number = 0.0
    private var itemList: ArrayList<CurrencyItem>? = null
    private lateinit var sPref: SharedPreferences
    private var sPrefPosition = -1
    private var sPrefPosition2 = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_calculator, container, false)

        sPref = binding.root.context.getSharedPreferences("shared", Context.MODE_PRIVATE)
        currencyList = arrayListOf()
        database = CurrencyDatabase.getInstance(requireContext())
        if (database.currencyDao().getInformationWithCurrencyLast() != null) {
            currencyList = database.currencyDao().getInformationWithCurrencyLast()!!.list
        }

        sutUpCustomSpinner()
        setUpCalculator()


        return binding.root
    }

    private fun setUpCalculator() {
        setUpListener()
    }

    private fun sutUpCustomSpinner() {
        itemList = arrayListOf()
        currencyList!!.forEachIndexed { index, it ->
            if (it.nbu_buy_price.isNotEmpty() && it.nbu_cell_price.isNotEmpty()) {
                itemList!!.add(CurrencyItem(it.code, it.nbu_buy_price, it.nbu_cell_price))
            } else {
                itemList!!.add(CurrencyItem(it.code, it.cb_price, it.cb_price))
            }
            if (sPref.getString("code", "null") == it.code) {
                sPrefPosition = index
            }
            if (sPref.getString("code2", "null") == it.code) {
                sPrefPosition2 = index
            }
        }
        itemList!!.add(CurrencyItem("UZS", "1", "1"))

        val adapter = SpinnerAdapter(requireContext(), itemList!!)
        binding.spinnerFirst.adapter = adapter
        binding.spinnerSecond.adapter = adapter
        if (sPrefPosition != -1) {
            binding.spinnerFirst.setSelection(sPrefPosition)
        }
        if (sPrefPosition2 != -1) {
            binding.spinnerSecond.setSelection(sPrefPosition2)
        } else binding.spinnerSecond.setSelection(itemList!!.size - 1)
        binding.spinnerFirst.onItemSelectedListener = this
        binding.spinnerSecond.onItemSelectedListener = this

        binding.exchangeIv.setOnClickListener {
            val firstPos = binding.spinnerFirst.selectedItemPosition
            val secondPos = binding.spinnerSecond.selectedItemPosition
            binding.spinnerFirst.setSelection(secondPos)
            binding.spinnerSecond.setSelection(firstPos)
            calculate()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun calculate() {
        val firstItem = itemList?.get(binding.spinnerFirst.selectedItemPosition)!!
        val secondItem = itemList?.get(binding.spinnerSecond.selectedItemPosition)!!
        val buySum: String =
            ((firstItem.nbu_buy_price?.toDouble()!! * number) / secondItem.nbu_buy_price!!.toDouble()).toString()
        val cellSum: String =
            ((firstItem.nbu_cell_price?.toDouble()!! * number) / secondItem.nbu_cell_price!!.toDouble()).toString()

        if (buySum.toDouble() < 1.0 && buySum.substring(0, 1) != "0") {
            binding.valueBuyTv.text = "${buySum.substring(0, 7)}e-5"
        } else binding.valueBuyTv.text = cellSum
        if (cellSum.toDouble() < 1.0 && cellSum.substring(0, 1) != "0") {
            binding.valueSellTv.text = "${cellSum.substring(0, 7)}e-5"
        } else binding.valueSellTv.text = cellSum
    }


    private fun setUpListener() {
        binding.apply {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    if (binding.editText.text.isEmpty()) {
                        binding.editText.hint = "0.0"
                        number = 0.0
                    } else number = binding.editText.text.toString().toDouble()
                    calculate()

                }

            })

        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        calculate()

        sPref.edit().putString("code",
            itemList?.get(binding.spinnerFirst.selectedItemPosition)?.currencyCode).apply()
        sPref.edit().putString("code2",
            itemList?.get(binding.spinnerSecond.selectedItemPosition)?.currencyCode).apply()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }


}