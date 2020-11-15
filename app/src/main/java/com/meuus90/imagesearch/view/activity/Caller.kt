package com.meuus90.imagesearch.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.meuus90.imagesearch.R

object Caller {
    internal fun openUrlLink(context: Context, url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(
            Intent.createChooser(
                browserIntent,
                context.getString(R.string.choose_browser)
            )
        )
    }
}