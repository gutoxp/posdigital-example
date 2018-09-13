package br.com.getnet.posdigital.base

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import br.com.getnet.posdigital.data.ItemData
import br.com.getnet.posdigital.extension.loadDrawable
import kotlinx.android.synthetic.main.item_view.view.*

open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    open fun onBind(itemData: ItemData, listener: ItemAdapter.AdapterMainListener) {
        toIcon(itemData)
        toButtons(itemData, listener)
        itemView.item_title.text = itemData.label
        itemView.item_text.text = itemData.message
        if (TextUtils.isEmpty(itemData.code)) {
            itemView.item_scroll.visibility = View.GONE
            itemView.item_code.visibility = View.GONE
        } else {
            itemView.item_code.text = itemData.code
            itemView.item_scroll.visibility = View.VISIBLE
            itemView.item_code.visibility = View.VISIBLE
        }
    }

    private fun toButtons(itemData: ItemData, listener: ItemAdapter.AdapterMainListener) {
        if (TextUtils.isEmpty(itemData.code)) {
            itemView.item_test.visibility = View.GONE
        } else {
            itemView.item_test.visibility = View.VISIBLE
            itemView.item_test.setOnClickListener {
                listener.onClickItem(itemData)
            }
        }
    }

    private fun toIcon(itemData: ItemData) {
        if (TextUtils.isEmpty(itemData.icon)) {
            itemView.item_icon.visibility = View.GONE
        } else {
            itemView.item_icon.visibility = View.VISIBLE
            itemView.item_icon.setImageDrawable(itemView.context.loadDrawable(itemData.icon))
        }
    }

}