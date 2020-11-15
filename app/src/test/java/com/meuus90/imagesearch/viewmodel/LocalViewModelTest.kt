package com.meuus90.imagesearch.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.meuus90.imagesearch.model.mock.FakeCache
import com.meuus90.imagesearch.utils.CoroutineTestRule
import com.meuus90.imagesearch.viewmodel.image.LocalViewModel
import io.mockk.MockKAnnotations
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestWatcher
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
@RunWith(MockitoJUnitRunner::class)
class LocalViewModelTest : TestWatcher() {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val coroutineContext = coroutineTestRule.coroutineContext

    private lateinit var viewModel: LocalViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        val cache = FakeCache()

        viewModel =
            LocalViewModel(cache)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun viewModelClearCacheTest() {
        viewModel.clearCache()

        println("viewModelClearCacheTest() pass")
    }
}