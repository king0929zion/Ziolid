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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousCapsule
import io.olvid.messenger.R

/**
 * iOS-style navigation bar with Liquid Glass effect.
 */
@Composable
fun LiquidGlassNavigationBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onBackClick: (() -> Unit)? = null,
    onTitleClick: (() -> Unit)? = null,
    avatar: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    backdrop: Backdrop? = null
) {
    val isLightTheme = !isSystemInDarkTheme()
    val colors = if (isLightTheme) LightLiquidGlassColors else DarkLiquidGlassColors

    val containerColor = if (isLightTheme) {
        Color.White.copy(alpha = 0.85f)
    } else {
        Color.Black.copy(alpha = 0.85f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (backdrop != null) {
                    Modifier.drawBackdrop(
                        backdrop = backdrop,
                        shape = { ContinuousCapsule },
                        effects = {
                            vibrancy()
                            blur(16.dp.toPx())
                        },
                        onDrawSurface = {
                            drawRect(containerColor)
                        }
                    )
                } else {
                    Modifier.background(containerColor)
                }
            )
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            onBackClick?.let { onClick ->
                IconButton(
                    onClick = onClick,
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Back",
                        tint = colors.accent,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Avatar (optional)
            avatar?.let {
                Box(
                    modifier = Modifier
                        .padding(start = if (onBackClick != null) 0.dp else 8.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                ) {
                    it()
                }
                Spacer(modifier = Modifier.width(12.dp))
            }

            // Title section
            Column(
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (onTitleClick != null) {
                            Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onTitleClick
                            )
                        } else Modifier
                    )
            ) {
                Text(
                    text = title,
                    color = colors.textPrimary,
                    style = TextStyle(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                subtitle?.let {
                    Text(
                        text = it,
                        color = colors.textSecondary,
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Action buttons
            Row(
                horizontalArrangement = Arrangement.End,
                content = actions
            )
        }
    }
}

/**
 * Large title navigation bar (iOS-style).
 */
@Composable
fun LiquidGlassLargeTitleBar(
    title: String,
    modifier: Modifier = Modifier,
    collapsed: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    backdrop: Backdrop? = null
) {
    val isLightTheme = !isSystemInDarkTheme()
    val colors = if (isLightTheme) LightLiquidGlassColors else DarkLiquidGlassColors

    val containerColor = if (isLightTheme) {
        Color.White.copy(alpha = 0.85f)
    } else {
        Color.Black.copy(alpha = 0.85f)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (backdrop != null) {
                    Modifier.drawBackdrop(
                        backdrop = backdrop,
                        shape = { ContinuousCapsule },
                        effects = {
                            vibrancy()
                            blur(16.dp.toPx())
                        },
                        onDrawSurface = {
                            drawRect(containerColor)
                        }
                    )
                } else {
                    Modifier.background(containerColor)
                }
            )
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        // Top row with back button and actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            onBackClick?.let { onClick ->
                IconButton(
                    onClick = onClick,
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Back",
                        tint = colors.accent,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                horizontalArrangement = Arrangement.End,
                content = actions
            )
        }

        // Large title (animated)
        AnimatedVisibility(
            visible = !collapsed,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = colors.textPrimary,
                style = TextStyle(
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        // Small title when collapsed
        AnimatedVisibility(
            visible = collapsed,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = colors.textPrimary,
                style = TextStyle(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Contextual action bar (for selection mode).
 */
@Composable
fun LiquidGlassContextualBar(
    title: String,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    backdrop: Backdrop? = null
) {
    val isLightTheme = !isSystemInDarkTheme()
    val colors = if (isLightTheme) LightLiquidGlassColors else DarkLiquidGlassColors

    val containerColor = colors.accent.copy(alpha = 0.9f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (backdrop != null) {
                    Modifier.drawBackdrop(
                        backdrop = backdrop,
                        shape = { ContinuousCapsule },
                        effects = {
                            vibrancy()
                            blur(16.dp.toPx())
                        },
                        onDrawSurface = {
                            drawRect(containerColor)
                        }
                    )
                } else {
                    Modifier.background(containerColor)
                }
            )
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Close button
            IconButton(
                onClick = onCloseClick,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Title
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                color = Color.White,
                style = TextStyle(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

            // Actions
            Row(
                horizontalArrangement = Arrangement.End,
                content = actions
            )
        }
    }
}

/**
 * Search header with integrated search bar.
 */
@Composable
fun LiquidGlassSearchHeader(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search",
    backdrop: Backdrop? = null
) {
    val isLightTheme = !isSystemInDarkTheme()
    val colors = if (isLightTheme) LightLiquidGlassColors else DarkLiquidGlassColors

    val containerColor = if (isLightTheme) {
        Color.White.copy(alpha = 0.85f)
    } else {
        Color.Black.copy(alpha = 0.85f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (backdrop != null) {
                    Modifier.drawBackdrop(
                        backdrop = backdrop,
                        shape = { ContinuousCapsule },
                        effects = {
                            vibrancy()
                            blur(16.dp.toPx())
                        },
                        onDrawSurface = {
                            drawRect(containerColor)
                        }
                    )
                } else {
                    Modifier.background(containerColor)
                }
            )
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search input
            LiquidGlassSearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                modifier = Modifier.weight(1f),
                placeholder = placeholder,
                showCancelButton = false
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Cancel button
            Text(
                text = "Cancel",
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onCancelClick
                    )
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                color = colors.accent,
                style = TextStyle(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}
