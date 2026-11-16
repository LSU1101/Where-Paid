package com.wherepaid.app.core.place

// ② 장소 매칭 모듈. 아직 구현이 없다.
//
// 만들 것: com.wherepaid.app.core.model.PlaceResolver 구현
//   - 카카오 로컬 API 키워드 검색 + 좌표 반경. 키는 BuildConfig.KAKAO_REST_API_KEY
//   - 가맹점명이 결제대행사(네이버페이, 구글페이먼트코리아, "(자동결제)" 등)면 검색하지 않고 null
//     → 호출한 쪽에서 "온라인"으로 처리한다 (인계 문서 §3.3)
//
// 구현이 생기면 :app의 AppContainer에서 FakePlaceResolver를 바꿔 끼운다.

/** 카카오 REST 키가 주입됐는지. 키가 없으면 장소 검색을 건너뛰어야 한다. */
val isKakaoRestApiKeyConfigured: Boolean
    get() = BuildConfig.KAKAO_REST_API_KEY.isNotEmpty()
