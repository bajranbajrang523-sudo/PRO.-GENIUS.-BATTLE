package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tournaments")
data class TournamentEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val matchType: String, // SOLO, DUO, SQUAD
    val mapName: String,   // Bermuda, Purgatory, Kalahari, Alpine, Nexterra
    val startTime: String, // e.g. "Today, 8:00 PM"
    val entryFee: Int,     // in Coins (0 for Free)
    val prizePool: Int,    // Total Coins
    val perKillBonus: Int, // Coins per kill
    val totalSlots: Int,   // e.g. 48 or 100
    val joinedSlots: Int,
    val status: String,    // UPCOMING, LIVE, COMPLETED
    val roomId: String,    // "ROOM-982143" or "RELEASING AT 7:45 PM"
    val roomPassword: String,
    val rules: String,
    val isJoined: Boolean = false,
    val joinedInGameName: String? = null,
    val joinedUid: String? = null,
    val winnerName: String? = null,
    val playerKills: Int = 0,
    val playerWinnings: Int = 0
)

@Entity(tableName = "wallet_transactions")
data class WalletTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Int,
    val type: String, // DEPOSIT, WITHDRAWAL, MATCH_FEE, WINNING, BONUS
    val timestamp: Long = System.currentTimeMillis(),
    val status: String, // SUCCESS, PENDING, REJECTED
    val paymentMethod: String = "JazzCash", // JazzCash, EasyPaisa, Bank, UPI
    val senderNumber: String? = null,
    val transactionId: String? = null,
    val accountTitle: String? = null,
    val adminNotes: String? = null
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val userId: String = "user_pgb_01",
    val isLoggedIn: Boolean = false,
    val email: String = "player@progenius.com",
    val password: String = "",
    val firstName: String = "Pro",
    val lastName: String = "Gamer",
    val fullName: String = "Pro Gamer",
    val phoneNumber: String = "03001234567",
    val promoCode: String = "",
    val referralCode: String = "",
    val role: String = "PLAYER", // "SUPER_ADMIN", "ADMIN", "PLAYER"
    val inGameName: String = "Pro_Sniper_FF",
    val inGameUid: String = "8934102941",
    val coinBalance: Int = 250,
    val totalMatches: Int = 12,
    val totalKills: Int = 47,
    val totalEarnings: Int = 1200,
    val rankTitle: String = "Grandmaster"
)

@Entity(tableName = "app_upgrades")
data class AppUpgradeEntity(
    @PrimaryKey val id: Int = 1,
    val currentVersion: String = "v2.3.0",
    val latestVersion: String = "v2.4.0 (Anti-Cheat & Ultra HD)",
    val releaseNotes: String = "Features high-speed live room sync, direct APK updater, Bermuda 4K resource download, and instant tournament payout system.",
    val fileSizeMb: Float = 28.5f,
    val isUpdateAvailable: Boolean = true,
    val downloadStatus: String = "NOT_STARTED", // NOT_STARTED, DOWNLOADING, PAUSED, COMPLETED, INSTALLED
    val downloadProgress: Float = 0.0f
)

@Entity(tableName = "map_resources")
data class MapResourceEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val category: String, // MAP_PACK, RULES_PDF, ANTI_CHEAT
    val description: String,
    val sizeMb: Float,
    val downloadStatus: String = "IDLE", // IDLE, DOWNLOADING, COMPLETED
    val progress: Float = 0.0f
)

data class AdminAnnouncement(
    val id: Int,
    val author: String = "ABDULLAH BAJRAN (Admin)",
    val message: String,
    val timestamp: String,
    val isImportant: Boolean = true
)

data class JoinedPlayerDetail(
    val playerName: String,
    val inGameName: String,
    val inGameUid: String,
    val matchTitle: String,
    val slotNumber: Int,
    val joinTime: String = "Just Now",
    val tournamentId: Int = 0,
    val isVerified: Boolean = true,
    val userEmail: String? = null,
    val phoneNumber: String? = null
)

