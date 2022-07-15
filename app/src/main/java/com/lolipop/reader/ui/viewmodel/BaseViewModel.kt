package com.lolipop.reader.ui.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @author FengZhongChan
 * @date 2022/7/7 15:26
 */
open class BaseViewModel : ViewModel() {
    private val mCompositeDisposable = CompositeDisposable()

    fun withDisposable(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.dispose()
    }
}