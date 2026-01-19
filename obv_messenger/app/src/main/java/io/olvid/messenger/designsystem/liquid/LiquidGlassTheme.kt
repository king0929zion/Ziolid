/*
 *  Olvid for Android
 *  Copyright © 2019-2025 Olvid SAS
 *
 *  This file is part of Olvid for Android.
 *
 *  Olvid is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License, version 3,
 *  as published by the Free Software Foundation.
 *
 *  Olvid is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with Olvid.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.olvid.messenger.designsystem.liquid

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * iOS 26 Liquid Glass theme colors and specifications.
 */
@Immutable
data class LiquidGlassColors(
    // Primary iMessage colors
    val sentBubble: Color,
    val receivedBubble: Color,
    val sentText: Color,
    val receivedText: Color,

    // Glass surface colors
    val glassSurface: Color,
    val glassSurfaceVariant: Color,
    val glassOverlay: Color,

    // Accent colors
    val accent: Color,
    val accentVariant: Color,
    val onAccent: Color,

    // Background colors
    val background: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,

    // Text colors
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,

    // Status colors
    val success: Color,
    val warning: Color,
    val error: Color,

    // Navigation colors
    val tabBarBackground: Color,
    val tabBarSelected: Color,
    val tabBarUnselected: Color,

    // Separator
    val separator: Color,

    // Input field
    val inputBackground: Color,
    val inputBorder: Color,
)

@Immutable
data class LiquidGlassShapes(
    val bubbleCornerRadius: Dp = 18.dp,
    val bubbleTailRadius: Dp = 4.dp,
    val cardCornerRadius: Dp = 16.dp,
    val buttonCornerRadius: Dp = 12.dp,
    val inputCornerRadius: Dp = 20.dp,
    val dialogCornerRadius: Dp = 24.dp,
    val avatarSize: Dp = 56.dp,
    val smallAvatarSize: Dp = 40.dp,
)

@Immutable
data class LiquidGlassEffects(
    val blurAmount: Dp = 16.dp,
    val lensRadius: Dp = 24.dp,
    val shadowElevation: Dp = 8.dp,
    val glassAlpha: Float = 0.6f,
    val overlayAlpha: Float = 0.1f,
)

val LightLiquidGlassColors = LiquidGlassColors(
    sentBubble = Color(0xFF007AFF),
    receivedBubble = Color(0xFFE5E5EA),
    sentText = Color.White,
    receivedText = Color.Black,

    glassSurface = Color.White.copy(alpha = 0.6f),
    glassSurfaceVariant = Color.White.copy(alpha = 0.4f),
    glassOverlay = Color.White.copy(alpha = 0.1f),

    accent = Color(0xFF007AFF),
    accentVariant = Color(0xFF4DA6FF),
    onAccent = Color.White,

    background = Color(0xFFF2F2F7),
    surfaceContainer = Color.White,
    surfaceContainerHigh = Color(0xFFF5F5F5),

    textPrimary = Color.Black,
    textSecondary = Color(0xFF8E8E93),
    textTertiary = Color(0xFFC7C7CC),

    success = Color(0xFF34C759),
    warning = Color(0xFFFF9500),
    error = Color(0xFFFF3B30),

    tabBarBackground = Color.White.copy(alpha = 0.85f),
    tabBarSelected = Color(0xFF007AFF),
    tabBarUnselected = Color(0xFF8E8E93),

    separator = Color(0xFFC6C6C8),

    inputBackground = Color(0xFFF2F2F7),
    inputBorder = Color(0xFFE5E5EA),
)

val DarkLiquidGlassColors = LiquidGlassColors(
    sentBubble = Color(0xFF0A84FF),
    receivedBubble = Color(0xFF2C2C2E),
    sentText = Color.White,
    receivedText = Color.White,

    glassSurface = Color.Black.copy(alpha = 0.6f),
    glassSurfaceVariant = Color.Black.copy(alpha = 0.4f),
    glassOverlay = Color.White.copy(alpha = 0.05f),

    accent = Color(0xFF0A84FF),
    accentVariant = Color(0xFF409CFF),
    onAccent = Color.White,

    background = Color.Black,
    surfaceContainer = Color(0xFF1C1C1E),
    surfaceContainerHigh = Color(0xFF2C2C2E),

    textPrimary = Color.White,
    textSecondary = Color(0xFF8E8E93),
    textTertiary = Color(0xFF48484A),

    success = Color(0xFF30D158),
    warning = Color(0xFFFF9F0A),
    error = Color(0xFFFF453A),

    tabBarBackground = Color.Black.copy(alpha = 0.85f),
    tabBarSelected = Color(0xFF0A84FF),
    tabBarUnselected = Color(0xFF8E8E93),

    separator = Color(0xFF38383A),

    inputBackground = Color(0xFF1C1C1E),
    inputBorder = Color(0xFF38383A),
)

val LocalLiquidGlassColors = staticCompositionLocalOf { LightLiquidGlassColors }
val LocalLiquidGlassShapes = staticCompositionLocalOf { LiquidGlassShapes() }
val LocalLiquidGlassEffects = staticCompositionLocalOf { LiquidGlassEffects() }

object LiquidGlassTheme {
    val colors: LiquidGlassColors
        @Composable
        get() = LocalLiquidGlassColors.current

    val shapes: LiquidGlassShapes
        @Composable
        get() = LocalLiquidGlassShapes.current

    val effects: LiquidGlassEffects
        @Composable
        get() = LocalLiquidGlassEffects.current
}

@Composable
fun LiquidGlassTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkLiquidGlassColors else LightLiquidGlassColors

    CompositionLocalProvider(
        LocalLiquidGlassColors provides colors,
        LocalLiquidGlassShapes provides LiquidGlassShapes(),
        LocalLiquidGlassEffects provides LiquidGlassEffects(),
        content = content
    )
}
