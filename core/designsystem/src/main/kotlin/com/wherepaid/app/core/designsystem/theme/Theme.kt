package com.wherepaid.app.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

/** 시안에 반복해서 나오는 모서리 둥글기. */
@Immutable
object WherePaidShapes {
    /** 칩 버튼, 작은 배지, 작은 버튼(높이 40), 안내 띠 */
    val small = RoundedCornerShape(10.dp)
    /** 중간 버튼(높이 48) */
    val medium = RoundedCornerShape(12.dp)
    /** 큰 버튼(높이 54) */
    val large = RoundedCornerShape(14.dp)
    /** 목록 앞 아이콘 칸 (40dp) */
    val tile = RoundedCornerShape(14.dp)
    /** 온보딩 권한 카드 */
    val card = RoundedCornerShape(16.dp)
    /** 지도 위 떠 있는 카드 (상단 총액) */
    val floatingCard = RoundedCornerShape(20.dp)
    /** 다이얼로그, 하단 시트 위쪽 */
    val dialog = RoundedCornerShape(24.dp)
    val sheet = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
}

private val LocalColors = staticCompositionLocalOf { WherePaidColors() }
private val LocalTypography = staticCompositionLocalOf { WherePaidTypography() }

/** 앱 전체 테마. 화면에서는 [WherePaidTheme.colors], [WherePaidTheme.typography]로 꺼내 쓴다. */
@Composable
fun WherePaidTheme(content: @Composable () -> Unit) {
    val colors = WherePaidColors()
    val typography = WherePaidTypography()
    // Material 3 컴포넌트(시트, 다이얼로그, 텍스트 기본값)도 시안 색과 글꼴을 따르도록 맞춘다.
    val scheme = lightColorScheme(
        primary = colors.primary,
        onPrimary = colors.onPrimary,
        background = colors.background,
        onBackground = colors.textPrimary,
        surface = colors.background,
        onSurface = colors.textPrimary,
        onSurfaceVariant = colors.textSecondary,
        surfaceContainerLow = colors.background,
        outline = colors.border,
        outlineVariant = colors.border,
        error = colors.danger,
        scrim = colors.scrim,
    )
    val materialTypography = Typography(
        bodyLarge = typography.body15,
        bodyMedium = typography.caption13,
        bodySmall = typography.caption12,
        labelLarge = typography.label14,
        titleLarge = typography.title20,
        titleMedium = typography.title18,
    )
    CompositionLocalProvider(LocalColors provides colors, LocalTypography provides typography) {
        MaterialTheme(colorScheme = scheme, typography = materialTypography, content = content)
    }
}

object WherePaidTheme {
    val colors: WherePaidColors
        @Composable @ReadOnlyComposable get() = LocalColors.current

    val typography: WherePaidTypography
        @Composable @ReadOnlyComposable get() = LocalTypography.current
}
