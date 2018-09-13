package br.com.getnet.posdigital.printer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.RemoteException
import br.com.getnet.posdigital.R
import br.com.getnet.posdigital.base.BaseActivity
import br.com.getnet.posdigital.base.ItemAdapter
import br.com.getnet.posdigital.data.DataGenerator
import br.com.getnet.posdigital.data.ItemData
import com.getnet.posdigital.PosDigital
import com.getnet.posdigital.extension.ViewUtils
import com.getnet.posdigital.printer.AlignMode
import com.getnet.posdigital.printer.FontFormat
import com.getnet.posdigital.printer.IPrinterCallback
import com.getnet.posdigital.printer.PrinterStatus

class PrinterActivity : BaseActivity(), ItemAdapter.AdapterMainListener {

    override fun getAdapter(): ItemAdapter {
        val items = DataGenerator().loadPrint(baseContext)
        return ItemAdapter(baseContext, items, this)
    }

    override fun onClickItem(itemData: ItemData) {
        when (PrinterMethod.valueOf(itemData.id)) {
            PrinterMethod.STATUS -> onPrintStatus()
            PrinterMethod.PRINTER -> onPrint()
            PrinterMethod.TEXT -> onPrint()
            PrinterMethod.DEFINE -> onPrint()
            PrinterMethod.GRAY -> onPrint()
            PrinterMethod.BMP -> onPrint()
            PrinterMethod.BARCODE -> onPrint()
            PrinterMethod.QRCODE -> onPrint()
            PrinterMethod.INIT -> onPrint()
            else -> onPrint()
        }
    }

    private fun parseStatus(status: Int): String {
        return when (status) {
            PrinterStatus.OK -> "OK"
            PrinterStatus.PRINTING -> "Imprimindo"
            PrinterStatus.ERROR_NOT_INIT -> "Impressora não iniciada"
            PrinterStatus.ERROR_OVERHEAT -> "Impressora superaquecida"
            PrinterStatus.ERROR_BUFOVERFLOW -> "Fila de impressão muito grande"
            PrinterStatus.ERROR_PARAM -> "Parametros incorretos"
            PrinterStatus.ERROR_LIFTHEAD -> "Porta da impressora aberta"
            PrinterStatus.ERROR_LOWTEMP -> "Temperatura baixa demais para impressão"
            PrinterStatus.ERROR_LOWVOL -> "Sem bateria suficiente para impressão"
            PrinterStatus.ERROR_MOTORERR -> "Motor de passo com problemas"
            PrinterStatus.ERROR_NO_PAPER -> "Sem bonina"
            PrinterStatus.ERROR_PAPERENDING -> "Bobina acabando"
            PrinterStatus.ERROR_PAPERJAM -> "Bobina travada"
            PrinterStatus.UNKNOW -> "Não foi possível definir o erro"
            else -> "Não foi possível definir o erro"
        }
    }

    private fun onPrintStatus() {
        try {
            openInfoDialog(String.format("Status [%s]", parseStatus(PosDigital.getInstance().getPrinter().status)))
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun getPrinterCallback(): IPrinterCallback.Stub {
        return object : IPrinterCallback.Stub() {

            @Throws(RemoteException::class)
            override fun onSuccess() {
                openInfoDialog("Impresso com sucesso")
            }

            @Throws(RemoteException::class)
            override fun onError(cause: Int) {
                openErrorDialog(parseStatus(cause))
            }
        }
    }

    private fun onPrint() {
        try {
            PosDigital.getInstance().getPrinter().init()
            PosDigital.getInstance().getPrinter().setGray(5)
            PosDigital.getInstance().getPrinter().defineFontFormat(FontFormat.MEDIUM)
            PosDigital.getInstance().getPrinter().addText(AlignMode.LEFT, "Text align:")
            PosDigital.getInstance().getPrinter().addText(AlignMode.LEFT, "LEFT")
            PosDigital.getInstance().getPrinter().addText(AlignMode.CENTER, "CENTER")
            PosDigital.getInstance().getPrinter().addText(AlignMode.RIGHT, "RIGHT")
            PosDigital.getInstance().getPrinter().addText(AlignMode.LEFT, "--------------------------------")
            PosDigital.getInstance().getPrinter().addText(AlignMode.LEFT, "Text Font Size:")
            PosDigital.getInstance().getPrinter().defineFontFormat(FontFormat.SMALL)
            PosDigital.getInstance().getPrinter().addText(AlignMode.LEFT, "SMALL TEXT")
            PosDigital.getInstance().getPrinter().defineFontFormat(FontFormat.MEDIUM)
            PosDigital.getInstance().getPrinter().addText(AlignMode.LEFT, "MEDIUM TEXT")
            PosDigital.getInstance().getPrinter().defineFontFormat(FontFormat.LARGE)
            PosDigital.getInstance().getPrinter().addText(AlignMode.LEFT, "LARGE TEXT")
            PosDigital.getInstance().getPrinter().defineFontFormat(FontFormat.MEDIUM)
            PosDigital.getInstance().getPrinter().addText(AlignMode.LEFT, "--------------------------------")
            PosDigital.getInstance().getPrinter().addText(AlignMode.LEFT, "Image From Assets:")
            PosDigital.getInstance().getPrinter().addImageBitmap(AlignMode.CENTER, loadFromAsset("logo_getnet.bmp"))
            PosDigital.getInstance().getPrinter().addText(AlignMode.LEFT, "--------------------------------")
            PosDigital.getInstance().getPrinter().addText(AlignMode.LEFT, "Image From Resource:")
            PosDigital.getInstance().getPrinter().addImageBitmap(AlignMode.CENTER, loadFromResource(R.drawable.logo_getnet))
            PosDigital.getInstance().getPrinter().addText(AlignMode.LEFT, "--------------------------------")
            PosDigital.getInstance().getPrinter().addText(AlignMode.LEFT, "Barcode:")
            PosDigital.getInstance().getPrinter().addBarCode(AlignMode.CENTER, "12345678901234567890")
            PosDigital.getInstance().getPrinter().addText(AlignMode.LEFT, "--------------------------------")
            PosDigital.getInstance().getPrinter().addText(AlignMode.LEFT, "QrCode:")
            PosDigital.getInstance().getPrinter().addQrCode(AlignMode.CENTER, 240, "www.getnet.com.br")
            PosDigital.getInstance().getPrinter().addText(AlignMode.LEFT, "--------------------------------")
            PosDigital.getInstance().getPrinter().addText(AlignMode.LEFT, "View as Bitmap:")

            val view = layoutInflater.inflate(R.layout.slip, null)
            PosDigital.getInstance().getPrinter().addImageBitmap(AlignMode.CENTER, ViewUtils.convertToBitmap(view))
            PosDigital.getInstance().getPrinter().print(getPrinterCallback())
        } catch (e: Exception) {
            openErrorDialogCheckConnection()
        }
    }

    private fun loadFromAsset(filePath: String): Bitmap {
        val input = assets.open(filePath)
        return BitmapFactory.decodeStream(input)
    }

    private fun loadFromResource(resource: Int): Bitmap {
        return BitmapFactory.decodeResource(resources, resource)
    }

}