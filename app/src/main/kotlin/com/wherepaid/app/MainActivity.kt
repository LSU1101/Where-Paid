package com.wherepaid.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wherepaid.app.core.designsystem.component.BottomTab
import com.wherepaid.app.core.designsystem.component.WherePaidBottomBar
import com.wherepaid.app.core.designsystem.component.WherePaidIcons
import com.wherepaid.app.core.designsystem.theme.WherePaidTheme
import com.wherepaid.app.feature.map.MapScreen
import com.wherepaid.app.feature.map.candidate.CandidatePickerRoute
import com.wherepaid.app.feature.onboarding.OnboardingScreen
import com.wherepaid.app.feature.placecheck.PlaceCheckRoute
import com.wherepaid.app.feature.report.ReportScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WherePaidTheme {
                WherePaidNav(appContainer)
            }
        }
    }
}

/** 화면 경로. 하위 화면(S-06 매장 찾기, S-07 온라인 결제, S-09 공유)은 화면을 만들 때 여기에 추가한다. */
private object Routes {
    const val ONBOARDING = "onboarding"
    const val MAP = "map"
    const val PLACE_CHECK = "place-check"
    const val REPORT = "report"
}

/** 하단 탭 (피그마 시안: 지도 / 장소 확인 / 리포트). */
private enum class TopLevelDestination(val route: String, val tab: BottomTab) {
    MAP(Routes.MAP, BottomTab("지도", WherePaidIcons.tabMap)),
    PLACE_CHECK(Routes.PLACE_CHECK, BottomTab("장소 확인", WherePaidIcons.tabPlace)),
    REPORT(Routes.REPORT, BottomTab("리포트", WherePaidIcons.tabReport)),
}

@Composable
private fun WherePaidNav(container: AppContainer) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val destination = backStackEntry?.destination
    val selectedTab = TopLevelDestination.entries.indexOfFirst { top ->
        destination?.hierarchy?.any { it.route == top.route } == true
    }
    val startDestination = remember { if (container.onboardingStore.isDone) Routes.MAP else Routes.ONBOARDING }
    // S-04 후보 고르기 시트는 지도와 장소 확인 어디서든 열리므로 화면 경로가 아니라 여기서 띄운다.
    var candidateSheetFor by rememberSaveable { mutableStateOf<String?>(null) }

    Scaffold(
        // 화면이 상태 바 영역을 직접 다룬다 (지도는 상태 바 밑까지 그린다).
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (selectedTab >= 0) {
                WherePaidBottomBar(
                    tabs = TopLevelDestination.entries.map { it.tab },
                    selectedIndex = selectedTab,
                    onSelect = { navController.navigateToTab(TopLevelDestination.entries[it].route) },
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Routes.ONBOARDING) {
                OnboardingScreen(onStart = {
                    container.onboardingStore.markDone()
                    navController.navigate(Routes.MAP) { popUpTo(Routes.ONBOARDING) { inclusive = true } }
                })
            }
            composable(Routes.MAP) { MapScreen() }
            composable(Routes.PLACE_CHECK) {
                val context = LocalContext.current
                PlaceCheckRoute(
                    repository = container.transactionRepository,
                    isOnline = container.networkMonitor.isOnline,
                    onChooseCandidate = { candidateSheetFor = it },
                    // TODO(③): S-06 매장 찾기 화면을 만들면 그 경로로 보낸다.
                    onFindPlace = { Toast.makeText(context, "매장 찾기 화면은 준비 중이에요", Toast.LENGTH_SHORT).show() },
                    onOpenMap = { navController.navigateToTab(Routes.MAP) },
                    modifier = Modifier.statusBarsPadding(),
                )
            }
            composable(Routes.REPORT) { ReportScreen(Modifier.statusBarsPadding()) }
        }
    }

    candidateSheetFor?.let { id ->
        CandidatePickerRoute(
            transactionId = id,
            repository = container.transactionRepository,
            onDismiss = { candidateSheetFor = null },
        )
    }
}

/**
 * 탭을 오갈 때 백스택이 쌓이지 않게 하고 각 탭의 상태를 유지한다.
 * 그래프의 시작 화면은 첫 실행 때 온보딩이라, 항상 지도 홈을 기준으로 정리한다.
 */
private fun NavHostController.navigateToTab(route: String) = navigate(route) {
    popUpTo(Routes.MAP) { saveState = true }
    launchSingleTop = true
    restoreState = true
}
