package com.wherepaid.app.core.model.fake

import com.wherepaid.app.core.model.LatLng
import com.wherepaid.app.core.model.PaymentKind
import com.wherepaid.app.core.model.Place
import com.wherepaid.app.core.model.Transaction

/**
 * 화면 개발과 테스트용 가짜 데이터. 가맹점과 장소는 모두 지어낸 것이다.
 * 실제 결제 기록이나 팀원의 위치를 여기에 넣지 않는다.
 */
object SampleData {
    /** 서울시청 근처. 가짜 좌표의 기준점. */
    val CITY_HALL = LatLng(37.5665, 126.9780)

    /** 2026-11-02 12:30 KST. 가짜 결제 시각의 기준점. */
    private const val BASE_TIME = 1_793_590_200_000L
    private const val HOUR = 3_600_000L

    val places: List<Place> = listOf(
        Place("fake-place-1", "샘플편의점 시청점", LatLng(37.5668, 126.9784), "편의점", "서울 중구 샘플로 1"),
        Place("fake-place-2", "샘플분식", LatLng(37.5659, 126.9771), "음식점 > 분식", "서울 중구 샘플로 12"),
        Place("fake-place-3", "샘플커피 광장점", LatLng(37.5672, 126.9768), "카페", "서울 중구 샘플로 27"),
    )

    val transactions: List<Transaction> = listOf(
        transaction("fake-tx-1", 4_500, "샘플커피 광장점", "샘플체크", BASE_TIME, places[2]),
        transaction("fake-tx-2", 8_000, "샘플분식", "샘플체크", BASE_TIME - 3 * HOUR, places[1]),
        transaction("fake-tx-3", 2_300, "샘플편의점 시청점", "샘플카드", BASE_TIME - 26 * HOUR, places[0]),
        // 온라인 결제: 위치도 장소도 없다.
        Transaction(
            id = "fake-tx-4", amount = 14_900, merchantRaw = "샘플페이먼츠(자동결제)", cardName = "샘플카드",
            payType = "일시불", kind = PaymentKind.PAYMENT, paidAt = BASE_TIME - 50 * HOUR,
            location = null, locationAccuracyM = null, placeId = null, isOnline = true, isUserEdited = false,
        ),
        // 취소
        transaction("fake-tx-5", 8_000, "샘플분식", "샘플체크", BASE_TIME - 2 * HOUR, places[1], PaymentKind.CANCEL),
    )

    private fun transaction(
        id: String,
        amount: Long,
        merchant: String,
        card: String,
        paidAt: Long,
        place: Place,
        kind: PaymentKind = PaymentKind.PAYMENT,
    ) = Transaction(
        id = id, amount = amount, merchantRaw = merchant, cardName = card, payType = "일시불",
        kind = kind, paidAt = paidAt, location = place.latLng, locationAccuracyM = 12.5f,
        placeId = place.id, isOnline = false, isUserEdited = false,
    )
}
