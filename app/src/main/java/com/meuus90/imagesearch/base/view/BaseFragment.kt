package com.meuus90.imagesearch.base.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.meuus90.imagesearch.base.arch.util.network.entity.NetworkError
import com.meuus90.imagesearch.di.Injectable

open class BaseFragment : Fragment(), Injectable {
    lateinit var baseActivity: BaseActivity
    private lateinit var context: Context

    override fun getContext() = context

    override fun onAttach(context: Context) {
        super.onAttach(context)

        baseActivity = (context as BaseActivity)
        this.context = context
    }

    internal fun addFragment(
        cls: Class<*>,
        backStackState: Int,
        bundle: Bundle? = null,
        sharedView: View? = null,
        useAnimation: Boolean = true
    ): Fragment {
        return baseActivity.addFragment(cls, backStackState, bundle, sharedView, useAnimation)
    }

    internal fun hideKeyboard() {
        baseActivity.hideKeyboard()
    }

    fun parseToNetworkError(errMsg: String?): NetworkError {
        errMsg?.let { message ->
            try {
                return Gson().fromJson(message, NetworkError::class.java)
            } catch (e: Exception) {

            }
        }
        return NetworkError(null)
    }
}