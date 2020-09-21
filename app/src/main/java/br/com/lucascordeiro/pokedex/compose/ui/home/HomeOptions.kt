package br.com.lucascordeiro.pokedex.compose.ui.home

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.drawLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import br.com.lucascordeiro.pokedex.compose.R
import br.com.lucascordeiro.pokedex.compose.ui.theme.grey900
import br.com.lucascordeiro.pokedex.compose.ui.utils.toPx

@Composable
fun HomeOption(
        title: String,
        color: Color,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
) {
    Card(
            elevation = 1.dp,
            shape = RoundedCornerShape(20.dp),
            backgroundColor = if (MaterialTheme.colors.isLight) color else MaterialTheme.colors.surface,
            modifier = modifier.padding(8.dp).preferredHeight(70.dp).preferredWidth(220.dp)
    ) {
        ConstraintLayout(
                Modifier.clickable(onClick = onClick)
        ){
            val (pokeballTop, pokeball, text) = createRefs()
            val image = vectorResource(id = R.drawable.ic_pokeball)
            Icon(
                    asset = image,
                    tint = (if(MaterialTheme.colors.isLight) Color.White else Color.Black).copy(alpha = 0.1f),
                    modifier = Modifier
                            .constrainAs(pokeballTop){
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }
                            .preferredSize(50.dp)
                            .drawLayer(translationX = -25.dp.toPx(ContextAmbient.current), translationY = -25.dp.toPx(ContextAmbient.current),)
            )
            Icon(
                    asset = image,
                    tint = (if(MaterialTheme.colors.isLight) Color.White else Color.Black).copy(alpha = 0.1f),
                    modifier = Modifier
                            .constrainAs(pokeball) {
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }
                            .preferredSize(90.dp)
                            .drawLayer(translationX = 35.dp.toPx(ContextAmbient.current))

            )
            Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    color = if (MaterialTheme.colors.isLight) Color.White else color,
                    modifier = Modifier.constrainAs(text) {
                        start.linkTo(parent.start, 16.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }
    }
}

