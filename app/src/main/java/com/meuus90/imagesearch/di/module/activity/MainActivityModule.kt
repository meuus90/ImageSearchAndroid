package com.meuus90.imagesearch.di.module.activity

import com.meuus90.imagesearch.di.module.fragment.ImageListFragmentModule
import com.meuus90.imagesearch.di.module.fragment.IntroFragmentModule
import com.meuus90.imagesearch.view.activity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(
        modules = [
            IntroFragmentModule::class,
            ImageListFragmentModule::class
        ]
    )
    internal abstract fun contributeMainActivity(): MainActivity
}