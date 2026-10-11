package com.example.ui.screens

import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfileEntity
import com.example.data.model.WalletTransactionEntity
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.FireOrange
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults

@Composable
fun UserWallet(
    userProfile: UserProfileEntity?,
    onTopUpClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                Brush.horizontalGradient(listOf(GoldPrimary, CyberCyan)),
                RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text("AVAILABLE BALANCE", style = MaterialTheme.typography.labelSmall, color = TextMuted)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.MonetizationOn,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "${userProfile?.coinBalance ?: 0} Coins",
                        style = MaterialTheme.typography.headlineMedium,
                        color = GoldPrimary,
                        fontWeight = FontWeight.Black
                    )
                }

                Button(
                    onClick = onTopUpClick,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = DarkBackground),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.AddCard, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("TOP UP", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun WalletScreen(
    userProfile: UserProfileEntity?,
    transactions: List<WalletTransactionEntity>,
    onAddCoinsClick: () -> Unit,
    onWithdrawCoinsClick: () -> Unit,
    onOpenAdminPanelClick: () -> Unit,
    onClaimDailyReward: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "MY WALLET & PASS",
                    style = MaterialTheme.typography.titleLarge,
                    color = GoldPrimary,
                    fontWeight = FontWeight.Black
                )

                Button(
                    onClick = onOpenAdminPanelClick,
                    colors = ButtonDefaults.buttonColors(containerColor = FireOrange, contentColor = TextPrimary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("ADMIN PANEL", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }

        // Wallet Balance Card using UserWallet
        item {
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                UserWallet(
                    userProfile = userProfile,
                    onTopUpClick = onAddCoinsClick
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Action Buttons Row: Deposit & Withdraw
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onAddCoinsClick,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = DarkBackground),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.AddCard, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("DEPOSIT", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onWithdrawCoinsClick,
                        border = androidx.compose.foundation.BorderStroke(1.dp, FireOrange),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = FireOrange, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("WITHDRAW", color = FireOrange, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkSurfaceVariant)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Total Earnings", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        Text(
                            "${userProfile?.totalEarnings ?: 0} Coins",
                            style = MaterialTheme.typography.titleSmall,
                            color = CyberCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column {
                        Text("Matches Played", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        Text(
                            "${userProfile?.totalMatches ?: 0}",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column {
                        Text("Total Kills", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        Text(
                            "${userProfile?.totalKills ?: 0}",
                            style = MaterialTheme.typography.titleSmall,
                            color = FireOrange,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Daily Bonus Claim Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .border(1.dp, CyberCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Daily Check-In Reward", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                            Text("Claim 25 Free Coins every day", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                        }
                    }

                    OutlinedButton(
                        onClick = onClaimDailyReward,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("CLAIM +25", color = CyberCyan, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Transactions History Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "TRANSACTION HISTORY",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (transactions.isEmpty()) {
            item {
                Text(
                    "No transactions recorded yet.",
                    color = TextMuted,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        } else {
            items(transactions, key = { it.id }) { tx ->
                val isPositive = tx.amount > 0
                val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isPositive) CyberCyan.copy(alpha = 0.15f) else FireOrange.copy(alpha = 0.15f))
                                        .padding(8.dp)
                                ) {
                                    Icon(
                                        if (isPositive) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                                        contentDescription = null,
                                        tint = if (isPositive) CyberCyan else FireOrange,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(tx.title, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                                    Text(
                                        dateFormat.format(Date(tx.timestamp)),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextMuted
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = if (isPositive) "+${tx.amount} Coins" else "${tx.amount} Coins",
                                    color = if (isPositive) CyberCyan else FireOrange,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )

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
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    )
                                }
                            }
                        }

                        if (!tx.senderNumber.isNullOrEmpty() || !tx.transactionId.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "${tx.paymentMethod} • Sender No: ${tx.senderNumber ?: "-"} ${if (!tx.transactionId.isNullOrEmpty()) "• TID: ${tx.transactionId}" else ""}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCoinsBottomSheet(
    onDismiss: () -> Unit,
    onConfirmAddCoins: (amount: Int, method: String) -> Unit,
    onSubmitJazzCashEasyPaisaDeposit: (amount: Int, method: String, senderNumber: String, tid: String) -> Unit = { _, _, _, _ -> }
) {
    var selectedAmount by remember { mutableStateOf(100) }
    var selectedMethod by remember { mutableStateOf("JazzCash") }
    var senderNumber by remember { mutableStateOf("") }
    var transactionId by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val amounts = listOf(50, 100, 200, 500)
    val methods = listOf("JazzCash", "EasyPaisa", "UPI / Google Pay", "Card")

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
            Text(
                "DEPOSIT COINS TO WALLET",
                style = MaterialTheme.typography.titleMedium,
                color = GoldPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Select coin package and payment method",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("SELECT COIN PACK", style = MaterialTheme.typography.labelSmall, color = TextMuted)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                amounts.forEach { amount ->
                    val isSelected = selectedAmount == amount
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                1.dp,
                                if (isSelected) GoldPrimary else DarkSurfaceVariant,
                                RoundedCornerShape(10.dp)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) DarkSurfaceVariant else DarkBackground
                        ),
                        onClick = { selectedAmount = amount }
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "$amount",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isSelected) GoldPrimary else TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text("Coins", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("PAYMENT METHOD", style = MaterialTheme.typography.labelSmall, color = TextMuted)
            Spacer(modifier = Modifier.height(8.dp))

            methods.forEach { method ->
                val isSelected = selectedMethod == method
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .border(
                            1.dp,
                            if (isSelected) CyberCyan else DarkSurfaceVariant,
                            RoundedCornerShape(8.dp)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) DarkSurfaceVariant else DarkBackground
                    ),
                    onClick = { selectedMethod = method; errorMessage = null }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Payment,
                            contentDescription = null,
                            tint = if (isSelected) CyberCyan else TextMuted
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            method,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isSelected) CyberCyan else TextPrimary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            // If JazzCash or EasyPaisa is selected, show Account Number & TID input
            if (selectedMethod == "JazzCash" || selectedMethod == "EasyPaisa") {
                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkSurfaceVariant)
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "📲 SEND PAYMENT TO ADMIN $selectedMethod:",
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Account No: 03207394660\nTitle: ABDULLAH BAJRAN",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        val context = LocalContext.current
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                val clip = android.content.ClipData.newPlainText("Admin Account", "03207394660")
                                clipboard.setPrimaryClip(clip)
                                android.widget.Toast.makeText(context, "$selectedMethod Account Number Copied!", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = GoldPrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = senderNumber,
                    onValueChange = { senderNumber = it; errorMessage = null },
                    label = { Text("Your Sender Mobile Number") },
                    placeholder = { Text("e.g. 03123456789") },
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

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = transactionId,
                    onValueChange = { transactionId = it; errorMessage = null },
                    label = { Text("Transaction ID (TID / Trx ID)") },
                    placeholder = { Text("e.g. 8019482710") },
                    leadingIcon = { Icon(Icons.Default.Tag, contentDescription = null, tint = CyberCyan) },
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

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(errorMessage!!, color = FireOrange, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (selectedMethod == "JazzCash" || selectedMethod == "EasyPaisa") {
                        if (senderNumber.trim().isEmpty() || transactionId.trim().isEmpty()) {
                            errorMessage = "Please enter your Mobile Sender Number and Transaction ID (TID)!"
                        } else {
                            onSubmitJazzCashEasyPaisaDeposit(selectedAmount, selectedMethod, senderNumber.trim(), transactionId.trim())
                        }
                    } else {
                        onConfirmAddCoins(selectedAmount, selectedMethod)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = DarkBackground),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    if (selectedMethod == "JazzCash" || selectedMethod == "EasyPaisa")
                        "SUBMIT $selectedAmount COINS DEPOSIT FOR ADMIN APPROVAL"
                    else
                        "ADD $selectedAmount COINS NOW",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
