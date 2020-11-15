package com.meuus90.imagesearch.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.meuus90.imagesearch.model.data.source.repository.image.ImagesRepository
import com.meuus90.imagesearch.model.mock.FakeSchema.mockImageSchema0
import com.meuus90.imagesearch.model.mock.FakeSchema.mockImageSchema1
import com.meuus90.imagesearch.model.mock.FakeSchema.mockImageSchema2
import com.meuus90.imagesearch.model.schema.image.ImageDoc
import com.meuus90.imagesearch.utils.CoroutineTestRule
import com.meuus90.imagesearch.utils.getOrAwaitValue
import com.meuus90.imagesearch.viewmodel.image.ImagesViewModel
import io.mockk.MockKAnnotations
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestWatcher
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
@RunWith(MockitoJUnitRunner::class)
class ImagesViewModelTest : TestWatcher() {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val coroutineContext = coroutineTestRule.coroutineContext

    private lateinit var viewModel: ImagesViewModel

    @Mock
    private lateinit var mockObserver: Observer<PagingData<ImageDoc>>

    @Captor
    private lateinit var captor: ArgumentCaptor<PagingData<ImageDoc>>

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        val repository = Mockito.mock(ImagesRepository::class.java) as ImagesRepository

        runBlockingTest {
            val flow = flow<PagingData<ImageDoc>> {
                emit(PagingData.empty())
            }
        }

        viewModel = ImagesViewModel(repository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun postImageSchemaWithDebounceTest() {
        runBlockingTest {
            viewModel.postRequestSchemaWithDebounce(mockImageSchema0)
            viewModel.postRequestSchemaWithDebounce(mockImageSchema1)
            viewModel.postRequestSchemaWithDebounce(mockImageSchema2)

            Assert.assertEquals(
                viewModel.org.getOrAwaitValue(),
                mockImageSchema2
            )

            println("postImageSchemaWithDebounceTest() pass")
        }
    }
}