package br.com.lucascordeiro.pokedex.compose.ui.home

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.ui.components.StaggeredVerticalGrid
import br.com.lucascordeiro.pokedex.compose.ui.theme.*

@Composable
fun Home(
        openPokedex: () -> Unit
){
    HomeScreen(openPokedex)
}

@Composable
fun HomeScreen(
        openPokedex: () -> Unit
){
    Surface(
            modifier = Modifier.clip(RoundedCornerShape(
                    topLeft = 0.dp,
                    topRight = 0.dp,
                    bottomLeft = 20.dp,
                    bottomRight = 20.dp
            ))
    ) {
        ConstraintLayout {
            val (title, options) = createRefs()

            Text(
                    text = "What Pokemon are you looking for?",
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier
                            .padding(20.dp,0.dp)
                            .constrainAs(title){
                                top.linkTo(parent.top, 30.dp)
                            }
            )

            StaggeredVerticalGrid(
                    maxColumnWidth = 220.dp,
                    modifier = Modifier
                            .padding(4.dp)
                            .constrainAs(options) {
                        top.linkTo(title.bottom, 20.dp)
                        bottom.linkTo(parent.bottom, 20.dp)
                    }
            ) {
                HomeOption(title = "Pokedex", color = grassLight, onClick = { openPokedex() })
                HomeOption(title = "Movies", color = fireLight, onClick = {})
                HomeOption(title = "Abilities", color = waterLight, onClick = {})
                HomeOption(title = "Items", color = electricLight, onClick = {})
                HomeOption(title = "Locations", color = poisonLight, onClick = {})
                HomeOption(title = "Types", color = groundLight, onClick = {})
            }


        }
    }
}



@Preview
@Composable
fun PreviewHomeScreen(){
    PokedexComposeTheme(darkTheme = true) {
        HomeScreen(
                openPokedex = {}
        )
    }
}