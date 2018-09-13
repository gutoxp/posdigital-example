package br.com.getnet.posdigital.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import br.com.getnet.posdigital.R
import br.com.getnet.posdigital.base.ItemAdapter
import br.com.getnet.posdigital.data.ItemData
import com.getnet.posdigital.service.integration.main.MainViewHolder

class MainAdapter(private val context: Context, items: List<ItemData>, adapterMainListener: AdapterMainListener) : ItemAdapter(context, items, adapterMainListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.main_item_view, parent, false)
        return MainViewHolder(view)
    }

}