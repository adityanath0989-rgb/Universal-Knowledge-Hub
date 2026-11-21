package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val subtitle: String,
    val category: String,
    val subcategory: String,
    val content: String,
    val infoboxJson: String = "{}",
    val author: String,
    val updatedAt: Long = System.currentTimeMillis(),
    val status: String = "Published", // Published, Draft, Needs References, Protected
    val citationsJson: String = "[]",
    val tags: String = "",
    val language: String = "English",
    val isSaved: Boolean = false,
    val viewCount: Int = 0,
    val imageUrl: String = ""
)
