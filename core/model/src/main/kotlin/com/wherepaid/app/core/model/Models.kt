package com.wherepaid.app.core.model

/**
 * 모듈 사이를 오가는 모델. 인계 문서 §6 초안이며 1주차 회의에서 확정한다.
 * 필드를 바꾸면 모든 모듈이 영향을 받으므로 PR에서 팀 전체 확인을 받는다.
 *
 * 알림 원문(title, text 등)은 어떤 모델에도 넣지 않는다. 파싱 결과만 다룬다.
 */

/** WGS84 좌표. */
data class LatLng(val latitude: Double, val longitude: Double)

/** 저장 대상인 알림 종류. 포인트 적립이나 기타 알림은 [PaymentSource]가 내보내지 않는다. */
enum class PaymentKind { PAYMENT, CANCEL, FAILED }

/** 토스 알림 1건을 파싱한 결과. */
data class ParsedPayment(
    val kind: PaymentKind,
    /** 원 단위 금액. */
    val amount: Long,
    val cardName: String?,
    /** 알림에 적힌 가맹점명 그대로. */
    val merchant: String?,
    /** "일시불", "할부 3개월" 등. */
    val payType: String?,
    /** 알림을 받은 시각 (epoch millis). */
    val receivedAt: Long,
)

/** 위치 1회 측정 결과. */
sealed interface LocationResult {
    data class Success(val latLng: LatLng, val accuracyM: Float?) : LocationResult
    data class Failure(val reason: LocationFailure) : LocationResult
}

enum class LocationFailure { NO_PERMISSION, LOCATION_DISABLED, TIMEOUT, UNKNOWN }

/** 카카오 로컬 API로 찾은 장소. */
data class Place(
    /** 카카오 장소 ID. */
    val id: String,
    val name: String,
    val latLng: LatLng,
    val category: String?,
    val address: String?,
)

/** 저장되는 거래 1건. */
data class Transaction(
    val id: String,
    /** 원 단위 금액. */
    val amount: Long,
    /** 알림에 적힌 가맹점명 그대로. */
    val merchantRaw: String?,
    val cardName: String?,
    val payType: String?,
    val kind: PaymentKind,
    /** 결제 시각 (epoch millis). */
    val paidAt: Long,
    /** 알림을 받은 순간 측정한 위치. 측정에 실패했으면 null. */
    val location: LatLng?,
    val locationAccuracyM: Float?,
    /** 연결된 [Place.id]. 아직 매칭 전이거나 온라인 결제면 null. */
    val placeId: String?,
    val isOnline: Boolean,
    /** 사용자가 장소나 내용을 직접 고쳤는지. 고친 값은 자동 매칭이 덮어쓰지 않는다. */
    val isUserEdited: Boolean,
)
