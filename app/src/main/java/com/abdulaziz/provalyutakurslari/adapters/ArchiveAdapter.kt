package com.abdulaziz.provalyutakurslari.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdulaziz.provalyutakurslari.R
import com.abdulaziz.provalyutakurslari.databinding.ItemArchiveBinding
import com.abdulaziz.provalyutakurslari.models.Currency

class ArchiveAdapter(val list: List<Currency>):RecyclerView.Adapter<ArchiveAdapter.ArchiveVH>() {

    inner class ArchiveVH(private val itemBinding: ItemArchiveBinding):RecyclerView.ViewHolder(itemBinding.root){

        fun onBind(currency: Currency){
            itemBinding.currency = currency
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchiveVH {
        return ArchiveVH(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_archive, parent, false))
    }

    override fun onBindViewHolder(holder: ArchiveVH, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}