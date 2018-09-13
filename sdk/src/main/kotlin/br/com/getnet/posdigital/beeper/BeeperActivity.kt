package br.com.getnet.posdigital.beeper

import br.com.getnet.posdigital.base.BaseActivity
import br.com.getnet.posdigital.base.ItemAdapter
import com.getnet.posdigital.PosDigital
import br.com.getnet.posdigital.data.DataGenerator
import br.com.getnet.posdigital.data.ItemData

class BeeperActivity : BaseActivity(), ItemAdapter.AdapterMainListener {

    override fun getAdapter(): ItemAdapter {
        val items = DataGenerator().loadBeeper(baseContext)
        return ItemAdapter(baseContext, items, this)
    }

    override fun onClickItem(itemData: ItemData) {
        when (BeeperMethod.valueOf(itemData.id)) {
            BeeperMethod.SUCCESS -> success()
            BeeperMethod.ERROR -> error()
            BeeperMethod.DIGIT -> digit()
            BeeperMethod.NFC -> nfc()
            BeeperMethod.CUSTOM -> custom()
        }
    }

    private fun success() {
        try {
            PosDigital.getInstance().getBeeper().success()
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun error() {
        try {
            PosDigital.getInstance().getBeeper().error()
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun digit() {
        try {
            PosDigital.getInstance().getBeeper().digit()
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun nfc() {
        try {
            PosDigital.getInstance().getBeeper().nfc()
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun custom() {
        try {
            PosDigital.getInstance().getBeeper().custom(5000)
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

}