pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "WherePaid"

include(":app")

// 모듈 간 약속: 모델, 인터페이스, 가짜 구현
include(":core:model")

// 구현 모듈 (담당은 CLAUDE.md 참고)
include(":core:notification")
include(":core:location")
include(":core:place")
include(":core:data")

// 화면
include(":feature:map")
include(":feature:history")
include(":feature:settings")
