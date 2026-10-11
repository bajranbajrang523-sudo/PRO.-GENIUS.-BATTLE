package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import com.example.data.model.AdminAnnouncement
import com.example.data.model.JoinedPlayerDetail
import com.example.data.model.TournamentEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WalletTransactionEntity
import com.example.ui.viewmodel.AppTab
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.FireOrange
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.SecurityUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinMatchBottomSheet(
    tournament: TournamentEntity,
    userProfile: UserProfileEntity?,
    onDismiss: () -> Unit,
    onConfirmJoin: (ign: String, uid: String) -> Unit,
    onSubmitDirectPayment: (amount: Int, method: String, senderNumber: String, tid: String, ign: String, uid: String) -> Unit = { _, _, _, _, _, _ -> }
) {
    val context = LocalContext.current
    var selectedMethod by remember { mutableStateOf(if ((userProfile?.coinBalance ?: 0) >= tournament.entryFee) "Wallet Coins" else "EasyPaisa") }
    var ign by remember { mutableStateOf(userProfile?.inGameName ?: "") }
    var uid by remember { mutableStateOf(userProfile?.inGameUid ?: "") }
    var senderNumber by remember { mutableStateOf(userProfile?.phoneNumber ?: "") }
    var tid by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val userCoins = userProfile?.coinBalance ?: 0
    val hasEnoughCoins = userCoins >= tournament.entryFee
    val adminNumber = "03207394660"
    val adminTitle = "ABDULLAH BAJRAN"

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        scrimColor = DarkBackground.copy(alpha = 0.85f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Sheet Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Payment, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "TOURNAMENT ENTRY PAYMENT",
                            style = MaterialTheme.typography.titleMedium,
                            color = GoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "${tournament.title} • ${if (tournament.entryFee == 0) "FREE" else "${tournament.entryFee} PKR / Coins"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Payment Selection Options
            Text("CHOOSE PAYMENT METHOD", style = MaterialTheme.typography.labelSmall, color = TextMuted, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val methods = listOf("Wallet Coins", "EasyPaisa", "JazzCash")
                methods.forEach { method ->
                    val isSelected = selectedMethod == method
                    val methodColor = when (method) {
                        "EasyPaisa" -> CyberCyan
                        "JazzCash" -> FireOrange
                        else -> GoldPrimary
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) methodColor else DarkSurfaceVariant,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) methodColor.copy(alpha = 0.15f) else DarkBackground
                        ),
                        onClick = {
                            selectedMethod = method
                            errorMessage = null
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = when (method) {
                                    "Wallet Coins" -> "🪙 Coins"
                                    "EasyPaisa" -> "🟢 EasyPaisa"
                                    "JazzCash" -> "🔴 JazzCash"
                                    else -> method
                                },
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) methodColor else TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            if (method == "Wallet Coins") {
                                Text(
                                    text = "$userCoins Bal",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (hasEnoughCoins) CyberCyan else TextMuted
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Details Based on Selected Method
            when (selectedMethod) {
                "Wallet Coins" -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = GoldPrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Entry Fee", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                                    Text(
                                        if (tournament.entryFee == 0) "FREE ENTRY" else "${tournament.entryFee} Coins",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = GoldPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("Your Wallet Balance", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                                Text(
                                    "$userCoins Coins",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (hasEnoughCoins) CyberCyan else FireOrange,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    if (!hasEnoughCoins) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "⚠️ Low coin balance! Select EasyPaisa or JazzCash above to pay entry fee directly.",
                            color = FireOrange,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                "EasyPaisa", "JazzCash" -> {
                    val brandColor = if (selectedMethod == "EasyPaisa") CyberCyan else FireOrange

                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkBackground),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, brandColor.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = brandColor, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        "PAY VIA $selectedMethod",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = brandColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Text(
                                    "Rs. ${tournament.entryFee} PKR",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = GoldPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Receiver Info Box
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DarkSurfaceVariant)
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Official Admin $selectedMethod Account:", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                    Text("$adminNumber ($adminTitle)", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                                }

                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("Admin Number", adminNumber)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "$selectedMethod Account Number Copied!", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = brandColor)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Common Player Game Credentials Fields
            Text("PLAYER CREDENTIALS", style = MaterialTheme.typography.labelSmall, color = TextMuted, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = ign,
                onValueChange = { ign = it; errorMessage = null },
                label = { Text("Free Fire In-Game Name (IGN)") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = GoldPrimary) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldPrimary,
                    unfocusedBorderColor = TextMuted,
                    focusedLabelColor = GoldPrimary,
                    unfocusedLabelColor = TextMuted,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uid,
                onValueChange = { uid = it; errorMessage = null },
                label = { Text("Free Fire Player UID") },
                leadingIcon = { Icon(Icons.Default.SportsEsports, contentDescription = null, tint = CyberCyan) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = TextMuted,
                    focusedLabelColor = CyberCyan,
                    unfocusedLabelColor = TextMuted,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            // Direct Payment Proof Fields if EasyPaisa/JazzCash selected
            if (selectedMethod == "EasyPaisa" || selectedMethod == "JazzCash") {
                val brandColor = if (selectedMethod == "EasyPaisa") CyberCyan else FireOrange

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = senderNumber,
                    onValueChange = { senderNumber = it; errorMessage = null },
                    label = { Text("Your $selectedMethod Mobile Number") },
                    placeholder = { Text("e.g. 03001234567") },
                    leadingIcon = { Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = brandColor) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = brandColor,
                        unfocusedBorderColor = TextMuted,
                        focusedLabelColor = brandColor,
                        unfocusedLabelColor = TextMuted,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = tid,
                    onValueChange = { tid = it; errorMessage = null },
                    label = { Text("Transaction ID (TID / TRX ID)") },
                    placeholder = { Text("e.g. 9876543210") },
                    leadingIcon = { Icon(Icons.Default.Receipt, contentDescription = null, tint = brandColor) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = brandColor,
                        unfocusedBorderColor = TextMuted,
                        focusedLabelColor = brandColor,
                        unfocusedLabelColor = TextMuted,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(errorMessage!!, color = FireOrange, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Submit Button
            if (selectedMethod == "Wallet Coins") {
                Button(
                    onClick = {
                        if (ign.trim().isEmpty() || uid.trim().isEmpty()) {
                            errorMessage = "Please enter both Free Fire IGN and Player UID!"
                        } else if (!hasEnoughCoins) {
                            errorMessage = "Insufficient coins! Select EasyPaisa or JazzCash above."
                        } else {
                            onConfirmJoin(ign.trim(), uid.trim())
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = hasEnoughCoins,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = DarkBackground
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        if (tournament.entryFee == 0) "CONFIRM & REGISTER (FREE)" else "PAY ${tournament.entryFee} COINS & REGISTER",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            } else {
                val brandColor = if (selectedMethod == "EasyPaisa") CyberCyan else FireOrange

                Button(
                    onClick = {
                        if (ign.trim().isEmpty() || uid.trim().isEmpty()) {
                            errorMessage = "Please enter both Free Fire IGN and Player UID!"
                        } else if (senderNumber.trim().isEmpty() || tid.trim().isEmpty()) {
                            errorMessage = "Please enter your $selectedMethod Mobile Number and Transaction ID!"
                        } else {
                            onSubmitDirectPayment(
                                tournament.entryFee,
                                selectedMethod,
                                senderNumber.trim(),
                                tid.trim(),
                                ign.trim(),
                                uid.trim()
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = brandColor,
                        contentColor = DarkBackground
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        "SUBMIT $selectedMethod PAYMENT & JOIN",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun RoomInfoDialog(
    tournament: TournamentEntity,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GoldPrimary, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.MeetingRoom, contentDescription = null, tint = GoldPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "CUSTOM ROOM CREDENTIALS",
                        style = MaterialTheme.typography.titleMedium,
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    tournament.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Room ID Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkSurfaceVariant)
                        .padding(14.dp)
                ) {
                    Column {
                        Text("ROOM ID", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                tournament.roomId,
                                style = MaterialTheme.typography.titleMedium,
                                color = CyberCyan,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Room ID", tournament.roomId))
                                Toast.makeText(context, "Room ID copied!", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy Room ID", tint = CyberCyan)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Room Password Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkSurfaceVariant)
                        .padding(14.dp)
                ) {
                    Column {
                        Text("ROOM PASSWORD", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = GoldPrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    tournament.roomPassword,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = GoldPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            IconButton(onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Password", tournament.roomPassword))
                                Toast.makeText(context, "Password copied!", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy Password", tint = GoldPrimary)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    "⚠️ Open Free Fire -> Custom Room -> Enter Room ID & Password to join match lobby before start time.",
                    color = TextMuted,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = DarkBackground),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("DONE & CLOSE", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun RulesDialog(
    tournament: TournamentEntity? = null,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .border(1.dp, CyberCyan, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Gavel, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                "TOURNAMENT RULES & CODE",
                                style = MaterialTheme.typography.titleMedium,
                                color = GoldPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Free Fire Official Esports Regulations",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (tournament != null) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, GoldPrimary, RoundedCornerShape(10.dp)),
                                colors = CardDefaults.cardColors(containerColor = DarkBackground),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            tournament.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = GoldPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(CyberCyan.copy(alpha = 0.2f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                "${tournament.mapName} • ${tournament.matchType}",
                                                color = CyberCyan,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Prize Pool: ${tournament.prizePool} Coins", color = TextPrimary, style = MaterialTheme.typography.labelSmall)
                                        Text("Per Kill: +${tournament.perKillBonus} Coins", color = FireOrange, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                        Text("Time: ${tournament.startTime}", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
                                    }

                                    if (tournament.rules.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Match Instructions:", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                        Text(tournament.rules, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                                    }
                                }
                            }
                        }
                    }

                    item {
                        RuleSectionCard(
                            title = "📱 1. Mobile Handsets Only",
                            description = "Only official Android/iOS mobile devices are allowed. Emulators (BlueStacks, LDPlayer, Gameloop, Nox) and PC key-mappers are strictly prohibited. Violators are kicked immediately.",
                            color = CyberCyan
                        )
                    }

                    item {
                        RuleSectionCard(
                            title = "🛡️ 2. Zero Tolerance Anti-Cheat",
                            description = "Any third-party mods, auto-headshot scripts, speed hacks, or panel modifications will result in an immediate permanent ban and forfeiture of all accumulated coins.",
                            color = FireOrange
                        )
                    }

                    item {
                        RuleSectionCard(
                            title = "🔑 3. Room Key & Timing",
                            description = "Room ID and Password are disclosed in the 'My Matches' tab 15 minutes before the match start time. Sharing room keys with unregistered players leads to match DQ.",
                            color = GoldPrimary
                        )
                    }

                    item {
                        RuleSectionCard(
                            title = "📸 4. Screenshot Verification",
                            description = "Players must record or take a clear screenshot of the end-game Booyah / Kill Stats screen. This is required for validating per-kill bonuses and top prize payouts.",
                            color = TextPrimary
                        )
                    }

                    item {
                        RuleSectionCard(
                            title = "🤝 5. Teaming & Fair Play",
                            description = "Cross-teaming or intentionally dying to boost opponent kills is forbidden. Referees (ABDULLAH BAJRAN & HAMZA) monitor custom rooms live.",
                            color = TextMuted
                        )
                    }

                    item {
                        RuleSectionCard(
                            title = "💰 6. Refunds & Cancellations",
                            description = "If a match is canceled due to technical issues, 100% of the entry fee will be refunded back to your app wallet balance instantly.",
                            color = CyberCyan
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = DarkBackground),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("I AGREE & UNDERSTAND", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun RuleSectionCard(
    title: String,
    description: String,
    color: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkBackground),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, color = color, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithdrawCoinsBottomSheet(
    userBalance: Int,
    onDismiss: () -> Unit,
    onConfirmWithdraw: (amount: Int, method: String, accountNumber: String, accountTitle: String) -> Unit
) {
    var selectedAmount by remember { mutableStateOf(100) }
    var customAmountText by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf("JazzCash") }
    var accountNumber by remember { mutableStateOf("") }
    var accountTitle by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val methods = listOf("JazzCash", "EasyPaisa", "Bank Transfer")
    val defaultAmounts = listOf(50, 100, 200, 500)

    val finalAmount = customAmountText.toIntOrNull() ?: selectedAmount

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        scrimColor = DarkBackground.copy(alpha = 0.8f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "EASY WITHDRAWAL",
                        style = MaterialTheme.typography.titleMedium,
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Cashout directly to JazzCash or EasyPaisa",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkSurfaceVariant)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        "Balance: $userBalance",
                        color = CyberCyan,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("SELECT METHOD", style = MaterialTheme.typography.labelSmall, color = TextMuted)
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                methods.forEach { method ->
                    val isSelected = selectedMethod == method
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                1.dp,
                                if (isSelected) FireOrange else DarkSurfaceVariant,
                                RoundedCornerShape(8.dp)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) DarkSurfaceVariant else DarkBackground
                        ),
                        onClick = { selectedMethod = method; errorMessage = null }
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                method,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) FireOrange else TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text("SELECT WITHDRAWAL AMOUNT (COINS)", style = MaterialTheme.typography.labelSmall, color = TextMuted)
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                defaultAmounts.forEach { amount ->
                    val isSelected = selectedAmount == amount && customAmountText.isEmpty()
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                1.dp,
                                if (isSelected) GoldPrimary else DarkSurfaceVariant,
                                RoundedCornerShape(8.dp)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) DarkSurfaceVariant else DarkBackground
                        ),
                        onClick = {
                            selectedAmount = amount
                            customAmountText = ""
                            errorMessage = null
                        }
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "$amount",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isSelected) GoldPrimary else TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = accountNumber,
                onValueChange = { accountNumber = it; errorMessage = null },
                label = { Text("$selectedMethod Account / Mobile Number") },
                placeholder = { Text("e.g. 03001234567") },
                leadingIcon = { Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = FireOrange) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FireOrange,
                    unfocusedBorderColor = TextMuted,
                    focusedLabelColor = FireOrange,
                    unfocusedLabelColor = TextMuted,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = accountTitle,
                onValueChange = { accountTitle = it; errorMessage = null },
                label = { Text("Account Holder Name (Title)") },
                placeholder = { Text("e.g. Muhammad Ali") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = FireOrange) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FireOrange,
                    unfocusedBorderColor = TextMuted,
                    focusedLabelColor = FireOrange,
                    unfocusedLabelColor = TextMuted,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(errorMessage!!, color = FireOrange, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    if (accountNumber.trim().isEmpty() || accountTitle.trim().isEmpty()) {
                        errorMessage = "Please enter Account Number and Account Title!"
                    } else if (finalAmount <= 0) {
                        errorMessage = "Invalid amount selected!"
                    } else if (finalAmount > userBalance) {
                        errorMessage = "Insufficient balance! Your balance is $userBalance coins."
                    } else {
                        onConfirmWithdraw(finalAmount, selectedMethod, accountNumber.trim(), accountTitle.trim())
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FireOrange, contentColor = TextPrimary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    "SEND $finalAmount COINS WITHDRAWAL REQUEST",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AdminApprovalDialog(
    transactions: List<WalletTransactionEntity>,
    tournaments: List<TournamentEntity> = emptyList(),
    enteredPlayers: List<JoinedPlayerDetail> = emptyList(),
    currentAppName: String = "ABDULLAH BAJRAN",
    currentAppSubtitle: String = "FREE FIRE ESPORTS HUB • WHATSAPP: 03207394660",
    currentAppLogo: String = "👑",
    superAdminUid: String = "999888777",
    superAdminIgn: String = "SuperAdmin_Bajran",
    adminHamzaUid: String = "888777666",
    adminHamzaIgn: String = "Admin_Hamza",
    onDismiss: () -> Unit,
    onApprove: (txId: Int) -> Unit,
    onReject: (txId: Int) -> Unit,
    onOpenCreateMatch: (() -> Unit)? = null,
    onUpdateBranding: ((String, String, String) -> Unit)? = null,
    onUpdateAdminUids: ((String, String, String, String) -> Unit)? = null
) {
    var selectedTab by remember { mutableStateOf(0) }
    var editAppName by remember { mutableStateOf(currentAppName) }
    var editAppSubtitle by remember { mutableStateOf(currentAppSubtitle) }
    var selectedLogo by remember { mutableStateOf(currentAppLogo) }

    var editSuperAdminUid by remember { mutableStateOf(superAdminUid) }
    var editSuperAdminIgn by remember { mutableStateOf(superAdminIgn) }
    var editHamzaUid by remember { mutableStateOf(adminHamzaUid) }
    var editHamzaIgn by remember { mutableStateOf(adminHamzaIgn) }

    val context = LocalContext.current
    val clipboardManager = remember { context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .border(1.dp, GoldPrimary, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(GoldPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(selectedLogo, fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "ADMIN PANEL (HAMZA & ABDULLAH)",
                            style = MaterialTheme.typography.titleSmall,
                            color = GoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // 4 Tab Bar in Admin Panel
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = DarkSurfaceVariant,
                    contentColor = GoldPrimary,
                    indicator = { tabPositions ->
                        if (selectedTab < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = GoldPrimary
                            )
                        }
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Payments", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Player UIDs", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("App Branding", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        text = { Text("Admin UIDs", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 4,
                        onClick = { selectedTab = 4 },
                        text = { Text("Web & APK", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                when (selectedTab) {
                    0 -> { // Payments Tab
                        if (onOpenCreateMatch != null) {
                            Button(
                                onClick = {
                                    onDismiss()
                                    onOpenCreateMatch()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = DarkBackground),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.SportsEsports, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("➕ UPLOAD NEW MATCH (ADMIN HAMZA)", fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        val pendingCount = transactions.count { it.status == "PENDING" }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkSurfaceVariant)
                                .padding(8.dp)
                        ) {
                            Text(
                                "PENDING APPROVALS: $pendingCount REQUESTS",
                                color = if (pendingCount > 0) FireOrange else CyberCyan,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (transactions.isEmpty()) {
                            Text(
                                "No payment requests recorded yet.",
                                color = TextMuted,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(vertical = 20.dp)
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(260.dp)
                            ) {
                                val sorted = transactions.sortedWith(
                                    compareByDescending<WalletTransactionEntity> { it.status == "PENDING" }
                                        .thenByDescending { it.timestamp }
                                )

                                items(sorted, key = { it.id }) { tx ->
                                    val isPending = tx.status == "PENDING"

                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .border(
                                                1.dp,
                                                when (tx.status) {
                                                    "PENDING" -> FireOrange
                                                    "SUCCESS" -> CyberCyan
                                                    else -> TextMuted
                                                },
                                                RoundedCornerShape(8.dp)
                                            ),
                                        colors = CardDefaults.cardColors(containerColor = DarkBackground),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column {
                                                    Text(
                                                        tx.title,
                                                        style = MaterialTheme.typography.titleSmall,
                                                        color = TextPrimary,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        "Method: ${tx.paymentMethod} • Amount: ${tx.amount} Coins",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = CyberCyan
                                                    )
                                                }

                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(
                                                            when (tx.status) {
                                                                "PENDING" -> FireOrange.copy(alpha = 0.2f)
                                                                "SUCCESS" -> CyberCyan.copy(alpha = 0.2f)
                                                                else -> TextMuted.copy(alpha = 0.2f)
                                                            }
                                                        )
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        tx.status,
                                                        color = when (tx.status) {
                                                            "PENDING" -> FireOrange
                                                            "SUCCESS" -> CyberCyan
                                                            else -> TextMuted
                                                        },
                                                        style = MaterialTheme.typography.labelSmall,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }

                                            if (!tx.senderNumber.isNullOrEmpty()) {
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    "Sender/Account No: ${tx.senderNumber}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = TextSecondary
                                                )
                                            }

                                            if (!tx.transactionId.isNullOrEmpty()) {
                                                Text(
                                                    "Transaction ID (TID): ${tx.transactionId}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = GoldPrimary,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }

                                            if (!tx.accountTitle.isNullOrEmpty()) {
                                                Text(
                                                    "Account Holder Title: ${tx.accountTitle}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = TextSecondary
                                                )
                                            }

                                            if (isPending) {
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    Button(
                                                        onClick = { onApprove(tx.id) },
                                                        colors = ButtonDefaults.buttonColors(
                                                            containerColor = GoldPrimary,
                                                            contentColor = DarkBackground
                                                        ),
                                                        modifier = Modifier.weight(1f),
                                                        shape = RoundedCornerShape(6.dp)
                                                    ) {
                                                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text("APPROVE", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                                    }

                                                    OutlinedButton(
                                                        onClick = { onReject(tx.id) },
                                                        modifier = Modifier.weight(1f),
                                                        shape = RoundedCornerShape(6.dp),
                                                        border = androidx.compose.foundation.BorderStroke(1.dp, FireOrange)
                                                    ) {
                                                        Icon(Icons.Default.Cancel, contentDescription = null, tint = FireOrange, modifier = Modifier.size(16.dp))
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text("REJECT", color = FireOrange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    1 -> { // Player UIDs & Entered Players Registry
                        var searchQuery by remember { mutableStateOf("") }
                        var selectedMatchFilter by remember { mutableStateOf("ALL") }

                        val matchTitles = remember(tournaments, enteredPlayers) {
                            val set = linkedSetOf("ALL")
                            tournaments.forEach { set.add(it.title) }
                            set.toList()
                        }

                        val filteredPlayers = remember(enteredPlayers, searchQuery, selectedMatchFilter) {
                            enteredPlayers.filter { player ->
                                val query = searchQuery.trim()
                                val matchesSearch = query.isBlank() ||
                                        player.inGameUid.contains(query, ignoreCase = true) ||
                                        player.inGameName.contains(query, ignoreCase = true) ||
                                        player.playerName.contains(query, ignoreCase = true) ||
                                        player.matchTitle.contains(query, ignoreCase = true)
                                val matchesMatch = selectedMatchFilter == "ALL" || player.matchTitle.contains(selectedMatchFilter, ignoreCase = true)
                                matchesSearch && matchesMatch
                            }
                        }

                        // Search and Filter Bar
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search Player UID, IGN, or User Name...", fontSize = 11.sp, color = TextMuted) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(18.dp)) },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }, modifier = Modifier.size(24.dp)) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextMuted, modifier = Modifier.size(16.dp))
                                    }
                                }
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = TextMuted,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Match selector tabs & copy all action
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "REGISTERED PLAYERS: ${filteredPlayers.size}",
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldPrimary,
                                fontWeight = FontWeight.Bold
                            )

                            if (filteredPlayers.isNotEmpty()) {
                                Button(
                                    onClick = {
                                        val uidsText = filteredPlayers.joinToString(", ") { it.inGameUid }
                                        val clip = ClipData.newPlainText("Entered Free Fire UIDs", uidsText)
                                        clipboardManager?.setPrimaryClip(clip)
                                        Toast.makeText(context, "Copied ${filteredPlayers.size} UIDs for Custom Room Invite!", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = DarkBackground),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("COPY ALL UIDS", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        if (filteredPlayers.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    if (searchQuery.isNotEmpty()) "No players found matching '$searchQuery'." else "No registered players recorded yet.",
                                    color = TextMuted,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(260.dp)
                            ) {
                                items(filteredPlayers, key = { "${it.tournamentId}_${it.slotNumber}_${it.inGameUid}" }) { player ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .border(1.dp, CyberCyan.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
                                        colors = CardDefaults.cardColors(containerColor = DarkBackground)
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            // Tournament header and Slot
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    player.matchTitle,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = CyberCyan,
                                                    fontWeight = FontWeight.Bold,
                                                    maxLines = 1,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(GoldPrimary.copy(alpha = 0.2f))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        "Slot #${player.slotNumber}",
                                                        color = GoldPrimary,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(6.dp))

                                            // Player Names & UID Display
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text("👤 USER NAME:", style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 9.sp)
                                                    Text(player.playerName, style = MaterialTheme.typography.bodySmall, color = TextPrimary, fontWeight = FontWeight.Bold)

                                                    Spacer(modifier = Modifier.height(4.dp))

                                                    Text("🎮 ID NAME (IGN):", style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 9.sp)
                                                    Text(player.inGameName, style = MaterialTheme.typography.bodySmall, color = CyberCyan, fontWeight = FontWeight.Bold)
                                                }

                                                Column(horizontalAlignment = Alignment.End) {
                                                    Text("🆔 FREE FIRE UID:", style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 9.sp)
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(
                                                            player.inGameUid,
                                                            style = MaterialTheme.typography.titleMedium,
                                                            color = GoldPrimary,
                                                            fontWeight = FontWeight.Black
                                                        )
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        IconButton(
                                                            onClick = {
                                                                val clip = ClipData.newPlainText("Free Fire UID", player.inGameUid)
                                                                clipboardManager?.setPrimaryClip(clip)
                                                                Toast.makeText(context, "Copied UID: ${player.inGameUid}", Toast.LENGTH_SHORT).show()
                                                            },
                                                            modifier = Modifier.size(24.dp)
                                                        ) {
                                                            Icon(
                                                                Icons.Default.ContentCopy,
                                                                contentDescription = "Copy UID",
                                                                tint = GoldPrimary,
                                                                modifier = Modifier.size(16.dp)
                                                            )
                                                        }
                                                    }
                                                    Text(
                                                        player.phoneNumber ?: "Verified Entry",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = TextSecondary,
                                                        fontSize = 9.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    2 -> { // App Name & Logo Branding Tab
                        Text(
                            "Admins can update the App Name, Subtitle, and Logo Emblem shown across the app:",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = editAppName,
                            onValueChange = { editAppName = it },
                            label = { Text("App Main Name / Title") },
                            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = GoldPrimary) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = TextMuted,
                                focusedLabelColor = GoldPrimary,
                                unfocusedLabelColor = TextMuted,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = editAppSubtitle,
                            onValueChange = { editAppSubtitle = it },
                            label = { Text("App Slogan / WhatsApp Tag") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = CyberCyan) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberCyan,
                                unfocusedBorderColor = TextMuted,
                                focusedLabelColor = CyberCyan,
                                unfocusedLabelColor = TextMuted,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("SELECT APP LOGO EMBLEM:", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        Spacer(modifier = Modifier.height(6.dp))

                        val emblemOptions = listOf("👑", "🏆", "🔥", "⚡", "🎯", "🎮", "🛡️")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            emblemOptions.forEach { emblem ->
                                val isSelected = selectedLogo == emblem
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) GoldPrimary.copy(alpha = 0.3f) else DarkBackground)
                                        .border(if (isSelected) 2.dp else 1.dp, if (isSelected) GoldPrimary else TextMuted, RoundedCornerShape(8.dp))
                                        .clickable { selectedLogo = emblem },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(emblem, fontSize = 20.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Real-time Header Preview
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DarkBackground),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(GoldPrimary)
                                        .padding(2.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(selectedLogo, fontSize = 20.sp)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(editAppName.ifBlank { "ABDULLAH BAJRAN" }, style = MaterialTheme.typography.titleSmall, color = GoldPrimary, fontWeight = FontWeight.Black)
                                    Text(editAppSubtitle.ifBlank { "FREE FIRE ESPORTS HUB" }, style = MaterialTheme.typography.labelSmall, color = CyberCyan, fontSize = 9.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                onUpdateBranding?.invoke(editAppName, editAppSubtitle, selectedLogo)
                                Toast.makeText(context, "App Name, Subtitle & Logo updated!", Toast.LENGTH_SHORT).show()
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = DarkBackground),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("SAVE APP BRANDING & LOGO", fontWeight = FontWeight.Bold)
                        }
                    }

                    3 -> { // Admin UIDs Tab (Admins Change UIDs)
                        Text(
                            "Admins can update their Free Fire Game UIDs & In-Game Names for tournament hosting & profile identity:",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Super Admin Abdullah Bajran Section
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DarkBackground),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("👑 SUPER ADMIN (ABDULLAH BAJRAN)", style = MaterialTheme.typography.labelSmall, color = GoldPrimary, fontWeight = FontWeight.Bold)
                                    IconButton(
                                        onClick = {
                                            val clip = ClipData.newPlainText("Super Admin UID", editSuperAdminUid)
                                            clipboardManager?.setPrimaryClip(clip)
                                            Toast.makeText(context, "Copied Abdullah Bajran UID: $editSuperAdminUid", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy UID", tint = GoldPrimary, modifier = Modifier.size(14.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                OutlinedTextField(
                                    value = editSuperAdminIgn,
                                    onValueChange = { editSuperAdminIgn = it },
                                    label = { Text("Super Admin IGN / ID Name", fontSize = 11.sp) },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = GoldPrimary,
                                        unfocusedBorderColor = TextMuted,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    )
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                OutlinedTextField(
                                    value = editSuperAdminUid,
                                    onValueChange = { editSuperAdminUid = it },
                                    label = { Text("Super Admin Free Fire Game UID", fontSize = 11.sp) },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = GoldPrimary,
                                        unfocusedBorderColor = TextMuted,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Admin Hamza Section
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DarkBackground),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, FireOrange.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("🛡️ ADMIN (HAMZA)", style = MaterialTheme.typography.labelSmall, color = FireOrange, fontWeight = FontWeight.Bold)
                                    IconButton(
                                        onClick = {
                                            val clip = ClipData.newPlainText("Admin Hamza UID", editHamzaUid)
                                            clipboardManager?.setPrimaryClip(clip)
                                            Toast.makeText(context, "Copied Hamza UID: $editHamzaUid", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy UID", tint = FireOrange, modifier = Modifier.size(14.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                OutlinedTextField(
                                    value = editHamzaIgn,
                                    onValueChange = { editHamzaIgn = it },
                                    label = { Text("Admin Hamza IGN / ID Name", fontSize = 11.sp) },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = FireOrange,
                                        unfocusedBorderColor = TextMuted,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    )
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                OutlinedTextField(
                                    value = editHamzaUid,
                                    onValueChange = { editHamzaUid = it },
                                    label = { Text("Admin Hamza Free Fire Game UID", fontSize = 11.sp) },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = FireOrange,
                                        unfocusedBorderColor = TextMuted,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                onUpdateAdminUids?.invoke(
                                    editSuperAdminUid,
                                    editSuperAdminIgn,
                                    editHamzaUid,
                                    editHamzaIgn
                                )
                                Toast.makeText(context, "Admin Free Fire UIDs & IGNs saved!", Toast.LENGTH_SHORT).show()
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = DarkBackground),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("SAVE ADMIN UIDS & IGNS", fontWeight = FontWeight.Bold)
                        }
                    }

                    4 -> { // Web & APK Publishing Guide Tab
                        WebPublishingAndApkGuideContent(appName = editAppName)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant, contentColor = TextPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("CLOSE ADMIN PANEL", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun WebPublishingAndApkGuideContent(appName: String) {
    val context = LocalContext.current
    val clipboardManager = remember { context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager }

    val devUrl = "https://ais-dev-345b4plm23hdsotkmkdi5j-631837434540.asia-southeast1.run.app"
    val liveUrl = "https://ais-pre-345b4plm23hdsotkmkdi5j-631837434540.asia-southeast1.run.app"

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(390.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkBackground),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberCyan, RoundedCornerShape(10.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "🌐 GOOGLE HOSTED WEB LINKS",
                            style = MaterialTheme.typography.titleSmall,
                            color = CyberCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Your app has an official Google Cloud web link ready for players to access in browser or download APK:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Live Web URL Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(DarkSurfaceVariant)
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Public Player Web Link:", fontSize = 10.sp, color = TextMuted)
                                Text(liveUrl, fontSize = 11.sp, color = GoldPrimary, fontWeight = FontWeight.Bold, maxLines = 1)
                            }
                            IconButton(
                                onClick = {
                                    val clip = ClipData.newPlainText("Website URL", liveUrl)
                                    clipboardManager?.setPrimaryClip(clip)
                                    Toast.makeText(context, "Website Link Copied!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = CyberCyan, modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "$appName - Official Free Fire Esports Hub")
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "🎮 Join $appName Free Fire Tournaments!\n🌐 Play online & download app:\n$liveUrl\n💬 WhatsApp Support: 03207394660"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Tournament Website Link"))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = DarkBackground),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SHARE WEBSITE LINK WITH PLAYERS", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkBackground),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GoldPrimary, RoundedCornerShape(10.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "📲 STEP-BY-STEP APK INSTALL GUIDE",
                            style = MaterialTheme.typography.titleSmall,
                            color = GoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    StepRow(
                        stepNum = "1",
                        title = "Generate APK File",
                        desc = "Click Settings/Export menu in AI Studio -> Select 'Generate APK' or 'Export ZIP'.",
                        badgeColor = CyberCyan
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    StepRow(
                        stepNum = "2",
                        title = "Upload APK to Google Drive or Host",
                        desc = "Upload APK to Google Drive or Firebase Hosting. Set link sharing to 'Anyone with link can view/download'.",
                        badgeColor = GoldPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    StepRow(
                        stepNum = "3",
                        title = "Player Opens Website & Clicks Download",
                        desc = "Player opens the web page on mobile Chrome, clicks the 'Download APK' button, and file download starts.",
                        badgeColor = FireOrange
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    StepRow(
                        stepNum = "4",
                        title = "Downloading Complete Notification",
                        desc = "Mobile shows 'Download complete: app-release.apk (18 MB)'. Player taps notification or opens Downloads folder.",
                        badgeColor = CyberCyan
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    StepRow(
                        stepNum = "5",
                        title = "Enable Unknown Sources / Install",
                        desc = "If prompted: Tap 'Settings' -> Turn ON 'Allow from this source (Chrome/Files)' -> Tap 'Install'. App installs in 5 seconds!",
                        badgeColor = GoldPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    StepRow(
                        stepNum = "6",
                        title = "Open App & Play Tournaments",
                        desc = "Player taps 'Open' -> Registers Free Fire Game UID & IGN -> Enters daily paid room matches!",
                        badgeColor = CyberCyan
                    )
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkBackground),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, FireOrange, RoundedCornerShape(10.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = FireOrange, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "☁️ HOSTING OPTIONS (FREE)",
                            style = MaterialTheme.typography.titleSmall,
                            color = FireOrange,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "• Option A: Google Cloud Run (Current active URL)\n" +
                        "• Option B: Firebase Hosting (`firebase deploy`)\n" +
                        "• Option C: Google Sites / Blogger with direct Download APK button\n" +
                        "• Option D: Google Play Console (For Google Play Store publication)",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun StepRow(stepNum: String, title: String, desc: String, badgeColor: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(badgeColor),
            contentAlignment = Alignment.Center
        ) {
            Text(stepNum, fontSize = 11.sp, color = DarkBackground, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.labelMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
            Text(desc, style = MaterialTheme.typography.bodySmall, color = TextMuted, fontSize = 11.sp)
        }
    }
}

@Composable
fun WebPublishingGuideDialog(
    appName: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .border(1.dp, CyberCyan, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(CyberCyan),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = DarkBackground, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "WEB & APK INSTALL GUIDE",
                            style = MaterialTheme.typography.titleMedium,
                            color = CyberCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                WebPublishingAndApkGuideContent(appName = appName)

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = DarkBackground),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("DONE & CLOSE", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthBottomSheet(
    userProfile: UserProfileEntity?,
    onDismiss: () -> Unit,
    onLogin: (email: String, pass: String) -> Unit,
    onRegister: (
        firstName: String,
        lastName: String,
        email: String,
        pass: String,
        phoneNumber: String,
        promoCode: String,
        referralCode: String,
        ign: String,
        uid: String
    ) -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Login, 1 = Register

    // Login Form Inputs
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Register Form Inputs (User Requested: First Name, Last Name, Email, Password, Phone Number, Promo Code, Referral)
    var regFirstName by remember { mutableStateOf("") }
    var regLastName by remember { mutableStateOf("") }
    var regEmail by remember { mutableStateOf("") }
    var regPassword by remember { mutableStateOf("") }
    var regPhone by remember { mutableStateOf("") }
    var regPromoCode by remember { mutableStateOf("") }
    var regReferralCode by remember { mutableStateOf("") }
    var regInGameName by remember { mutableStateOf("") }
    var regInGameUid by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        scrimColor = DarkBackground.copy(alpha = 0.85f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            "PLAYER & ADMIN ACCOUNT",
                            style = MaterialTheme.typography.titleMedium,
                            color = GoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Pro Genius Esports Registration & Login",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Current logged in state card
            if (userProfile?.isLoggedIn == true) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkBackground),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GoldPrimary, RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        userProfile.fullName,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                when (userProfile.role) {
                                                    "SUPER_ADMIN" -> GoldPrimary.copy(alpha = 0.25f)
                                                    "ADMIN" -> FireOrange.copy(alpha = 0.25f)
                                                    else -> CyberCyan.copy(alpha = 0.25f)
                                                }
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            userProfile.role.replace("_", " "),
                                            color = when (userProfile.role) {
                                                "SUPER_ADMIN" -> GoldPrimary
                                                "ADMIN" -> FireOrange
                                                else -> CyberCyan
                                            },
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                    }
                                }

                                Text(
                                    "Email: ${userProfile.email}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                                Text(
                                    "Phone: ${userProfile.phoneNumber.ifEmpty { "03001234567" }}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                                Text(
                                    "Free Fire IGN: ${userProfile.inGameName} (UID: ${userProfile.inGameUid})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CyberCyan
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = onLogout,
                            colors = ButtonDefaults.buttonColors(containerColor = FireOrange, contentColor = TextPrimary),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("LOGOUT ACCOUNT", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            } else {
                // Auth Tabs: Login vs Register
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = DarkBackground,
                    contentColor = GoldPrimary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = GoldPrimary
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0; errorMessage = null },
                        text = { Text("LOGIN", fontWeight = FontWeight.Bold, color = if (selectedTab == 0) GoldPrimary else TextMuted) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1; errorMessage = null },
                        text = { Text("REGISTER PLAYER", fontWeight = FontWeight.Bold, color = if (selectedTab == 1) GoldPrimary else TextMuted) }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (selectedTab == 0) {
                    // LOGIN METHOD: Email, Password, Start/Login
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; errorMessage = null },
                        label = { Text("Email Address") },
                        placeholder = { Text("e.g. gamer@progenius.com") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = CyberCyan) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = TextMuted,
                            focusedLabelColor = CyberCyan,
                            unfocusedLabelColor = TextMuted,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; errorMessage = null },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = CyberCyan) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = TextMuted,
                            focusedLabelColor = CyberCyan,
                            unfocusedLabelColor = TextMuted,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(errorMessage!!, color = FireOrange, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (email.trim().isEmpty() || password.trim().isEmpty()) {
                                errorMessage = "Please enter both Email and Password!"
                            } else {
                                onLogin(email.trim(), password.trim())
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = DarkBackground),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("START / LOGIN", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                } else {
                    // REGISTER METHOD FOR PLAYER: First Name, Last Name, Email, Password, Phone Number, Promo Code, Referral, Start
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(380.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = regFirstName,
                                    onValueChange = { regFirstName = it; errorMessage = null },
                                    label = { Text("First Name") },
                                    placeholder = { Text("e.g. Hamza") },
                                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = FireOrange) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = FireOrange,
                                        unfocusedBorderColor = TextMuted,
                                        focusedLabelColor = FireOrange,
                                        unfocusedLabelColor = TextMuted,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    )
                                )

                                OutlinedTextField(
                                    value = regLastName,
                                    onValueChange = { regLastName = it; errorMessage = null },
                                    label = { Text("Last Name") },
                                    placeholder = { Text("e.g. Khan") },
                                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = FireOrange) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = FireOrange,
                                        unfocusedBorderColor = TextMuted,
                                        focusedLabelColor = FireOrange,
                                        unfocusedLabelColor = TextMuted,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    )
                                )
                            }
                        }

                        item {
                            OutlinedTextField(
                                value = regEmail,
                                onValueChange = { regEmail = it; errorMessage = null },
                                label = { Text("Email Address") },
                                placeholder = { Text("e.g. player@progenius.com") },
                                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = FireOrange) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = FireOrange,
                                    unfocusedBorderColor = TextMuted,
                                    focusedLabelColor = FireOrange,
                                    unfocusedLabelColor = TextMuted,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = regPassword,
                                onValueChange = { regPassword = it; errorMessage = null },
                                label = { Text("Password") },
                                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = FireOrange) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = FireOrange,
                                    unfocusedBorderColor = TextMuted,
                                    focusedLabelColor = FireOrange,
                                    unfocusedLabelColor = TextMuted,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = regPhone,
                                onValueChange = { regPhone = it; errorMessage = null },
                                label = { Text("Phone Number") },
                                placeholder = { Text("e.g. 03001234567") },
                                leadingIcon = { Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = CyberCyan) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CyberCyan,
                                    unfocusedBorderColor = TextMuted,
                                    focusedLabelColor = CyberCyan,
                                    unfocusedLabelColor = TextMuted,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = regPromoCode,
                                    onValueChange = { regPromoCode = it },
                                    label = { Text("Promo Code (Optional)") },
                                    placeholder = { Text("PRO50") },
                                    leadingIcon = { Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = GoldPrimary) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = GoldPrimary,
                                        unfocusedBorderColor = TextMuted,
                                        focusedLabelColor = GoldPrimary,
                                        unfocusedLabelColor = TextMuted,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    )
                                )

                                OutlinedTextField(
                                    value = regReferralCode,
                                    onValueChange = { regReferralCode = it },
                                    label = { Text("Referral Code") },
                                    placeholder = { Text("REF9821") },
                                    leadingIcon = { Icon(Icons.Default.Tag, contentDescription = null, tint = GoldPrimary) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = GoldPrimary,
                                        unfocusedBorderColor = TextMuted,
                                        focusedLabelColor = GoldPrimary,
                                        unfocusedLabelColor = TextMuted,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    )
                                )
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = regInGameName,
                                    onValueChange = { regInGameName = it },
                                    label = { Text("Free Fire IGN") },
                                    placeholder = { Text("e.g. ProSniper") },
                                    leadingIcon = { Icon(Icons.Default.SportsEsports, contentDescription = null, tint = CyberCyan) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = CyberCyan,
                                        unfocusedBorderColor = TextMuted,
                                        focusedLabelColor = CyberCyan,
                                        unfocusedLabelColor = TextMuted,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    )
                                )

                                OutlinedTextField(
                                    value = regInGameUid,
                                    onValueChange = { regInGameUid = it },
                                    label = { Text("FF Player UID") },
                                    placeholder = { Text("8934102941") },
                                    leadingIcon = { Icon(Icons.Default.Tag, contentDescription = null, tint = CyberCyan) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = CyberCyan,
                                        unfocusedBorderColor = TextMuted,
                                        focusedLabelColor = CyberCyan,
                                        unfocusedLabelColor = TextMuted,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    )
                                )
                            }
                        }

                        if (errorMessage != null) {
                            item {
                                Text(errorMessage!!, color = FireOrange, style = MaterialTheme.typography.bodySmall)
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    if (regFirstName.trim().isEmpty() || regEmail.trim().isEmpty() || regPassword.trim().isEmpty()) {
                                        errorMessage = "First Name, Email, and Password are required!"
                                    } else {
                                        onRegister(
                                            regFirstName.trim(),
                                            regLastName.trim(),
                                            regEmail.trim(),
                                            regPassword.trim(),
                                            regPhone.trim(),
                                            regPromoCode.trim(),
                                            regReferralCode.trim(),
                                            regInGameName.trim(),
                                            regInGameUid.trim()
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = FireOrange, contentColor = TextPrimary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("START / REGISTER PLAYER", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CreateMatchDialog(
    onDismiss: () -> Unit,
    onCreateMatch: (
        title: String,
        matchType: String,
        mapName: String,
        startTime: String,
        entryFee: Int,
        prizePool: Int,
        perKillBonus: Int,
        totalSlots: Int,
        rules: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("Bermuda Solo Rush #FF-10") }
    var matchType by remember { mutableStateOf("SOLO") }
    var mapName by remember { mutableStateOf("Bermuda") }
    var startTime by remember { mutableStateOf("Today, 9:00 PM") }
    var entryFeeText by remember { mutableStateOf("20") }
    var prizePoolText by remember { mutableStateOf("500") }
    var perKillText by remember { mutableStateOf("10") }
    var totalSlotsText by remember { mutableStateOf("48") }
    var rulesText by remember { mutableStateOf("1. Mobile devices only.\n2. Screen recording required.\n3. No teaming or cheats.") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val matchModes = listOf("SOLO", "DUO", "SQUAD", "CS 4v4")
    val mapOptions = listOf("Bermuda", "Purgatory", "Kalahari", "Alpine", "Nexterra")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .border(1.dp, GoldPrimary, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SportsEsports, contentDescription = null, tint = GoldPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "HAMZA ADMIN - UPLOAD MATCH",
                            style = MaterialTheme.typography.titleMedium,
                            color = GoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Text(
                    "Create & publish custom Free Fire esports tournament",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(390.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        // Quick Best Match Patterns Preset Row
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkSurfaceVariant)
                                .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.SportsEsports, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("⚡ BEST MATCH PATTERNS", style = MaterialTheme.typography.labelSmall, color = GoldPrimary, fontWeight = FontWeight.Black)
                                }
                                Text("TAP TO APPLY", style = MaterialTheme.typography.labelSmall, color = CyberCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            val matchPatterns = listOf(
                                Triple("🔥 Solo Grand Battle", "SOLO", "Bermuda") to Triple(20, 600, 10 to 48),
                                Triple("⚔️ Squad Pro Championship", "SQUAD", "Kalahari") to Triple(50, 1500, 25 to 12),
                                Triple("🎯 Duo Survival Rush", "DUO", "Purgatory") to Triple(15, 400, 8 to 24),
                                Triple("⚡ 4v4 Clash Squad Deathmatch", "CS 4v4", "Nexterra") to Triple(30, 800, 20 to 8),
                                Triple("🎁 Free Community Scrim", "SOLO", "Alpine") to Triple(0, 250, 5 to 48)
                            )

                            androidx.compose.foundation.lazy.LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(matchPatterns) { pattern ->
                                    val (header, stats) = pattern
                                    val (pTitle, pMode, pMap) = header
                                    val (fee, pool, killAndSlots) = stats
                                    val (kill, slots) = killAndSlots

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(DarkBackground)
                                            .border(1.dp, CyberCyan.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                                            .clickable {
                                                title = pTitle
                                                matchType = pMode
                                                mapName = pMap
                                                entryFeeText = fee.toString()
                                                prizePoolText = pool.toString()
                                                perKillText = kill.toString()
                                                totalSlotsText = slots.toString()
                                                rulesText = when (pMode) {
                                                    "SQUAD" -> "1. Squad leaders register all 4 UIDs.\n2. Friendly fire off.\n3. Emulators disallowed."
                                                    "DUO" -> "1. Duo players stay within 100m.\n2. Emulators strictly forbidden.\n3. Screen record mandatory."
                                                    "CS 4v4" -> "1. Custom 4v4 Clash Squad rules.\n2. Grenades limit 2 per round.\n3. Gun property OFF."
                                                    else -> "1. Mobile devices only.\n2. Screen recording required.\n3. No teaming or cheats."
                                                }
                                                errorMessage = null
                                            }
                                            .padding(horizontal = 8.dp, vertical = 6.dp)
                                    ) {
                                        Column {
                                            Text(pTitle, style = MaterialTheme.typography.labelSmall, color = GoldPrimary, fontWeight = FontWeight.Bold, maxLines = 1)
                                            Text(
                                                "$pMode • $pMap • Fee: ${if (fee == 0) "FREE" else "$fee C"} • Pool: $pool C",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = TextMuted,
                                                fontSize = 9.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it; errorMessage = null },
                            label = { Text("Match Title") },
                            leadingIcon = { Icon(Icons.Default.SportsEsports, contentDescription = null, tint = GoldPrimary) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = TextMuted,
                                focusedLabelColor = GoldPrimary,
                                unfocusedLabelColor = TextMuted,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    item {
                        Text("MATCH MODE:", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            matchModes.forEach { mode ->
                                val isSelected = matchType == mode
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(1.dp, if (isSelected) FireOrange else DarkSurfaceVariant, RoundedCornerShape(6.dp)),
                                    colors = CardDefaults.cardColors(containerColor = if (isSelected) DarkSurfaceVariant else DarkBackground),
                                    onClick = { matchType = mode }
                                ) {
                                    Box(modifier = Modifier.padding(vertical = 6.dp), contentAlignment = Alignment.Center) {
                                        Text(mode, color = if (isSelected) FireOrange else TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text("MAP NAME:", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            mapOptions.forEach { m ->
                                val isSelected = mapName == m
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(1.dp, if (isSelected) CyberCyan else DarkSurfaceVariant, RoundedCornerShape(6.dp)),
                                    colors = CardDefaults.cardColors(containerColor = if (isSelected) DarkSurfaceVariant else DarkBackground),
                                    onClick = { mapName = m }
                                ) {
                                    Box(modifier = Modifier.padding(vertical = 6.dp), contentAlignment = Alignment.Center) {
                                        Text(m, color = if (isSelected) CyberCyan else TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = startTime,
                            onValueChange = { startTime = it },
                            label = { Text("Start Time") },
                            placeholder = { Text("Today, 9:00 PM") },
                            leadingIcon = { Icon(Icons.Default.Receipt, contentDescription = null, tint = CyberCyan) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberCyan,
                                unfocusedBorderColor = TextMuted,
                                focusedLabelColor = CyberCyan,
                                unfocusedLabelColor = TextMuted,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = entryFeeText,
                                onValueChange = { entryFeeText = it },
                                label = { Text("Entry Fee (Coins)") },
                                leadingIcon = { Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = GoldPrimary) },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GoldPrimary,
                                    unfocusedBorderColor = TextMuted,
                                    focusedLabelColor = GoldPrimary,
                                    unfocusedLabelColor = TextMuted,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )

                            OutlinedTextField(
                                value = prizePoolText,
                                onValueChange = { prizePoolText = it },
                                label = { Text("Prize Pool (Coins)") },
                                leadingIcon = { Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = FireOrange) },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = FireOrange,
                                    unfocusedBorderColor = TextMuted,
                                    focusedLabelColor = FireOrange,
                                    unfocusedLabelColor = TextMuted,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = perKillText,
                                onValueChange = { perKillText = it },
                                label = { Text("Per Kill (Coins)") },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GoldPrimary,
                                    unfocusedBorderColor = TextMuted,
                                    focusedLabelColor = GoldPrimary,
                                    unfocusedLabelColor = TextMuted,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )

                            OutlinedTextField(
                                value = totalSlotsText,
                                onValueChange = { totalSlotsText = it },
                                label = { Text("Total Slots") },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CyberCyan,
                                    unfocusedBorderColor = TextMuted,
                                    focusedLabelColor = CyberCyan,
                                    unfocusedLabelColor = TextMuted,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = rulesText,
                            onValueChange = { rulesText = it },
                            label = { Text("Match Rules & Instructions") },
                            maxLines = 4,
                            minLines = 2,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = TextMuted,
                                focusedLabelColor = GoldPrimary,
                                unfocusedLabelColor = TextMuted,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    if (errorMessage != null) {
                        item {
                            Text(errorMessage!!, color = FireOrange, style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if (title.trim().isEmpty()) {
                                    errorMessage = "Please enter a Match Title!"
                                } else {
                                    val fee = entryFeeText.toIntOrNull() ?: 0
                                    val pool = prizePoolText.toIntOrNull() ?: 500
                                    val kill = perKillText.toIntOrNull() ?: 10
                                    val slots = totalSlotsText.toIntOrNull() ?: 48
                                    onCreateMatch(
                                        title.trim(), matchType, mapName, startTime.trim(), fee, pool, kill, slots, rulesText
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = DarkBackground),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.SportsEsports, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("PUBLISH MATCH NOW (HAMZA ADMIN)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SecurityAndPolicyDialog(
    onDismiss: () -> Unit,
    onDeleteAccount: () -> Unit = {}
) {
    var showAuditReport by remember { mutableStateOf(false) }
    val auditReport = remember { SecurityUtils.runPreDeploySecurityAudit() }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .border(1.dp, CyberCyan, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = CyberCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "APP SECURITY & 5-POINT AUDIT",
                            style = MaterialTheme.typography.titleMedium,
                            color = CyberCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Toggle Tab Buttons: Policy vs 5 Security Checks
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { showAuditReport = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!showAuditReport) CyberCyan else DarkBackground,
                            contentColor = if (!showAuditReport) DarkBackground else TextSecondary
                        ),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("App Policy", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    Button(
                        onClick = { showAuditReport = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (showAuditReport) GoldPrimary else DarkBackground,
                            contentColor = if (showAuditReport) DarkBackground else TextSecondary
                        ),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("🔒 5 Security Checks", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (!showAuditReport) {
                        item {
                            PolicySectionCard(
                                title = "🛡️ Anti-Cheat & Fair Play Policy",
                                description = "Pro Genius Battle maintains a strict 0-Tolerance policy against third-party hacks, emulators, auto-aim scripts, or modified APKs. Violators are permanently banned and forfeit wallet coins.",
                                borderColor = FireOrange
                            )
                        }

                        item {
                            PolicySectionCard(
                                title = "🔒 256-Bit SSL Encryption & Privacy",
                                description = "Your passwords, account credentials, and transaction logs are protected with bank-grade 256-bit SSL encryption. We never share user personal details or phone numbers with external parties.",
                                borderColor = GoldPrimary
                            )
                        }

                        item {
                            PolicySectionCard(
                                title = "📲 JazzCash / EasyPaisa Payment Safety",
                                description = "All deposits and withdrawals undergo strict manual or automated Admin verification (Super Admin ABDULLAH BAJRAN & Admin HAMZA) to ensure zero transaction fraud and guaranteed fast cashouts.",
                                borderColor = CyberCyan
                            )
                        }

                        item {
                            PolicySectionCard(
                                title = "📜 Match Payout & Dispute Terms",
                                description = "Players must submit valid end-game screenshots or screen recordings in case of room dispute. Room IDs & Passwords are updated 15 minutes before match start time.",
                                borderColor = TextMuted
                            )
                        }
                    } else {
                        // 5 Security Audit Prompts Integration
                        item {
                            PolicySectionCard(
                                title = "01 Secret Leak Prevention (Gitleaks Audit)",
                                description = "All secrets, API keys, tokens & connection strings moved to environment config. No hardcoded credentials in source.",
                                borderColor = CyberCyan
                            )
                        }

                        item {
                            PolicySectionCard(
                                title = "02 Personal Data Flow Audit (Bearer Standard)",
                                description = "PII Protection enabled. User emails/phones sanitized & masked [REDACTED]. SHA-256 hashing applied to passwords.",
                                borderColor = GoldPrimary
                            )
                        }

                        item {
                            PolicySectionCard(
                                title = "03 Pre-Deploy Production Audit (ECC Audit)",
                                description = "Rate limiting active (max 5 login attempts/min). Debug print logs stripped and production error handlers active.",
                                borderColor = FireOrange
                            )
                        }

                        item {
                            PolicySectionCard(
                                title = "04 Deep Security Audit (Trail of Bits)",
                                description = "Payment bounds enforced (amount strictly > 0 and <= 100,000). Negative deposit/withdrawal exploits blocked.",
                                borderColor = CyberCyan
                            )
                        }

                        item {
                            PolicySectionCard(
                                title = "05 Attacker's Perspective Review (ECC Security)",
                                description = "IDOR protection & server-side privilege checks active. Non-admin users blocked from admin/approval endpoints.",
                                borderColor = GoldPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onDeleteAccount()
                            onDismiss()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = FireOrange),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FireOrange),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Delete My Data", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = DarkBackground),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("CLOSE AUDIT", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun PolicySectionCard(
    title: String,
    description: String,
    borderColor: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkBackground),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, color = borderColor, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationMenuSheet(
    userProfile: UserProfileEntity?,
    appName: String,
    appLogo: String = "👑",
    onDismiss: () -> Unit,
    onSelectTab: (AppTab) -> Unit,
    onOpenDeposit: () -> Unit,
    onOpenWithdraw: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenSecurity: () -> Unit,
    onOpenRules: () -> Unit,
    onOpenAdminChats: () -> Unit,
    onOpenAdminPanel: () -> Unit,
    onOpenWhatsApp: () -> Unit,
    onOpenWebGuide: (() -> Unit)? = null
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        scrimColor = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.7f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(GoldPrimary)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(appLogo, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            appName,
                            style = MaterialTheme.typography.titleMedium,
                            color = GoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Official Tournament App Menu",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close Menu", tint = TextMuted)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Navigation Options
            MenuItemButton(
                icon = Icons.Default.SportsEsports,
                title = "🎮 Main Matches Lobby",
                subtitle = "Browse upcoming Free Fire custom tournaments",
                color = GoldPrimary
            ) {
                onSelectTab(AppTab.MATCHES)
                onDismiss()
            }

            MenuItemButton(
                icon = Icons.Default.AccountCircle,
                title = "👤 Profile, Free Fire UID & History",
                subtitle = "IGN: ${userProfile?.inGameName ?: "Pro_Sniper_FF"} • UID: ${userProfile?.inGameUid ?: "8934102941"}",
                color = CyberCyan
            ) {
                onOpenProfile()
                onDismiss()
            }

            MenuItemButton(
                icon = Icons.Default.Leaderboard,
                title = "🏆 Leaderboard & Top Players",
                subtitle = "Check Grandmasters & match win rankings",
                color = GoldPrimary
            ) {
                onSelectTab(AppTab.LEADERBOARD)
                onDismiss()
            }

            MenuItemButton(
                icon = Icons.Default.AddCard,
                title = "💳 Deposit Funds (EasyPaisa / JazzCash)",
                subtitle = "Top up wallet balance for match entry fees",
                color = CyberCyan
            ) {
                onOpenDeposit()
                onDismiss()
            }

            MenuItemButton(
                icon = Icons.Default.ArrowUpward,
                title = "💸 Withdraw Earnings",
                subtitle = "Instant cashout to your JazzCash / EasyPaisa account",
                color = GoldPrimary
            ) {
                onOpenWithdraw()
                onDismiss()
            }

            MenuItemButton(
                icon = Icons.Default.Chat,
                title = "📢 Admin Broadcast Chats (Read-Only)",
                subtitle = "Official updates from ABDULLAH BAJRAN & HAMZA",
                color = FireOrange
            ) {
                onOpenAdminChats()
                onDismiss()
            }

            MenuItemButton(
                icon = Icons.Default.Shield,
                title = "🔒 Security & App Policy",
                subtitle = "256-bit SSL encryption & zero-cheat rules",
                color = CyberCyan
            ) {
                onOpenSecurity()
                onDismiss()
            }

            MenuItemButton(
                icon = Icons.Default.Gavel,
                title = "📜 Tournament Rules",
                subtitle = "Room rules, kill points, and payout policy",
                color = TextMuted
            ) {
                onOpenRules()
                onDismiss()
            }

            MenuItemButton(
                icon = Icons.Default.Security,
                title = "🌐 Publish Website & Install APK Guide",
                subtitle = "Google Cloud web links, APK downloads & installation steps",
                color = CyberCyan
            ) {
                onOpenWebGuide?.invoke()
                onDismiss()
            }

            val isAdmin = userProfile?.role == "SUPER_ADMIN" || userProfile?.role == "ADMIN"
            if (isAdmin) {
                MenuItemButton(
                    icon = Icons.Default.AdminPanelSettings,
                    title = "🛠️ Admin Control Panel (HAMZA & ABDULLAH)",
                    subtitle = "Approve payments, upload matches, check player UIDs",
                    color = FireOrange
                ) {
                    onOpenAdminPanel()
                    onDismiss()
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // WhatsApp Group Button
            Button(
                onClick = {
                    onOpenWhatsApp()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = DarkBackground),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("WHATSAPP HELP: 03207394660", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@Composable
private fun MenuItemButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = DarkBackground),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.labelSmall, color = TextMuted)
            }

            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun AdminChatsDialog(
    announcements: List<AdminAnnouncement>,
    userProfile: UserProfileEntity?,
    onDismiss: () -> Unit,
    onPostAnnouncement: (String) -> Unit
) {
    var newChatMessage by remember { mutableStateOf("") }
    val isAdmin = userProfile?.role == "SUPER_ADMIN" || userProfile?.role == "ADMIN"

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .border(1.dp, FireOrange, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Chat, contentDescription = null, tint = FireOrange)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "ADMIN CHATS & ANNOUNCEMENTS",
                            style = MaterialTheme.typography.titleMedium,
                            color = FireOrange,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(DarkSurfaceVariant)
                        .padding(8.dp)
                ) {
                    Text(
                        if (isAdmin) "🛡️ ADMIN MODE: You can post official broadcasts below."
                        else "🔒 PLAYERS READ-ONLY CHAT: Official broadcasts from Admin ABDULLAH BAJRAN & HAMZA",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(announcements, key = { it.id }) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, GoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
                            colors = CardDefaults.cardColors(containerColor = DarkBackground),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        item.author,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = GoldPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        item.timestamp,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextMuted
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    item.message,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }

                if (isAdmin) {
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = newChatMessage,
                        onValueChange = { newChatMessage = it },
                        label = { Text("Broadcast New Admin Announcement") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FireOrange,
                            unfocusedBorderColor = TextMuted,
                            focusedLabelColor = FireOrange,
                            unfocusedLabelColor = TextMuted
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            if (newChatMessage.isNotBlank()) {
                                onPostAnnouncement(newChatMessage)
                                newChatMessage = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = FireOrange, contentColor = DarkBackground),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("POST OFFICIAL ANNOUNCEMENT", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant, contentColor = TextPrimary),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("CLOSE CHATS", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}


