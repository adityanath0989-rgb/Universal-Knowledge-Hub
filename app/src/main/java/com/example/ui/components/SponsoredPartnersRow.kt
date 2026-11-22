package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class SponsorPartner(
    val name: String,
    val category: String,
    val description: String,
    val badge: String
)

@Composable
fun SponsoredPartnersRow(onSponsorClick: (SponsorPartner) -> Unit) {
    val sponsors = listOf(
        SponsorPartner("Google Cloud", "Cloud & AI Partner", "Powering global scale infrastructure and Gemini AI intelligence.", "Platinum Sponsor"),
        SponsorPartner("JetBrains", "Developer Ecosystem", "Creators of Kotlin, Android Studio, and advanced IDE developer tools.", "Technology Partner"),
        SponsorPartner("GitHub", "Code Collaboration", "The complete developer platform to build, scale, and secure software.", "Ecosystem Partner"),
        SponsorPartner("NASA Research", "Science & Space", "Advancing aerospace research, astronomy, and Earth science data.", "Scientific Partner"),
        SponsorPartner("Open Knowledge Foundation", "Global Education", "Empowering open access to human knowledge and universal education.", "Global Partner")
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Featured Corporate Partners & Sponsors",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Sponsored",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(sponsors) { sponsor ->
                Card(
                    modifier = Modifier
                        .width(240.dp)
                        .height(130.dp)
                        .clickable { onSponsorClick(sponsor) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Business,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = sponsor.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = sponsor.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = sponsor.category,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                            Badge(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                                Text(
                                    text = sponsor.badge,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
