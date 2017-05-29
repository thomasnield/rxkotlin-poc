package io.reactivex.rxkotlin.observable

import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

inline fun <T,R> Observable<T>.map(crossinline mapper: (T) -> R): Observable<R> = object: Observable<R> {

    private val upstream: Observable<T> = this@map

    override fun subscribe(subscriber: Subscriber<in R>) {
        upstream.subscribe(object: Subscriber<T> {
            override fun onComplete() = subscriber.onComplete()

            override fun onNext(t: T) = subscriber.onNext(mapper(t))

            override fun onError(t: Throwable) = subscriber.onError(t)

            override fun onSubscribe(s: Subscription) = subscriber.onSubscribe(s)
        })
    }
}

inline fun <T> Observable<T>.filter(crossinline predicate: (T) -> Boolean): Observable<T> = object: Observable<T> {

    private val upstream: Observable<T> = this@filter

    override fun subscribe(subscriber: Subscriber<in T>) {
        upstream.subscribe(object: Subscriber<T> {
            override fun onComplete() = subscriber.onComplete()

            override fun onNext(t: T) = if (predicate(t)) subscriber.onNext(t) else Unit

            override fun onError(t: Throwable) = subscriber.onError(t)

            override fun onSubscribe(s: Subscription) = subscriber.onSubscribe(s)
        })
    }
}