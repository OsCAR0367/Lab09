package com.estiven.lab09

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun PantallaPosts(navController: NavHostController, servicio: PostApiService) {
    var listaPosts by remember { mutableStateOf<List<PostModel>>(emptyList()) }
    var estaCargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            estaCargando = true
            listaPosts = servicio.getUserPosts()
            estaCargando = false
        } catch (e: Exception) {
            error = "Error al cargar los posts: ${e.message}"
            estaCargando = false
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        when {
            estaCargando -> {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
            error != null -> {
                Text(error!!, color = Color.Red, modifier = Modifier.padding(16.dp))
            }
            listaPosts.isEmpty() -> {
                Text("No se encontraron posts", modifier = Modifier.padding(16.dp))
            }
            else -> {
                LazyColumn {
                    items(listaPosts) { item ->
                        PostItem(item, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun PostItem(item: PostModel, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = item.id.toString(),
                modifier = Modifier.weight(0.1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = item.title,
                modifier = Modifier.weight(0.7f),
                maxLines = 1
            )
            IconButton(
                onClick = {
                    navController.navigate("postsVer/${item.id}")
                    Log.d("POSTS", "ID = ${item.id}")
                },
                modifier = Modifier.weight(0.2f)
            ) {
                Icon(Icons.Outlined.Search, contentDescription = "Ver")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenPost(navController: NavHostController, servicio: PostApiService, id: Int) {
    var post by remember { mutableStateOf<PostModel?>(null) }
    var estaCargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(id) {
        try {
            estaCargando = true
            post = servicio.getUserPostById(id)
            estaCargando = false
        } catch (e: Exception) {
            error = "Error al cargar el post: ${e.message}"
            estaCargando = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            estaCargando -> {
                CircularProgressIndicator()
            }
            error != null -> {
                Text(error!!, color = Color.Red)
            }
            post != null -> {
                OutlinedTextField(
                    value = post!!.id.toString(),
                    onValueChange = {},
                    label = { Text("ID") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = post!!.userId.toString(),
                    onValueChange = {},
                    label = { Text("User ID") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = post!!.title,
                    onValueChange = {},
                    label = { Text("Título") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = post!!.body,
                    onValueChange = {},
                    label = { Text("Contenido") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4
                )
            }
        }
    }
}