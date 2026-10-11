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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.FireOrange
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class LeaderboardPlayer(
    val rank: Int,
    val name: String,
    val kills: Int,
    val earnings: Int,
    val badge: String
)

@Composable
fun LeaderboardScreen(modifier: Modifier = Modifier) {
    val topPlayers = listOf(
        LeaderboardPlayer(1, "⚡ Thunder_FF", 182, 4500, "👑 Grandmaster"),
        LeaderboardPlayer(2, "🔥 Phoenix_Killer", 164, 3900, "🏅 Heroic V"),
        LeaderboardPlayer(3, "🎯 Headshot_God", 149, 3400, "🏅 Heroic IV"),
        LeaderboardPlayer(4, "Pro_Sniper_FF (You)", 128, 2850, "💎 Diamond IV"),
        LeaderboardPlayer(5, "🛡️ Shield_Ninja", 115, 2400, "💎 Diamond III"),
        LeaderboardPlayer(6, "☠️ Venom_Slayer", 102, 2100, "💎 Diamond II"),
        LeaderboardPlayer(7, "🚀 Cyber_Rush", 94, 1850, "💎 Diamond I"),
        LeaderboardPlayer(8, "🎮 Sultan_FF", 88, 1600, "🏆 Platinum IV")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                "FREE FIRE TOP FRAGGERS",
                style = MaterialTheme.typography.titleLarge,
                color = GoldPrimary,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        // Podium Top 3 Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(GoldPrimary, CyberCyan)),
                        RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "WEEKLY HALL OF FAME",
                            style = MaterialTheme.typography.titleMedium,
                            color = GoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // Rank 2
                        PodiumColumn(topPlayers[1], rankColor = Color(0xFFC0C0C0), heightDp = 100)
                        // Rank 1
                        PodiumColumn(topPlayers[0], rankColor = GoldPrimary, heightDp = 125)
                        // Rank 3
                        PodiumColumn(topPlayers[2], rankColor = Color(0xFFCD7F32), heightDp = 85)
                    }
                }
            }
        }

        // Leaderboard List
        itemsIndexed(topPlayers) { index, player ->
            val isUser = player.name.contains("(You)")
            val rankColor = when (player.rank) {
                1 -> GoldPrimary
                2 -> Color(0xFFC0C0C0)
                3 -> Color(0xFFCD7F32)
                else -> TextMuted
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(
                        1.dp,
                        if (isUser) CyberCyan else DarkSurfaceVariant,
                        RoundedCornerShape(10.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isUser) DarkSurfaceVariant else DarkSurface
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(rankColor.copy(alpha = 0.2f))
                                .border(1.dp, rankColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "#${player.rank}",
                                color = rankColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                player.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isUser) CyberCyan else TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                player.badge,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(horizontalAlignment = Alignment.End) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MilitaryTech, contentDescription = null, tint = FireOrange, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("${player.kills} Kills", style = MaterialTheme.typography.titleSmall, color = FireOrange, fontWeight = FontWeight.Bold)
                            }
                            Text("${player.earnings} Coins", style = MaterialTheme.typography.labelSmall, color = GoldPrimary)
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun PodiumColumn(player: LeaderboardPlayer, rankColor: Color, heightDp: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.Star, contentDescription = null, tint = rankColor, modifier = Modifier.size(20.dp))
        Text(
            player.name.substringBefore(" "),
            style = MaterialTheme.typography.labelSmall,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
        Text(
            "${player.kills} Kills",
            style = MaterialTheme.typography.labelSmall,
            color = FireOrange
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .width(75.dp)
                .height(heightDp.dp)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(rankColor.copy(alpha = 0.3f))
                .border(1.dp, rankColor, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "#${player.rank}",
                style = MaterialTheme.typography.headlineSmall,
                color = rankColor,
                fontWeight = FontWeight.Black
            )
        }
    }
}
