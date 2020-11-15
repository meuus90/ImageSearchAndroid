package com.meuus90.imagesearch.base.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.annotation.GlideModule
import com.meuus90.imagesearch.R
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), HasAndroidInjector {
    companion object {
        const val BACK_STACK_STATE_NEW = -1
        const val BACK_STACK_STATE_REPLACE = 0
        const val BACK_STACK_STATE_ADD = 1
        const val BACK_STACK_STATE_POP_AND_ADD = 2
    }

    open val frameLayoutId = 0

    private lateinit var glideRequestManager: RequestManager

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector() = dispatchingAndroidInjector

    protected abstract fun setContentView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @GlideModule
        glideRequestManager = Glide.with(this)

        setContentView()
    }

    internal fun replaceFragment(cls: Class<*>, bundle: Bundle? = null): Fragment {
        val fragment = getFragmentInstance(cls)
        if (bundle != null)
            fragment.arguments = bundle
        callFragment(fragment)

        return fragment
    }

    internal fun addFragment(
        cls: Class<*>,
        backStackState: Int,
        bundle: Bundle? = null,
        sharedView: View? = null,
        useAnimation: Boolean = true
    ): Fragment {
        val fragment = getFragmentInstance(cls)
        if (bundle != null)
            fragment.arguments = bundle

        supportFragmentManager.apply {
            when (backStackState) {
                BACK_STACK_STATE_NEW -> {
                    popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                    beginTransaction().setCustomAnimations(0, 0, 0, 0)
                }
                BACK_STACK_STATE_REPLACE -> {
                    popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                    if (useAnimation)
                        beginTransaction().setCustomAnimations(
                            0, 0,
                            R.anim.slide_in_left_right,
                            R.anim.slide_out_left_right
                        )
                    else
                        beginTransaction().setCustomAnimations(
                            0, 0, 0, 0
                        )
                }
                BACK_STACK_STATE_ADD -> {
                    if (useAnimation)
                        beginTransaction().setCustomAnimations(
                            R.anim.slide_in_right_left,
                            R.anim.slide_out_right_left,
                            R.anim.slide_in_left_right,
                            R.anim.slide_out_left_right
                        )
                    else
                        beginTransaction().setCustomAnimations(
                            0, 0, 0, 0
                        )
                }
                BACK_STACK_STATE_POP_AND_ADD -> {
                    popBackStack()

                    beginTransaction().setCustomAnimations(
                        R.anim.slide_in_right_left,
                        R.anim.slide_out_right_left,
                        R.anim.slide_in_left_right,
                        R.anim.slide_out_left_right
                    )
                }
            }
        }

        callFragment(fragment, sharedView)

        return fragment
    }

    private fun getFragmentInstance(cls: Class<*>): Fragment {
        var fragment = supportFragmentManager.findFragmentByTag(cls.name)

        fragment?.let {
            supportFragmentManager.beginTransaction().remove(fragment!!).commit()
        }

        fragment = supportFragmentManager.fragmentFactory.instantiate(cls.classLoader!!, cls.name)
        return fragment
    }

    private fun callFragment(
        fragment: Fragment,
        sharedView: View? = null
    ) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .apply {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

                addToBackStack(fragment.javaClass.name)

                if (sharedView != null)
                    addSharedElement(sharedView, sharedView.transitionName)
                replace(frameLayoutId, fragment, fragment.javaClass.name)
            }.commit()
    }

    protected fun getCurrentFragment(): BaseFragment? {
        val fragmentCount = supportFragmentManager.backStackEntryCount
        if (fragmentCount > 0) {
            val backEntry = supportFragmentManager.getBackStackEntryAt(fragmentCount - 1)
            val f = supportFragmentManager.findFragmentByTag(backEntry.name)
            return if (f != null) f as BaseFragment
            else null
        }

        return null
    }

    override fun onBackPressed() {
        val fragment = getCurrentFragment()
        if (fragment != null) {
            val fragmentStackSize = supportFragmentManager.backStackEntryCount
            if (fragmentStackSize <= 1) {
//                supportFragmentManager.popBackStack()
                supportFinishAfterTransition()
            } else
                supportFragmentManager.popBackStack()
//                super.onBackPressed()

        } else {
            super.onBackPressed()
        }
    }

    internal fun hideKeyboard() {
        val inputManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        currentFocus?.windowToken?.let {
            inputManager.hideSoftInputFromWindow(
                it,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}