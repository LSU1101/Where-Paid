package com.wherepaid.app.core.notification

// ① 알림 수집·파싱 모듈. 아직 구현이 없다.
//
// 만들 것: com.wherepaid.app.core.model.PaymentSource 구현
//   - NotificationListenerService: 토스(viva.republica.toss) 알림만, 진행 중(isOngoing)·그룹 요약 알림 제외
//   - 파서: 결제/취소/실패로 판별된 알림만 ParsedPayment로 내보낸다. 규칙은 파일 하나에 모은다
//   - 알림 원문(title, text, extras)은 저장하지도 로그에 남기지도 않는다
//
// 가져올 코드: 검증 앱의 parse/TossNotificationParser.kt(+ 테스트), capture/PaymentNotificationListener.kt
// 자세한 내용: 인계 문서 §3, §5 (README.md의 링크)
//
// 구현이 생기면 :app의 AppContainer에서 FakePaymentSource를 바꿔 끼운다.
