# RxKotlin POC

**NOTE: This project is at the moment abandoned** 

This is an attempt to implement Rx in pure Kotlin using Kotlin coroutines to handle concurrency and backpressure. There are only a handful of operators at the moment, and a couple loose ends need to be worked out like disposal. 

But so far, it is clear there is an opportunity to create an Rx implementation in Kotlin with less effort than Java. 

## Jake Wharton's POC

Be sure to check out Jake Wharton's Reagent POC, which is seeking to be multiplatform and a more thought-out type hierarchy. 

https://github.com/JakeWharton/Reagent/

## Recommended reading to help contribute

[Coroutines Guide](https://github.com/Kotlin/kotlinx.coroutines/blob/master/coroutines-guide.md)

[Coroutines and Reactive Streams](https://github.com/Kotlin/kotlinx.coroutines/blob/master/reactive/coroutines-guide-reactive.md)

## Examples

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

@Test
fun testObservableDefer() {
    val state = AtomicBoolean(true)

    val source = Observable.defer { Observable.just(state.getAndSet(false)) }

    source.subscribe { println("Sub 1: $it")}
    source.subscribe { println("Sub 2: $it")}
}

@Test
fun testRangeAndTake() {
    Observable.range(1,10)
            .take(5)
            .subscribe(onNext=::println)
}

@Test
fun testInterval() = runBlocking {

    Observable.interval(1, TimeUnit.SECONDS)
            .subscribe(onNext=::println)

    delay(5000)
}
```

