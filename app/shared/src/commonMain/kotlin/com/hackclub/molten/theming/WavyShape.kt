package com.hackclub.molten.theming

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.round
import kotlin.math.sin

// Claude wrote this btw
class WavyShape(
    private val amplitude: Float = 8f,     // max inward dent depth, in px, on a full-size hump
    private val wavelength: Float = 120f,   // width of one inward hump, in px
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

            addWavyEdge(cornerRadius, size.width - cornerRadius, axisPos = 0f, inwardSign = 1f, horizontal = true)
            if (cornerRadius > 0f) quadraticBezierTo(size.width, 0f, size.width, cornerRadius)

            addWavyEdge(cornerRadius, size.height - cornerRadius, axisPos = size.width, inwardSign = -1f, horizontal = false)
            if (cornerRadius > 0f) quadraticBezierTo(size.width, size.height, size.width - cornerRadius, size.height)

            addWavyEdge(size.width - cornerRadius, cornerRadius, axisPos = size.height, inwardSign = -1f, horizontal = true)
            if (cornerRadius > 0f) quadraticBezierTo(0f, size.height, 0f, size.height - cornerRadius)

            addWavyEdge(size.height - cornerRadius, cornerRadius, axisPos = 0f, inwardSign = 1f, horizontal = false)
            if (cornerRadius > 0f) quadraticBezierTo(0f, 0f, cornerRadius, 0f)

            close()
        }

        return Outline.Generic(path)
    }

    private fun Path.addWavyEdge(
        start: Float,
        end: Float,
        axisPos: Float,
        inwardSign: Float,
        horizontal: Boolean
    ) {
        val length = end - start
        if (length == 0f) return
        val absLength = abs(length)

        val numHumps = max(1, round(absLength / wavelength).toInt())
        val humpLen = length / numHumps
        val twoPi = (2.0 * PI).toFloat()

        // If forcing a minimum of one hump onto this edge squeezed it shorter
        // than a "full size" hump would be, shrink the dent depth to match —
        // a short button edge gets a subtle ripple instead of a fixed-depth notch.
        val effectiveAmplitude = amplitude * (abs(humpLen) / wavelength).coerceIn(0f, 1f)

        val phaseFrac = (phase / wavelength).let { it - floor(it) }

        fun humpValue(f: Float): Float = inwardSign * effectiveAmplitude * (1f - cos(twoPi * f)) / 2f
        fun humpSlopeWrtPos(f: Float): Float =
            inwardSign * effectiveAmplitude / 2f * sin(twoPi * f) * (twoPi / humpLen)

        var pos = start
        repeat(numHumps) {
            val nextPos = pos + humpLen
            val midPos = pos + humpLen / 2f

            for ((segStart, segEnd) in listOf(pos to midPos, midPos to nextPos)) {
                val fStart = (segStart - pos) / humpLen + phaseFrac
                val fEnd = (segEnd - pos) / humpLen + phaseFrac
                val v0 = humpValue(fStart)
                val v1 = humpValue(fEnd)
                val m0 = humpSlopeWrtPos(fStart)
                val m1 = humpSlopeWrtPos(fEnd)

                val segLen = segEnd - segStart
                val third = segLen / 3f
                val c1Pos = segStart + third
                val c1Val = v0 + m0 * third
                val c2Pos = segEnd - third
                val c2Val = v1 - m1 * third

                if (horizontal) {
                    cubicTo(c1Pos, axisPos + c1Val, c2Pos, axisPos + c2Val, segEnd, axisPos + v1)
                } else {
                    cubicTo(axisPos + c1Val, c1Pos, axisPos + c2Val, c2Pos, axisPos + v1, segEnd)
                }
            }
            pos = nextPos
        }
    }
}