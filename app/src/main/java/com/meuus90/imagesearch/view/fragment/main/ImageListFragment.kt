package com.meuus90.imagesearch.view.fragment.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meuus90.imagesearch.R
import com.meuus90.imagesearch.base.constant.AppConfig
import com.meuus90.imagesearch.base.view.BaseFragment
import com.meuus90.imagesearch.base.view.ext.dpToPx
import com.meuus90.imagesearch.base.view.ext.gone
import com.meuus90.imagesearch.base.view.ext.hideKeyboard
import com.meuus90.imagesearch.base.view.ext.show
import com.meuus90.imagesearch.base.view.util.GridSpacingItemDecoration
import com.meuus90.imagesearch.model.paging.image.ImagesPageKeyedMediator
import com.meuus90.imagesearch.model.schema.image.ImageRequest
import com.meuus90.imagesearch.model.schema.image.ImageRequest.Companion.SORT_ACCURACY
import com.meuus90.imagesearch.model.schema.image.ImageRequest.Companion.SORT_RECENCY
import com.meuus90.imagesearch.view.activity.Caller
import com.meuus90.imagesearch.view.dialog.ImageDialog
import com.meuus90.imagesearch.view.fragment.main.adapter.CollectionListAdapter
import com.meuus90.imagesearch.view.fragment.main.adapter.ImageListAdapter
import com.meuus90.imagesearch.viewmodel.image.ImagesViewModel
import com.meuus90.imagesearch.viewmodel.image.LocalViewModel
import kotlinx.android.synthetic.main.fragment_image_list.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class ImageListFragment : BaseFragment() {
    companion object {
        const val KEY_SEARCH = "KEY_SEARCH"
    }

    @Inject
    internal lateinit var introViewModel: LocalViewModel

    @Inject
    internal lateinit var imageViewModel: ImagesViewModel

    private val searchSchema = ImageRequest("", SORT_ACCURACY, AppConfig.remotePagingSize, null)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_list, container, false)
    }

    private var isRecyclerViewInitialized = false
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val searchText = arguments?.getString(KEY_SEARCH, "") ?: ""

        tv_accuracy.isSelected = true

        initViewsListener()

        if (!isRecyclerViewInitialized) {
            initAdapter()

            isRecyclerViewInitialized = true
        }

        et_search.setText(searchText)
    }

    private var dialog: ImageDialog? = null
    private val adapter = ImageListAdapter { item, _ ->
        if (dialog != null)
            dialog?.dismiss()

        dialog = ImageDialog(context, item) {
            Caller.openUrlLink(context, it)
        }
        dialog?.show(childFragmentManager, tag)
    }
    private val collectionAdapter = CollectionListAdapter { collection ->
        val isChanged = searchSchema.setCollection(collection)

        if (isChanged)
            updateList()
    }

    private var collections = arrayListOf<String?>()
    private fun initAdapter() {
        rv_collection.adapter = collectionAdapter
        rv_collection.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rv_collection.itemAnimator = null

        recyclerView.adapter = adapter
        recyclerView.setItemViewCacheSize(AppConfig.recyclerViewCacheSize)
        recyclerView.setHasFixedSize(false)
        recyclerView.isVerticalScrollBarEnabled = false
        recyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                dpToPx(context, 6f).toInt(),
                true
            )
        )

        val layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        recyclerView.layoutManager = layoutManager

        lifecycleScope.launchWhenCreated {
            imageViewModel.images
                .collectLatest {
                    recyclerView.show()
                    v_error.gone()

                    adapter.submitData(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            introViewModel.collections
                .collectLatest {
                    collectionAdapter.setList(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.append }
                .collectLatest {
                    val state = it.append
                    if (state is LoadState.Error)
                        updateErrorUI(state)
                    else {
                        introViewModel.getCollectionList(searchSchema.collection)

                        recyclerView.show()
                        v_error.gone()
                    }
                }
        }
    }

    private fun setSortType(sort: String) {
        val isChanged = searchSchema.setSortType(sort)

        if (isChanged)
            updateList()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViewsListener() {
        iv_home.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }

        iv_search.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
            updateList()
        }

        et_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                updateList()
                true
            } else {
                false
            }
        }

        et_search.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateList()
                true
            } else {
                false
            }
        }

        tv_accuracy.setOnClickListener {
            setSortType(SORT_ACCURACY)
            tv_accuracy.isSelected = true
            tv_recency.isSelected = false
        }

        tv_recency.setOnClickListener {
            setSortType(SORT_RECENCY)
            tv_accuracy.isSelected = false
            tv_recency.isSelected = true
        }

        recyclerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_MOVE)
                et_search.hideKeyboard()
            false
        }

        et_search.setOnClickListener {
            appbar_search.setExpanded(true)
        }

        et_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    updateListByDebounce()
                }
            }
        })
    }

    private fun updateErrorUI(state: LoadState.Error) {
        val error = state.error
        if (error is ImagesPageKeyedMediator.EmptyResultException) {
            showErrorView(
                R.drawable.brand_wearefriends4,
                getString(
                    R.string.network_message_no_item_title,
                    searchSchema.query
                ),
                getString(R.string.network_message_no_item_message)
            )
        } else if (error is HttpException) {
            val bodyStr = error.response()?.errorBody()?.string()
            val networkError = parseToNetworkError(bodyStr)

//            Timber.e(error)
            Timber.e(bodyStr)

            if (networkError.isMissingParameter()) {
                showErrorView(
                    R.drawable.brand_wearefriends4,
                    getString(R.string.network_message_no_item_title, ""),
                    getString(R.string.network_message_no_item_message)
                )
            } else {
                showErrorView(
                    R.drawable.brand_wearefriends7,
                    networkError.message,
                    getString(R.string.network_message_error_message)
                )
            }
        } else {
            showErrorView(
                R.drawable.brand_wearefriends7,
                getString(R.string.network_message_error_title),
                getString(R.string.network_message_error_message)
            )

        }
    }

    private fun updateListByDebounce() {
        et_search.text?.trim().toString().let {
            searchSchema.query = it
            imageViewModel.postRequestSchemaWithDebounce(searchSchema)
        }
    }

    private fun updateList() {
        hideKeyboard()

        et_search.text?.trim().toString().let {
            searchSchema.query = it
            imageViewModel.postRequestSchema(searchSchema)
        }
    }

    private fun showErrorView(drawableResId: Int, title: String, message: String) {
        recyclerView.gone()
        v_error.show()
        iv_error.setImageDrawable(ContextCompat.getDrawable(context, drawableResId))
        tv_error_title.text = title
        tv_error_message.text = message
    }
}