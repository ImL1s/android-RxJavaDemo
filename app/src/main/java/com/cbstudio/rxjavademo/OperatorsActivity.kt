package com.cbstudio.rxjavademo

import android.os.Bundle
import android.util.Log
import android.view.View
import io.reactivex.Observable
import io.reactivex.rxkotlin.concatAll
import io.reactivex.rxkotlin.mergeAll
import io.reactivex.rxkotlin.switchLatest
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class OperatorsActivity : BaseActivity() {

    private val concatSource = PublishSubject.create<Unit>()
    private val mergeSource = PublishSubject.create<Unit>()
    private val switchSource = PublishSubject.create<Unit>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operators)
        subscribe()
    }


    override fun subscribe() {
        // concat
        var disposable = concatSource.map { Observable.interval(1000, TimeUnit.MILLISECONDS) }
                .concatAll()
                .subscribe({
                    Log.d(LOG_TAG_DEBUG, "concatSource next: $it")
                }, {
                    Log.d(LOG_TAG_DEBUG, "concatSource error: $it")
                }, {
                    Log.d(LOG_TAG_DEBUG, "concatSource finish")
                })
        disposableList.add(disposable)


        // merge
        disposable = mergeSource.map { Observable.interval(1000, TimeUnit.MILLISECONDS) }
                .mergeAll()
                .subscribe({
                    Log.d(LOG_TAG_DEBUG, "mergeSource next: $it")
                }, {
                    Log.d(LOG_TAG_DEBUG, "mergeSource error: $it")
                }, {
                    Log.d(LOG_TAG_DEBUG, "mergeSource finish")
                })
        disposableList.add(disposable)

        // switch
        disposable = switchSource.map { Observable.interval(1000, TimeUnit.MILLISECONDS) }
                .switchLatest()
                .subscribe({
                    Log.d(LOG_TAG_DEBUG, "switchSource next: $it")
                }, {
                    Log.d(LOG_TAG_DEBUG, "switchSource error: $it")
                }, {
                    Log.d(LOG_TAG_DEBUG, "switchSource finish")
                })
        disposableList.add(disposable)
    }


    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.tv_concat -> concatSource.onNext(Unit)
            R.id.tv_merge -> mergeSource.onNext(Unit)
            R.id.tv_switch -> switchSource.onNext(Unit)
            R.id.btn_un_subscribe -> {
                clearAllSubscribe()
                subscribe()
            }
        }
    }
}
