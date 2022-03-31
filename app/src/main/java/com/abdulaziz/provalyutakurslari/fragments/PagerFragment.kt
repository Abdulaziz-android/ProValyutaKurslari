package com.abdulaziz.provalyutakurslari.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.abdulaziz.provalyutakurslari.R
import com.abdulaziz.provalyutakurslari.databinding.FragmentPagerBinding
import com.abdulaziz.provalyutakurslari.models.Currency

private const val ARG_PARAM1 = "param1"

class PagerFragment : Fragment() {

    private var param1: Currency? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as Currency?
        }
    }

    private lateinit var binding: FragmentPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_pager, container, false)

        binding.currency = param1



        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: Currency) =
            PagerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                }
            }
    }
}