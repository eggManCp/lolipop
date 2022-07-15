package com.lolipop.reader.ui.viewmodel

import com.lolipop.reader.network.NetService
import com.lolipop.reader.network.RequestManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Dispatcher

/**
 * @author FengZhongChan
 * @date 2022/7/7 15:15
 */
class CategoryListModel : BaseViewModel() {
    private val service = RequestManager.getInstance().create(NetService::class.java)

    fun getCategoryEnd() {
        withDisposable(service.getCategoryEnd(1, 10)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

            })
    }
}