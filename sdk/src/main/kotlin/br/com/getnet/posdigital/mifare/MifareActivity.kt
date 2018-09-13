package br.com.getnet.posdigital.mifare

import br.com.getnet.posdigital.base.BaseActivity
import br.com.getnet.posdigital.base.ItemAdapter
import br.com.getnet.posdigital.data.DataGenerator
import br.com.getnet.posdigital.data.ItemData
import com.getnet.posdigital.PosDigital
import com.getnet.posdigital.mifare.IMifareActivateCallback
import com.getnet.posdigital.mifare.IMifareCallback
import com.getnet.posdigital.mifare.MifareStatus

class MifareActivity : BaseActivity(), ItemAdapter.AdapterMainListener {

    private val block = 40
    private val key = byteArrayOf(0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte())

    override fun getAdapter(): ItemAdapter {
        val items = DataGenerator().loadMifare(baseContext)
        return ItemAdapter(baseContext, items, this)
    }

    override fun onClickItem(itemData: ItemData) {
        when (MifareMethod.valueOf(itemData.id)) {
            MifareMethod.SEARCH_CARD -> searchCard()
            MifareMethod.SEARCH_CARD_AND_ACTIVATE -> searchCardAndActivate()
            MifareMethod.AUTH_SECTOR_KEY_A -> authSectorA()
            MifareMethod.AUTH_SECTOR_KEY_B -> authSectorB()
            MifareMethod.AUTH_BLOCK_KEY_A -> authBlockA()
            MifareMethod.AUTH_BLOCK_KEY_B -> authBlockB()
            MifareMethod.CLOSE -> close()
            MifareMethod.DECREMENT -> decrement()
            MifareMethod.INCREMENT -> increment()
            MifareMethod.READ -> read()
            MifareMethod.EXIST -> exist()
            MifareMethod.WRITE -> write()
            MifareMethod.RESTORE -> restore()
            MifareMethod.TRANSFER -> transfer()
            MifareMethod.ACTIVATE -> activate()
            MifareMethod.HALT -> halt()
            MifareMethod.SERIAL_NO -> serialNo()
        }
    }

    private fun halt() {
        PosDigital.getInstance().getMifare().halt()
        openInfoDialog("Comandos encerrados")
    }

    private fun activate() {
        val callback = openActionDialog("Mifare", "Aproxime o cartão ...")
        waitCard(object : MifareCallback {
            override fun onSuccess(type: Int) {
                val uid = PosDigital.getInstance().getMifare().activate(type) == MifareStatus.SUCCESS
                callback.onFinish("activate : $uid")
            }

            override fun onError(message: String) {
                callback.onFinish(message)
            }
        })
    }

    private fun serialNo() {
        val callback = openActionDialog("Mifare", "Aproxime o cartão ...")
        waitCard(object : MifareCallback {
            override fun onSuccess(type: Int) {
                val uid = PosDigital.getInstance().getMifare().getCardSerialNo(type)
                callback.onFinish("serialNo : $uid")
            }

            override fun onError(message: String) {
                callback.onFinish(message)
            }
        })
    }

    private fun searchCard() {
        val callback = openActionDialog("Mifare", "Aproxime o cartão ...")
        waitCard(object : MifareCallback {
            override fun onSuccess(type: Int) {
                val name = getName(type)
                callback.onFinish("Card Type ? $name")
            }

            override fun onError(message: String) {
                callback.onFinish(message)
            }
        })
    }

    private fun searchCardAndActivate() {
        val callback = openActionDialog("Mifare", "Aproxime o cartão ...")
        PosDigital.getInstance().getMifare().searchCardAndActivate(object : IMifareActivateCallback.Stub() {

            override fun onActivate(key: ByteArray) {
                callback.onFinish("Ativado: $key")
            }

            override fun onError(error: String) {
                callback.onFinish(error)
            }
        })
    }

    private fun getName(cardType: Int): String {
        return when (cardType) {
            MifareType.S50_CARD -> "S50"
            MifareType.S70_CARD -> "S70"
            MifareType.PRO_CARD -> "PRO"
            MifareType.S50_PRO_CARD -> "S50_PRO"
            MifareType.S70_PRO_CARD -> "S70_PRO"
            MifareType.CPU_CARD -> "CPU"
            else -> "unknow"
        }
    }

    private fun authSectorA() {
        val callback = openActionDialog("Mifare", "Aproxime o cartão ...")
        waitCard(object : MifareCallback {
            override fun onSuccess(type: Int) {
                val authenticated = PosDigital.getInstance().getMifare().authenticateSectorWithKeyA(block / 4, key) == MifareStatus.SUCCESS
                callback.onFinish("Authenticated : $authenticated")
            }

            override fun onError(message: String) {
                callback.onFinish(message)
            }
        })
    }

    private fun authSectorB() {
        val callback = openActionDialog("Mifare", "Aproxime o cartão ...")
        waitCard(object : MifareCallback {
            override fun onSuccess(type: Int) {
                val authenticated = PosDigital.getInstance().getMifare().authenticateSectorWithKeyB(block / 4, key) == MifareStatus.SUCCESS
                callback.onFinish("Authenticated : $authenticated")
            }

            override fun onError(message: String) {
                callback.onFinish(message)
            }
        })
    }

    private fun authBlockA() {
        val callback = openActionDialog("Mifare", "Aproxime o cartão ...")
        waitCard(object : MifareCallback {
            override fun onSuccess(type: Int) {
                val authenticated = PosDigital.getInstance().getMifare().authenticateBlockWithKeyA(block, key) == MifareStatus.SUCCESS
                callback.onFinish("Authenticated : $authenticated")
            }

            override fun onError(message: String) {
                callback.onFinish(message)
            }
        })
    }

    private fun authBlockB() {
        val callback = openActionDialog("Mifare", "Aproxime o cartão ...")
        waitCard(object : MifareCallback {
            override fun onSuccess(type: Int) {
                val authenticated = PosDigital.getInstance().getMifare().authenticateBlockWithKeyB(block, key) == MifareStatus.SUCCESS
                callback.onFinish("Authenticated : $authenticated")
            }

            override fun onError(message: String) {
                callback.onFinish(message)
            }
        })
    }

    private fun read() {
        val callback = openActionDialog("Mifare", "Aproxime o cartão ...")

        waitCard(object : MifareCallback {
            override fun onSuccess(type: Int) {

                val activateResponse = PosDigital.getInstance().getMifare().activate(type)

                if (activateResponse != MifareStatus.SUCCESS) {
                    callback.onFinish("Não foi possível ativar o dispositivo")
                    return
                }

                val authSectorResponse = PosDigital.getInstance().getMifare().authenticateSectorWithKeyA(block / 4, key)

                if (authSectorResponse != MifareStatus.SUCCESS) {
                    callback.onFinish("Não foi possível authenticar o setor")
                    return
                }

                val authBlockResponse = PosDigital.getInstance().getMifare().authenticateBlockWithKeyA(block, key)

                if (authBlockResponse != MifareStatus.SUCCESS) {
                    callback.onFinish("Não foi possível authenticar o bloco")
                    return
                }

                val result = PosDigital.getInstance().getMifare().readBlock(block)

                callback.onFinish("Read [$result]")

            }

            override fun onError(message: String) {
                callback.onFinish(message)
            }

        })
    }

    private fun increment() {
        val callback = openActionDialog("Mifare", "Aproxime o cartão ...")
        waitCard(object : MifareCallback {
            override fun onSuccess(type: Int) {

                val activateResponse = PosDigital.getInstance().getMifare().activate(type)

                if (activateResponse != MifareStatus.SUCCESS) {
                    callback.onFinish("Não foi possível ativar o dispositivo")
                    return
                }

                val authSectorResponse = PosDigital.getInstance().getMifare().authenticateSectorWithKeyA(block / 4, key)

                if (authSectorResponse != MifareStatus.SUCCESS) {
                    callback.onFinish("Não foi possível authenticar o setor")
                    return
                }

                val authBlockResponse = PosDigital.getInstance().getMifare().authenticateBlockWithKeyA(block, key)

                if (authBlockResponse != MifareStatus.SUCCESS) {
                    callback.onFinish("Não foi possível authenticar o bloco")
                    return
                }

                val incrementResponse = PosDigital.getInstance().getMifare().increment(block, 100) == MifareStatus.SUCCESS

                val transferResponse = PosDigital.getInstance().getMifare().transfer(block)
                if (transferResponse != MifareStatus.SUCCESS) {
                    callback.onFinish("Não foi possível realizar o transfer")
                    return
                }

                callback.onFinish("incremented [$incrementResponse]")
            }

            override fun onError(message: String) {
                callback.onFinish(message)
            }
        })
    }

    private fun decrement() {
        val callback = openActionDialog("Mifare", "Aproxime o cartão ...")
        waitCard(object : MifareCallback {
            override fun onSuccess(type: Int) {

                val activateResponse = PosDigital.getInstance().getMifare().activate(type)

                if (activateResponse != MifareStatus.SUCCESS) {
                    callback.onFinish("Não foi possível ativar o dispositivo")
                    return
                }

                val authSectorResponse = PosDigital.getInstance().getMifare().authenticateSectorWithKeyA(block / 4, key)

                if (authSectorResponse != MifareStatus.SUCCESS) {
                    callback.onFinish("Não foi possível authenticar o setor")
                    return
                }

                val authBlockResponse = PosDigital.getInstance().getMifare().authenticateBlockWithKeyA(block, key)

                if (authBlockResponse != MifareStatus.SUCCESS) {
                    callback.onFinish("Não foi possível authenticar o bloco")
                    return
                }

                val decrementResponse = PosDigital.getInstance().getMifare().decrement(block, 100) == MifareStatus.SUCCESS

                val transferResponse = PosDigital.getInstance().getMifare().transfer(block)
                if (transferResponse != MifareStatus.SUCCESS) {
                    callback.onFinish("Não foi possível realizar o transfer")
                    return
                }

                callback.onFinish("decremented [$decrementResponse]")
            }

            override fun onError(message: String) {
                callback.onFinish(message)
            }
        })
    }

    private fun exist() {
        val exist = PosDigital.getInstance().getMifare().isExist
        openInfoDialog("Exist: $exist")
    }

    private fun write() {
        val callback = openActionDialog("Mifare", "Aproxime o cartão ...")
        waitCard(object : MifareCallback {
            override fun onSuccess(type: Int) {

                val activateResponse = PosDigital.getInstance().getMifare().activate(type)

                if (activateResponse != MifareStatus.SUCCESS) {
                    callback.onFinish("Não foi possível ativar o dispositivo")
                    return
                }

                val authSectorResponse = PosDigital.getInstance().getMifare().authenticateSectorWithKeyA(block / 4, key)

                if (authSectorResponse != MifareStatus.SUCCESS) {
                    callback.onFinish("Não foi possível authenticar o setor")
                    return
                }

                val authBlockResponse = PosDigital.getInstance().getMifare().authenticateBlockWithKeyA(block, key)

                if (authBlockResponse != MifareStatus.SUCCESS) {
                    callback.onFinish("Não foi possível authenticar o bloco")
                    return
                }

                val writeResponse = PosDigital.getInstance().getMifare().writeBlock(block, "00000000000000000000000000000012")

                if (writeResponse != MifareStatus.SUCCESS) {
                    callback.onFinish("Não foi possível escrever no bloco")
                    return
                }

                val result = PosDigital.getInstance().getMifare().readBlock(block)

                callback.onFinish("write [$result]")
            }

            override fun onError(message: String) {
                callback.onFinish(message)
            }
        })
    }

    private fun restore() {
        val callback = openActionDialog("Mifare", "Aproxime o cartão ...")
        waitCard(object : MifareCallback {
            override fun onSuccess(type: Int) {
                val response = PosDigital.getInstance().getMifare().restore(block) == MifareStatus.SUCCESS
                callback.onFinish("restore [$response]")
            }

            override fun onError(message: String) {
                callback.onFinish(message)
            }
        })
    }

    private fun transfer() {
        val callback = openActionDialog("Mifare", "Aproxime o cartão ...")
        waitCard(object : MifareCallback {
            override fun onSuccess(type: Int) {
                val response = PosDigital.getInstance().getMifare().transfer(block) == MifareStatus.SUCCESS
                callback.onFinish("transfer [$response]")
            }

            override fun onError(message: String) {
                callback.onFinish(message)
            }
        })
    }

    private fun waitCard(callback: MifareCallback) {
        try {
            PosDigital.getInstance().getMifare().searchCard(
                    object : IMifareCallback.Stub() {
                        override fun onCard(type: Int) {
                            callback.onSuccess(type)
                        }

                        override fun onError(error: String) {
                            callback.onError(error)
                        }
                    })
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun close() {
        try {
            PosDigital.getInstance().getMifare().close()
            openInfoDialog("Antena desligada")
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }


    interface MifareCallback {

        fun onSuccess(type: Int)

        fun onError(message: String)

    }

}
