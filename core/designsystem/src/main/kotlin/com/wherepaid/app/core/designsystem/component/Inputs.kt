package com.wherepaid.app.core.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wherepaid.app.core.designsystem.theme.WherePaidShapes
import com.wherepaid.app.core.designsystem.theme.WherePaidTheme

/** 검색 입력칸 (S-06): 주황 테두리, ⌕ 기호, 지우기 ✕. */
@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "매장 이름으로 찾기",
) {
    val colors = WherePaidTheme.colors
    val type = WherePaidTheme.typography
    Row(
        modifier
            .fillMaxWidth()
            .clip(WherePaidShapes.medium)
            .border(1.5.dp, colors.primary, WherePaidShapes.medium)
            .padding(horizontal = 14.dp, vertical = 13.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("⌕", style = type.button16.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold), color = colors.textSecondary)
        Box(Modifier.weight(1f)) {
            if (value.isEmpty()) Text(placeholder, style = type.body15Medium, color = colors.textSecondary)
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = type.body15Medium.copy(color = colors.textPrimary),
                cursorBrush = SolidColor(colors.primary),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        if (value.isNotEmpty()) {
            Text(
                "✕",
                style = type.label13,
                color = colors.textSecondary,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(role = Role.Button) { onValueChange("") }
                    .semantics { contentDescription = "검색어 지우기" },
            )
        }
    }
}

/** 라디오 표시 (22dp). 선택: 주황 굵은 링에 흰 가운데. 미선택: 회색 가는 링. */
@Composable
fun RadioMark(selected: Boolean, modifier: Modifier = Modifier) {
    val colors = WherePaidTheme.colors
    Canvas(modifier.size(22.dp)) {
        val unit = size.minDimension / 22f
        if (selected) {
            drawCircle(colors.background, radius = 8 * unit)
            drawCircle(colors.primary, radius = 8 * unit, style = Stroke(width = 6 * unit))
        } else {
            drawCircle(colors.border, radius = 10 * unit, style = Stroke(width = 2 * unit))
        }
    }
}

/** 로딩 중 회색 막대. */
@Composable
fun SkeletonBar(width: Dp, height: Dp, modifier: Modifier = Modifier) {
    Box(
        modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(height / 2))
            .background(WherePaidTheme.colors.surfaceMuted),
    )
}

/** 로딩 중 목록 한 줄 (S-06 61:2): 원 28 + 막대 두 개. */
@Composable
fun SkeletonRow(titleWidth: Dp, subtitleWidth: Dp, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth().padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(28.dp).clip(CircleShape).background(WherePaidTheme.colors.surfaceMuted))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SkeletonBar(titleWidth, 14.dp)
            SkeletonBar(subtitleWidth, 10.dp)
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun InputsPreview() {
    WherePaidTheme {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SearchField("KFC", {}, {})
            SearchField("", {}, {})
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                RadioMark(selected = true)
                RadioMark(selected = false)
            }
            SkeletonRow(150.dp, 110.dp)
        }
    }
}
