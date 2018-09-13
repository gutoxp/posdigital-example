package br.com.getnet.posdigital.card

import br.com.getnet.posdigital.base.BaseActivity
import br.com.getnet.posdigital.base.ItemAdapter
import com.getnet.posdigital.PosDigital
import com.getnet.posdigital.card.CardResponse
import com.getnet.posdigital.card.ICardCallback
import com.getnet.posdigital.card.SearchType
import br.com.getnet.posdigital.card.CardMethod.*
import br.com.getnet.posdigital.data.DataGenerator
import br.com.getnet.posdigital.data.ItemData

class CardActivity : BaseActivity(), ItemAdapter.AdapterMainListener {

    private val timeout = 30L

    override fun getAdapter(): ItemAdapter {
        val items = DataGenerator().loadCard(baseContext)
        return ItemAdapter(baseContext, items, this)
    }

    override fun onClickItem(itemData: ItemData) {
        when (valueOf(itemData.id)) {
            NFC -> onCardNFC()
            CHIP -> onCardChip()
            MAG -> onCardMag()
            else -> onCardAll()
        }
    }

    private fun onCardNFC() {
        try {
            val callback = openActionDialog("Cartão", "Aproxime o cartão ...")
            val searchType = arrayOf(SearchType.NFC)
            PosDigital.getInstance().getCard().search(timeout, searchType, callback(callback))
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun onCardChip() {
        try {
            val callback = openActionDialog("Cartão", "Insira o cartão ...")
            val searchType = arrayOf(SearchType.CHIP)
            PosDigital.getInstance().getCard().search(timeout, searchType, callback(callback))
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun onCardMag() {
        try {
            val callback = openActionDialog("Cartão", "Passe o cartão ...")
            val searchType = arrayOf(SearchType.MAG)
            PosDigital.getInstance().getCard().search(timeout, searchType, callback(callback))
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun onCardAll() {
        try {
            val callback = openActionDialog("Cartão", "Passe, insira ou aproxime o cartão ...")
            val searchType = arrayOf(SearchType.MAG, SearchType.CHIP, SearchType.NFC)
            PosDigital.getInstance().getCard().search(timeout, searchType, callback(callback))
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun callback(callback: BottomSheetDialogCallback): ICardCallback.Stub {
        return object : ICardCallback.Stub() {

            override fun onCard(cardResponse: CardResponse) {
                val builder = StringBuilder()
                builder.append("Type :[").append(getCardType(cardResponse.type)).append("]\n")
                builder.append("PAN :[").append(cardResponse.pan).append("]\n")
                builder.append("Track 1 :[").append(cardResponse.track1).append("]\n")
                builder.append("Track 2 :[").append(cardResponse.track2).append("]\n")
                builder.append("Track 3 :[").append(cardResponse.track3).append("]\n")
                builder.append("EXPIRE DATE :[").append(cardResponse.expireDate).append("]\n")
                builder.append("SERVICE CODE :[").append(cardResponse.serviceCode).append("]")
                callback.onFinish(builder.toString())
            }

            override fun onMessage(message: String) {
                callback.onMessage(message)
            }

            override fun onError(error: String) {
                callback.onFinish(error)
            }

        }
    }

    private fun getCardType(cardType: String?): String {
        return when (cardType) {
            SearchType.MAG -> "MAG"
            SearchType.NFC -> "NFC"
            SearchType.CHIP -> "CHIP"
            else -> "Could not define"
        }
    }

}