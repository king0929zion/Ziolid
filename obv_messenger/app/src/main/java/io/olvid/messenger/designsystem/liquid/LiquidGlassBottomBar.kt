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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousCapsule
import io.olvid.messenger.R
import io.olvid.messenger.designsystem.theme.OlvidTypography

/**
 * iOS-style Liquid Glass Bottom Navigation Bar.
 */
@Composable
fun LiquidGlassBottomBar(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    backdrop: com.kyant.backdrop.Backdrop? = null,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val isLightTheme = !isSystemInDarkTheme()

    val containerColor = if (isLightTheme) {
        Color.White.copy(alpha = 0.6f)
    } else {
        Color.Black.copy(alpha = 0.6f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .then(
                if (backdrop != null) {
                    Modifier.drawBackdrop(
                        backdrop = backdrop,
                        shape = { ContinuousCapsule },
                        effects = {
                            vibrancy()
                            blur(16.dp.toPx())
                            lens(24.dp.toPx(), 24.dp.toPx())
                        },
                        onDrawSurface = { drawRect(containerColor) }
                    )
                } else {
                    Modifier.background(containerColor)
                }
            )
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

/**
 * Individual Liquid Glass Bottom Tab Item.
 */
@Composable
fun LiquidGlassBottomTab(
    iconResId: Int,
    label: String,
    isSelected: Boolean,
    showBadge: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = colorResource(R.color.imessage_blue)
    val unselectedColor = colorResource(R.color.imessage_tab_bar_unselected)

    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "tabScale"
    )

    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) accentColor else unselectedColor,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "tabColor"
    )

    Box(
        modifier = modifier
            .weight(1f)
            .fillMaxWidth()
            .height(56.dp)
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.14f))
            )
        }

        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = animatedColor
            )

            Text(
                text = label,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .alpha(if (isSelected) 1f else 0.7f),
                style = OlvidTypography.caption,
                color = animatedColor
            )
        }

        if (showBadge) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp, start = 18.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(colorResource(R.color.imessage_blue))
            )
        }
    }
}

/**
 * iOS-style floating action button with liquid glass effect.
 */
@Composable
fun LiquidGlassFab(
    iconResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = colorResource(R.color.imessage_blue)
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(containerColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color.White
        )
    }
}
