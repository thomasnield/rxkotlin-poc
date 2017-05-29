package io.reactivex.rxkotlin.subscriber

import io.reactivex.rxkotlin.observable.Observable
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

fun <T> Observable<T>.subscribe(onComplete: (() -> Unit)? = null,
                                onError: ((Throwable) -> Unit)? = null,
                                onNext: ((T) -> Unit)? = null) = subscribe(
        object: Subscriber<T> {
            override fun onNext(t: T) = onNext?.invoke(t)?:Unit

            override fun onComplete() = onComplete?.invoke()?:Unit

            override fun onError(t: Throwable) = onError?.invoke(t)?:Unit

            override fun onSubscribe(s: Subscription?) {
            }
        }
)