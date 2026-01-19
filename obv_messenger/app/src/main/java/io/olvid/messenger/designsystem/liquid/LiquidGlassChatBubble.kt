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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousCapsule

/**
 * iMessage-style bubble shape with optional tail.
 */
class IMessageBubbleShape(
    private val cornerRadius: Dp,
    private val tailRadius: Dp,
    private val isSent: Boolean,
    private val showTail: Boolean
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val cornerPx = with(density) { cornerRadius.toPx() }
        val tailPx = with(density) { tailRadius.toPx() }
        val width = size.width
        val height = size.height

        if (isSent) {
            // Sent message - tail on right
            path.apply {
                moveTo(cornerPx, 0f)
                lineTo(width - cornerPx, 0f)
                quadraticTo(width, 0f, width, cornerPx)
                if (showTail) {
                    lineTo(width, height - tailPx * 2)
                    quadraticTo(width, height - tailPx, width + tailPx, height)
                    quadraticTo(width - tailPx, height - tailPx, width - cornerPx, height - tailPx)
                } else {
                    lineTo(width, height - cornerPx)
                    quadraticTo(width, height, width - cornerPx, height)
                }
                lineTo(cornerPx, height)
                quadraticTo(0f, height, 0f, height - cornerPx)
                lineTo(0f, cornerPx)
                quadraticTo(0f, 0f, cornerPx, 0f)
                close()
            }
        } else {
            // Received message - tail on left
            path.apply {
                moveTo(cornerPx, 0f)
                lineTo(width - cornerPx, 0f)
                quadraticTo(width, 0f, width, cornerPx)
                lineTo(width, height - cornerPx)
                quadraticTo(width, height, width - cornerPx, height)
                if (showTail) {
                    lineTo(cornerPx, height - tailPx)
                    quadraticTo(tailPx, height - tailPx, -tailPx, height)
                    quadraticTo(0f, height - tailPx, 0f, height - tailPx * 2)
                } else {
                    lineTo(cornerPx, height)
                    quadraticTo(0f, height, 0f, height - cornerPx)
                }
                lineTo(0f, cornerPx)
                quadraticTo(0f, 0f, cornerPx, 0f)
                close()
            }
        }

        return Outline.Generic(path)
    }
}

/**
 * iOS iMessage-style chat bubble with Liquid Glass effect.
 */
@Composable
fun LiquidGlassChatBubble(
    modifier: Modifier = Modifier,
    isSent: Boolean,
    showTail: Boolean = true,
    maxWidth: Dp = 280.dp,
    backdrop: Backdrop? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val isLightTheme = !isSystemInDarkTheme()
    val colors = if (isLightTheme) LightLiquidGlassColors else DarkLiquidGlassColors
    val shapes = LiquidGlassShapes()

    val bubbleColor = if (isSent) colors.sentBubble else colors.receivedBubble
    val bubbleShape = remember(isSent, showTail) {
        IMessageBubbleShape(
            cornerRadius = shapes.bubbleCornerRadius,
            tailRadius = shapes.bubbleTailRadius,
            isSent = isSent,
            showTail = showTail
        )
    }

    Box(
        modifier = modifier
            .widthIn(max = maxWidth)
            .clip(bubbleShape)
            .then(
                if (backdrop != null && !isSent) {
                    // Apply glass effect only to received messages
                    Modifier.drawBackdrop(
                        backdrop = backdrop,
                        shape = { ContinuousCapsule },
                        effects = {
                            vibrancy()
                            blur(12.dp.toPx())
                            lens(16.dp.toPx(), 16.dp.toPx())
                        },
                        onDrawSurface = {
                            drawRect(bubbleColor)
                        }
                    )
                } else {
                    Modifier.background(bubbleColor)
                }
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        content = content
    )
}

/**
 * Simple text message bubble.
 */
@Composable
fun LiquidGlassTextBubble(
    text: String,
    isSent: Boolean,
    modifier: Modifier = Modifier,
    showTail: Boolean = true,
    timestamp: String? = null,
    backdrop: Backdrop? = null
) {
    val isLightTheme = !isSystemInDarkTheme()
    val colors = if (isLightTheme) LightLiquidGlassColors else DarkLiquidGlassColors
    val textColor = if (isSent) colors.sentText else colors.receivedText

    LiquidGlassChatBubble(
        modifier = modifier,
        isSent = isSent,
        showTail = showTail,
        backdrop = backdrop
    ) {
        Column {
            Text(
                text = text,
                color = textColor,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 20.sp
                )
            )
            timestamp?.let {
                Text(
                    text = it,
                    color = textColor.copy(alpha = 0.6f),
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 4.dp)
                )
            }
        }
    }
}

/**
 * Message status indicator (sent, delivered, read).
 */
@Composable
fun MessageStatusIndicator(
    status: MessageStatus,
    modifier: Modifier = Modifier,
    tint: Color = LiquidGlassTheme.colors.accent
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (status) {
            MessageStatus.SENDING -> {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(tint.copy(alpha = 0.5f))
                )
            }
            MessageStatus.SENT -> {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(tint)
                )
            }
            MessageStatus.DELIVERED -> {
                Row {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(tint)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(tint)
                    )
                }
            }
            MessageStatus.READ -> {
                Row {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(LiquidGlassTheme.colors.success)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(LiquidGlassTheme.colors.success)
                    )
                }
            }
            MessageStatus.FAILED -> {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(LiquidGlassTheme.colors.error)
                )
            }
        }
    }
}

enum class MessageStatus {
    SENDING,
    SENT,
    DELIVERED,
    READ,
    FAILED
}

/**
 * Typing indicator with animated dots.
 */
@Composable
fun LiquidGlassTypingIndicator(
    modifier: Modifier = Modifier,
    backdrop: Backdrop? = null
) {
    LiquidGlassChatBubble(
        modifier = modifier,
        isSent = false,
        showTail = true,
        backdrop = backdrop
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(LiquidGlassTheme.colors.textSecondary)
                )
                if (index < 2) {
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }
}

/**
 * Date separator in chat.
 */
@Composable
fun LiquidGlassDateSeparator(
    date: String,
    modifier: Modifier = Modifier,
    backdrop: Backdrop? = null
) {
    val isLightTheme = !isSystemInDarkTheme()
    val containerColor = if (isLightTheme) {
        Color.Black.copy(alpha = 0.06f)
    } else {
        Color.White.copy(alpha = 0.1f)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .then(
                    if (backdrop != null) {
                        Modifier.drawBackdrop(
                            backdrop = backdrop,
                            shape = { ContinuousCapsule },
                            effects = {
                                vibrancy()
                                blur(8.dp.toPx())
                            },
                            onDrawSurface = {
                                drawRect(containerColor)
                            }
                        )
                    } else {
                        Modifier.background(containerColor)
                    }
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = date,
                color = LiquidGlassTheme.colors.textSecondary,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
