import io.reactivex.rxkotlin.observable.Observable
import io.reactivex.rxkotlin.observable.*
import io.reactivex.rxkotlin.subscriber.subscribe

fun main(args: Array<String>) {
    Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
            .map { it.length }
            .filter { it >= 5}
            .subscribe { println(it) }
}