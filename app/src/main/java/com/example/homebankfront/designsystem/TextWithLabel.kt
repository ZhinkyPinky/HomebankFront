package com.example.homebankfront.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
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
    labelColor: Color = MaterialTheme.colorScheme.onSurface,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
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
            color = labelColor,
            fontSize = 12.sp
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
        TextWithLabel(
            label = "Label",
            text = "Text"
        )
    }
}