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

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.matchParentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousCapsule

/**
 * iOS-style Liquid Glass container with backdrop effect.
 *
 * @param modifier The modifier for this composable.
 * @param cornerRadius The corner radius of the liquid glass container.
 * @param blurAmount The amount of blur effect (default 16dp).
 * @param lensRadius The radius of the lens effect (default 24dp).
 * @param tintColor The tint color to apply to the glass surface.
 * @param showInnerRefraction Whether to show inner refraction effect.
 * @param backdrop Backdrop source used for blur/lens. When null, draws a translucent surface only.
 * @param content The content to display inside the liquid glass container.
 */
@Composable
fun LiquidGlassContainer(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    blurAmount: Dp = 16.dp,
    lensRadius: Dp = 24.dp,
    tintColor: Color = Color.Unspecified,
    showInnerRefraction: Boolean = true,
    backdrop: Backdrop? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val isLightTheme = !isSystemInDarkTheme()

    val containerColor = remember(isLightTheme) {
        if (isLightTheme) {
            Color.White.copy(alpha = 0.6f)
        } else {
            Color.Black.copy(alpha = 0.6f)
        }
    }

    val shape: Shape = remember(cornerRadius) { RoundedCornerShape(cornerRadius) }

    Box(
        modifier
            .clip(shape)
            .then(
                if (backdrop != null) {
                    Modifier.drawBackdrop(
                        backdrop = backdrop,
                        shape = { ContinuousCapsule },
                        effects = {
                            vibrancy()
                            blur(blurAmount.toPx())
                            if (showInnerRefraction) {
                                lens(lensRadius.toPx(), lensRadius.toPx())
                            }
                        },
                        onDrawSurface = {
                            drawRect(containerColor)
                            if (tintColor != Color.Unspecified) {
                                drawRect(tintColor)
                            }
                        }
                    )
                } else {
                    Modifier
                        .background(containerColor)
                        .then(
                            if (tintColor != Color.Unspecified) Modifier.background(tintColor)
                            else Modifier
                        )
                }
            )
    ) {
        content()
    }
}

/**
 * A smaller liquid glass button with interactive highlight effect.
 */
@Composable
fun LiquidGlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    tintColor: Color = Color.Unspecified,
    backdrop: Backdrop? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val isLightTheme = !isSystemInDarkTheme()

    val containerColor = remember(isLightTheme) {
        if (isLightTheme) {
            Color.White.copy(alpha = 0.5f)
        } else {
            Color.Black.copy(alpha = 0.5f)
        }
    }

    val shape: Shape = remember(cornerRadius) { RoundedCornerShape(cornerRadius) }
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier
            .clip(shape)
            .then(
                if (backdrop != null) {
                    Modifier.drawBackdrop(
                        backdrop = backdrop,
                        shape = { ContinuousCapsule },
                        effects = {
                            vibrancy()
                            blur(8.dp.toPx())
                            lens(16.dp.toPx(), 16.dp.toPx())
                        },
                        onDrawSurface = {
                            drawRect(containerColor)
                            if (tintColor != Color.Unspecified) {
                                drawRect(tintColor.copy(alpha = 0.3f))
                            }
                        }
                    )
                } else {
                    Modifier
                        .background(containerColor)
                        .then(
                            if (tintColor != Color.Unspecified) {
                                Modifier.background(tintColor.copy(alpha = 0.3f))
                            } else {
                                Modifier
                            }
                        )
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        content()
    }
}

/**
 * A floating liquid glass panel for dialogs and popups.
 */
@Composable
fun LiquidGlassPanel(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 24.dp,
    blurAmount: Dp = 24.dp,
    backdrop: Backdrop? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val isLightTheme = !isSystemInDarkTheme()

    val panelColor = remember(isLightTheme) {
        if (isLightTheme) {
            Color.White.copy(alpha = 0.7f)
        } else {
            Color.Black.copy(alpha = 0.7f)
        }
    }

    val shape: Shape = remember(cornerRadius) { RoundedCornerShape(cornerRadius) }

    Box(
        modifier
            .clip(shape)
            .then(
                if (backdrop != null) {
                    Modifier.drawBackdrop(
                        backdrop = backdrop,
                        shape = { ContinuousCapsule },
                        effects = {
                            vibrancy()
                            blur(blurAmount.toPx())
                            lens(20.dp.toPx(), 20.dp.toPx())
                        },
                        onDrawSurface = { drawRect(panelColor) }
                    )
                } else {
                    Modifier.background(panelColor)
                }
            )
    ) {
        content()
    }
}

/**
 * Root container that records background content into a [LayerBackdrop], then renders overlays
 * (e.g., Liquid Glass surfaces) using that backdrop.
 */
@Composable
fun LiquidGlassBackdropBox(
    modifier: Modifier = Modifier,
    background: @Composable BoxScope.() -> Unit,
    overlay: @Composable BoxScope.(backdrop: Backdrop) -> Unit,
) {
    val layerBackdrop = rememberLayerBackdrop()
    Box(modifier) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .layerBackdrop(layerBackdrop),
            content = background
        )
        overlay(layerBackdrop)
    }
}

/**
 * Convenience for circular Liquid Glass surfaces (e.g. icon buttons).
 */
@Composable
fun LiquidGlassCircle(
    modifier: Modifier = Modifier,
    backdrop: Backdrop? = null,
    blurAmount: Dp = 12.dp,
    lensRadius: Dp = 16.dp,
    tintColor: Color = Color.Unspecified,
    content: @Composable BoxScope.() -> Unit,
) {
    val isLightTheme = !isSystemInDarkTheme()
    val containerColor = remember(isLightTheme) {
        if (isLightTheme) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.5f)
    }
    Box(
        modifier
            .clip(CircleShape)
            .then(
                if (backdrop != null) {
                    Modifier.drawBackdrop(
                        backdrop = backdrop,
                        shape = { ContinuousCapsule },
                        effects = {
                            vibrancy()
                            blur(blurAmount.toPx())
                            lens(lensRadius.toPx(), lensRadius.toPx())
                        },
                        onDrawSurface = {
                            drawRect(containerColor)
                            if (tintColor != Color.Unspecified) drawRect(tintColor)
                        }
                    )
                } else {
                    Modifier
                        .background(containerColor)
                        .then(if (tintColor != Color.Unspecified) Modifier.background(tintColor) else Modifier)
                }
            )
    ) {
        content()
    }
}
