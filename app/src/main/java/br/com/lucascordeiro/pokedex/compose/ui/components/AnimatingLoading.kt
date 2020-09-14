package br.com.lucascordeiro.pokedex.compose.ui.components

import androidx.compose.animation.core.*
import androidx.compose.animation.transition
import androidx.compose.foundation.Box
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Icon
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Layout
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawLayer
import androidx.compose.ui.res.loadVectorResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.R
import br.com.lucascordeiro.pokedex.compose.ui.theme.grey900
import kotlin.math.roundToInt



@Composable
fun AnimatingLoading(
    background: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
){
    val transitionDefinition = remember { loadTransitionDefinition }
    val transition = transition(
        definition = transitionDefinition,
        initState = LoadingState.START,
        toState = LoadingState.END
    )
    Loading(
        background = background,
        icon = icon,
        rotationProgress = {transition[rotation]},
        modifier = modifier)
}

@Composable
fun Loading(
    background: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    rotationProgress: () -> Float,
    modifier: Modifier
) {
    Layout(
        modifier = modifier,
        children = {
            background()
            Box(modifier = Modifier.drawLayer(rotationZ = rotationProgress())) {
                icon()
            }
        }
    ) { measurables, constraints ->
        val backgroundPlaceable = measurables[0].measure(constraints)
        val iconPlaceable = measurables[1].measure(constraints)

        val height = constraints.maxHeight
        val width = constraints.maxWidth

        layout(width, height) {
            backgroundPlaceable.place(
                constraints.maxWidth/2 -  backgroundPlaceable.width / 2,
                constraints.maxHeight/2 -  backgroundPlaceable.height / 2
            )
            iconPlaceable.place(
                constraints.maxWidth/2 - iconPlaceable.width / 2,
                constraints.maxHeight/2 - iconPlaceable.height / 2
            )
        }
    }
}

private val rotation = FloatPropKey()
private enum class LoadingState {START, END}
private val loadTransitionDefinition = transitionDefinition<LoadingState> {
    state(LoadingState.START){
        this[rotation] = 0f
    }
    state(LoadingState.END){
        this[rotation] = 360f
    }
    transition(fromState = LoadingState.START, toState = LoadingState.END){
        rotation using repeatable(
            animation =  tween(
                easing = FastOutSlowInEasing,
                durationMillis = (3000 / 3f).roundToInt()
            ),
            iterations = AnimationConstants.Infinite
        )
    }
}