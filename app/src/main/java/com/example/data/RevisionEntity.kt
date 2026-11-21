package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "revisions")
data class RevisionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val articleId: Long,
    val editorId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val changeDescription: String,
    val previousContent: String,
    val updatedContent: String
)
