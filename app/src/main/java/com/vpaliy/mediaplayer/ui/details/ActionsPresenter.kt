package com.vpaliy.mediaplayer.ui.details

import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.details.ActionsContract.View
import com.vpaliy.mediaplayer.ui.details.ActionsContract.Presenter
import com.vpaliy.mediaplayer.domain.interactor.ModifyInteractor
import com.vpaliy.mediaplayer.domain.interactor.params.ModifyRequest
import com.vpaliy.mediaplayer.domain.model.TrackType
import javax.inject.Inject
import com.vpaliy.mediaplayer.di.scope.ViewScope

@ViewScope
class ActionsPresenter @Inject constructor(private val modifier: ModifyInteractor) : Presenter {

  private lateinit var view: View

  override fun add(track: Track) {
    val param = ModifyRequest(TrackType.HISTORY, track)
    modifier.insert(view::added, this::error, param)
  }

  override fun remove(track: Track) {
    val param = ModifyRequest(TrackType.HISTORY, track)
    modifier.remove(view::removed, this::error, param)
  }

  override fun dislike(track: Track) {
    val param = ModifyRequest(TrackType.FAVORITE, track)
    modifier.remove(view::disliked, this::error, param)
  }

  override fun like(track: Track) {
    val param = ModifyRequest(TrackType.FAVORITE, track)
    modifier.insert(view::liked, this::error, param)
  }

  private fun error(ex: Throwable) {
    view.error()
    ex.printStackTrace()
  }

  override fun attach(view: View) {
    this.view = view
  }
}