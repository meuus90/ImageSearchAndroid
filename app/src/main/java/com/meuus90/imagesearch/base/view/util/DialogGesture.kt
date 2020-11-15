package com.meuus90.imagesearch.base.view.util

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.fragment.app.DialogFragment
import kotlin.math.abs


open class DialogGesture(private val dialog: DialogFragment) :
    GestureDetector.SimpleOnGestureListener() {
    companion object {
        const val SWIPE_MIN_DISTANCE = 120
        const val SWIPE_THRESHOLD_VELOCITY = 200
    }

    override fun onFling(
        e1: MotionEvent, e2: MotionEvent, velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (e1.x - e2.x > SWIPE_MIN_DISTANCE
            && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY
        ) {
            // swipe right to left
            dialog.dismiss()
        } else if (e2.x - e1.x > SWIPE_MIN_DISTANCE
            && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY
        ) {
            // swipe left to right
            dialog.dismiss()
        } else if (e1.y - e2.y > SWIPE_MIN_DISTANCE
            && abs(velocityY) > SWIPE_THRESHOLD_VELOCITY
        ) {
            // top to bottom
            dialog.dismiss()
        } else if (e2.y - e1.y > SWIPE_MIN_DISTANCE
            && abs(velocityY) > SWIPE_THRESHOLD_VELOCITY
        ) {
            // bottom to top
            dialog.dismiss()
        }
        return super.onFling(e1, e2, velocityX, velocityY)
    }
}