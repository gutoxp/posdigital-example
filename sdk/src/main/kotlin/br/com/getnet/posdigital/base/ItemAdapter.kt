package br.com.getnet.posdigital.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import br.com.getnet.posdigital.R
import br.com.getnet.posdigital.data.ItemData


open class ItemAdapter(private val context: Context, private val items: List<ItemData>, private val adapterMainListener: AdapterMainListener) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false)
        return BaseViewHolder(view)
    }

    private fun getItem(position: Int): ItemData {
        return items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val itemModel = getItem(position)
        holder.onBind(itemModel, adapterMainListener)
    }

    interface AdapterMainListener {

        fun onClickItem(itemData: ItemData)

    }

}