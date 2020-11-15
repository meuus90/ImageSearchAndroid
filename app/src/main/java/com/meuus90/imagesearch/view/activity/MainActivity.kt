package com.meuus90.imagesearch.view.activity

import android.os.Bundle
import com.meuus90.imagesearch.R
import com.meuus90.imagesearch.base.view.BaseActivity
import com.meuus90.imagesearch.view.fragment.intro.IntroFragment

class MainActivity : BaseActivity() {
    override val frameLayoutId = R.id.contentFrame

    override fun setContentView() {
        setContentView(R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        replaceFragment(IntroFragment::class.java)
    }
}