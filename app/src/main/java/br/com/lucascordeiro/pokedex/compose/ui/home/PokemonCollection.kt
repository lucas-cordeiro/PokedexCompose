package br.com.lucascordeiro.pokedex.compose.ui.home

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.animation.core.AnimationConstants.Infinite
import androidx.compose.animation.transition
import androidx.compose.foundation.*
import androidx.compose.foundation.animation.FlingConfig
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cached
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Layout
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.drawLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.loadImageResource
import androidx.compose.ui.res.loadVectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.R
import br.com.lucascordeiro.pokedex.compose.ui.components.AnimatingLoading
import br.com.lucascordeiro.pokedex.compose.ui.theme.PokedexComposeTheme
import br.com.lucascordeiro.pokedex.compose.ui.theme.grey900
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlin.math.ceil
import kotlin.math.roundToInt

@Composable
fun PokemonCollection(
    onPokemonSelected: (Pokemon) -> Unit,
    scrollPosition: () -> Float,
    setScrollPosition: (Float) -> Unit,
    pokemons: List<Pokemon>,
    loadMoreItems: () -> Unit,
    loading: Boolean,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    scrollState.scrollTo(scrollPosition())

    Stack() {
        ScrollableColumn(
            scrollState = scrollState,
            modifier = modifier
        ) {
            StaggeredVerticalGrid(
                maxColumnWidth = 220.dp,
                modifier = Modifier.padding(4.dp)
            ) {
                pokemons.forEach { pokemon ->
                    PokemonItem(
                        scrollState = scrollState,
                        setPosition = setScrollPosition,
                        onPokemonSelected = onPokemonSelected,
                        modifier = modifier,
                        pokemon = pokemon
                    )
                }
            }

            Button(
                onClick = {
                    setScrollPosition(scrollState.value)
                    loadMoreItems()
                },
                modifier = Modifier
                    .padding(0.dp, 8.dp)
                    .drawOpacity(if (loading) 0f else 1f),
                backgroundColor = MaterialTheme.colors.background
            ) {
                Text(
                    text = "Carregar mais",
                    style = TextStyle(
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(4.dp)

                )
            }
        }


        ConstraintLayout(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(1f)
                .preferredHeight(40.dp)
                .drawOpacity(if (loading) 1f else 0f)
        ) {
            val loadingRef = createRef()
            AnimatingLoading(
                background = {
                    Canvas(modifier = Modifier.preferredSize(40.dp)) {
                        drawCircle(color = grey900, radius = this.size.width / 2)
                    }
                },
                icon = {
                    val image = loadVectorResource(id = R.drawable.ic_refresh)
                    image.resource.resource?.let {
                        Icon(
                            asset = it,
                            tint = MaterialTheme.colors.primary,
                            modifier = modifier
                                .preferredSize(24.dp)
                        )
                    }
                },
                modifier = Modifier.constrainAs(loadingRef){
                    centerTo(parent)
                }
            )
        }
    }
}

    @Composable
fun StaggeredVerticalGrid(
    modifier: Modifier = Modifier,
    maxColumnWidth: Dp,
    children: @Composable () -> Unit
) {
    Layout(
        children = children,
        modifier = modifier
    ) { measurables, constraints ->
        check(constraints.hasBoundedWidth) {
            "Unbounded width not supported"
        }
        val columns = ceil(constraints.maxWidth / maxColumnWidth.toPx()).toInt()
        val columnWidth = constraints.maxWidth / columns
        val itemConstraints = constraints.copy(maxWidth = columnWidth)
        val colHeights = IntArray(columns) { 0 } // track each column's height
        val placeables = measurables.map { measurable ->
            val column = shortestColumn(colHeights)
            val placeable = measurable.measure(itemConstraints)
            colHeights[column] += placeable.height
            placeable
        }

        val height = colHeights.maxOrNull()?.coerceIn(constraints.minHeight, constraints.maxHeight)
            ?: constraints.minHeight
        layout(
            width = constraints.maxWidth,
            height = height
        ) {
            val colY = IntArray(columns) { 0 }
            placeables.forEach { placeable ->
                val column = shortestColumn(colY)
                placeable.place(
                    x = columnWidth * column,
                    y = colY[column]
                )
                colY[column] += placeable.height
            }
        }
    }
}

private fun shortestColumn(colHeights: IntArray): Int {
    var minHeight = Int.MAX_VALUE
    var column = 0
    colHeights.forEachIndexed { index, height ->
        if (height < minHeight) {
            minHeight = height
            column = index
        }
    }
    return column
}


@Preview
@Composable
fun PreviewPokemonList(){
    PokedexComposeTheme(darkTheme = true) {
        Surface(contentColor = contentColor()) {
            PokemonCollection(setScrollPosition = {},scrollPosition = {1f}, pokemons = remember { generateList() }, onPokemonSelected = {}, loadMoreItems = {}, loading = false)
        }
    }
}
