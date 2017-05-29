import io.reactivex.rxkotlin.observable.*
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
}