package com.abdulaziz.provalyutakurslari.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdulaziz.provalyutakurslari.R
import com.abdulaziz.provalyutakurslari.databinding.ItemCurrencyBinding
import com.abdulaziz.provalyutakurslari.models.Currency
import com.squareup.picasso.Picasso

class CurrencyAdapter(val list: List<Currency>, val listener: OnItemClickListener):RecyclerView.Adapter<CurrencyAdapter.CurrencyVH>() {


    inner class CurrencyVH(private val itemBinding: ItemCurrencyBinding):RecyclerView.ViewHolder(itemBinding.root){

        fun onBind(currency: Currency){
            Picasso.get().load("https://nbu.uz/local/templates/nbu/images/flags/${currency.code}.png").into(itemBinding.flagIv)
            itemBinding.currency = currency
            itemBinding.calculatorIv.setOnClickListener {
                listener.onItemClicked(currency.code)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyVH {
        return CurrencyVH(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_currency, parent, false))
    }

    override fun onBindViewHolder(holder: CurrencyVH, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onItemClicked(code:String)
    }
}