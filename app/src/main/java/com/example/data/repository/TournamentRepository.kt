package com.example.data.repository

import com.example.data.dao.TournamentDao
import com.example.data.dao.UpgradeDao
import com.example.data.dao.UserDao
import com.example.data.dao.WalletDao
import com.example.data.model.AppUpgradeEntity
import com.example.data.model.MapResourceEntity
import com.example.data.model.TournamentEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WalletTransactionEntity
import com.example.util.SecurityUtils
import kotlinx.coroutines.flow.Flow

class TournamentRepository(
    private val tournamentDao: TournamentDao,
    private val walletDao: WalletDao,
    private val userDao: UserDao,
    private val upgradeDao: UpgradeDao
) {
    val allTournaments: Flow<List<TournamentEntity>> = tournamentDao.getAllTournaments()
    val joinedTournaments: Flow<List<TournamentEntity>> = tournamentDao.getJoinedTournaments()
    val walletTransactions: Flow<List<WalletTransactionEntity>> = walletDao.getTransactions()
    val userProfile: Flow<UserProfileEntity?> = userDao.getUserProfile()
    val upgradeInfo: Flow<AppUpgradeEntity?> = upgradeDao.getUpgradeInfo()
    val mapResources: Flow<List<MapResourceEntity>> = upgradeDao.getMapResources()

    suspend fun seedInitialDataIfEmpty() {
        if (tournamentDao.getTournamentCount() == 0) {
            val initialTournaments = listOf(
                TournamentEntity(
                    id = 101,
                    title = "🔥 Grand Bermuda Solo Battle #101",
                    matchType = "SOLO",
                    mapName = "Bermuda",
                    startTime = "Today, 8:30 PM",
                    entryFee = 20,
                    prizePool = 600,
                    perKillBonus = 10,
                    totalSlots = 48,
                    joinedSlots = 38,
                    status = "UPCOMING",
                    roomId = "PGB-882193",
                    roomPassword = "7712",
                    rules = "1. No Emulators or Scripts. 2. Must join room before 8:25 PM. 3. Screenshots required for disputes.",
                    isJoined = true,
                    joinedInGameName = "Pro_Sniper_FF",
                    joinedUid = "8934102941"
                ),
                TournamentEntity(
                    id = 102,
                    title = "⚡ Pro Squad Kalahari Championship",
                    matchType = "SQUAD",
                    mapName = "Kalahari",
                    startTime = "Today, 9:45 PM",
                    entryFee = 50,
                    prizePool = 1500,
                    perKillBonus = 25,
                    totalSlots = 12, // 12 Squads = 48 players
                    joinedSlots = 9,
                    status = "UPCOMING",
                    roomId = "PGB-901124",
                    roomPassword = "9901",
                    rules = "1. Squad leaders must register all 4 Free Fire UIDs. 2. Friendly fire disabled. 3. Recording recommended."
                ),
                TournamentEntity(
                    id = 103,
                    title = "🎯 Duo Purgatory Survival Rush",
                    matchType = "DUO",
                    mapName = "Purgatory",
                    startTime = "Tomorrow, 6:00 PM",
                    entryFee = 15,
                    prizePool = 400,
                    perKillBonus = 8,
                    totalSlots = 24,
                    joinedSlots = 18,
                    status = "UPCOMING",
                    roomId = "RELEASING AT 5:45 PM",
                    roomPassword = "LOCKED",
                    rules = "1. Duo players must stay within 100m in-game. 2. Respect anti-cheat policies."
                ),
                TournamentEntity(
                    id = 104,
                    title = "🏆 Midnight Alpine Solo Deathmatch",
                    matchType = "SOLO",
                    mapName = "Alpine",
                    startTime = "LIVE NOW",
                    entryFee = 30,
                    prizePool = 800,
                    perKillBonus = 15,
                    totalSlots = 48,
                    joinedSlots = 48,
                    status = "LIVE",
                    roomId = "LIVE-ROOM-441",
                    roomPassword = "3310",
                    rules = "1. Match is actively streaming. Spectators blocked."
                ),
                TournamentEntity(
                    id = 105,
                    title = "🎁 Weekend Free Fire Practice Cup (Free)",
                    matchType = "SOLO",
                    mapName = "Bermuda",
                    startTime = "Tomorrow, 4:00 PM",
                    entryFee = 0,
                    prizePool = 200,
                    perKillBonus = 5,
                    totalSlots = 100,
                    joinedSlots = 64,
                    status = "UPCOMING",
                    roomId = "RELEASING AT 3:45 PM",
                    roomPassword = "LOCKED",
                    rules = "1. Free entry cup for all registered Free Fire gamers."
                )
            )
            tournamentDao.insertTournaments(initialTournaments)
        }

        if (tournamentDao.getTournamentById(98) == null) {
            val pastParticipations = listOf(
                TournamentEntity(
                    id = 98,
                    title = "🏆 Bermuda Pro Champions Cup #98",
                    matchType = "SOLO",
                    mapName = "Bermuda",
                    startTime = "Yesterday, 9:00 PM",
                    entryFee = 30,
                    prizePool = 800,
                    perKillBonus = 15,
                    totalSlots = 48,
                    joinedSlots = 48,
                    status = "COMPLETED",
                    roomId = "PGB-COMP-771",
                    roomPassword = "CLOSED",
                    rules = "1. Official Pro Genius Tournament.\n2. Final rankings verified.",
                    isJoined = true,
                    joinedInGameName = "Pro_Sniper_FF",
                    joinedUid = "8934102941",
                    winnerName = "Pro_Sniper_FF (You!)",
                    playerKills = 7,
                    playerWinnings = 350
                ),
                TournamentEntity(
                    id = 99,
                    title = "⚡ Purgatory Duo Clash #99",
                    matchType = "DUO",
                    mapName = "Purgatory",
                    startTime = "2 Days Ago, 7:30 PM",
                    entryFee = 20,
                    prizePool = 500,
                    perKillBonus = 10,
                    totalSlots = 24,
                    joinedSlots = 24,
                    status = "COMPLETED",
                    roomId = "PGB-COMP-512",
                    roomPassword = "CLOSED",
                    rules = "1. Official Duo Tournament.",
                    isJoined = true,
                    joinedInGameName = "Pro_Sniper_FF",
                    joinedUid = "8934102941",
                    winnerName = "Ghost_Rider_PK",
                    playerKills = 4,
                    playerWinnings = 120
                )
            )
            tournamentDao.insertTournaments(pastParticipations)
        }

        if (userDao.getUserProfileDirect() == null) {
            userDao.insertOrUpdateProfile(UserProfileEntity())
        }

        if (walletDao.getTransactions() == null) {
            val sampleTx = listOf(
                WalletTransactionEntity(
                    title = "Welcome Bonus Coins",
                    amount = 250,
                    type = "BONUS",
                    status = "SUCCESS"
                )
            )
            walletDao.insertTransactions(sampleTx)
        }

        if (upgradeDao.getUpgradeInfoDirect() == null) {
            upgradeDao.insertOrUpdateUpgrade(AppUpgradeEntity())
        }

        val mapCount = upgradeDao.getMapResources()
        // Insert map resources default
        upgradeDao.insertMapResources(
            listOf(
                MapResourceEntity(
                    id = 1,
                    title = "Bermuda Remastered 4K Pack",
                    category = "MAP_PACK",
                    description = "Ultra high resolution textures and zone markers for Free Fire Bermuda.",
                    sizeMb = 42.5f,
                    downloadStatus = "IDLE"
                ),
                MapResourceEntity(
                    id = 2,
                    title = "Kalahari & Alpine Esports Resource",
                    category = "MAP_PACK",
                    description = "Official Esports custom room assets and anti-lag graphics profiles.",
                    sizeMb = 68.0f,
                    downloadStatus = "IDLE"
                ),
                MapResourceEntity(
                    id = 3,
                    title = "Pro Genius Rulebook & FairPlay PDF",
                    category = "RULES_PDF",
                    description = "Complete guidelines on kill verification, room credentials, and tournament regulations.",
                    sizeMb = 3.2f,
                    downloadStatus = "COMPLETED",
                    progress = 1.0f
                ),
                MapResourceEntity(
                    id = 4,
                    title = "Anti-Cheat Guard Engine v2.4",
                    category = "ANTI_CHEAT",
                    description = "Automated room integrity scanner to detect unauthorized scripts and third-party tools.",
                    sizeMb = 12.8f,
                    downloadStatus = "COMPLETED",
                    progress = 1.0f
                )
            )
        )
    }

    suspend fun joinTournament(
        tournamentId: Int,
        ign: String,
        uid: String
    ): Result<String> {
        val tournament = tournamentDao.getTournamentById(tournamentId)
            ?: return Result.failure(Exception("Tournament not found"))

        if (tournament.isJoined) {
            return Result.failure(Exception("Already joined this tournament!"))
        }

        val profile = userDao.getUserProfileDirect() ?: UserProfileEntity()
        if (profile.coinBalance < tournament.entryFee) {
            return Result.failure(Exception("Insufficient Coin Balance! Please add coins to your wallet."))
        }

        // Deduct Fee if entryFee > 0
        if (tournament.entryFee > 0) {
            userDao.updateCoinBalance(-tournament.entryFee)
            walletDao.insertTransaction(
                WalletTransactionEntity(
                    title = "Joined ${tournament.title}",
                    amount = -tournament.entryFee,
                    type = "MATCH_FEE",
                    status = "SUCCESS"
                )
            )
        }

        // Update tournament state
        val updated = tournament.copy(
            isJoined = true,
            joinedInGameName = ign,
            joinedUid = uid,
            joinedSlots = (tournament.joinedSlots + 1).coerceAtMost(tournament.totalSlots)
        )
        tournamentDao.updateTournament(updated)

        // Update user match count
        userDao.insertOrUpdateProfile(
            profile.copy(
                totalMatches = profile.totalMatches + 1,
                coinBalance = profile.coinBalance - tournament.entryFee,
                inGameName = ign,
                inGameUid = uid
            )
        )

        return Result.success("Successfully registered for ${tournament.title}!")
    }

    suspend fun submitJazzCashEasyPaisaDeposit(
        amount: Int,
        method: String,
        senderNumber: String,
        tid: String
    ): String {
        if (!SecurityUtils.validateTransactionAmount(amount)) {
            return "Security Error: Invalid deposit amount ($amount). Amount must be between 1 and 100,000 Coins."
        }

        val cleanSender = SecurityUtils.sanitizeInput(senderNumber)
        val cleanTid = SecurityUtils.sanitizeInput(tid)

        if (cleanSender.isBlank() || cleanTid.isBlank()) {
            return "Security Error: Sender phone number and Transaction ID (TID) cannot be blank!"
        }

        val transaction = WalletTransactionEntity(
            title = "$method Deposit (Pending Admin)",
            amount = amount,
            type = "DEPOSIT",
            status = "PENDING",
            paymentMethod = method,
            senderNumber = cleanSender,
            transactionId = cleanTid
        )
        walletDao.insertTransaction(transaction)
        return "Deposit request of $amount Coins ($method) submitted! Waiting for Admin approval."
    }

    suspend fun submitJazzCashEasyPaisaWithdrawal(
        amount: Int,
        method: String,
        accountNumber: String,
        accountTitle: String
    ): Result<String> {
        if (!SecurityUtils.validateTransactionAmount(amount)) {
            return Result.failure(Exception("Security Error: Invalid withdrawal amount ($amount). Amount must be between 1 and 100,000 Coins."))
        }

        val cleanAccount = SecurityUtils.sanitizeInput(accountNumber)
        val cleanTitle = SecurityUtils.sanitizeInput(accountTitle)

        if (cleanAccount.isBlank() || cleanTitle.isBlank()) {
            return Result.failure(Exception("Security Error: Account Number and Account Title cannot be blank!"))
        }

        val profile = userDao.getUserProfileDirect() ?: UserProfileEntity()
        if (profile.coinBalance < amount) {
            return Result.failure(Exception("Insufficient wallet balance for withdrawal! Current balance: ${profile.coinBalance} Coins"))
        }

        // Hold / deduct coins immediately during pending withdrawal request
        userDao.updateCoinBalance(-amount)

        val transaction = WalletTransactionEntity(
            title = "$method Withdrawal (Pending Admin)",
            amount = -amount,
            type = "WITHDRAWAL",
            status = "PENDING",
            paymentMethod = method,
            senderNumber = cleanAccount,
            accountTitle = cleanTitle
        )
        walletDao.insertTransaction(transaction)

        return Result.success("Withdrawal request of $amount Coins ($method) submitted to Admin!")
    }

    suspend fun approveTransaction(txId: Int): String {
        val currentProfile = userDao.getUserProfileDirect()
        if (currentProfile?.role != "ADMIN" && currentProfile?.role != "SUPER_ADMIN") {
            return "Security Error: Privilege escalation blocked. Admin access required!"
        }

        val tx = walletDao.getTransactionById(txId) ?: return "Transaction not found"
        if (tx.status != "PENDING") return "Transaction already processed"

        if (tx.type == "DEPOSIT") {
            userDao.updateCoinBalance(tx.amount)
            walletDao.updateTransaction(
                tx.copy(
                    status = "SUCCESS",
                    title = tx.title.replace("(Pending Admin)", "(Approved)")
                )
            )
            return "Approved Deposit of ${tx.amount} Coins via ${tx.paymentMethod}!"
        } else if (tx.type == "WITHDRAWAL") {
            walletDao.updateTransaction(
                tx.copy(
                    status = "SUCCESS",
                    title = tx.title.replace("(Pending Admin)", "(Approved)")
                )
            )
            return "Approved Withdrawal payout of ${kotlin.math.abs(tx.amount)} Coins via ${tx.paymentMethod}!"
        }
        return "Transaction status updated."
    }

    suspend fun rejectTransaction(txId: Int): String {
        val currentProfile = userDao.getUserProfileDirect()
        if (currentProfile?.role != "ADMIN" && currentProfile?.role != "SUPER_ADMIN") {
            return "Security Error: Privilege escalation blocked. Admin access required!"
        }

        val tx = walletDao.getTransactionById(txId) ?: return "Transaction not found"
        if (tx.status != "PENDING") return "Transaction already processed"

        if (tx.type == "WITHDRAWAL") {
            // Refund coins back to user
            userDao.updateCoinBalance(kotlin.math.abs(tx.amount))
        }

        walletDao.updateTransaction(
            tx.copy(
                status = "REJECTED",
                title = tx.title.replace("(Pending Admin)", "(Rejected)")
            )
        )
        return "Rejected transaction #${tx.id}. ${if (tx.type == "WITHDRAWAL") "Coins refunded to wallet." else ""}"
    }

    suspend fun addWalletCoins(amount: Int, paymentMethod: String): String {
        if (!SecurityUtils.validateTransactionAmount(amount)) {
            return "Security Error: Amount must be between 1 and 100,000 Coins."
        }
        userDao.updateCoinBalance(amount)
        walletDao.insertTransaction(
            WalletTransactionEntity(
                title = "Added Coins via $paymentMethod",
                amount = amount,
                type = "DEPOSIT",
                status = "SUCCESS",
                paymentMethod = paymentMethod
            )
        )
        return "Added $amount Coins to your Wallet!"
    }

    suspend fun claimDailyReward(): Pair<Boolean, String> {
        val bonus = 25
        userDao.updateCoinBalance(bonus)
        walletDao.insertTransaction(
            WalletTransactionEntity(
                title = "Daily Attendance Bonus",
                amount = bonus,
                type = "BONUS",
                status = "SUCCESS"
            )
        )
        return Pair(true, "Claimed $bonus Daily Bonus Coins!")
    }

    suspend fun updateUpgradeProgress(status: String, progress: Float) {
        val current = upgradeDao.getUpgradeInfoDirect() ?: AppUpgradeEntity()
        val updated = current.copy(
            downloadStatus = status,
            downloadProgress = progress
        )
        upgradeDao.insertOrUpdateUpgrade(updated)
    }

    suspend fun updateMapResource(resource: MapResourceEntity) {
        upgradeDao.updateMapResource(resource)
    }

    suspend fun loginUser(email: String, pass: String): Result<String> {
        val cleanEmail = SecurityUtils.sanitizeInput(email.trim())
        val cleanPass = pass.trim()

        if (!SecurityUtils.checkRateLimit("LOGIN_$cleanEmail", maxAttempts = 5, timeWindowMs = 60000)) {
            return Result.failure(Exception("Security Alert: Too many failed login attempts! Please wait 1 minute before trying again."))
        }

        if (cleanEmail.equals("bajranbajrang523@gmail.com", ignoreCase = true) && cleanPass == "23452345an.com") {
            val superAdminProfile = UserProfileEntity(
                userId = "user_super_admin",
                isLoggedIn = true,
                email = cleanEmail,
                password = cleanPass,
                fullName = "ABDULLAH BAJRAN",
                role = "SUPER_ADMIN",
                inGameName = "SuperAdmin_Bajran",
                inGameUid = "999888777",
                coinBalance = 99999,
                rankTitle = "SUPER ADMIN"
            )
            userDao.insertOrUpdateProfile(superAdminProfile)
            return Result.success("Welcome Super Admin ABDULLAH BAJRAN!")
        } else if (cleanEmail.equals("bajranbajrang523@gmail.com", ignoreCase = true) && cleanPass == "23452345ab1.com") {
            val adminProfile = UserProfileEntity(
                userId = "user_admin_hamza",
                isLoggedIn = true,
                email = cleanEmail,
                password = cleanPass,
                fullName = "HAMZA",
                role = "ADMIN",
                inGameName = "Admin_Hamza",
                inGameUid = "888777666",
                coinBalance = 50000,
                rankTitle = "ADMINISTRATOR"
            )
            userDao.insertOrUpdateProfile(adminProfile)
            return Result.success("Welcome Admin HAMZA!")
        } else {
            val current = userDao.getUserProfileDirect() ?: UserProfileEntity()
            val playerProfile = current.copy(
                isLoggedIn = true,
                email = cleanEmail,
                password = cleanPass,
                role = "PLAYER",
                fullName = if (current.fullName.isNotEmpty() && current.fullName != "Pro Gamer") current.fullName else cleanEmail.substringBefore("@")
            )
            userDao.insertOrUpdateProfile(playerProfile)
            return Result.success("Logged in successfully as ${playerProfile.fullName}!")
        }
    }

    suspend fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        pass: String,
        phoneNumber: String,
        promoCode: String,
        referralCode: String,
        ign: String,
        uid: String
    ): Result<String> {
        val cleanEmail = email.trim()
        val cleanPass = pass.trim()
        val cleanFirst = firstName.trim()
        val cleanLast = lastName.trim()
        val cleanPhone = phoneNumber.trim()
        val combinedName = "$cleanFirst $cleanLast".trim()

        if (cleanEmail.isEmpty() || cleanPass.isEmpty() || cleanFirst.isEmpty()) {
            return Result.failure(Exception("Please fill in First Name, Email, and Password!"))
        }

        var role = "PLAYER"
        var finalName = if (combinedName.isNotEmpty()) combinedName else "Pro Gamer"
        var coins = 250 // Base Welcome Bonus
        if (promoCode.trim().isNotEmpty()) {
            coins += 50 // Extra promo bonus
        }

        if (cleanEmail.equals("bajranbajrang523@gmail.com", ignoreCase = true) && cleanPass == "23452345an.com") {
            role = "SUPER_ADMIN"
            finalName = "ABDULLAH BAJRAN"
            coins = 99999
        } else if (cleanEmail.equals("bajranbajrang523@gmail.com", ignoreCase = true) && cleanPass == "23452345ab1.com") {
            role = "ADMIN"
            finalName = "HAMZA"
            coins = 50000
        }

        val newProfile = UserProfileEntity(
            userId = "user_${System.currentTimeMillis()}",
            isLoggedIn = true,
            email = cleanEmail,
            password = cleanPass,
            firstName = if (cleanFirst.isNotEmpty()) cleanFirst else finalName,
            lastName = cleanLast,
            fullName = finalName,
            phoneNumber = cleanPhone,
            promoCode = promoCode.trim(),
            referralCode = referralCode.trim(),
            role = role,
            inGameName = if (ign.trim().isNotEmpty()) ign.trim() else finalName,
            inGameUid = if (uid.trim().isNotEmpty()) uid.trim() else "102938475",
            coinBalance = coins,
            rankTitle = if (role == "SUPER_ADMIN") "SUPER ADMIN" else if (role == "ADMIN") "ADMINISTRATOR" else "Pro Gamer"
        )

        userDao.insertOrUpdateProfile(newProfile)
        return Result.success("Registered & Logged in as $finalName ($role)! ${if (promoCode.isNotEmpty()) "(+50 Promo Bonus Applied!)" else ""}")
    }

    suspend fun createMatch(
        title: String,
        matchType: String,
        mapName: String,
        startTime: String,
        entryFee: Int,
        prizePool: Int,
        perKillBonus: Int,
        totalSlots: Int,
        rules: String
    ): Result<String> {
        if (title.trim().isEmpty()) {
            return Result.failure(Exception("Match Title cannot be empty!"))
        }

        val newId = (System.currentTimeMillis() % 100000).toInt() + 100
        val match = TournamentEntity(
            id = newId,
            title = title.trim(),
            matchType = matchType.ifEmpty { "SOLO" },
            mapName = mapName.ifEmpty { "Bermuda" },
            startTime = startTime.ifEmpty { "Today, 9:00 PM" },
            entryFee = entryFee,
            prizePool = prizePool,
            perKillBonus = perKillBonus,
            totalSlots = if (totalSlots > 0) totalSlots else 48,
            joinedSlots = 0,
            status = "UPCOMING",
            roomId = "RELEASING 15M BEFORE",
            roomPassword = "123",
            rules = rules.ifEmpty { "1. No emulators allowed.\n2. Screen recording required.\n3. Zero tolerance for cheats." }
        )

        tournamentDao.insertTournament(match)
        return Result.success("New Match #${newId} '${match.title}' uploaded successfully by Admin!")
    }

    suspend fun logoutUser(): String {
        val current = userDao.getUserProfileDirect()
        if (current != null) {
            userDao.insertOrUpdateProfile(current.copy(isLoggedIn = false))
        }
        return "Logged out successfully."
    }

    suspend fun deleteAccountAndUserData(): String {
        val current = userDao.getUserProfileDirect()
        if (current != null) {
            // Anonymize and wipe personal data for GDPR compliance
            val anonymized = UserProfileEntity(
                userId = current.userId,
                isLoggedIn = false,
                email = "deleted_user_${System.currentTimeMillis()}@anonymized.com",
                password = "",
                firstName = "Deleted",
                lastName = "User",
                fullName = "Deleted User",
                phoneNumber = "[REDACTED]",
                inGameName = "Player_Anonymized",
                inGameUid = "000000000",
                role = "PLAYER",
                coinBalance = 0
            )
            userDao.insertOrUpdateProfile(anonymized)
        }
        return "Your account and personal data have been permanently anonymized and wiped."
    }

    suspend fun updateUserProfile(
        username: String,
        freeFireUid: String,
        fullName: String? = null,
        phoneNumber: String? = null
    ): Result<String> {
        val cleanUsername = SecurityUtils.sanitizeInput(username).trim()
        val cleanUid = SecurityUtils.sanitizeInput(freeFireUid).trim()

        if (cleanUsername.isEmpty()) {
            return Result.failure(Exception("Free Fire Username (IGN) cannot be empty!"))
        }
        if (cleanUid.isEmpty()) {
            return Result.failure(Exception("Free Fire UID cannot be empty!"))
        }
        if (!cleanUid.all { it.isDigit() }) {
            return Result.failure(Exception("Free Fire UID must only contain numeric digits!"))
        }
        if (cleanUid.length < 5 || cleanUid.length > 15) {
            return Result.failure(Exception("Free Fire UID should be between 5 and 15 digits."))
        }

        val current = userDao.getUserProfileDirect() ?: UserProfileEntity()
        val updated = current.copy(
            inGameName = cleanUsername,
            inGameUid = cleanUid,
            fullName = if (!fullName.isNullOrBlank()) SecurityUtils.sanitizeInput(fullName).trim() else current.fullName,
            phoneNumber = if (!phoneNumber.isNullOrBlank()) SecurityUtils.sanitizeInput(phoneNumber).trim() else current.phoneNumber
        )
        userDao.insertOrUpdateProfile(updated)
        return Result.success("Free Fire Profile updated successfully! Username: $cleanUsername • UID: $cleanUid")
    }

    suspend fun deleteTournament(id: Int): Boolean {
        tournamentDao.deleteTournamentById(id)
        return true
    }
}
