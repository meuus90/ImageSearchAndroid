package com.meuus90.imagesearch.view.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.meuus90.imagesearch.R
import com.meuus90.imagesearch.base.view.ext.setDefaultWindowTheme
import com.meuus90.imagesearch.base.view.util.DialogGesture
import com.meuus90.imagesearch.model.schema.image.ImageDoc
import kotlinx.android.synthetic.main.dialog_image.*
import kotlin.math.abs


open class ImageDialog(
    val mContext: Context,
    val image: ImageDoc,
    val onClickURL: (url: String) -> Unit
) : DialogFragment() {
    override fun onStart() {
        super.onStart()
        dialog?.setDefaultWindowTheme()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, R.style.DialogFullscreenTransparent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return activity?.layoutInflater?.inflate(R.layout.dialog_image, container, false)
    }

    private var oldX = 0f
    private var oldY = 0f

    private var imageX = 0f
    private var imageY = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val gestureDetector = GestureDetector(mContext, DialogGesture(this))

        dialog?.window?.decorView?.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    oldX = event.x
                    oldY = event.y

                    imageX = iv_image.x
                    imageY = iv_image.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val xMove = event.x - oldX
                    val yMove = event.y - oldY

                    val ratio =
                        ((v.width - abs(xMove)) / v.width + (v.height - abs(yMove)) / v.height)

                    v_root.alpha = if (ratio - 1 < 0) 0f else ratio - 1

                    iv_image.x = imageX + xMove
                    iv_image.y = imageY + yMove
                }
                MotionEvent.ACTION_UP -> {
                    v_root.alpha = 1f

                    iv_image.x = imageX
                    iv_image.y = imageY
                }
            }
            gestureDetector.onTouchEvent(event)
        }

        iv_close.setOnClickListener {
            dismiss()
        }

        Glide.with(mContext).asDrawable().clone()
            .load(image.image_url)
            .centerInside()
            .error(R.drawable.ic_no_image)
            .into(iv_image)

        tv_site_name.text =
            if (image.display_sitename.isEmpty()) mContext.getString(R.string.item_web) else image.display_sitename
        tv_url.text = image.doc_url

        v_title.setOnClickListener {
            onClickURL(image.doc_url)
        }
    }
}