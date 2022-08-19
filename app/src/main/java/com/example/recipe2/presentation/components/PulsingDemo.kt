package com.example.recipe2.presentation.components


import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp

@Composable
fun PulsingDemo() {
    val color = MaterialTheme.colors.primary

    val infiniteTransition = rememberInfiniteTransition()
    val pulseMagnitude by infiniteTransition.animateFloat(
        initialValue = PulseAnimationDefinitions.PULSE_STATE_INITIAL,
        targetValue = PulseAnimationDefinitions.PULSE_STATE_FINAL,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .height(pulseMagnitude.dp)
                .width(pulseMagnitude.dp),
            imageVector = Icons.Default.Favorite, contentDescription = "")
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
    ) {
        drawCircle(
            radius = pulseMagnitude,
            brush = SolidColor(color)
        )
    }
}

object PulseAnimationDefinitions{
    const val PULSE_STATE_INITIAL = 40f
    const val PULSE_STATE_FINAL = 50f
}