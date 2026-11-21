package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.ArticleEntity
import com.example.viewmodel.KnowledgeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditArticleScreen(
    articleId: Long?,
    viewModel: KnowledgeViewModel,
    onBackClick: () -> Unit,
    onArticlePublished: (Long) -> Unit
) {
    val existingArticleFlow = remember(articleId) {
        if (articleId != null) viewModel.getArticle(articleId) else null
    }
    val existingArticle by (existingArticleFlow?.collectAsState(initial = null) ?: remember { mutableStateOf(null) })

    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Science") }
    var subcategory by remember { mutableStateOf("General") }
    var content by remember { mutableStateOf("") }
    var infoboxJson by remember { mutableStateOf("{\"Author\": \"Expert Contributor\", \"Year\": \"2026\"}") }
    var author by remember { mutableStateOf("Editor Contributor") }
    var citationsJson by remember { mutableStateOf("[{\"title\": \"Encyclopedia Source\", \"year\": \"2026\"}]") }
    var tags by remember { mutableStateOf("encyclopedia, knowledge") }
    var imageUrl by remember { mutableStateOf("https://images.unsplash.com/photo-1451187580459-43490279c0fa") }

    LaunchedEffect(existingArticle) {
        existingArticle?.let {
            title = it.title
            subtitle = it.subtitle
            category = it.category
            subcategory = it.subcategory
            content = it.content
            infoboxJson = it.infoboxJson
            author = it.author
            citationsJson = it.citationsJson
            tags = it.tags
            imageUrl = it.imageUrl
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (articleId == null) "Create New Article" else "Edit Article", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            if (title.isNotBlank() && content.isNotBlank()) {
                                if (articleId == null) {
                                    viewModel.createArticle(
                                        title = title,
                                        subtitle = subtitle,
                                        category = category,
                                        subcategory = subcategory,
                                        content = content,
                                        infoboxJson = infoboxJson,
                                        author = author,
                                        citationsJson = citationsJson,
                                        tags = tags,
                                        language = "English",
                                        imageUrl = imageUrl,
                                        onComplete = { newId -> onArticlePublished(newId) }
                                    )
                                } else {
                                    existingArticle?.let { curr ->
                                        viewModel.updateArticle(
                                            article = curr,
                                            newTitle = title,
                                            newSubtitle = subtitle,
                                            newCategory = category,
                                            newSubcategory = subcategory,
                                            newContent = content,
                                            newInfobox = infoboxJson,
                                            newCitations = citationsJson,
                                            newTags = tags,
                                            newLanguage = "English",
                                            newImageUrl = imageUrl,
                                            editor = author,
                                            changeDescription = "Updated article content",
                                            onComplete = { onArticlePublished(curr.id) }
                                        )
                                    }
                                }
                            }
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Publish")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Article Title *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = subtitle,
                onValueChange = { subtitle = it },
                label = { Text("Subtitle / Summary Line") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = subcategory,
                    onValueChange = { subcategory = it },
                    label = { Text("Subcategory") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Author / Contributor Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("Image Illustration URL") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Main Article Content (Rich text / paragraphs) *") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = infoboxJson,
                onValueChange = { infoboxJson = it },
                label = { Text("Infobox Quick Facts (JSON format)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = citationsJson,
                onValueChange = { citationsJson = it },
                label = { Text("References & Citations (JSON format)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = tags,
                onValueChange = { tags = it },
                label = { Text("Tags (comma separated)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        }
    }
}
