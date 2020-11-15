package com.meuus90.imagesearch.model.data.source.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.meuus90.imagesearch.model.data.source.repository.image.ImagesRepository
import com.meuus90.imagesearch.model.mock.FakeCache
import com.meuus90.imagesearch.model.mock.FakeDaumAPI
import com.meuus90.imagesearch.model.mock.FakeSchema.mockImageSchema
import com.meuus90.imagesearch.model.schema.image.ImageDoc
import com.meuus90.imagesearch.utils.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestWatcher
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
@RunWith(MockitoJUnitRunner::class)
class ImagesRepositoryTest : TestWatcher() {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val mockRepository = Mockito.mock(ImagesRepository::class.java) as ImagesRepository

    private val repository =
        ImagesRepository(
            FakeCache(),
            FakeDaumAPI()
        )

    private val flow = flowOf(PagingData.empty<ImageDoc>())

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        Mockito.`when`(mockRepository.execute(mockImageSchema))
            .thenReturn(flow)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun imageRepositoryTest() {
        runBlockingTest {
            Assert.assertEquals(mockRepository.execute(mockImageSchema), flow)

            println("imageRepositoryTest() pass")
        }
    }
}