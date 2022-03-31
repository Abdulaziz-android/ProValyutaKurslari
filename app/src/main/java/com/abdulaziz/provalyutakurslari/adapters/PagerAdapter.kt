package com.abdulaziz.provalyutakurslari.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.abdulaziz.provalyutakurslari.fragments.PagerFragment
import com.abdulaziz.provalyutakurslari.models.Currency

class PagerAdapter(fa: FragmentActivity, val list: List<Currency>) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {
        return PagerFragment.newInstance(list[position])
    }
}