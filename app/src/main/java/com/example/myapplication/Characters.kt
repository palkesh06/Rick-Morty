package com.example.myapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun CharactersScreen(modifier: Modifier = Modifier) {
    val itemsList = listOf(
        "This is the element 1 ",
        "This is the element 2",
        "This is the element 3",
        "This is the element 4",
        "This is the element 5",
        "This is the element 6",
        "This is the element 7",
        "This is the element 8",
        "This is the element 9",
        "This is the element 10",
        "This is the element 11",
        "This is the element 12",
        "This is the element 13",
        "This is the element 14",
        "This is the element 15",
        "This is the element 16",
        "This is the element 17",
        "This is the element 18",
        "This is the element 19",
        "This is the element 20"
    )

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(itemsList) { item ->
            CharacterCard(item)
        }
    }
}

@Composable
fun CharacterCard(item: String) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentSize()
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://rickandmortyapi.com/api/character/avatar/1.jpeg"),
                contentDescription = "Character image",
                modifier = Modifier
                    .size(128.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = item,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewScreen(modifier: Modifier = Modifier) {
    CharactersScreen()
}