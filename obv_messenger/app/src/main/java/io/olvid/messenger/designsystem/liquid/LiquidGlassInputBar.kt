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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousCapsule
import io.olvid.messenger.R

/**
 * iOS iMessage-style input bar with Liquid Glass effect.
 */
@Composable
fun LiquidGlassInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Message",
    onAttachmentClick: (() -> Unit)? = null,
    onCameraClick: (() -> Unit)? = null,
    onVoiceClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    backdrop: Backdrop? = null
) {
    val isLightTheme = !isSystemInDarkTheme()
    val colors = if (isLightTheme) LightLiquidGlassColors else DarkLiquidGlassColors

    var isFocused by remember { mutableStateOf(false) }

    val containerColor = if (isLightTheme) {
        Color.White.copy(alpha = 0.85f)
    } else {
        Color.Black.copy(alpha = 0.85f)
    }

    val inputBackgroundColor = if (isLightTheme) {
        Color(0xFFF2F2F7)
    } else {
        Color(0xFF1C1C1E)
    }

    val borderColor by animateColorAsState(
        targetValue = if (isFocused) colors.accent else colors.inputBorder,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "borderColor"
    )

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
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Attachment button
            onAttachmentClick?.let { onClick ->
                IconButton(
                    onClick = onClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_attachment),
                        contentDescription = "Attachment",
                        tint = colors.accent,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Camera button
            onCameraClick?.let { onClick ->
                IconButton(
                    onClick = onClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "Camera",
                        tint = colors.accent,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Text input field
            Box(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 36.dp, max = 120.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(inputBackgroundColor)
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { isFocused = it.isFocused },
                    enabled = enabled,
                    textStyle = TextStyle(
                        color = colors.textPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    cursorBrush = SolidColor(colors.accent),
                    decorationBox = { innerTextField ->
                        Box {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    color = colors.textSecondary,
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            // Send or Voice button
            if (value.isNotEmpty()) {
                // Send button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(colors.accent)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onSendClick
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_send),
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                // Voice button
                onVoiceClick?.let { onClick ->
                    IconButton(
                        onClick = onClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_microphone),
                            contentDescription = "Voice",
                            tint = colors.accent,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Compact input bar for quick replies.
 */
@Composable
fun LiquidGlassQuickReplyBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Reply...",
    backdrop: Backdrop? = null
) {
    val isLightTheme = !isSystemInDarkTheme()
    val colors = if (isLightTheme) LightLiquidGlassColors else DarkLiquidGlassColors

    val containerColor = if (isLightTheme) {
        Color.White.copy(alpha = 0.9f)
    } else {
        Color.Black.copy(alpha = 0.9f)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .then(
                if (backdrop != null) {
                    Modifier.drawBackdrop(
                        backdrop = backdrop,
                        shape = { ContinuousCapsule },
                        effects = {
                            vibrancy()
                            blur(12.dp.toPx())
                        },
                        onDrawSurface = {
                            drawRect(containerColor)
                        }
                    )
                } else {
                    Modifier.background(containerColor)
                }
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            textStyle = TextStyle(
                color = colors.textPrimary,
                fontSize = 15.sp
            ),
            cursorBrush = SolidColor(colors.accent),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = colors.textSecondary,
                            style = TextStyle(fontSize = 15.sp)
                        )
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        val sendButtonScale by animateFloatAsState(
            targetValue = if (value.isNotEmpty()) 1f else 0.8f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "sendScale"
        )

        Box(
            modifier = Modifier
                .size(32.dp)
                .graphicsLayer {
                    scaleX = sendButtonScale
                    scaleY = sendButtonScale
                    alpha = if (value.isNotEmpty()) 1f else 0.5f
                }
                .clip(CircleShape)
                .background(if (value.isNotEmpty()) colors.accent else colors.textTertiary)
                .clickable(
                    enabled = value.isNotEmpty(),
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onSendClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_send),
                contentDescription = "Send",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

/**
 * Voice recording indicator.
 */
@Composable
fun LiquidGlassVoiceRecordingBar(
    duration: String,
    onCancelClick: () -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
    backdrop: Backdrop? = null
) {
    val isLightTheme = !isSystemInDarkTheme()
    val colors = if (isLightTheme) LightLiquidGlassColors else DarkLiquidGlassColors

    val containerColor = if (isLightTheme) {
        Color.White.copy(alpha = 0.9f)
    } else {
        Color.Black.copy(alpha = 0.9f)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
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
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Cancel button
        IconButton(
            onClick = onCancelClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "Cancel",
                tint = colors.error,
                modifier = Modifier.size(24.dp)
            )
        }

        // Recording indicator
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(colors.error)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = duration,
                color = colors.textPrimary,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }

        // Send button
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(colors.accent)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onSendClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_send),
                contentDescription = "Send",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
