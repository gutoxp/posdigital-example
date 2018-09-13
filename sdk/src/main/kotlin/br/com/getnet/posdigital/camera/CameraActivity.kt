package br.com.getnet.posdigital.camera

import br.com.getnet.posdigital.base.BaseActivity
import br.com.getnet.posdigital.base.ItemAdapter
import com.getnet.posdigital.PosDigital
import com.getnet.posdigital.camera.ICameraCallback
import br.com.getnet.posdigital.data.DataGenerator
import br.com.getnet.posdigital.data.ItemData

class CameraActivity : BaseActivity(), ItemAdapter.AdapterMainListener {

    override fun getAdapter(): ItemAdapter {
        val items = DataGenerator().loadCamera(baseContext)
        return ItemAdapter(baseContext, items, this)
    }

    override fun onClickItem(itemData: ItemData) {
        when (CameraMethod.valueOf(itemData.id)) {
            CameraMethod.START_BACK -> startBack()
            CameraMethod.START_FRONT -> startFront()
        }
    }

    private fun startBack() {
        try {
            val timeout = 30
            PosDigital.getInstance().getCamera().readBack(timeout, callback())
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun startFront() {
        try {
            val timeout = 30
            PosDigital.getInstance().getCamera().readFront(timeout, callback())
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun callback(): ICameraCallback.Stub {
        return object : ICameraCallback.Stub() {
            override fun onSuccess(code: String?) {
                openInfoDialog("Code : $code")
            }

            override fun onTimeout() {
                openInfoDialog("Timeout")
            }

            override fun onCancel() {
                openInfoDialog("Cancel")
            }

            override fun onError(error: String) {
                openInfoDialog("Error : $error")
            }
        }
    }

}