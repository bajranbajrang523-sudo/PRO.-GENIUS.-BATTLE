package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TournamentEntity
import com.example.data.model.UserProfileEntity
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.FireOrange
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

private enum class ParticipationFilter {
    ALL,
    COMPLETED,
    ACTIVE_UPCOMING
}

@Composable
fun ProfileScreen(
    userProfile: UserProfileEntity?,
    joinedTournaments: List<TournamentEntity>,
    onSaveProfile: (username: String, uid: String, fullName: String?, phone: String?) -> Unit,
    onViewRoomClick: (TournamentEntity) -> Unit,
    onOpenRulesClick: (TournamentEntity) -> Unit = {},
    onExploreMatches: () -> Unit = {},
    onOpenWallet: () -> Unit = {},
    onOpenSecurity: () -> Unit = {},
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isEditing by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf(ParticipationFilter.ALL) }

    // Editable form state initialized from current userProfile
    var ignInput by remember(userProfile?.inGameName) { mutableStateOf(userProfile?.inGameName ?: "") }
    var uidInput by remember(userProfile?.inGameUid) { mutableStateOf(userProfile?.inGameUid ?: "") }
    var fullNameInput by remember(userProfile?.fullName) { mutableStateOf(userProfile?.fullName ?: "") }
    var phoneInput by remember(userProfile?.phoneNumber) { mutableStateOf(userProfile?.phoneNumber ?: "") }
    var inputError by remember { mutableStateOf<String?>(null) }
    var saveSuccessMessage by remember { mutableStateOf<String?>(null) }

    // Filter participation history
    val filteredHistory = remember(joinedTournaments, selectedFilter) {
        when (selectedFilter) {
            ParticipationFilter.ALL -> joinedTournaments
            ParticipationFilter.COMPLETED -> joinedTournaments.filter { it.status == "COMPLETED" }
            ParticipationFilter.ACTIVE_UPCOMING -> joinedTournaments.filter { it.status != "COMPLETED" }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("profile_screen_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Screen Title Banner
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "FREE FIRE PLAYER PROFILE",
                        style = MaterialTheme.typography.titleLarge,
                        color = GoldPrimary,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        "UID Verification • Username & Match Career",
                        style = MaterialTheme.typography.bodySmall,
                        color = CyberCyan
                    )
                }

                OutlinedButton(
                    onClick = {
                        isEditing = !isEditing
                        saveSuccessMessage = null
                        inputError = null
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (isEditing) FireOrange else CyberCyan
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isEditing) FireOrange else CyberCyan
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("toggle_edit_profile_btn")
                ) {
                    Icon(
                        if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isEditing) "CLOSE" else "EDIT UID", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // 2. Gamer Identity Card (Hero Card)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.5.dp,
                        Brush.horizontalGradient(listOf(GoldPrimary, CyberCyan)),
                        RoundedCornerShape(16.dp)
                    )
                    .testTag("gamer_identity_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Gamer Avatar with Neon Glow
                        Box(
                            modifier = Modifier
                                .size(68.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(GoldPrimary.copy(alpha = 0.3f), DarkSurfaceVariant)
                                    )
                                )
                                .border(2.dp, GoldPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.SportsEsports,
                                contentDescription = "Gamer Avatar",
                                tint = GoldPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    userProfile?.inGameName?.ifEmpty { "Pro_Sniper_FF" } ?: "Pro_Sniper_FF",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    Icons.Default.Verified,
                                    contentDescription = "Verified Free Fire Player",
                                    tint = CyberCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                userProfile?.fullName ?: "Pro Gamer",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Badges: Level & Rank
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(GoldPrimary.copy(alpha = 0.2f))
                                        .border(1.dp, GoldPrimary, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        "LVL 68 • HEROIC",
                                        color = GoldPrimary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                val role = userProfile?.role ?: "PLAYER"
                                val roleColor = when (role) {
                                    "SUPER_ADMIN" -> GoldPrimary
                                    "ADMIN" -> FireOrange
                                    else -> CyberCyan
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(roleColor.copy(alpha = 0.15f))
                                        .border(1.dp, roleColor, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        role.replace("_", " "),
                                        color = roleColor,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Prominent Free Fire UID Box with One-Tap Copy
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(DarkBackground)
                            .border(1.dp, CyberCyan.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "FREE FIRE UID (PLAYER ID)",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                userProfile?.inGameUid?.ifEmpty { "8934102941" } ?: "8934102941",
                                style = MaterialTheme.typography.titleMedium,
                                color = CyberCyan,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }

                        Button(
                            onClick = {
                                val currentUid = userProfile?.inGameUid?.ifEmpty { "8934102941" } ?: "8934102941"
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Free Fire UID", currentUid)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Free Fire UID ($currentUid) Copied!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyberCyan.copy(alpha = 0.15f),
                                contentColor = CyberCyan
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("copy_uid_button")
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy UID", modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("COPY UID", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 3. Editable Free Fire UID & Username Section
        item {
            AnimatedVisibility(visible = isEditing) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GoldPrimary, RoundedCornerShape(14.dp))
                        .testTag("edit_profile_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Edit, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "UPDATE FREE FIRE CREDENTIALS",
                                style = MaterialTheme.typography.titleSmall,
                                color = GoldPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            "Save your official in-game name and UID. These are automatically attached to custom tournament room slots.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                        )

                        // Username Field
                        OutlinedTextField(
                            value = ignInput,
                            onValueChange = {
                                ignInput = it
                                inputError = null
                            },
                            label = { Text("Free Fire Username (In-Game Name)") },
                            placeholder = { Text("e.g. Pro_Sniper_FF") },
                            leadingIcon = {
                                Icon(Icons.Default.SportsEsports, contentDescription = null, tint = GoldPrimary)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = TextMuted,
                                focusedLabelColor = GoldPrimary,
                                unfocusedLabelColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                cursorColor = GoldPrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("profile_ign_input"),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Free Fire UID Field
                        OutlinedTextField(
                            value = uidInput,
                            onValueChange = {
                                // Allow only numeric characters for UID
                                if (it.all { char -> char.isDigit() }) {
                                    uidInput = it
                                    inputError = null
                                }
                            },
                            label = { Text("Free Fire UID (Numeric ID)") },
                            placeholder = { Text("e.g. 8934102941") },
                            leadingIcon = {
                                Icon(Icons.Default.Badge, contentDescription = null, tint = CyberCyan)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberCyan,
                                unfocusedBorderColor = TextMuted,
                                focusedLabelColor = CyberCyan,
                                unfocusedLabelColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                cursorColor = CyberCyan
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("profile_uid_input"),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Full Name
                        OutlinedTextField(
                            value = fullNameInput,
                            onValueChange = { fullNameInput = it },
                            label = { Text("Player Real Name / Alias") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = TextMuted)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = TextMuted,
                                focusedLabelColor = GoldPrimary,
                                unfocusedLabelColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Phone Number
                        OutlinedTextField(
                            value = phoneInput,
                            onValueChange = { phoneInput = it },
                            label = { Text("WhatsApp / JazzCash / EasyPaisa Phone") },
                            placeholder = { Text("03001234567") },
                            leadingIcon = {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = TextMuted)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = TextMuted,
                                focusedLabelColor = GoldPrimary,
                                unfocusedLabelColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        inputError?.let { err ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "⚠️ $err",
                                color = FireOrange,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        saveSuccessMessage?.let { successMsg ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "✓ $successMsg",
                                color = CyberCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                val trimmedIgn = ignInput.trim()
                                val trimmedUid = uidInput.trim()

                                when {
                                    trimmedIgn.isEmpty() -> {
                                        inputError = "Please enter your Free Fire In-Game Name (IGN)."
                                    }
                                    trimmedUid.isEmpty() -> {
                                        inputError = "Please enter your Free Fire UID."
                                    }
                                    !trimmedUid.all { it.isDigit() } -> {
                                        inputError = "Free Fire UID must only contain numeric digits."
                                    }
                                    trimmedUid.length < 5 || trimmedUid.length > 15 -> {
                                        inputError = "Free Fire UID must be between 5 and 15 digits."
                                    }
                                    else -> {
                                        inputError = null
                                        onSaveProfile(trimmedIgn, trimmedUid, fullNameInput.trim(), phoneInput.trim())
                                        saveSuccessMessage = "Free Fire UID & Username saved successfully!"
                                        Toast.makeText(context, "Profile Saved! UID: $trimmedUid", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldPrimary,
                                contentColor = DarkBackground
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("save_profile_button")
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("SAVE FREE FIRE PROFILE", fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // 4. Career Performance Stats
        item {
            Text(
                "ESPORTS PERFORMANCE STATS",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    title = "MATCHES",
                    value = "${joinedTournaments.size.coerceAtLeast(userProfile?.totalMatches ?: 0)}",
                    subtitle = "Participated",
                    accentColor = GoldPrimary,
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = "TOTAL KILLS",
                    value = "${userProfile?.totalKills ?: 47}",
                    subtitle = "Eliminations",
                    accentColor = FireOrange,
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = "WINNINGS",
                    value = "${userProfile?.totalEarnings ?: 1200}",
                    subtitle = "Coins Won",
                    accentColor = CyberCyan,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Wallet Quick Action Card
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .clickable { onOpenWallet() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                "WALLET BALANCE",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "${userProfile?.coinBalance ?: 250} Coins (PKR)",
                                style = MaterialTheme.typography.titleMedium,
                                color = GoldPrimary,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Button(
                        onClick = onOpenWallet,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = DarkBackground
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Wallet, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("TOP UP", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 5. Participation History Section Header & Filter Chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.History, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "PARTICIPATION HISTORY",
                        style = MaterialTheme.typography.titleMedium,
                        color = CyberCyan,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    "${filteredHistory.size} Matches",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == ParticipationFilter.ALL,
                    onClick = { selectedFilter = ParticipationFilter.ALL },
                    label = { Text("All (${joinedTournaments.size})", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyberCyan,
                        selectedLabelColor = DarkBackground,
                        containerColor = DarkSurface,
                        labelColor = TextSecondary
                    )
                )

                val completedCount = joinedTournaments.count { it.status == "COMPLETED" }
                FilterChip(
                    selected = selectedFilter == ParticipationFilter.COMPLETED,
                    onClick = { selectedFilter = ParticipationFilter.COMPLETED },
                    label = { Text("Completed ($completedCount)", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GoldPrimary,
                        selectedLabelColor = DarkBackground,
                        containerColor = DarkSurface,
                        labelColor = TextSecondary
                    )
                )

                val activeCount = joinedTournaments.count { it.status != "COMPLETED" }
                FilterChip(
                    selected = selectedFilter == ParticipationFilter.ACTIVE_UPCOMING,
                    onClick = { selectedFilter = ParticipationFilter.ACTIVE_UPCOMING },
                    label = { Text("Active ($activeCount)", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = FireOrange,
                        selectedLabelColor = DarkBackground,
                        containerColor = DarkSurface,
                        labelColor = TextSecondary
                    )
                )
            }
        }

        // 6. Participation History List Items
        if (filteredHistory.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.SportsEsports,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "No Participated Matches in this Filter",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Join custom Free Fire tournaments from the lobby to build your career record!",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = onExploreMatches,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldPrimary,
                                contentColor = DarkBackground
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("EXPLORE TOURNAMENTS", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(filteredHistory, key = { it.id }) { tournament ->
                ParticipationMatchCard(
                    tournament = tournament,
                    currentProfile = userProfile,
                    onViewRoomClick = { onViewRoomClick(tournament) },
                    onOpenRulesClick = { onOpenRulesClick(tournament) }
                )
            }
        }

        // 7. Security & Fair Play Hub / Sign Out
        item {
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "FAIR PLAY & ANTI-CHEAT VERIFICATION",
                            style = MaterialTheme.typography.titleSmall,
                            color = CyberCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        "All tournament registrations verify your Free Fire UID against third-party emulator/hack blacklists. Pro Genius Battle ensures 100% fair play payouts.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onOpenSecurity,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("SECURITY AUDIT", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onLogout,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FireOrange.copy(alpha = 0.2f),
                                contentColor = FireOrange
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("LOGOUT", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                title,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                color = accentColor,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun ParticipationMatchCard(
    tournament: TournamentEntity,
    currentProfile: UserProfileEntity?,
    onViewRoomClick: () -> Unit,
    onOpenRulesClick: () -> Unit
) {
    val isCompleted = tournament.status == "COMPLETED"
    val isLive = tournament.status == "LIVE"
    val isWinner = tournament.winnerName?.contains("You", ignoreCase = true) == true ||
            tournament.winnerName?.contains(currentProfile?.inGameName ?: "Pro_Sniper_FF", ignoreCase = true) == true

    val statusBadgeColor = when {
        isWinner -> GoldPrimary
        isCompleted -> CyberCyan
        isLive -> FireOrange
        else -> GoldPrimary
    }

    val statusText = when {
        isWinner -> "VICTORY 🏆"
        isCompleted -> "COMPLETED 🏁"
        isLive -> "LIVE MATCH 🔴"
        else -> "REGISTERED ⏳"
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isWinner) GoldPrimary else DarkSurfaceVariant,
                RoundedCornerShape(14.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Row: Title & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        tournament.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        "${tournament.mapName} • ${tournament.matchType} • ${tournament.startTime}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusBadgeColor.copy(alpha = 0.2f))
                        .border(1.dp, statusBadgeColor, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        statusText,
                        color = statusBadgeColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Player Participation Credential Details Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(DarkBackground)
                    .padding(10.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Registered IGN:",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                        Text(
                            tournament.joinedInGameName ?: currentProfile?.inGameName ?: "Pro_Sniper_FF",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Free Fire UID:",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                        Text(
                            tournament.joinedUid ?: currentProfile?.inGameUid ?: "8934102941",
                            style = MaterialTheme.typography.labelSmall,
                            color = CyberCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Entry Fee Paid:",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                        Text(
                            if (tournament.entryFee == 0) "FREE" else "${tournament.entryFee} Coins",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }

                    // Completed Match Results: Kills & Winnings
                    if (isCompleted) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(DarkSurfaceVariant)
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "Kills: ",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                                Text(
                                    "${tournament.playerKills} Kills",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FireOrange,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "Winnings: ",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                                Text(
                                    if (tournament.playerWinnings > 0) "+${tournament.playerWinnings} Coins" else "0 Coins",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (tournament.playerWinnings > 0) GoldPrimary else TextMuted,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenRulesClick,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TextMuted.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("RULES", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onViewRoomClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (tournament.status == "UPCOMING" || tournament.status == "LIVE") GoldPrimary else CyberCyan,
                        contentColor = DarkBackground
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        if (isCompleted) "MATCH ROOM" else "VIEW ROOM ID",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
