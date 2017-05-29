package io.reactivex.rxkotlin.observable

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import kotlin.coroutines.experimental.CoroutineContext

inline fun <T,R> Observable<T>.map(crossinline mapper: (T) -> R): Observable<R> = object: Observable<R> {

    private val upstream: Observable<T> = this@map

    override fun subscribe(subscriber: Subscriber<in R>) {
        upstream.subscribe(object: Subscriber<T> {
            override fun onComplete() = subscriber.onComplete()

            override fun onNext(t: T) = try {
                subscriber.onNext(mapper(t))
            } catch (e: Throwable) {
                onError(e)
            }

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

            override fun onNext(t: T) = try {
                if (predicate(t)) subscriber.onNext(t) else Unit
            } catch (e: Throwable) {
                onError(e)
            }

            override fun onError(t: Throwable) = subscriber.onError(t)

            override fun onSubscribe(s: Subscription) = subscriber.onSubscribe(s)
        })
    }
}

fun <T> Observable<T>.subscribeOn(context: CoroutineContext): Observable<T> = object: Observable<T> {

    private val upstream: Observable<T> = this@subscribeOn

    override fun subscribe(subscriber: Subscriber<in T>) {
        var job: Job? = null
        job = launch(context) {

            upstream.subscribe(object : Subscriber<T> {
                override fun onComplete() = subscriber.onComplete()

                override fun onNext(t: T) = try {
                    subscriber.onNext(t)
                } catch (e: Throwable) {
                    onError(e)
                }

                override fun onError(t: Throwable) = subscriber.onError(t)

                override fun onSubscribe(s: Subscription) {

                    val upstreamAndDownstreamSubscription = object: Subscription {
                        override fun cancel() {
                            s.cancel()
                            job?.cancel()
                        }

                        override fun request(n: Long) {
                            s.request(n) }

                    }
                    subscriber.onSubscribe(upstreamAndDownstreamSubscription)
                }
            })
        }
    }
}

fun <T> Observable<T>.observeOn(context: CoroutineContext): Observable<T> = object: Observable<T> {

    private val upstream: Observable<T> = this@observeOn

    override fun subscribe(subscriber: Subscriber<in T>) {

        upstream.subscribe(object : Subscriber<T> {
            var job: Job? = null
            override fun onComplete() {
                job = launch(context) {
                    subscriber.onComplete()
                }
            }

            override fun onNext(t: T) {
                try {
                    job = launch(context) {
                        subscriber.onNext(t)
                    }
                } catch (e: Throwable) {
                    onError(e)
                }
            }

            override fun onError(t: Throwable) {
                launch(context) {
                    subscriber.onError(t)
                }
            }

            override fun onSubscribe(s: Subscription) {

                val upstreamAndDownstreamSubscription = object: Subscription {
                    override fun cancel() {
                        s.cancel()
                        job?.cancel()
                    }

                    override fun request(n: Long) {
                        s.request(n) }

                }
                subscriber.onSubscribe(upstreamAndDownstreamSubscription)
            }
        })
    }
}

inline fun <T> Observable<T>.doOnNext(crossinline onNext: (T) -> Unit) = object: Observable<T> {
    private val upstream: Observable<T> = this@doOnNext

    override fun subscribe(subscriber: Subscriber<in T>) {

        upstream.subscribe(object : Subscriber<T> {
            override fun onComplete() = subscriber.onComplete()

            override fun onNext(t: T) {
                try {
                    onNext(t)
                    subscriber.onNext(t)
                } catch (e: Throwable) {
                    onError(e)
                }
            }

            override fun onError(t: Throwable) = subscriber.onError(t)

            override fun onSubscribe(s: Subscription) = subscriber.onSubscribe(s)
        })
    }
}


fun <T> Observable<T>.take(takeCount: Int) = object: Observable<T> {

    override fun subscribe(subscriber: Subscriber<in T>) {

        this@take.subscribe(object : Subscriber<T> {

            var count = 0

            override fun onComplete() = subscriber.onComplete()

            override fun onNext(t: T) {
                try {
                    count++
                    if (count <= takeCount) {
                        subscriber.onNext(t)
                    } else {
                        //not sure how to dispose
                    }

                } catch (e: Throwable) {
                    onError(e)
                }
            }

            override fun onError(t: Throwable) = subscriber.onError(t)

            override fun onSubscribe(s: Subscription) = subscriber.onSubscribe(s)
        })
    }
}