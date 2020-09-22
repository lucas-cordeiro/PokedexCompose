package br.com.lucascordeiro.pokedex.compose.ui.splash

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onActive
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.R
import br.com.lucascordeiro.pokedex.compose.di.component.PokedexComponent

@Composable
fun Splash(
        openHome: () -> Unit
){
    val viewModel: SplashViewModel = viewModel(
            null,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return SplashViewModel(PokedexComponent().useCase) as T
                }
            }
    )
    if(viewModel.openHome)
        openHome()

    SplashScreen(
            progress = viewModel.progress,
            retry = { viewModel.initialize() },
            errorMessage = viewModel.errorMessage?:"Download error, try again",
            error = !viewModel.errorMessage.isNullOrBlank()
    )

    onActive(callback = {
        viewModel.initialize()
    })
}

@Composable
fun SplashScreen(
        progress: Float,
        retry: () -> Unit,
        errorMessage: String,
        error:Boolean,
        modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (pokeball, progressBar, text, buttonRetry) = createRefs()
        Icon(
            asset = vectorResource(id = R.drawable.ic_pokeball),
            tint = MaterialTheme.colors.onBackground.copy(alpha = 0.2f),
            modifier = Modifier
                .preferredSize(100.dp)
                .constrainAs(pokeball) {
                    bottom.linkTo(progressBar.top, 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .padding(
                    20.dp,
                    0.dp
                )
                .fillMaxWidth(1f)
                .constrainAs(progressBar) {
                    centerTo(parent)
                }
        )

        Text(
            text = if (error) errorMessage else "Loading data... please wait",
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center,
            color = if (error) MaterialTheme.colors.error else MaterialTheme.colors.onBackground,
            modifier = Modifier
                .padding(20.dp, 0.dp)
                .constrainAs(text) {
                    top.linkTo(progressBar.bottom, 5.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        Button(
            onClick = retry,
            backgroundColor = MaterialTheme.colors.error,
            modifier = Modifier
                .constrainAs(buttonRetry) {
                    top.linkTo(text.bottom, 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }.drawOpacity(if (error) 1f else 0f)
        ) {
            Text(
                text = "Retry",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onError
            )
        }
    }
}

@Preview
@Composable
fun PreviewSplashScreen(){
    SplashScreen(progress = 50f, error = false, errorMessage = "", retry = {})
}