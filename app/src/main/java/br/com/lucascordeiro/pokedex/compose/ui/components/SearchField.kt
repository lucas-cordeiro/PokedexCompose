package br.com.lucascordeiro.pokedex.compose.ui.components

import android.util.Log
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.ui.theme.PokedexComposeTheme
import br.com.lucascordeiro.pokedex.compose.ui.theme.purple200

@Composable
fun SearchField(
        value:String,
        hint: String,
        iconTint: Color = MaterialTheme.colors.onSurface,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier
) {
    TextField(
            leadingIcon = {
                Icon(
                        asset = Icons.Default.Search,
                        tint = iconTint
                )
            },
            shape = RoundedCornerShape(20.dp),
            activeColor = MaterialTheme.colors.surface.copy(alpha = 0.0f),
            inactiveColor = MaterialTheme.colors.surface,
            value = value,
            placeholder = {
                Text(
                        text = hint,
                        style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = TextUnit.Companion.Sp(16),
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                        )
                )
            },
            textStyle = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = TextUnit.Companion.Sp(16),
                    color = MaterialTheme.colors.onSurface
            ),
            keyboardType = KeyboardType.Text,
            onValueChange = onValueChange,
            modifier = modifier
    )
}

@Preview
@Composable
fun SearchFieldPreview(){
    PokedexComposeTheme(darkTheme = true) {
        SearchField(value = "", hint = "Search",onValueChange = {})
    }
}