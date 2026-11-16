# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

"어디서돈썼지도"는 토스 결제 알림을 받은 순간의 GPS 위치로 결제 장소를 지도에 남기는 안드로이드 앱이다 (팀 4명, 시연용 직접 설치, Play 스토어 출시 안 함).
결정 사항과 검증 결과의 근거는 검증 앱 저장소의 인계 문서에 있다: https://github.com/LSU1101/MobileProgramming/blob/main/docs/HANDOFF.md

PR 리뷰어(Claude Review 워크플로)도 이 파일을 기준으로 리뷰한다. 아래 "팀 규칙"을 어긴 변경은 지적한다.

## 명령

```bash
./gradlew build                      # 전 모듈 컴파일 + 단위 테스트 + lint (PR 전에 통과시킨다)
./gradlew testDebugUnitTest :core:model:test   # 단위 테스트만 (:core:model은 순수 Kotlin이라 태스크가 test)
./gradlew :core:model:test --tests "com.wherepaid.app.core.model.fake.FakesTest"   # 테스트 클래스 하나
./gradlew :core:data:testDebugUnitTest --tests "*.SomeTest.someMethod"            # Android 모듈의 테스트 하나
./gradlew :app:lintDebug             # lint만
./gradlew installDebug               # 연결된 기기/에뮬레이터에 설치
adb shell am start -n com.wherepaid.app/.MainActivity
```

Android SDK 위치는 `ANDROID_HOME` 또는 `local.properties`의 `sdk.dir`로 잡는다. 클라우드 세션에서는 `dl.google.com`, `maven.google.com`이 허용돼야 빌드된다.

## 구조

```
:app                 Application, AppContainer(수동 DI), 하단 탭 네비게이션
:core:model          순수 Kotlin. 모델 + 인터페이스 4개(Contracts.kt) + 가짜 구현(fake/)
:core:notification   ① PaymentSource 구현 자리 (알림 수집·파싱)
:core:location       ② LocationProvider 구현 자리 (GPS 1회 측정)
:core:place          ② PlaceResolver 구현 자리 (카카오 로컬 API)
:core:data           ④ Room + TransactionRepository 구현
:feature:map|history|settings   ③ 화면 (Compose)
```

데이터 흐름 (목표. 파이프라인은 아직 없다):
토스 알림 → 필터(진행 중·묶음 제외) → 파싱 → 결제/취소/실패가 아니면 종료 → GPS 1회 측정 → Room에 저장 → 가맹점명 + 좌표로 장소 검색(결제대행사면 "온라인") → 원격 동기화 → 화면

- 모듈 사이의 약속은 `core/model`의 `Contracts.kt`(인터페이스)와 `Models.kt`(모델)가 전부다. 화면은 이 인터페이스만 받고, 어떤 구현을 넣을지는 `app`의 `AppContainer` 한 곳에서 정한다. 구현이 없는 인터페이스에는 `core/model`의 `fake/` 구현이 들어가 있다.
- `local.properties`의 `wherepaid.useFakeData`(기본 `true`)가 `FakeTransactionRepository`(메모리 샘플)와 Room 구현을 고른다. CI 빌드는 `false`다.
- 기기 DB(Room)가 원본이다. 원격 저장 방식(Firebase / 구글 드라이브)은 **미정**이며 `TransactionRepository.sync()` 뒤에 숨긴다. 정해지기 전에는 Firebase·Drive 의존성을 추가하지 않는다.
- 비밀 값(서명 키, 카카오 키)은 루트 `build.gradle.kts`가 `local.properties` → 환경변수 순으로 읽어 `rootProject.extra["secrets"]`에 둔다. 새 키가 필요하면 그 맵과 `local.properties.example`, `.github/workflows/android.yml`, README를 같이 고친다. 값이 비어 있어도 빌드는 돼야 한다.
- 패키지: `com.wherepaid.app`(app), `com.wherepaid.app.core.<모듈>`, `com.wherepaid.app.feature.<모듈>`. 버전은 `gradle/libs.versions.toml`에서만 관리한다.

## 팀 규칙

### 모듈 경계
- 의존 방향: `feature:* → core:model` / `core:{notification,location,place,data} → core:model` / `app → 전부`.
- feature끼리, core 구현 모듈끼리는 서로 의존하지 않는다. feature가 `core:data` 같은 구현 모듈을 의존하지 않는다.
- `core:model`에는 Android 의존성을 넣지 않는다.
- Room 타입(Entity, Dao, Database)은 `core:data` 안에서 `internal`로 둔다.
- `Contracts.kt`와 `Models.kt`를 바꾸면 모든 담당이 영향을 받는다. 다른 작업에 섞지 말고 그 변경만 담은 PR로 올려 팀 전체 확인을 받는다.
- 담당: ① `core:notification` / ② `core:location`, `core:place` / ③ `feature:*`, `app`의 네비게이션·테마 / ④ `core:data`, `.github/`, 문서. 남의 모듈을 고쳐야 하면 PR 설명에 적는다.

### 개인정보와 데이터
- **알림 원문을 저장하지 않는다.** 알림의 title, text, extras는 파싱에만 쓰고 DB, 파일, 로그(`Log.*`), 원격 어디에도 남기지 않는다. 저장하는 것은 파싱 결과(`Transaction`)뿐이다. 모델과 Entity에 원문 필드를 추가하지 않는다.
- 실제 결제 기록, 팀원의 위치, 실제 알림 원문을 테스트·샘플 데이터·문서로 커밋하지 않는다. 파서 테스트 케이스는 가맹점과 금액을 바꾼 것을 쓴다.
- 서명 키(`*.jks`, `*.keystore`), `local.properties`, `google-services.json`, API 키를 커밋하지 않는다.

### 알림과 위치 (검증으로 확인된 것)
- 대상은 토스(`viva.republica.toss`) 알림뿐이다. `sbn.isOngoing`과 `FLAG_GROUP_SUMMARY` 알림은 무시한다.
- **파싱이 먼저다.** 결제·취소·실패로 판별된 알림에서만 위치를 측정한다 (토스 알림의 95%는 결제가 아니다).
- 위치는 매번 새로 측정한다 (`getCurrentLocation`, 고정확도, `maxUpdateAge = 0`, 제한 10~15초). 캐시 위치(`lastLocation`)는 대체값으로도 쓰지 않는다. 검증에서 최대 2.2km 어긋났다.
- 결제 실패는 결제로 세지 않는다. "잔액 N원" 줄을 결제 금액으로 읽지 않는다.
- 파싱 규칙(정규식, 키워드)은 파일 하나에 모으고, 새 알림 형식을 지원할 때는 테스트 케이스를 같이 추가한다.

### PR
- `main`에 직접 푸시하지 않는다. 브랜치 → PR → Claude 리뷰 확인 + 팀원 1명 승인 → 병합.
- PR을 올리기 전에 `./gradlew build`를 통과시킨다.
- PR 하나에는 한 가지 일만 담고, 화면을 바꿨으면 스크린샷을 붙인다.
- Room 구조를 바꾸면 `version`을 올리고 마이그레이션과 `core/data/schemas/`의 새 JSON을 같이 커밋한다.
