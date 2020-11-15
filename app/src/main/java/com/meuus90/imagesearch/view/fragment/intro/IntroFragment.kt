package com.meuus90.imagesearch.view.fragment.intro

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.meuus90.imagesearch.R
import com.meuus90.imagesearch.base.view.BaseActivity.Companion.BACK_STACK_STATE_ADD
import com.meuus90.imagesearch.base.view.BaseFragment
import com.meuus90.imagesearch.base.view.util.DetailsTransition
import com.meuus90.imagesearch.view.fragment.main.ImageListFragment
import com.meuus90.imagesearch.view.fragment.main.ImageListFragment.Companion.KEY_SEARCH
import com.meuus90.imagesearch.viewmodel.image.LocalViewModel
import kotlinx.android.synthetic.main.fragment_intro.*
import javax.inject.Inject

class IntroFragment : BaseFragment() {
    @Inject
    internal lateinit var splashViewModel: LocalViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        et_search.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                onSearch(et_search.text?.trim().toString())
                true
            } else {
                false
            }
        }

        iv_search.setOnClickListener {
            onSearch(et_search.text?.trim().toString())
        }
    }

    override fun onResume() {
        super.onResume()

        et_search?.setText("")
    }

    private fun onSearch(searchText: String) {
        splashViewModel.clearCache()

        val bundle = Bundle()
        bundle.putString(KEY_SEARCH, searchText)

        val newFragment = addFragment(
            ImageListFragment::class.java,
            BACK_STACK_STATE_ADD,
            bundle,
            et_search,
            false
        )

        newFragment.sharedElementEnterTransition = DetailsTransition()
        newFragment.enterTransition = null
        exitTransition = null
        newFragment.sharedElementReturnTransition = DetailsTransition()
    }
}