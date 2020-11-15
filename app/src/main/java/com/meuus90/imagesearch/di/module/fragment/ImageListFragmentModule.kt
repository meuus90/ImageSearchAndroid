package com.meuus90.imagesearch.di.module.fragment

import com.meuus90.imagesearch.view.fragment.main.ImageListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ImageListFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeImageListFragment(): ImageListFragment
}