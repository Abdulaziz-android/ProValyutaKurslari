package com.abdulaziz.provalyutakurslari.fragments

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.abdulaziz.provalyutakurslari.MainActivity
import com.abdulaziz.provalyutakurslari.R
import com.abdulaziz.provalyutakurslari.adapters.CurrencyAdapter
import com.abdulaziz.provalyutakurslari.databinding.FragmentSearchBinding
import com.abdulaziz.provalyutakurslari.db.CurrencyDatabase
import com.abdulaziz.provalyutakurslari.models.Currency


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var database: CurrencyDatabase
    private var currencyList: List<Currency>? = null
    private lateinit var currencyAdapter: CurrencyAdapter
    private lateinit var sPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_search, container, false)

        currencyList = arrayListOf()
        sPref = binding.root.context.getSharedPreferences("shared", MODE_PRIVATE)
        database = CurrencyDatabase.getInstance(requireContext())
        if (database.currencyDao().getInformationWithCurrencyLast() != null) {
            currencyList = database.currencyDao().getInformationWithCurrencyLast()!!.list
        }

        loadData(currencyList!!)

        binding.searchEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    val list: ArrayList<Currency> = arrayListOf()
                    currencyList!!.forEach {
                        if (it.code.lowercase().contains(p0.toString().lowercase())){
                            list.add(it)
                        }
                    }
                    loadData(list)
                }else loadData(currencyList!!)
            }

        })

        binding.closeIv.setOnClickListener {
            if (binding.searchEt.text.toString().isNotEmpty()){
                binding.searchEt.setText("")
            }else {
                hideKeyboard()
                (activity as MainActivity).onBackPressed()
            }
        }


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).showToolbar()
    }

    private fun loadData(list: List<Currency>) {
        currencyAdapter =
            CurrencyAdapter(list, object : CurrencyAdapter.OnItemClickListener {
                override fun onItemClicked(code: String) {
                    sPref.edit().putString("code", code).apply()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_container, CalculatorFragment()).commit()
                    (activity as MainActivity).getCalculator()
                    (activity as MainActivity).showToolbar()
                    hideKeyboard()
                }
            })
        binding.rv.adapter = currencyAdapter
    }
    fun Fragment.hideKeyboard() {
        view?.let { (activity as MainActivity).hideKeyboard(it) }
    }
}