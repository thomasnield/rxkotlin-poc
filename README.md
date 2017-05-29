# RxKotlin POC

![](https://www.javacodegeeks.com/wp-content/uploads/2016/02/rx_logo_512.png)

This is an attempt to implement Rx in pure Kotlin using Kotlin coroutines to handle concurrency and backpressure. There are only a handful of operators at the moment, and a couple loose ends need to be worked out like disposal. 

But so far, it is clear there is an opportunity to create an Rx implementation in Kotlin with little effort. 

**Recommended reading to help contribute:** 

[Coroutines Guide](https://github.com/Kotlin/kotlinx.coroutines/blob/master/coroutines-guide.md)

[Coroutines and Reactive Streams](https://github.com/Kotlin/kotlinx.coroutines/blob/master/reactive/coroutines-guide-reactive.md)

**Examples**

```kotlin
@Test
fun testJust() {
    Observable.just("Alpha", "Beta", "Gamma", "Delta")
            .map { it.length }
            .filter { it >= 5 }
            .subscribe { println(it) }
}

@Test
fun testSubscribeOn() = runBlocking {
    Observable.just("Alpha", "Beta", "Gamma", "Delta")
            .subscribeOn(CommonPool)
            .subscribe {
                println("$it ${Thread.currentThread().name}")
            }
}

@Test
fun testObserveOn() = runBlocking {
    Observable.just("Alpha", "Beta", "Gamma", "Delta")
            .subscribeOn(CommonPool)
            .doOnNext { println("$it ${Thread.currentThread().name}") }
            .map { it.length }
            .observeOn(CommonPool)
            .subscribe {
                println("$it ${Thread.currentThread().name}")
            }
}
```

