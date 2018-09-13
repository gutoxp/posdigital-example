package com.getnet.posdigital.service.integration.main

import android.view.View
import br.com.getnet.posdigital.base.ItemAdapter
import br.com.getnet.posdigital.base.BaseViewHolder
import br.com.getnet.posdigital.data.ItemData
import br.com.getnet.posdigital.extension.loadDrawable
import kotlinx.android.synthetic.main.main_item_view.view.*

class MainViewHolder(itemView: View) : BaseViewHolder(itemView) {

    override fun onBind(itemData: ItemData, listener: ItemAdapter.AdapterMainListener) {
        itemView.adapter_icon.setImageDrawable(itemView.context.loadDrawable(itemData.icon))
        itemView.item_adapter_label.text = itemData.label
        itemView.setOnClickListener {
            listener.onClickItem(itemData)
        }
    }

}