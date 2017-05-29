import io.reactivex.rxkotlin.observable.Observable
import io.reactivex.rxkotlin.observable.just
import io.reactivex.rxkotlin.observable.map
import io.reactivex.rxkotlin.observable.subscribeOn
import io.reactivex.rxkotlin.subscribe
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test

class ObservableTest {
    @Test
    fun testJust() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta").run {
            subscribe { println(it)}
            map { it.length }.subscribe { println(it) }
        }
    }

    @Test
    fun testSubscribeOn() = runBlocking {
        Observable.just("Alpha", "Beta", "Gamma", "Delta")
                .subscribeOn(CommonPool)
                .subscribe {
                    println("$it ${Thread.currentThread().name}")
                }
    }
}