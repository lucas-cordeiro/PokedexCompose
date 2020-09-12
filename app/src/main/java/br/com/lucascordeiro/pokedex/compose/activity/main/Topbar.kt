package br.com.lucascordeiro.pokedex.compose.activity.main

import androidx.compose.foundation.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.ui.PokedexComposeTheme

@Composable
fun TopBar(
    title: String,
    modifier: Modifier = Modifier
){
    TopAppBar(
        title = {
            Text(text = title)
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun PreviewTopBar(){
    PokedexComposeTheme(darkTheme = true) {
        TopBar(title = "Pokedex")
    }
}