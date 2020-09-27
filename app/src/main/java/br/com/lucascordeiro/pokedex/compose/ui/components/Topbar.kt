package br.com.lucascordeiro.pokedex.compose.ui.components

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.material.icons.rounded.KeyboardBackspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.ui.theme.PokedexComposeTheme

@Composable
fun TopBar(
    title: String,
    tint: Color = MaterialTheme.colors.onSurface,
    onBackClick: () -> Unit,
    filter: Boolean = false,
    onFilterMode: Boolean = false,
    applyFilter: () -> Unit = {},
    cancelFilter: () -> Unit = {},
    openFilter: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = tint
            )
        },
        navigationIcon = {
            IconButton(onClick = {if(onFilterMode) cancelFilter() else onBackClick() }) {
                Icon(
                    asset = if(onFilterMode) Icons.Rounded.Clear else Icons.Rounded.KeyboardBackspace,
                    tint = if(onFilterMode) MaterialTheme.colors.onSurface.copy(alpha = 0.3f) else tint
                )
            }
        },
        actions = {
            if(filter){
                IconButton(onClick = {
                    if(onFilterMode){
                        applyFilter()
                    }else{
                        openFilter()
                    }
                }) {
                    Icon(
                        asset = if(onFilterMode) Icons.Rounded.Done else Icons.Rounded.FilterAlt,
                        tint = if(onFilterMode) Color.Green else tint
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun PreviewTopBar() {
    PokedexComposeTheme(darkTheme = true) {
        TopBar(title = "Pokedex", onBackClick = {})
    }
}
