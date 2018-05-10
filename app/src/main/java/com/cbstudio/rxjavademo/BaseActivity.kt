package com.cbstudio.rxjavademo

import android.app.Activity
import android.view.View
import io.reactivex.disposables.Disposable


/**
 * Created by ImL1s on 2018/5/10.
 * Description:
 */
abstract class BaseActivity : Activity() {

    val disposableList: ArrayList<Disposable> = arrayListOf()

    protected open fun subscribe(){}

    override fun onDestroy() {
        super.onDestroy()
        clearAllSubscribe()
    }

    protected fun clearAllSubscribe() {
        disposableList.forEach { it.dispose() }
    }

    open fun onClick(v: View) {
        when (v.id) {
            R.id.btn_un_subscribe -> {
                clearAllSubscribe()
                subscribe()
            }
        }
    }
}