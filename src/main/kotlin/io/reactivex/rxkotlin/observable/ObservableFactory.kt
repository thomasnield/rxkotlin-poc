package io.reactivex.rxkotlin.observable

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.reactivestreams.Subscriber
import java.util.concurrent.TimeUnit
import kotlin.coroutines.experimental.CoroutineContext

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

inline fun <T> Observable.Factory.defer(crossinline observable: () -> Observable<T>) = object: Observable<T> {
    override fun subscribe(subscriber: Subscriber<in T>) {
        val newObservable = observable()
        newObservable.subscribe(subscriber)
    }
}


fun Observable.Factory.range(rangeStart: Int, rangeEnd: Int) = object: Observable<Int> {
    override fun subscribe(subscriber: Subscriber<in Int>) {
        try {
            (rangeStart..rangeEnd)
                    .forEach(subscriber::onNext)
            subscriber.onComplete()
        } catch (e: Exception) {
            subscriber.onError(e)
        }
    }
}

fun Observable.Factory.interval(time: Long, timeUnit: TimeUnit, context: CoroutineContext = CommonPool) = object: Observable<Int> {
    override fun subscribe(subscriber: Subscriber<in Int>) {
        try {
            launch(context) {
                repeat(Int.MAX_VALUE) {
                    delay(time, timeUnit)
                    subscriber.onNext(it)
                }
            }
        } catch (e: Exception) {
            subscriber.onError(e)
        }
    }
}

