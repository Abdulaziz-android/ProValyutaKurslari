package com.abdulaziz.provalyutakurslari.fragments

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.abdulaziz.provalyutakurslari.viewmodel.CurrencyViewModel
import com.abdulaziz.provalyutakurslari.MainActivity
import com.abdulaziz.provalyutakurslari.R
import com.abdulaziz.provalyutakurslari.adapters.CurrencyAdapter
import com.abdulaziz.provalyutakurslari.databinding.FragmentCurrencyBinding
import com.abdulaziz.provalyutakurslari.db.CurrencyDatabase
import com.abdulaziz.provalyutakurslari.models.Currency

class CurrencyFragment : Fragment() {

    private lateinit var binding: FragmentCurrencyBinding
    private lateinit var viewModel: CurrencyViewModel
    private var currencyList: List<Currency>? = null
    private lateinit var currencyDatabase: CurrencyDatabase
    private lateinit var currencyAdapter: CurrencyAdapter
    private lateinit var sPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_currency, container, false)

        sPref = binding.root.context.getSharedPreferences("shared", MODE_PRIVATE)
        currencyDatabase = CurrencyDatabase.getInstance(requireContext())

        if (!currencyDatabase.currencyDao().getInformationWithCurrency().isNullOrEmpty()) {
            currencyList = currencyDatabase.currencyDao().getInformationWithCurrencyLast()!!.list
            loadData()
        }

        viewModel = ViewModelProvider(requireActivity()).get(CurrencyViewModel::class.java)
        viewModel.getCurrency().observe(requireActivity()) { currencyList ->
            this.currencyList = currencyList
            loadData()
        }

        return binding.root
    }


    override fun onStop() {
        super.onStop()
        viewModel.getCurrency().removeObservers(requireActivity())
    }

    private fun loadData() {
        currencyAdapter =
            CurrencyAdapter(currencyList!!, object : CurrencyAdapter.OnItemClickListener {
                override fun onItemClicked(code: String) {
                    sPref.edit().putString("code", code).apply()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_container, CalculatorFragment()).commit()
                    (activity as MainActivity).getCalculator()
                }
            })
        binding.rv.adapter = currencyAdapter
    }

}