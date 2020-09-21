package br.com.lucascordeiro.pokedex.compose.ui.components

import androidx.compose.animation.ColorPropKey
import androidx.compose.animation.core.*
import androidx.compose.animation.transition
import androidx.compose.foundation.Box
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Layout
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnimatingLoading(
    backgroundColor: Color? = MaterialTheme.colors.background,
    backgroundPadding: Dp = 4.dp,
    progressColor: Color = MaterialTheme.colors.primary,
    progressColorSecondary: Color = MaterialTheme.colors.secondary,
    progressWidth: () -> Float = { 10f },
    progress: () -> Float = { 0f },
    infinite: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (infinite) {
        val transitionDefinition = remember {
            loadTransitionDefinition(range = 30f, startColor = progressColor, endColor = progressColorSecondary)
        }
        val transition = transition(
            definition = transitionDefinition,
            initState = LoadingState.START,
            toState = LoadingState.END
        )
        Loading(
            background = {
                backgroundColor?.let {
                    Background(backgroundColor = backgroundColor)
                }
            },
            progressBar = {
                ProgressBar(
                    angle = { transition[angle] },
                    progress = { transition[progressValue] },
                    progressColor = { transition[color] },
                    progressWidth = progressWidth
                )
            },
            backgroundPadding = backgroundPadding,
            modifier = modifier
        )
    }
    else {
        Loading(
            background = {
                backgroundColor?.let {
                    Background(backgroundColor = backgroundColor)
                }
            },
            progressBar = {
                ProgressBar(
                    angle = { 0f },
                    progress = progress,
                    progressColor = { progressColor },
                    progressWidth = progressWidth
                )
            },
            backgroundPadding = backgroundPadding,
            modifier = modifier
        )
    }
}

@Composable
fun Background(
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        drawCircle(color = backgroundColor, radius = this.size.width / 2)
    }
}

@Composable
fun ProgressBar(
    progress: () -> Float,
    angle: () -> Float,
    progressColor: () -> Color,
    progressWidth: () -> Float
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize(1f)
    ) {
        val (progressBar, center) = createRefs()
        Canvas(
            modifier = Modifier.fillMaxSize().constrainAs(progressBar) {
                centerTo(parent)
            }
        ) {
            drawArc(
                color = progressColor(),
                startAngle = 360f * (angle() / 100),
                sweepAngle = 360f * (progress() / 100),
                useCenter = false,
                style = Stroke(
                    width = progressWidth()
                )
            )
        }
    }
}

@Composable
fun Loading(
    background: @Composable () -> Unit,
    progressBar: @Composable () -> Unit,
    backgroundPadding: Dp,
    modifier: Modifier
) {
    Layout(
        modifier = modifier,
        children = {
            background()
            Box(modifier = Modifier.padding(backgroundPadding)) {
                progressBar()
            }
        }
    ) { measurables, constraints ->
        if(measurables.size > 1) {
            val backgroundPlaceable = measurables[0].measure(constraints)
            val iconPlaceable = measurables[1].measure(constraints)

            val height = constraints.maxHeight
            val width = constraints.maxWidth

            layout(width, height) {
                backgroundPlaceable.place(
                    constraints.maxWidth / 2 - backgroundPlaceable.width / 2,
                    constraints.maxHeight / 2 - backgroundPlaceable.height / 2
                )
                iconPlaceable.place(
                    constraints.maxWidth / 2 - iconPlaceable.width / 2,
                    constraints.maxHeight / 2 - iconPlaceable.height / 2
                )
            }
        }else{
            val iconPlaceable = measurables[0].measure(constraints)

            val height = constraints.maxHeight
            val width = constraints.maxWidth

            layout(width, height) {
                iconPlaceable.place(
                    constraints.maxWidth / 2 - iconPlaceable.width / 2,
                    constraints.maxHeight / 2 - iconPlaceable.height / 2
                )
            }
        }
    }
}

private val angle = FloatPropKey()
private val progressValue = FloatPropKey()
private val color = ColorPropKey()

private enum class LoadingState { START, END }

private fun loadTransitionDefinition(
    duration: Int = 1000,
    delay: Int = 0,
    range: Float = 30f,
    startColor: Color,
    endColor: Color
) = transitionDefinition<LoadingState> {
    state(LoadingState.START) {
        this[angle] = 0f
        this[progressValue] = 0f
        this[color] = startColor
    }
    state(LoadingState.END) {
        this[angle] = 100f
        this[progressValue] = range
        this[color] = endColor
    }
    transition(fromState = LoadingState.START, toState = LoadingState.END) {
        angle using repeatable(
            animation = tween(
                easing = LinearEasing,
                durationMillis = duration,
                delayMillis = delay,
            ),
//            repeatMode = RepeatMode.Reverse,
            iterations = AnimationConstants.Infinite
        )

        color using repeatable(
            animation = tween(
                easing = LinearEasing,
                durationMillis = duration * 2,
                delayMillis = delay,
            ),
            iterations = AnimationConstants.Infinite
        )

        progressValue using repeatable(
            animation = tween(
                easing = LinearEasing,
                durationMillis = duration / 2,
                delayMillis = delay
            ),
            repeatMode = RepeatMode.Reverse,
            iterations = AnimationConstants.Infinite
        )
    }
}
