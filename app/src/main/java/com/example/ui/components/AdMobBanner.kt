package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

@Composable
fun AdMobBanner(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Advertisement",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(vertical = 2.dp)
        )
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { ctx ->
                try {
                    MobileAds.initialize(ctx) {}
                } catch (e: Exception) {
                    // ignore initialization errors in test/offline
                }
                AdView(ctx).apply {
                    setAdSize(AdSize.BANNER)
                    // Google test ad unit ID
                    adUnitId = "ca-app-pub-3940256099942544/6300978111"
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}
