package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ArticleEntity::class, RevisionEntity::class], version = 1, exportSchema = false)
abstract class KnowledgeDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao

    companion object {
        @Volatile
        private var INSTANCE: KnowledgeDatabase? = null

        fun getDatabase(context: Context): KnowledgeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KnowledgeDatabase::class.java,
                    "knowledge_database"
                )
                    .addCallback(KnowledgeDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class KnowledgeDatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database.articleDao())
                    }
                }
            }

            suspend fun populateInitialData(dao: ArticleDao) {
                val sample1 = ArticleEntity(
                    title = "Quantum Computing",
                    subtitle = "Computation utilizing quantum mechanics phenomena",
                    category = "Science",
                    subcategory = "Physics",
                    content = "Quantum computing is a rapidly-emerging technology that harnesses the laws of quantum mechanics to solve problems too complex for classical computers. While classical computers encode information into binary bits (0 or 1), quantum computers utilize qubits, which can exist in a superposition of states.\n\nKey Principles:\n- Superposition: Qubits can represent combinations of both 0 and 1 simultaneously.\n- Entanglement: Quantum links between qubits allow instantaneous correlation.\n- Interference: Amplifying correct computational paths while canceling incorrect ones.\n\nApplications include cryptography, molecular modeling, material science, and optimization problems in logistics and artificial intelligence.",
                    infoboxJson = "{\"Paradigm\": \"Quantum mechanics\", \"Primary Developer\": \"Global Research Labs\", \"First Proposed\": \"1980\", \"Core Unit\": \"Qubit\"}",
                    author = "Dr. Alan Turing Jr.",
                    status = "Published",
                    citationsJson = "[{\"title\": \"Quantum Mechanics and Computation\", \"author\": \"Nielsen & Chuang\", \"year\": \"2010\"}]",
                    tags = "physics, technology, computing, quantum",
                    language = "English",
                    viewCount = 1420,
                    imageUrl = "https://images.unsplash.com/photo-1635070041078-e363dbe005cb"
                )
                val sample2 = ArticleEntity(
                    title = "Amazon Rainforest",
                    subtitle = "The world's largest tropical rainforest",
                    category = "Geography",
                    subcategory = "Physical Geography",
                    content = "The Amazon rainforest, covering much of northwestern Brazil and extending into Colombia, Peru and other South American countries, is the world's largest tropical rainforest, famed for its biodiversity. It is crisscrossed by thousands of rivers, including the mighty Amazon River.\n\nBiodiversity:\n- Home to over 3 million species of plants and animals.\n- Approximately 10% of known world species reside here.\n- Vital carbon sink for global climate stability.\n\nConservation efforts focus on halting deforestation caused by agriculture, illegal logging, and mining activities.",
                    infoboxJson = "{\"Area\": \"5,500,000 km²\", \"Countries\": \"Brazil, Peru, Colombia, Venezuela\", \"Biome\": \"Tropical rainforest\", \"River\": \"Amazon River\"}",
                    author = "Maria Silva",
                    status = "Published",
                    citationsJson = "[{\"title\": \"Amazon Biodiversity Report\", \"author\": \"WWF Conservation\", \"year\": \"2022\"}]",
                    tags = "geography, rainforest, nature, amazon, wildlife",
                    language = "English",
                    viewCount = 2890,
                    imageUrl = "https://images.unsplash.com/photo-1516026662326-147d4b26fe60"
                )
                val sample3 = ArticleEntity(
                    title = "Ancient Roman Empire",
                    subtitle = "Post-republican period of ancient Rome",
                    category = "History",
                    subcategory = "Ancient History",
                    content = "The Roman Empire was the post-republican state of ancient Rome. It is generally understood to mean the period and territory ruled by Romans following the rise of Augustus Caesar in 27 BC.\n\nKey Eras:\n- Pax Romana: Two centuries of relative peace and stability across the Mediterranean world.\n- Technological Advancements: Aqueducts, concrete architecture, complex road networks, and legal codes.\n- Fall of Western Empire: 476 AD, marking the transition to the medieval European era.",
                    infoboxJson = "{\"Capital\": \"Rome / Constantinople\", \"Languages\": \"Latin, Greek\", \"Government\": \"Autocratic Monarchy / Empire\", \"Founded\": \"27 BC\"}",
                    author = "Marcus Aurelius Historian",
                    status = "Published",
                    citationsJson = "[{\"title\": \"The History of the Decline and Fall of the Roman Empire\", \"author\": \"Edward Gibbon\", \"year\": \"1776\"}]",
                    tags = "history, rome, antiquity, empire",
                    language = "English",
                    viewCount = 3150,
                    imageUrl = "https://images.unsplash.com/photo-1552832230-c0197dd311b5"
                )
                val sample4 = ArticleEntity(
                    title = "Artificial Intelligence",
                    subtitle = "Intelligence demonstrated by machines",
                    category = "Science",
                    subcategory = "Technology",
                    content = "Artificial intelligence (AI) is the intelligence of machines or software, as opposed to the intelligence of living beings, primarily of humans. It is a field of study in computer science that develops and studies intelligent machines.\n\nSubfields:\n- Machine Learning: Algorithms that improve through experience.\n- Deep Learning: Neural networks with multiple processing layers.\n- Natural Language Processing: Understanding and generating human language.\n- Computer Vision: Extracting meaningful information from digital images.",
                    infoboxJson = "{\"Pioneers\": \"Alan Turing, John McCarthy\", \"Major Milestone\": \"Deep Learning Era\", \"Primary Languages\": \"Python, C++\", \"Applications\": \"Healthcare, Robotics, NLP\"}",
                    author = "TechResearcher",
                    status = "Published",
                    citationsJson = "[{\"title\": \"Artificial Intelligence: A Modern Approach\", \"author\": \"Stuart Russell & Peter Norvig\", \"year\": \"2020\"}]",
                    tags = "ai, technology, machine learning, science",
                    language = "English",
                    viewCount = 5400,
                    imageUrl = "https://images.unsplash.com/photo-1677442136019-21780efad99a"
                )
                val sample5 = ArticleEntity(
                    title = "Renaissance Art",
                    subtitle = "Cultural movement bridging Middle Ages and modern history",
                    category = "Culture",
                    subcategory = "Art & Literature",
                    content = "Renaissance art is the painting, sculpture, and decorative arts of the period of European history known as the Renaissance, emerging as a distinct style in Italy in about 1400, in parallel with developments which occurred in philosophy, literature, music, science, and technology.\n\nCharacteristics:\n- Linear perspective and realism.\n- Humanism and anatomical accuracy.\n- Masterworks by Leonardo da Vinci, Michelangelo, and Raphael.",
                    infoboxJson = "{\"Period\": \"14th to 17th century\", \"Birthplace\": \"Florence, Italy\", \"Key Figures\": \"Leonardo da Vinci, Michelangelo, Raphael\", \"Focus\": \"Humanism, Perspective\"}",
                    author = "Elena Vance",
                    status = "Published",
                    citationsJson = "[{\"title\": \"The Italian Renaissance\", \"author\": \"J.H. Plumb\", \"year\": \"1961\"}]",
                    tags = "art, history, culture, renaissance, italy",
                    language = "English",
                    viewCount = 1890,
                    imageUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675"
                )

                dao.insertArticle(sample1)
                dao.insertArticle(sample2)
                dao.insertArticle(sample3)
                dao.insertArticle(sample4)
                dao.insertArticle(sample5)
            }
        }
    }
}
