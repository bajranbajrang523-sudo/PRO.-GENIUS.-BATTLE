package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.TournamentEntity
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.FireOrange
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.StatusLive
import com.example.ui.theme.StatusUpcoming
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MatchFilter

import androidx.compose.material.icons.filled.Gavel

@Composable
fun TournamentLobby(
    tournaments: List<TournamentEntity>,
    selectedFilter: MatchFilter = MatchFilter.ALL,
    onSelectFilter: (MatchFilter) -> Unit = {},
    onJoinClick: (TournamentEntity) -> Unit = {},
    onViewRoomClick: (TournamentEntity) -> Unit = {},
    onOpenRulesClick: (TournamentEntity) -> Unit = {},
    onOpenUpgradeScreen: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Hero Esports Banner
        item {
            HeroBannerCard(onOpenUpgradeScreen = onOpenUpgradeScreen)
        }

        // Best Tournament Match Patterns Showcase
        item {
            TournamentPatternsShowcaseCard()
        }

        // Filter Chips Row
        item {
            FilterChipsRow(
                selectedFilter = selectedFilter,
                onSelectFilter = onSelectFilter
            )
        }

        // Tournament List Items
        items(tournaments, key = { it.id }) { tournament ->
            TournamentCard(
                tournament = tournament,
                onJoinClick = { onJoinClick(tournament) },
                onViewRoomClick = { onViewRoomClick(tournament) },
                onOpenRulesClick = { onOpenRulesClick(tournament) }
            )
        }

        if (tournaments.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.SportsEsports,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "No tournaments match this filter",
                            color = TextMuted,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MatchesScreen(
    tournaments: List<TournamentEntity>,
    selectedFilter: MatchFilter,
    onSelectFilter: (MatchFilter) -> Unit,
    onJoinClick: (TournamentEntity) -> Unit,
    onViewRoomClick: (TournamentEntity) -> Unit,
    onOpenRulesClick: (TournamentEntity) -> Unit = {},
    onOpenUpgradeScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    TournamentLobby(
        tournaments = tournaments,
        selectedFilter = selectedFilter,
        onSelectFilter = onSelectFilter,
        onJoinClick = onJoinClick,
        onViewRoomClick = onViewRoomClick,
        onOpenRulesClick = onOpenRulesClick,
        onOpenUpgradeScreen = onOpenUpgradeScreen,
        modifier = modifier
    )
}

@Composable
fun HeroBannerCard(onOpenUpgradeScreen: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(170.dp)
            .border(1.dp, Brush.horizontalGradient(listOf(GoldPrimary, CyberCyan)), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_banner_1784956794508),
                contentDescription = "Esports Banner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, DarkBackground.copy(alpha = 0.85f))
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(FireOrange)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("SEASON 12 BATTLE", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(CyberCyan.copy(alpha = 0.2f))
                            .border(1.dp, CyberCyan, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                            .clickable { onOpenUpgradeScreen() }
                    ) {
                        Text("⚡ UPDATER & MAPS", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "FREE FIRE CHAMPIONS LEAGUE",
                    style = MaterialTheme.typography.titleLarge,
                    color = GoldPrimary,
                    fontWeight = FontWeight.Black
                )
                Text(
                    "Compete in Daily Rooms • Earn Real Coins • Automated Payouts",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextPrimary
                )
            }
        }
    }
}

@Composable
fun TournamentPatternsShowcaseCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.SportsEsports,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "BEST TOURNAMENT MATCH PATTERNS",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldPrimary,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(CyberCyan.copy(alpha = 0.2f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        "OFFICIAL ESPORTS RULES",
                        color = CyberCyan,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            data class PatternItem(
                val icon: String,
                val mode: String,
                val map: String,
                val fee: String,
                val prize: String,
                val killBonus: String,
                val slots: String,
                val tag: String
            )

            val patterns = listOf(
                PatternItem("🔥", "SOLO DEATHMATCH", "Bermuda", "20 Coins", "600 Coins", "+10 Coins", "48 Slots", "Most Popular"),
                PatternItem("⚔️", "SQUAD CHAMPIONS", "Kalahari", "50 Coins", "1500 Coins", "+25 Coins", "12 Squads", "High Stakes"),
                PatternItem("🎯", "DUO SURVIVAL RUSH", "Purgatory", "15 Coins", "400 Coins", "+8 Coins", "24 Teams", "Top Survival"),
                PatternItem("⚡", "4v4 CLASH SQUAD", "Nexterra", "30 Coins", "800 Coins", "+20 Coins", "8 Teams", "Rapid Action"),
                PatternItem("🎁", "PRACTICE SCRIM", "Alpine", "FREE", "250 Coins", "+5 Coins", "48 Slots", "Zero Entry Fee")
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(patterns) { p ->
                    Box(
                        modifier = Modifier
                            .width(185.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkBackground)
                            .border(1.dp, DarkSurfaceVariant, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("${p.icon} ${p.mode}", style = MaterialTheme.typography.labelSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Map: ${p.map} • ${p.slots}", style = MaterialTheme.typography.labelSmall, color = CyberCyan, fontSize = 10.sp)

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("ENTRY", fontSize = 8.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                                    Text(p.fee, fontSize = 11.sp, color = if (p.fee == "FREE") CyberCyan else GoldPrimary, fontWeight = FontWeight.Black)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("POOL", fontSize = 8.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                                    Text(p.prize, fontSize = 11.sp, color = GoldPrimary, fontWeight = FontWeight.Black)
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Kill: ${p.killBonus}", fontSize = 9.sp, color = FireOrange, fontWeight = FontWeight.Bold)
                                Text(p.tag, fontSize = 8.sp, color = TextMuted)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChipsRow(
    selectedFilter: MatchFilter,
    onSelectFilter: (MatchFilter) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(MatchFilter.entries.toTypedArray()) { filter ->
            val isSelected = selectedFilter == filter
            FilterChip(
                selected = isSelected,
                onClick = { onSelectFilter(filter) },
                label = {
                    Text(
                        when (filter) {
                            MatchFilter.ALL -> "ALL MATCHES"
                            MatchFilter.UPCOMING -> "🔥 UPCOMING"
                            MatchFilter.LIVE -> "🔴 LIVE ROOMS"
                            MatchFilter.SOLO -> "🎯 SOLO"
                            MatchFilter.SQUAD -> "⚔️ SQUAD / DUO"
                        },
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = GoldPrimary,
                    selectedLabelColor = DarkBackground,
                    containerColor = DarkSurfaceVariant,
                    labelColor = TextPrimary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = TextMuted,
                    selectedBorderColor = GoldPrimary
                )
            )
        }
    }
}

@Composable
fun TournamentCard(
    tournament: TournamentEntity,
    onJoinClick: () -> Unit,
    onViewRoomClick: () -> Unit,
    onOpenRulesClick: () -> Unit = {}
) {
    val progress = if (tournament.totalSlots > 0) {
        tournament.joinedSlots.toFloat() / tournament.totalSlots.toFloat()
    } else 0f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(
                1.dp,
                if (tournament.isJoined) CyberCyan else DarkSurfaceVariant,
                RoundedCornerShape(14.dp)
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Title & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = tournament.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Map, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "${tournament.mapName} • ${tournament.matchType}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                // Status Chip
                val statusColor = when (tournament.status) {
                    "LIVE" -> StatusLive
                    "UPCOMING" -> StatusUpcoming
                    else -> TextMuted
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusColor.copy(alpha = 0.2f))
                        .border(1.dp, statusColor, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        tournament.status,
                        color = statusColor,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Details Grid: Prize Pool, Entry Fee, Per Kill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkSurfaceVariant)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Prize Pool", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    }
                    Text(
                        "${tournament.prizePool} Coins",
                        style = MaterialTheme.typography.titleSmall,
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Entry Fee", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    }
                    Text(
                        if (tournament.entryFee == 0) "FREE" else "${tournament.entryFee} Coins",
                        style = MaterialTheme.typography.titleSmall,
                        color = CyberCyan,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = FireOrange, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Per Kill", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    }
                    Text(
                        "+${tournament.perKillBonus} Coins",
                        style = MaterialTheme.typography.titleSmall,
                        color = FireOrange,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Slots Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(tournament.startTime, style = MaterialTheme.typography.bodySmall, color = TextMuted)
                }

                Text(
                    "${tournament.joinedSlots}/${tournament.totalSlots} Slots Joined",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (progress >= 1f) FireOrange else GoldPrimary,
                trackColor = DarkSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons Row: RULES & JOIN / VIEW ROOM
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material3.OutlinedButton(
                    onClick = onOpenRulesClick,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldPrimary)
                ) {
                    Icon(Icons.Default.Gavel, contentDescription = "Rules", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("RULES", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Box(modifier = Modifier.weight(1f)) {
                    if (tournament.isJoined) {
                        Button(
                            onClick = onViewRoomClick,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyberCyan,
                                contentColor = DarkBackground
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("VIEW ROOM", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    } else if (tournament.joinedSlots >= tournament.totalSlots) {
                        Button(
                            onClick = { },
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                disabledContainerColor = DarkSurfaceVariant,
                                disabledContentColor = TextMuted
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("SLOTS FULL", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    } else {
                        Button(
                            onClick = onJoinClick,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldPrimary,
                                contentColor = DarkBackground
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                if (tournament.entryFee == 0) "JOIN (FREE)" else "JOIN (${tournament.entryFee} COINS)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
