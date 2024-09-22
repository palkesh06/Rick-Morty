package com.example.myapplication.ui.navigationScreens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.Data.dataclass.characters.Result
import com.example.myapplication.ui.viewModel.CharactersViewModel
import com.example.myapplication.ui.viewModelFactory.CharactersViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(modifier: Modifier = Modifier) {

    val viewModel: CharactersViewModel = viewModel(
        factory = CharactersViewModelFactory()
    )

    // Observe LiveData from the ViewModel
    val characters by viewModel.characters.observeAsState()
    val loading by viewModel.loading.observeAsState(false)
    val error by viewModel.error.observeAsState()

    // State to track the selected character
    var selectedCharacter by remember { mutableStateOf<Result?>(null) }

    // State for the Bottom Sheet
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetShadowElevation = 16.dp,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            selectedCharacter?.let { CharacterDetailScreen(it) }
        },
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Characters",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }

            // Show loading spinner if data is being loaded
            if (loading && viewModel.currentPage == 1) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                return@BottomSheetScaffold
            }

            // Show error message if there was an error
            if (error != null) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Warning sign",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = error ?: "Unknown error",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                return@BottomSheetScaffold
            }

            if (characters != null) {
                // store state of list
                val listState = rememberLazyGridState()

                // observe list scrolling
                val reachedBottom: Boolean by remember {
                    derivedStateOf {
                        val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                        lastVisibleItem?.index != 0 && lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - 1
                    }
                }

                // load more if scrolled to bottom
                LaunchedEffect(reachedBottom) {
                    if (reachedBottom && !viewModel.isLastPage) viewModel.loadCharacters()
                }

                Column(
                    modifier = Modifier.padding(bottom = if (loading) 32.dp else 0.dp)
                ) {
                    LazyVerticalGrid(
                        state = listState,
                        columns = GridCells.Adaptive(minSize = 128.dp),
                        modifier = modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        characters?.let {
                            items(it.results) { character ->
                                CharacterCard(character) {
                                    selectedCharacter = character
                                    scope.launch {
                                        scaffoldState.bottomSheetState.expand()
                                    }
                                }
                            }
                        }
                    }
                    if (loading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterCard(character: Result, onClick: () ->Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentSize()
            .clickable(enabled = true, onClick = onClick)
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
                painter = rememberAsyncImagePainter(character.image),
                contentDescription = "Character image",
                modifier = Modifier
                    .size(128.dp)
            )
            Text(
                text = character.name,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Status: " + character.status,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun CharacterDetailScreen(character: Result) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))
    ) {
        // Header
        Text(
            text = "About Character",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            // Character Image
            Image(
                painter = rememberAsyncImagePainter(model = character.image),
                contentDescription = "Character image",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                    .padding(4.dp) // Padding around the image
            )

            // Character Details
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Name: ${character.name}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(bottom = 8.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Status: ${character.status}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = when (character.status) {
                            "Alive" -> Color.Green
                            "Dead" -> Color.Red
                            else -> Color.Gray
                        }
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Species: ${character.species}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // Space between sections

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Origin: ${character.origin.name}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Location: ${character.location.name}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Gender: ${character.gender}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Type: ${character.type.ifBlank { "N/A" }}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (character.episode.isNotEmpty()) {
                Text(
                    text = "Episodes: ${character.episode.size}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Text(
                text = "Created At: ${character.created.toGMTString()}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}



@Composable
@Preview(showBackground = true)
fun PreviewCharactersScreen(modifier: Modifier = Modifier) {
    CharactersScreen()
}