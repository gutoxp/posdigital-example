package br.com.getnet.posdigital.data

import android.content.Context
import br.com.getnet.posdigital.R
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader


class DataGenerator {

    fun loadItems(context: Context): List<ItemData> {
        return load(context, R.raw.main_items)
    }

    fun loadPrint(context: Context): List<ItemData> {
        return load(context, R.raw.printer_items)
    }

    fun loadCard(context: Context): List<ItemData> {
        return load(context, R.raw.card_items)
    }

    fun loadMifare(context: Context): List<ItemData> {
        return load(context, R.raw.mifare_items)
    }

    fun loadLed(context: Context): List<ItemData> {
        return load(context, R.raw.led_items)
    }

    fun loadCamera(context: Context): List<ItemData> {
        return load(context, R.raw.camera_items)
    }

    fun loadBeeper(context: Context): List<ItemData> {
        return load(context, R.raw.beeper_items)
    }

    private fun load(context: Context, resource: Int): List<ItemData> {
        val input = context.resources.openRawResource(resource)
        val reader = BufferedReader(InputStreamReader(input, "UTF-8"))
        val type = object : TypeToken<List<ItemData>>() {}.type
        return GsonBuilder().create().fromJson(reader, type)
    }

}