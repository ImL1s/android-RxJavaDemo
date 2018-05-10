package com.cbstudio.rxjavademo

import android.os.Bundle
import android.util.Log
import android.view.View
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.activity_living_example.*
import java.util.concurrent.TimeUnit

class LivingExampleActivity : BaseActivity() {

    private val apiRetryUntilSucc1Source: Subject<Unit> = PublishSubject.create()
    private val apiRetryUntilSucc2Source: Subject<Unit> = PublishSubject.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_living_example)
        subscribe()
    }

    override fun subscribe() {
        mockAPIRetryUntilSucc1()
        mockAPIRetryUntilSucc2()
    }

    private fun mockAPIRetryUntilSucc1() {
        var result: String = ""
        val disposable = apiRetryUntilSucc1Source
                .concatMap { Observable.just("交易失敗1", "交易失敗2") }
                .concatMap { Observable.just(it).delay(1000, TimeUnit.MILLISECONDS) }
                .doOnNext { result = it }
                .repeatUntil { result == "交易成功" }
                .subscribe(
                        { Log.d(LOG_TAG_DEBUG, "onNext: $it") },
                        { err -> Log.d(LOG_TAG_DEBUG, "onNex: $err") },
                        { Log.d(LOG_TAG_DEBUG, "finish") }
                )
        disposableList.add(disposable)
        // log:
        // 交易失敗1
        // 交易失敗2
        // 因為apiRetryUntilSucc1Source沒有onComplete,所以repeatUnit無法repeat(不會重新訂閱)
        // 這邊會卡住直到重新觸發apiRetryUntilSucc1Source
    }

    private fun mockAPIRetryUntilSucc2() {
        var result: String = ""
        val disposable = apiRetryUntilSucc2Source
                .concatMap { Observable.just("交易失敗1", "交易失敗2", "交易成功") }
                .concatMap { Observable.just(it).delay(1000, TimeUnit.MILLISECONDS) }
                .doOnNext { result = it }
                .repeatUntil { result == "交易成功" }
                .subscribe(
                        { Log.d(LOG_TAG_DEBUG, "onNext: $it") },
                        { err -> Log.d(LOG_TAG_DEBUG, "onNex: $err") },
                        { Log.d(LOG_TAG_DEBUG, "finish") }
                )
        disposableList.add(disposable)
        // log:
        // 交易失敗1
        // 交易失敗2
        // 交易成功
        // finish
    }

    private fun mockAPIRetryUntilSucc1WithoutSubject() {
        var result: String = ""
        val disposable = Observable.just("交易失敗1", "交易失敗2")
                .concatMap { Observable.just(it).delay(1000, TimeUnit.MILLISECONDS) }
                .doOnNext { result = it }
                .repeatUntil { result == "交易成功" }
                .subscribe(
                        { Log.d(LOG_TAG_DEBUG, "onNext: $it") },
                        { err -> Log.d(LOG_TAG_DEBUG, "onNex: $err") },
                        { Log.d(LOG_TAG_DEBUG, "finish") }
                )
        disposableList.add(disposable)
        // log:
        // 交易失敗1
        // 交易失敗2
        // 交易失敗1
        // 交易失敗2
        // 交易失敗1
        // 交易失敗2
        // ...
    }

    private fun mockAPIRetryUntilSucc2WithoutSubject() {
        var result: String = ""
        val disposable = Observable.just("交易失敗1", "交易失敗2", "交易成功")
                .concatMap { Observable.just(it).delay(1000, TimeUnit.MILLISECONDS) }
                .doOnNext { result = it }
                .repeatUntil { result == "交易成功" }
                .subscribe(
                        { Log.d(LOG_TAG_DEBUG, "onNext: $it") },
                        { err -> Log.d(LOG_TAG_DEBUG, "onNex: $err") },
                        { Log.d(LOG_TAG_DEBUG, "finish") }
                )
        disposableList.add(disposable)
        // log:
        // 交易失敗1
        // 交易失敗2
        // 交易成功
        // finish
    }


    // TODO
//        Observable.merge(Observable.just(1), Observable.just(2), Observable.just(3))
//                .collect({ return@collect arrayListOf<Int>() }, { container, element ->
//                    container.add(element)
//                }).toObservable()
//                .doOnNext { it.sort() }
//                .subscribe { Log.d(LOG_TAG_DEBUG, "merge collect next: $it") }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            btn_api_retry_until_succ_1 -> apiRetryUntilSucc1Source.onNext(Unit)
            btn_api_retry_until_succ_2 -> apiRetryUntilSucc2Source.onNext(Unit)
            btn_api_retry_until_succ_1_without_subject -> mockAPIRetryUntilSucc1WithoutSubject()
            btn_api_retry_until_succ_2_without_subject -> mockAPIRetryUntilSucc2WithoutSubject()
        }
    }
}
