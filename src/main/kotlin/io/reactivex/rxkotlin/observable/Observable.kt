package io.reactivex.rxkotlin.observable

import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber

interface Observable<T>: Publisher<T> {
    override fun subscribe(subscriber: Subscriber<in T>)
    companion object Factory
}

