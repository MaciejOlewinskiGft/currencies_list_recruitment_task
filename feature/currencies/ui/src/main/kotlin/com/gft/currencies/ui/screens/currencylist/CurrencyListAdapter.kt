package com.gft.currencies.ui.screens.currencylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gft.currencies.ui.common.models.CurrencyInfo
import com.gft.currencies.ui.databinding.CurrencyListItemBinding
import com.gft.currencies.ui.screens.currencylist.CurrencyListAdapter.CurrencyListItemViewHolder

internal class CurrencyListAdapter : ListAdapter<CurrencyInfo, CurrencyListItemViewHolder>(CurrencyListItemDiffCallback()) {

    private class CurrencyListItemDiffCallback : DiffUtil.ItemCallback<CurrencyInfo>() {
        override fun areContentsTheSame(oldItem: CurrencyInfo, newItem: CurrencyInfo) = oldItem == newItem
        override fun areItemsTheSame(oldItem: CurrencyInfo, newItem: CurrencyInfo) = oldItem.id == newItem.id
    }

    internal class CurrencyListItemViewHolder(
        private val currencyListItemBinding: CurrencyListItemBinding,
    ) : RecyclerView.ViewHolder(currencyListItemBinding.root) {

        fun bind(currencyInfo: CurrencyInfo) {
            with(currencyListItemBinding) {
                currencyInitial.text = currencyInfo.name.first().toString()
                currencyName.text = currencyInfo.name
                currencyCode.text = currencyInfo.code
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyListItemViewHolder {
        val currencyListItemBinding = CurrencyListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return CurrencyListItemViewHolder(currencyListItemBinding)
    }

    override fun onBindViewHolder(holder: CurrencyListItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
