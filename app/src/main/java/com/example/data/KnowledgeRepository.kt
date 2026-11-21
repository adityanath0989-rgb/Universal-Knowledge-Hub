package com.example.data

import kotlinx.coroutines.flow.Flow

class KnowledgeRepository(private val articleDao: ArticleDao) {
    val allArticles: Flow<List<ArticleEntity>> = articleDao.getAllArticles()

    fun getArticleById(id: Long): Flow<ArticleEntity?> = articleDao.getArticleById(id)

    fun searchArticles(query: String): Flow<List<ArticleEntity>> = articleDao.searchArticles(query)

    fun getArticlesByCategory(category: String): Flow<List<ArticleEntity>> = articleDao.getArticlesByCategory(category)

    fun getSavedArticles(): Flow<List<ArticleEntity>> = articleDao.getSavedArticles()

    fun getArticlesByAuthor(author: String): Flow<List<ArticleEntity>> = articleDao.getArticlesByAuthor(author)

    suspend fun insertArticle(article: ArticleEntity): Long {
        val id = articleDao.insertArticle(article)
        articleDao.insertRevision(
            RevisionEntity(
                articleId = id,
                editorId = article.author,
                changeDescription = "Initial publication",
                previousContent = "",
                updatedContent = article.content
            )
        )
        return id
    }

    suspend fun updateArticle(article: ArticleEntity, editor: String, changeDescription: String) {
        val oldArticleFlow = articleDao.getArticleById(article.id)
        // We update the article directly
        articleDao.updateArticle(article)
        articleDao.insertRevision(
            RevisionEntity(
                articleId = article.id,
                editorId = editor,
                changeDescription = changeDescription,
                previousContent = "",
                updatedContent = article.content
            )
        )
    }

    suspend fun deleteArticle(article: ArticleEntity) {
        articleDao.deleteArticle(article)
    }

    suspend fun updateBookmark(id: Long, isSaved: Boolean) {
        articleDao.updateBookmark(id, isSaved)
    }

    suspend fun incrementViewCount(id: Long) {
        articleDao.incrementViewCount(id)
    }

    fun getRevisions(articleId: Long): Flow<List<RevisionEntity>> = articleDao.getRevisionsForArticle(articleId)
}
