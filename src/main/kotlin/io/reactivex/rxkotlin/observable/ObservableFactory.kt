package io.reactivex.rxkotlin.observable

import org.reactivestreams.Subscriber

fun <T> Observable.Factory.just(vararg items: T) = object: Observable<T> {
    override fun subscribe(subscriber: Subscriber<in T>) {

        try {
            items.forEach(subscriber::onNext)
            subscriber.onComplete()
        } catch (e: Exception) {
            subscriber.onError(e)
        }
    }
}

fun <T> Observable.Factory.fromIterable(items: Iterable<T>) = object: Observable<T> {
    override fun subscribe(subscriber: Subscriber<in T>) {
        try {
            items.forEach(subscriber::onNext)
            subscriber.onComplete()
        } catch (e: Exception) {
            subscriber.onError(e)
        }
    }
}

