package com.vpaliy.mediaplayer.ui.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.View
import com.vpaliy.mediaplayer.FitnessSound
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.di.component.DaggerViewComponent
import com.vpaliy.mediaplayer.di.module.PresenterModule
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.base.BaseActivity
import com.vpaliy.mediaplayer.ui.base.BaseAdapter
import com.vpaliy.mediaplayer.ui.home.TrackAdapter
import com.vpaliy.mediaplayer.ui.search.SearchContract.Presenter
import kotlinx.android.synthetic.main.activity_search.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.text.TextUtils
import android.content.Intent
import javax.inject.Inject

class SearchActivity:BaseActivity(), SearchContract.View{

    private lateinit var presenter:Presenter
    private lateinit var adapter:BaseAdapter<Track>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        adapter=TrackAdapter(this,{navigator.navigate(this,it)})
        result.adapter=adapter
        setupSearch()
    }

    private fun setupSearch(){
        val searchManager=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint=getString(R.string.search_hint)
        searchView.inputType=InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
        searchView.imeOptions = searchView.imeOptions or EditorInfo.IME_ACTION_SEARCH or
                EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_FLAG_NO_FULLSCREEN
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrEmpty()) clear()
                return true
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                presenter.query(query)
                searchView.clearFocus()
                hideKeyboard()
                return true
            }
        })
    }

    override fun onNewIntent(intent: Intent) {
        if (intent.hasExtra(SearchManager.QUERY)) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            if (!TextUtils.isEmpty(query)) {
                searchView.setQuery(query, false)
                searchView.clearFocus()
                hideKeyboard()
                presenter.query(query)
            }
        }
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        view?.let{
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun clear(){

    }

    override fun inject(){
        DaggerViewComponent.builder()
                .presenterModule(PresenterModule())
                .applicationComponent(FitnessSound.app().component())
                .build().inject(this)
    }

    override fun error() {

    }

    override fun empty() {

    }

    override fun setLoading(isLoading: Boolean) {
        progress.visibility=if(isLoading)
            View.VISIBLE else View.GONE
    }

    @Inject
    override fun attach(presenter: Presenter) {
        this.presenter=presenter
        presenter.attachView(this)
    }

    override fun show(list: List<Track>)=adapter.set(list.toMutableList())

    override fun append(list: List<Track>)=adapter.appendData(list.toMutableList())

    override fun onDestroy() {
        super.onDestroy()
        presenter.stop()
    }
}