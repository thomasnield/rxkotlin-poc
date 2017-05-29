import io.reactivex.rxkotlin.observable.Observable
import io.reactivex.rxkotlin.observable.just
import io.reactivex.rxkotlin.subscribe

fun main(args: Array<String>) {
    Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
            .subscribe { println(it) }
}