package br.com.getnet.posdigital.component

import android.content.Context
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import br.com.getnet.posdigital.R
import kotlinx.android.synthetic.main.header_view.view.*

class HeaderView : FrameLayout {

    constructor(@NonNull context: Context) : super(context) {
        init()
    }

    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.header_view, this)
    }


    fun changeStatus(connected: Boolean) {
        rootView.header_status_icon.isSelected = connected
    }

}