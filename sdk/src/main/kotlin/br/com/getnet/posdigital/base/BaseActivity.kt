package br.com.getnet.posdigital.base

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import br.com.getnet.posdigital.R
import com.getnet.posdigital.PosDigital
import kotlinx.android.synthetic.main.basic_button_sheet_dialog.view.*
import kotlinx.android.synthetic.main.content_activity.*
import kotlinx.android.synthetic.main.header_view.*

abstract class BaseActivity : AppCompatActivity() {

    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_activity)
        connectPosDigitalService()
        initRecycler()
        initBottomSheet()
        initHeaderEvents()
    }

    private fun initHeaderEvents() {
        header_reconnect.setOnClickListener {
            if (PosDigital.getInstance().isInitiated()) {
                PosDigital.unregister(applicationContext)
            } else {
                connectPosDigitalService()
            }
        }
    }

    private fun initRecycler() {
        recyclerView.adapter = getAdapter()
        recyclerView.layoutManager = LinearLayoutManager(baseContext)
    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from<View>(bottom_sheet)
    }

    private fun connectPosDigitalService() {
        PosDigital.register(applicationContext, bindCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        PosDigital.unregister(applicationContext)
    }


    private val bindCallback: PosDigital.BindCallback
        get() = object : PosDigital.BindCallback {
            override fun onError(e: Exception) {
                main_header.changeStatus(false)
                openErrorDialog(e.message!!)
            }

            override fun onConnected() {
                main_header.changeStatus(true)
            }

            override fun onDisconnected() {
                main_header.changeStatus(false)
            }

        }

    private fun openBottomSheetDialog(title: String, message: String): View {
        if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        val view = layoutInflater.inflate(R.layout.basic_button_sheet_dialog, null)
        runOnUiThread {
            val bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()

            view.title.text = title
            view.message.text = message
            view.button.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }
        return view
    }

    fun startActivity(activity: Class<*>) {
        startActivity(Intent(baseContext, activity))
    }

    interface BottomSheetDialogCallback {

        fun onMessage(message: String)

        fun onFinish(message: String)

    }

    fun openActionDialog(title: String, message: String): BottomSheetDialogCallback {
        val view = openBottomSheetDialog(title, message)
        view.button.visibility = View.INVISIBLE
        return object : BottomSheetDialogCallback {
            override fun onMessage(message: String) {
                runOnUiThread {
                    view.message.text = message
                }
            }

            override fun onFinish(message: String) {
                runOnUiThread {
                    view.message.text = message
                    view.button.visibility = View.VISIBLE
                }
            }
        }
    }

    fun openErrorDialogCheckConnection() {
        openErrorDialog("Check the service connection")
    }

    fun openInfoDialog(message: String) {
        openBottomSheetDialog("Info", message)
    }

    fun openErrorDialog(message: String) {
        openBottomSheetDialog("Error", message)
    }

    abstract fun getAdapter(): ItemAdapter

}