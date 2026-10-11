package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppUpgradeEntity
import com.example.data.model.MapResourceEntity
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.FireOrange
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.StatusUpcoming
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun UpgradeDownloadsScreen(
    upgradeInfo: AppUpgradeEntity?,
    mapResources: List<MapResourceEntity>,
    onStartUpgradeDownload: () -> Unit,
    onPauseUpgradeDownload: () -> Unit,
    onInstallUpgrade: () -> Unit,
    onDownloadMapResource: (MapResourceEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                "APP UPGRADES & DOWNLOADS",
                style = MaterialTheme.typography.titleLarge,
                color = GoldPrimary,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        // Section 1: In-App Version Upgrade Card
        item {
            val isDownloading = upgradeInfo?.downloadStatus == "DOWNLOADING"
            val isCompleted = upgradeInfo?.downloadStatus == "COMPLETED"
            val isInstalled = upgradeInfo?.downloadStatus == "INSTALLED"

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(GoldPrimary, CyberCyan)),
                        RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.SystemUpdate, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("APP UPDATE AVAILABLE", style = MaterialTheme.typography.titleMedium, color = GoldPrimary, fontWeight = FontWeight.Bold)
                                Text("Current: ${upgradeInfo?.currentVersion ?: "v2.3.0"}  →  Latest: ${upgradeInfo?.latestVersion ?: "v2.4.0"}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isInstalled) StatusUpcoming.copy(alpha = 0.2f) else FireOrange.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                if (isInstalled) "INSTALLED" else "v2.4.0 RELEASE",
                                color = if (isInstalled) StatusUpcoming else FireOrange,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        upgradeInfo?.releaseNotes ?: "Features high-speed live room sync, direct APK updater, Bermuda 4K resource download, and instant tournament payout system.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Live Download Progress Bar
                    val progress = upgradeInfo?.downloadProgress ?: 0f
                    val progressPercent = (progress * 100).toInt()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = when (upgradeInfo?.downloadStatus) {
                                "DOWNLOADING" -> "Downloading APK... ($progressPercent%)"
                                "PAUSED" -> "Download Paused ($progressPercent%)"
                                "COMPLETED" -> "APK Ready to Install!"
                                "INSTALLED" -> "App Upgraded to v2.4.0"
                                else -> "File Size: ${upgradeInfo?.fileSizeMb ?: 28.5f} MB"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = CyberCyan
                        )
                        Text(
                            "${upgradeInfo?.fileSizeMb ?: 28.5f} MB",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (isCompleted || isInstalled) StatusUpcoming else CyberCyan,
                        trackColor = DarkSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Buttons Action Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (isInstalled) {
                            Button(
                                onClick = { },
                                enabled = false,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    disabledContainerColor = DarkSurfaceVariant,
                                    disabledContentColor = TextMuted
                                )
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusUpcoming)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("UP TO DATE (v2.4.0)", fontWeight = FontWeight.Bold)
                            }
                        } else if (isCompleted) {
                            Button(
                                onClick = onInstallUpgrade,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = DarkBackground)
                            ) {
                                Icon(Icons.Default.SystemUpdate, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("INSTALL UPDATE NOW", fontWeight = FontWeight.Bold)
                            }
                        } else if (isDownloading) {
                            OutlinedButton(
                                onClick = onPauseUpgradeDownload,
                                modifier = Modifier.fillMaxWidth(),
                                border = androidx.compose.foundation.BorderStroke(1.dp, FireOrange)
                            ) {
                                Icon(Icons.Default.Pause, contentDescription = null, tint = FireOrange)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("PAUSE DOWNLOAD", color = FireOrange, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Button(
                                onClick = onStartUpgradeDownload,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = DarkBackground)
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("DOWNLOAD APP UPDATE (${upgradeInfo?.fileSizeMb ?: 28.5f} MB)", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Section 2: Resource & Map Pack Downloads Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.FolderZip, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "ESPORTS MAP PACKS & RESOURCES",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        items(mapResources, key = { it.id }) { item ->
            MapResourceCard(
                resource = item,
                onDownloadClick = { onDownloadMapResource(item) }
            )
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun MapResourceCard(
    resource: MapResourceEntity,
    onDownloadClick: () -> Unit
) {
    val isDownloading = resource.downloadStatus == "DOWNLOADING"
    val isCompleted = resource.downloadStatus == "COMPLETED"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(
                1.dp,
                if (isCompleted) StatusUpcoming else DarkSurfaceVariant,
                RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    val icon = when (resource.category) {
                        "MAP_PACK" -> Icons.Default.Map
                        "ANTI_CHEAT" -> Icons.Default.Security
                        else -> Icons.Default.DownloadForOffline
                    }
                    val iconTint = when (resource.category) {
                        "MAP_PACK" -> GoldPrimary
                        "ANTI_CHEAT" -> FireOrange
                        else -> CyberCyan
                    }

                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(resource.title, style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                        Text("${resource.sizeMb} MB • ${resource.category.replace("_", " ")}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    }
                }

                if (isCompleted) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(StatusUpcoming.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("DOWNLOADED", color = StatusUpcoming, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(resource.description, style = MaterialTheme.typography.bodySmall, color = TextSecondary)

            if (isDownloading) {
                Spacer(modifier = Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { resource.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = CyberCyan,
                    trackColor = DarkSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (!isCompleted) {
                Button(
                    onClick = onDownloadClick,
                    enabled = !isDownloading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberCyan,
                        contentColor = DarkBackground
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        if (isDownloading) "DOWNLOADING (${(resource.progress * 100).toInt()}%)..." else "DOWNLOAD RESOURCE (${resource.sizeMb} MB)",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
