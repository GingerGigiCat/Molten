package com.hackclub.molten.theming

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

class WavyShape(
    private val amplitude: Float = 4f,     // wave height in px
    private val wavelength: Float = 120f,  // wave length in px
    private val cornerRadius: Float = 20f, // optional corner radius
    private val phase: Float = 0f          // phase shift for animation
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(cornerRadius, 0f)

            // top edge: left -> right
            addWavyEdge(
                start = cornerRadius, end = size.width - cornerRadius,
                axisPos = 0f, sign = -1f, horizontal = true
            )

            if (cornerRadius > 0f) quadraticBezierTo(size.width, 0f, size.width, cornerRadius)

            // right edge: top -> bottom
            addWavyEdge(
                start = cornerRadius, end = size.height - cornerRadius,
                axisPos = size.width, sign = 1f, horizontal = false
            )

            if (cornerRadius > 0f) {
                quadraticBezierTo(size.width, size.height, size.width - cornerRadius, size.height)
            }

            // bottom edge: right -> left
            addWavyEdge(
                start = size.width - cornerRadius, end = cornerRadius,
                axisPos = size.height, sign = 1f, horizontal = true
            )

            if (cornerRadius > 0f) quadraticBezierTo(0f, size.height, 0f, size.height - cornerRadius)

            // left edge: bottom -> top
            addWavyEdge(
                start = size.height - cornerRadius, end = cornerRadius,
                axisPos = 0f, sign = -1f, horizontal = false
            )

            if (cornerRadius > 0f) quadraticBezierTo(0f, 0f, cornerRadius, 0f)

            close()
        }

        return Outline.Generic(path)
    }

    /**
     * Draws a smooth sine wave along one edge using cubic Bézier segments whose
     * control points match the sine curve's actual slope at each sample point,
     * so segments join with no visible kink (C1 continuity).
     *
     * [start]/[end] are positions along the edge (in px, can run either direction).
     * [axisPos] is the fixed coordinate of the straight edge (e.g. y=0 for the top).
     * [sign] flips which way the wave bulges (in vs. out).
     * [horizontal] picks whether the wave displaces along y (top/bottom edges) or x (left/right edges).
     */
    private fun Path.addWavyEdge(
        start: Float,
        end: Float,
        axisPos: Float,
        sign: Float,
        horizontal: Boolean
    ) {
        val length = end - start
        if (length == 0f) return
        val dir = if (length > 0f) 1f else -1f
        val absLength = kotlin.math.abs(length)

        // Fit a whole number of quarter-periods into the available length so the
        // wave is continuous and evenly spaced, rather than getting cut off mid-wave.
        val quarterLen = wavelength / 4f
        val steps = max(1, (absLength / quarterLen).let { kotlin.math.round(it) }.toInt())
        val stepLen = length / steps
        val twoPi = (2.0 * PI).toFloat()

        fun waveValue(pos: Float): Float {
            val t = (pos + phase) / wavelength
            return sign * amplitude * sin(twoPi * t)
        }

        fun waveSlope(pos: Float): Float {
            // d/dpos [sign * A * sin(2π(pos+phase)/λ)]
            val t = (pos + phase) / wavelength
            return sign * amplitude * (twoPi / wavelength) * cos(twoPi * t)
        }

        var pos = start
        repeat(steps) {
            val nextPos = pos + stepLen
            val v0 = waveValue(pos)
            val v1 = waveValue(nextPos)
            val m0 = waveSlope(pos)
            val m1 = waveSlope(nextPos)

            // Convert Hermite (point + tangent at each end) to cubic Bézier control points,
            // placed at 1/3 of the segment length along the tangent direction.
            val third = stepLen / 3f
            val c1Pos = pos + third
            val c1Val = v0 + m0 * third
            val c2Pos = nextPos - third
            val c2Val = v1 - m1 * third

            if (horizontal) {
                cubicTo(c1Pos, axisPos + c1Val, c2Pos, axisPos + c2Val, nextPos, axisPos + v1)
            } else {
                cubicTo(axisPos + c1Val, c1Pos, axisPos + c2Val, c2Pos, axisPos + v1, nextPos)
            }

            pos = nextPos
        }
    }
}