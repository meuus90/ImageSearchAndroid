package com.meuus90.imagesearch.di.component

import android.app.Application
import com.meuus90.imagesearch.ImageSearch
import com.meuus90.imagesearch.di.module.AppModule
import com.meuus90.imagesearch.di.module.activity.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        MainActivityModule::class
    ]
)
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }

    fun inject(app: ImageSearch)
}