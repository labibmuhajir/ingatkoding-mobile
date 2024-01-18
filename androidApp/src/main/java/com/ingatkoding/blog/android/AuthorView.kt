package com.ingatkoding.blog.android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ingatkoding.blog.model.Author

@Composable
fun AuthorView(author: Author) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AsyncImage(
            model = author.imageUrl,
            contentDescription = "author photo",
            modifier = Modifier
                .width(32.dp)
                .clip(CircleShape),
        )
        Text(
            text = author.name, style = MaterialTheme.typography.titleSmall.plus(
                TextStyle(color = Color.Gray)
            )
        )
    }
}