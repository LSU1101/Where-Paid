package com.wherepaid.app

import android.app.Application
import android.content.Context
import com.wherepaid.app.core.data.createTransactionRepository
import com.wherepaid.app.core.model.LocationProvider
import com.wherepaid.app.core.model.PaymentSource
import com.wherepaid.app.core.model.PlaceResolver
import com.wherepaid.app.core.model.TransactionRepository
import com.wherepaid.app.core.model.fake.FakeLocationProvider
import com.wherepaid.app.core.model.fake.FakePaymentSource
import com.wherepaid.app.core.model.fake.FakePlaceResolver
import com.wherepaid.app.core.model.fake.FakeTransactionRepository

class WherePaidApp : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}

val Context.appContainer: AppContainer get() = (applicationContext as WherePaidApp).container

/**
 * 수동 의존성 주입. 인터페이스마다 어떤 구현을 쓸지 여기 한 곳에서 정한다.
 * 실제 구현이 생기면 해당 줄의 가짜 구현을 바꿔 끼운다.
 */
class AppContainer(context: Context) {
    /** local.properties의 wherepaid.useFakeData (기본 true). */
    val usesFakeData: Boolean = BuildConfig.USE_FAKE_DATA

    val transactionRepository: TransactionRepository by lazy {
        if (usesFakeData) FakeTransactionRepository() else createTransactionRepository(context)
    }

    /** 첫 실행 온보딩을 끝냈는지. 기기에만 저장하는 작은 설정값이라 SharedPreferences로 둔다. */
    val onboardingStore = OnboardingStore(context)

    // TODO(①): :core:notification 구현으로 교체
    val paymentSource: PaymentSource by lazy { FakePaymentSource() }

    // TODO(②): :core:location 구현으로 교체
    val locationProvider: LocationProvider by lazy { FakeLocationProvider() }

    // TODO(②): :core:place 구현으로 교체
    val placeResolver: PlaceResolver by lazy { FakePlaceResolver() }

    // TODO(④): paymentSource → locationProvider → transactionRepository → placeResolver를 잇는
    //  파이프라인은 아직 없다 (인계 문서 §6 데이터 흐름).
}
