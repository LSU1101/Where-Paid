package com.wherepaid.app.core.location

// ② 위치 모듈. 아직 구현이 없다.
//
// 만들 것: com.wherepaid.app.core.model.LocationProvider 구현
//   - FusedLocationProviderClient.getCurrentLocation: 고정확도, 새로 측정(maxUpdateAge = 0), 제한 10~15초
//   - 캐시 위치(lastKnown)는 쓰지 않는다. 검증에서 최대 2.2km 어긋났다 (인계 문서 §2)
//   - 권한 상태 확인과 설정 화면 이동 도우미
//
// 가져올 코드: 검증 앱의 capture/LocationCapturer.kt, util/Permissions.kt, util/Geo.kt
//
// 구현이 생기면 :app의 AppContainer에서 FakeLocationProvider를 바꿔 끼운다.
