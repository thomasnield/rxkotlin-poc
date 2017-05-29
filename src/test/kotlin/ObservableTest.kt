import io.reactivex.rxkotlin.observable.Observable
import io.reactivex.rxkotlin.observable.just
import io.reactivex.rxkotlin.subscribe
import org.junit.Test

class ObservableTest {
    @Test
    fun testJust() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta")
                .subscribe { println(it)}

    }
}