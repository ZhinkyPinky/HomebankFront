package com.example.homebankfront.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.homebankfront.ui.theme.HomeBankFrontTheme
import com.example.homebankfront.ui.theme.ThemePreviews

@Composable
fun TextWithLabel(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textSoftWrap: Boolean = false,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    backgroundColor: Color = Color.Transparent
) {
    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .then(modifier)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
        )

        Text(
            text = text,
            color = textColor,
            softWrap = textSoftWrap,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@ThemePreviews
@Composable
fun TextWithLabelPreview() {
    HomeBankFrontTheme {
        Surface {
            TextWithLabel(
                label = "Label",
                text = "Text"
            )
        }
    }
}