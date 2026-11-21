package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.ArticleEntity
import com.example.viewmodel.KnowledgeViewModel
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    articleId: Long,
    viewModel: KnowledgeViewModel,
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    onHistoryClick: (Long) -> Unit
) {
    val context = LocalContext.current
    val articleFlow = remember(articleId) { viewModel.getArticle(articleId) }
    val article by articleFlow.collectAsState(initial = null)
    var showAiDialog by remember { mutableStateOf(false) }

    LaunchedEffect(articleId) {
        viewModel.incrementViewCount(articleId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(article?.title ?: "Article", maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    article?.let { currentArticle ->
                        // AI Assistant Button
                        IconButton(onClick = { showAiDialog = true }) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = "AI Assistant")
                        }
                        // Bookmark Button
                        IconButton(onClick = { viewModel.toggleBookmark(currentArticle.id, currentArticle.isSaved) }) {
                            Icon(
                                imageVector = if (currentArticle.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (currentArticle.isSaved) MaterialTheme.colorScheme.primary else LocalContentColor.current
                            )
                        }
                        // Edit Button
                        IconButton(onClick = { onEditClick(currentArticle.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        // History Button
                        IconButton(onClick = { onHistoryClick(currentArticle.id) }) {
                            Icon(Icons.Default.History, contentDescription = "History")
                        }
                        // Share Button
                        IconButton(onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Check out '${currentArticle.title}' on Universal Knowledge Hub:\n\n${currentArticle.subtitle}")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Article"))
                        }) {
                            Icon(Icons.Default.Share, contentDescription = "Share")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (article == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val curr = article!!
            val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(curr.updatedAt))

            val infoboxMap = remember(curr.infoboxJson) {
                val map = mutableMapOf<String, String>()
                try {
                    if (curr.infoboxJson.isNotBlank() && curr.infoboxJson != "{}") {
                        val json = JSONObject(curr.infoboxJson)
                        val keys = json.keys()
                        while (keys.hasNext()) {
                            val key = keys.next()
                            map[key] = json.getString(key)
                        }
                    }
                } catch (e: Exception) {
                    // ignore
                }
                map.toMap()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Category & Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Badge(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                        Text(text = curr.category, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                    Text(
                        text = "Last updated: $dateStr",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                // Title & Subtitle
                Text(
                    text = curr.title,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = curr.subtitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Contributor: ${curr.author} • Views: ${curr.viewCount}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )

                // Image if available
                if (curr.imageUrl.isNotBlank()) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            AsyncImage(
                                model = curr.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp),
                                contentScale = ContentScale.Crop
                            )
                            Box(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Illustration for ${curr.title}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }

                // Infobox Card if present
                if (infoboxMap.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Quick Facts",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Divider()
                            for ((key, value) in infoboxMap) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = key, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                                    Text(text = value, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }

                // Main Description / Content
                Divider()
                Text(
                    text = curr.content,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.3f
                )

                // Citations & References
                if (curr.citationsJson.isNotBlank() && curr.citationsJson != "[]") {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "References & Sources",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Divider()
                            Text(
                                text = curr.citationsJson,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            if (showAiDialog) {
                AiAssistantDialog(
                    articleTitle = curr.title,
                    articleContent = curr.content,
                    viewModel = viewModel,
                    onDismiss = { showAiDialog = false }
                )
            }
        }
    }
}
