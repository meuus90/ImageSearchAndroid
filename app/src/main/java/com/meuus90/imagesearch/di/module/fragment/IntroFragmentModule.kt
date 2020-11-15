package com.meuus90.imagesearch.di.module.fragment

import com.meuus90.imagesearch.view.fragment.intro.IntroFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class IntroFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeIntroFragment(): IntroFragment
}