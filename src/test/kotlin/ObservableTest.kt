import io.reactivex.rxkotlin.observable.*
import io.reactivex.rxkotlin.subscriber.subscribe
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.EmptyCoroutineContext

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
                .subscribeOn(EmptyCoroutineContext)
                .subscribe {
                    println("$it ${Thread.currentThread().name}")
                }
    }

    @Test
    fun testObserveOn() = runBlocking {
        Observable.just("Alpha", "Beta", "Gamma", "Delta")
                .subscribeOn(EmptyCoroutineContext)
                .doOnNext { println("$it ${Thread.currentThread().name}") }
                .map { it.length }
                .observeOn(EmptyCoroutineContext)
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
}