package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.viewmodel.KnowledgeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantDialog(
    articleTitle: String,
    articleContent: String,
    viewModel: KnowledgeViewModel,
    onDismiss: () -> Unit
) {
    val aiResponse by viewModel.aiResponse.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()
    var customPrompt by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.requestAiAssistance("Provide a professional encyclopedia summary and key insights.", "$articleTitle: $articleContent")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Gemini AI Knowledge Assistant")
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Analyzing article: $articleTitle",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.requestAiAssistance("Summarize this article", articleContent) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Summarize")
                    }
                    OutlinedButton(
                        onClick = { viewModel.requestAiAssistance("Suggest an article outline", articleContent) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Outline")
                    }
                }

                OutlinedTextField(
                    value = customPrompt,
                    onValueChange = { customPrompt = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ask anything about this topic...") },
                    trailingIcon = {
                        IconButton(onClick = {
                            if (customPrompt.isNotBlank()) {
                                viewModel.requestAiAssistance(customPrompt, articleContent)
                                customPrompt = ""
                            }
                        }) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = "Ask AI")
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "AI Response:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (isAiLoading) {
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Consulting Gemini AI...", style = MaterialTheme.typography.bodyMedium)
                        } else {
                            Text(
                                text = aiResponse ?: "Ready to assist.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                viewModel.clearAiResponse()
                onDismiss()
            }) {
                Text("Close")
            }
        }
    )
}
