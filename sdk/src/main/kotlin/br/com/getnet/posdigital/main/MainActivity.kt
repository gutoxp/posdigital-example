package br.com.getnet.posdigital.main

import br.com.getnet.posdigital.base.BaseActivity
import br.com.getnet.posdigital.base.ItemAdapter
import br.com.getnet.posdigital.beeper.BeeperActivity
import br.com.getnet.posdigital.camera.CameraActivity
import br.com.getnet.posdigital.card.CardActivity
import br.com.getnet.posdigital.data.DataGenerator
import br.com.getnet.posdigital.data.ItemData
import br.com.getnet.posdigital.led.LedActivity
import com.getnet.posdigital.service.integration.main.MainType
import br.com.getnet.posdigital.mifare.MifareActivity
import br.com.getnet.posdigital.printer.PrinterActivity

class MainActivity : BaseActivity(), ItemAdapter.AdapterMainListener {

    override fun onClickItem(itemData: ItemData) {
        when (MainType.valueOf(itemData.id)) {
            MainType.CARD -> startActivity(CardActivity::class.java)
            MainType.MIFARE -> startActivity(MifareActivity::class.java)
            MainType.PRINTER -> startActivity(PrinterActivity::class.java)
            MainType.BEEPER -> startActivity(BeeperActivity::class.java)
            MainType.LED -> startActivity(LedActivity::class.java)
            MainType.CAMERA -> startActivity(CameraActivity::class.java)
        }
    }

    override fun getAdapter(): MainAdapter {
        val items = DataGenerator().loadItems(baseContext)
        return MainAdapter(baseContext, items, this)
    }

}
