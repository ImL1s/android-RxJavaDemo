package com.cbstudio.rxjavademo

import io.reactivex.Observable
import io.reactivex.rxkotlin.concatAll
import org.junit.Test
import java.util.concurrent.TimeUnit


/**
 * Created by ImL1s on 2018/5/10.
 * Description:
 */
class SwitchMergeConcatTest {

    @Test
    fun concatTest() {
        fun concatTest() {
            val originSource = Observable.just(1, 2)
            val secondSource = originSource.map { Observable.interval(1000, TimeUnit.MILLISECONDS) }
            val lastSource = secondSource.concatAll()
            lastSource.subscribe({

            }, {

            }, {

            })
        }
    }
}