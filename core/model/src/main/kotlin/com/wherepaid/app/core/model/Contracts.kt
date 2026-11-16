package com.wherepaid.app.core.model

import kotlinx.coroutines.flow.Flow

/**
 * 모듈 간 약속. 화면(feature)은 이 인터페이스만 알고, 구현은 :app의 AppContainer가 골라 넣는다.
 * 시그니처를 바꾸면 모든 모듈이 영향을 받으므로 PR에서 팀 전체 확인을 받는다.
 */

/** ① 알림 수집·파싱 (:core:notification). 결제·취소·실패로 판별된 알림만 내보낸다. */
interface PaymentSource {
    val payments: Flow<ParsedPayment>
}

/** ② 위치 (:core:location). 호출할 때마다 새로 1회 측정한다. 캐시 위치(lastKnown)는 쓰지 않는다. */
interface LocationProvider {
    suspend fun capture(): LocationResult
}

/** ② 장소 매칭 (:core:place). 못 찾았거나 온라인 결제면 null. */
interface PlaceResolver {
    suspend fun resolve(merchant: String, near: LatLng?): Place?
}

/**
 * ④ 저장 (:core:data). 기기 DB가 원본이다.
 * 원격 저장 방식(Firebase / 드라이브)은 미정이며 이 인터페이스 뒤에 숨긴다.
 */
interface TransactionRepository {
    /** 최근 결제가 먼저 오도록 정렬된 전체 거래. */
    fun observeAll(): Flow<List<Transaction>>

    suspend fun get(id: String): Transaction?

    /** 같은 id가 있으면 덮어쓴다. */
    suspend fun upsert(transaction: Transaction)

    suspend fun delete(id: String)

    suspend fun getPlace(id: String): Place?

    suspend fun upsertPlace(place: Place)

    /** 원격 저장소와 동기화. 저장 방식이 정해질 때까지는 아무 일도 하지 않는다. */
    suspend fun sync()
}
