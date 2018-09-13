package br.com.getnet.posdigital.led

import br.com.getnet.posdigital.base.BaseActivity
import br.com.getnet.posdigital.base.ItemAdapter
import br.com.getnet.posdigital.data.DataGenerator
import br.com.getnet.posdigital.data.ItemData
import com.getnet.posdigital.PosDigital

class LedActivity : BaseActivity(), ItemAdapter.AdapterMainListener {

    override fun getAdapter(): ItemAdapter {
        val items = DataGenerator().loadLed(baseContext)
        return ItemAdapter(baseContext, items, this)
    }

    override fun onClickItem(itemData: ItemData) {
        when (LedMethod.valueOf(itemData.id)) {
            LedMethod.OFF_ALL -> offAll()
            LedMethod.ON_ALL -> onAll()
            LedMethod.ON_RED -> onRed()
            LedMethod.OFF_RED -> offRed()
            LedMethod.ON_BLUE -> onBlue()
            LedMethod.OFF_BLUE -> offBlue()
            LedMethod.ON_YELLOW -> onYellow()
            LedMethod.OFF_YELLOW -> offYellow()
            LedMethod.ON_GREEN -> onGreen()
            LedMethod.OFF_GREEN -> offGreen()
        }
    }

    private fun offAll() {
        try {
            PosDigital.getInstance().getLed().turnOffAll()
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun onAll() {
        try {
            PosDigital.getInstance().getLed().turnOnAll()
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun onRed() {
        try {
            PosDigital.getInstance().getLed().turnOnRed()
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun offRed() {
        try {
            PosDigital.getInstance().getLed().turnOffRed()
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun onBlue() {
        try {
            PosDigital.getInstance().getLed().turnOnBlue()
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun offBlue() {
        try {
            PosDigital.getInstance().getLed().turnOffBlue()
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun onYellow() {
        try {
            PosDigital.getInstance().getLed().turnOnYellow()
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun offYellow() {
        try {
            PosDigital.getInstance().getLed().turnOffYellow()
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun onGreen() {
        try {
            PosDigital.getInstance().getLed().turnOnGreen()
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun offGreen() {
        try {
            PosDigital.getInstance().getLed().turnOffGreen()
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

}