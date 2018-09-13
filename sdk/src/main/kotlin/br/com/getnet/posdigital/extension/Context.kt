package br.com.getnet.posdigital.extension

import android.content.Context
import android.graphics.drawable.Drawable


fun Context.loadDrawable(identifier: String): Drawable {
    val resourceId = resources.getIdentifier(identifier, "drawable", packageName)
    return resources.getDrawable(resourceId, null)
}