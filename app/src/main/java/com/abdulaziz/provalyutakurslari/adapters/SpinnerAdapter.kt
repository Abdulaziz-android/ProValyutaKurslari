package com.abdulaziz.provalyutakurslari.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.abdulaziz.provalyutakurslari.R
import com.abdulaziz.provalyutakurslari.databinding.CurrencySpinnerRowBinding
import com.abdulaziz.provalyutakurslari.utils.CurrencyItem
import com.squareup.picasso.Picasso

class SpinnerAdapter(context: Context, currencyItemList: List<CurrencyItem>) :
    ArrayAdapter<CurrencyItem>(context, 0, currencyItemList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)

    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)

    }

    private fun initView(position: Int, parent: ViewGroup): View {
        val currency = getItem(position)

        val view = DataBindingUtil.inflate<CurrencySpinnerRowBinding>(LayoutInflater.from(context),
            R.layout.currency_spinner_row,
            parent,
            false)
        if (currency?.currencyCode.equals("UZS")) {
            Picasso.get()
                .load(R.drawable.image_uzb)
                .into(view.imageView)
        } else Picasso.get()
            .load("https://nbu.uz/local/templates/nbu/images/flags/${currency!!.currencyCode}.png")
            .into(view.imageView)
        view.textView.text = currency?.currencyCode.toString()
        return view.root
    }
}