package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.AdminAnnouncement
import com.example.data.model.AppUpgradeEntity
import com.example.data.model.JoinedPlayerDetail
import com.example.data.model.MapResourceEntity
import com.example.data.model.TournamentEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WalletTransactionEntity
import com.example.data.repository.TournamentRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab {
    MATCHES,
    MY_MATCHES,
    WALLET,
    PROFILE,
    LEADERBOARD,
    UPGRADE_DOWNLOADS
}

enum class MatchFilter {
    ALL,
    UPCOMING,
    LIVE,
    SOLO,
    SQUAD
}

class TournamentViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = TournamentRepository(
        tournamentDao = db.tournamentDao(),
        walletDao = db.walletDao(),
        userDao = db.userDao(),
        upgradeDao = db.upgradeDao()
    )

    private val _currentTab = MutableStateFlow(AppTab.MATCHES)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _currentFilter = MutableStateFlow(MatchFilter.ALL)
    val currentFilter: StateFlow<MatchFilter> = _currentFilter.asStateFlow()

    private val _selectedTournamentForJoin = MutableStateFlow<TournamentEntity?>(null)
    val selectedTournamentForJoin: StateFlow<TournamentEntity?> = _selectedTournamentForJoin.asStateFlow()

    private val _selectedTournamentForRoom = MutableStateFlow<TournamentEntity?>(null)
    val selectedTournamentForRoom: StateFlow<TournamentEntity?> = _selectedTournamentForRoom.asStateFlow()

    private val _showDepositSheet = MutableStateFlow(false)
    val showDepositSheet: StateFlow<Boolean> = _showDepositSheet.asStateFlow()

    private val _showRulesDialog = MutableStateFlow(false)
    val showRulesDialog: StateFlow<Boolean> = _showRulesDialog.asStateFlow()

    private val _selectedTournamentForRules = MutableStateFlow<TournamentEntity?>(null)
    val selectedTournamentForRules: StateFlow<TournamentEntity?> = _selectedTournamentForRules.asStateFlow()

    private val _showWithdrawSheet = MutableStateFlow(false)
    val showWithdrawSheet: StateFlow<Boolean> = _showWithdrawSheet.asStateFlow()

    private val _showAdminDialog = MutableStateFlow(false)
    val showAdminDialog: StateFlow<Boolean> = _showAdminDialog.asStateFlow()

    private val _showAuthSheet = MutableStateFlow(false)
    val showAuthSheet: StateFlow<Boolean> = _showAuthSheet.asStateFlow()

    private val _showSecurityDialog = MutableStateFlow(false)
    val showSecurityDialog: StateFlow<Boolean> = _showSecurityDialog.asStateFlow()

    private val _showCreateMatchDialog = MutableStateFlow(false)
    val showCreateMatchDialog: StateFlow<Boolean> = _showCreateMatchDialog.asStateFlow()

    private val _showTopMenuSheet = MutableStateFlow(false)
    val showTopMenuSheet: StateFlow<Boolean> = _showTopMenuSheet.asStateFlow()

    private val _showAdminChatsDialog = MutableStateFlow(false)
    val showAdminChatsDialog: StateFlow<Boolean> = _showAdminChatsDialog.asStateFlow()

    private val _showWebGuideDialog = MutableStateFlow(false)
    val showWebGuideDialog: StateFlow<Boolean> = _showWebGuideDialog.asStateFlow()

    private val _appName = MutableStateFlow("ABDULLAH BAJRAN")
    val appName: StateFlow<String> = _appName.asStateFlow()

    private val _appSubtitle = MutableStateFlow("FREE FIRE ESPORTS HUB • WHATSAPP: 03207394660")
    val appSubtitle: StateFlow<String> = _appSubtitle.asStateFlow()

    private val _appLogo = MutableStateFlow("👑")
    val appLogo: StateFlow<String> = _appLogo.asStateFlow()

    private val _superAdminUid = MutableStateFlow("999888777")
    val superAdminUid: StateFlow<String> = _superAdminUid.asStateFlow()

    private val _superAdminIgn = MutableStateFlow("SuperAdmin_Bajran")
    val superAdminIgn: StateFlow<String> = _superAdminIgn.asStateFlow()

    private val _adminHamzaUid = MutableStateFlow("888777666")
    val adminHamzaUid: StateFlow<String> = _adminHamzaUid.asStateFlow()

    private val _adminHamzaIgn = MutableStateFlow("Admin_Hamza")
    val adminHamzaIgn: StateFlow<String> = _adminHamzaIgn.asStateFlow()

    private val _adminChatsList = MutableStateFlow(
        listOf(
            AdminAnnouncement(
                id = 1,
                author = "ABDULLAH BAJRAN (Super Admin)",
                message = "Welcome to Free Fire Official Tournaments! Deposit via EasyPaisa/JazzCash and contact WhatsApp 03207394660 for instant verification.",
                timestamp = "Today 10:00 AM"
            ),
            AdminAnnouncement(
                id = 2,
                author = "HAMZA (Match Admin)",
                message = "Room IDs & Passwords will be released 15 minutes before match start time. Make sure your Free Fire UID matches your registration!",
                timestamp = "Today 11:30 AM"
            )
        )
    )
    val adminChatsList: StateFlow<List<AdminAnnouncement>> = _adminChatsList.asStateFlow()

    val allTournaments: StateFlow<List<TournamentEntity>> = repository.allTournaments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val walletTransactions: StateFlow<List<WalletTransactionEntity>> = repository.walletTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val joinedTournaments: StateFlow<List<TournamentEntity>> = repository.joinedTournaments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val upgradeInfo: StateFlow<AppUpgradeEntity?> = repository.upgradeInfo
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val mapResources: StateFlow<List<MapResourceEntity>> = repository.mapResources
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredTournaments: StateFlow<List<TournamentEntity>> = combine(
        repository.allTournaments,
        _currentFilter
    ) { tournaments, filter ->
        when (filter) {
            MatchFilter.ALL -> tournaments
            MatchFilter.UPCOMING -> tournaments.filter { it.status == "UPCOMING" }
            MatchFilter.LIVE -> tournaments.filter { it.status == "LIVE" }
            MatchFilter.SOLO -> tournaments.filter { it.matchType == "SOLO" }
            MatchFilter.SQUAD -> tournaments.filter { it.matchType == "SQUAD" || it.matchType == "DUO" }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val enteredPlayersList: StateFlow<List<JoinedPlayerDetail>> = combine(
        repository.allTournaments,
        repository.userProfile
    ) { tournaments, profile ->
        val list = mutableListOf<JoinedPlayerDetail>()

        // Dynamically include user registrations if joined
        tournaments.forEach { t ->
            if (t.isJoined) {
                list.add(
                    JoinedPlayerDetail(
                        playerName = profile?.fullName ?: "Pro Gamer",
                        inGameName = t.joinedInGameName ?: profile?.inGameName ?: "Pro_Sniper_FF",
                        inGameUid = t.joinedUid ?: profile?.inGameUid ?: "8934102941",
                        matchTitle = t.title,
                        slotNumber = 1,
                        joinTime = "Confirmed (Current Player)",
                        tournamentId = t.id,
                        isVerified = true,
                        userEmail = profile?.email ?: "player@progenius.com",
                        phoneNumber = profile?.phoneNumber ?: "03001234567"
                    )
                )
            }
        }

        // Active registered players roster across scheduled tournaments
        val defaultRoster = listOf(
            JoinedPlayerDetail(
                playerName = "Hamza King",
                inGameName = "Hamza_FF_Pro",
                inGameUid = "7721098412",
                matchTitle = "🔥 Grand Bermuda Solo Battle #101",
                slotNumber = 2,
                joinTime = "Today, 7:15 PM",
                tournamentId = 101,
                isVerified = true,
                userEmail = "hamzaking99@gmail.com",
                phoneNumber = "03215551234"
            ),
            JoinedPlayerDetail(
                playerName = "Abdullah Player",
                inGameName = "Abdullah_Gamer",
                inGameUid = "9102837411",
                matchTitle = "🔥 Grand Bermuda Solo Battle #101",
                slotNumber = 3,
                joinTime = "Today, 7:22 PM",
                tournamentId = 101,
                isVerified = true,
                userEmail = "abdullah_play@gmail.com",
                phoneNumber = "03129876543"
            ),
            JoinedPlayerDetail(
                playerName = "Ali Raza",
                inGameName = "Dark_Hunter_007",
                inGameUid = "6621903410",
                matchTitle = "🔥 Grand Bermuda Solo Battle #101",
                slotNumber = 4,
                joinTime = "Today, 7:30 PM",
                tournamentId = 101,
                isVerified = true,
                userEmail = "aliraza.ff@gmail.com",
                phoneNumber = "03456677889"
            ),
            JoinedPlayerDetail(
                playerName = "Usman Tariq",
                inGameName = "Headshot_King_PK",
                inGameUid = "5512903841",
                matchTitle = "🔥 Grand Bermuda Solo Battle #101",
                slotNumber = 5,
                joinTime = "Today, 7:45 PM",
                tournamentId = 101,
                isVerified = true,
                userEmail = "usman.pk99@gmail.com",
                phoneNumber = "03334445566"
            ),
            JoinedPlayerDetail(
                playerName = "Squad Leader Bilal",
                inGameName = "Bilal_Captain",
                inGameUid = "4419082312",
                matchTitle = "⚡ Pro Squad Kalahari Championship",
                slotNumber = 1,
                joinTime = "Today, 6:50 PM",
                tournamentId = 102,
                isVerified = true,
                userEmail = "bilal.squad@gmail.com",
                phoneNumber = "03012233445"
            ),
            JoinedPlayerDetail(
                playerName = "Saad Ahmed",
                inGameName = "Silent_Killer_PK",
                inGameUid = "3312098471",
                matchTitle = "⚡ Pro Squad Kalahari Championship",
                slotNumber = 2,
                joinTime = "Today, 7:05 PM",
                tournamentId = 102,
                isVerified = true,
                userEmail = "saad.ahmed@gmail.com",
                phoneNumber = "03158889900"
            ),
            JoinedPlayerDetail(
                playerName = "Zain Ul Abideen",
                inGameName = "Zain_Thunder",
                inGameUid = "2201984732",
                matchTitle = "⚡ Pro Squad Kalahari Championship",
                slotNumber = 3,
                joinTime = "Today, 7:18 PM",
                tournamentId = 102,
                isVerified = true,
                userEmail = "zain.thunder@gmail.com",
                phoneNumber = "03067778899"
            ),
            JoinedPlayerDetail(
                playerName = "Farhan Ali",
                inGameName = "Farhan_Rusher",
                inGameUid = "1190283746",
                matchTitle = "🎯 Duo Purgatory Survival Rush",
                slotNumber = 1,
                joinTime = "Today, 6:30 PM",
                tournamentId = 103,
                isVerified = true,
                userEmail = "farhan.ali@gmail.com",
                phoneNumber = "03221112233"
            ),
            JoinedPlayerDetail(
                playerName = "Kamran Khan",
                inGameName = "Kamran_AWM_Pro",
                inGameUid = "9901827364",
                matchTitle = "🎯 Duo Purgatory Survival Rush",
                slotNumber = 2,
                joinTime = "Today, 6:40 PM",
                tournamentId = 103,
                isVerified = true,
                userEmail = "kamran.khan@gmail.com",
                phoneNumber = "03445556677"
            ),
            JoinedPlayerDetail(
                playerName = "Shahid Afridi FF",
                inGameName = "BoomBoom_Sniper",
                inGameUid = "8810293847",
                matchTitle = "🏆 Midnight Alpine Solo Deathmatch",
                slotNumber = 1,
                joinTime = "Today, 5:15 PM",
                tournamentId = 104,
                isVerified = true,
                userEmail = "shahid.afridi@gmail.com",
                phoneNumber = "03009988776"
            ),
            JoinedPlayerDetail(
                playerName = "Noman Butt",
                inGameName = "Nomi_Gamer_01",
                inGameUid = "7701928374",
                matchTitle = "🏆 Midnight Alpine Solo Deathmatch",
                slotNumber = 2,
                joinTime = "Today, 5:40 PM",
                tournamentId = 104,
                isVerified = true,
                userEmail = "noman.butt@gmail.com",
                phoneNumber = "03134443322"
            )
        )

        list.addAll(defaultRoster)
        list.distinctBy { "${it.matchTitle}_${it.inGameUid}" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var appDownloadJob: Job? = null
    private var mapDownloadJobs = mutableMapOf<Int, Job>()

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun setFilter(filter: MatchFilter) {
        _currentFilter.value = filter
    }

    fun openJoinSheet(tournament: TournamentEntity) {
        _selectedTournamentForJoin.value = tournament
    }

    fun closeJoinSheet() {
        _selectedTournamentForJoin.value = null
    }

    fun openRoomModal(tournament: TournamentEntity) {
        _selectedTournamentForRoom.value = tournament
    }

    fun closeRoomModal() {
        _selectedTournamentForRoom.value = null
    }

    fun toggleDepositSheet(show: Boolean) {
        _showDepositSheet.value = show
    }

    fun toggleWithdrawSheet(show: Boolean) {
        _showWithdrawSheet.value = show
    }

    fun toggleAdminDialog(show: Boolean) {
        _showAdminDialog.value = show
    }

    fun submitJazzCashEasyPaisaDeposit(
        amount: Int,
        method: String,
        senderNumber: String,
        tid: String
    ) {
        viewModelScope.launch {
            val msg = repository.submitJazzCashEasyPaisaDeposit(amount, method, senderNumber, tid)
            _snackbarMessage.value = msg
            _showDepositSheet.value = false
        }
    }

    fun submitJazzCashEasyPaisaWithdrawal(
        amount: Int,
        method: String,
        accountNumber: String,
        accountTitle: String
    ) {
        viewModelScope.launch {
            val result = repository.submitJazzCashEasyPaisaWithdrawal(amount, method, accountNumber, accountTitle)
            result.onSuccess { msg ->
                _snackbarMessage.value = msg
                _showWithdrawSheet.value = false
            }.onFailure { err ->
                _snackbarMessage.value = err.message ?: "Withdrawal request failed"
            }
        }
    }

    fun approveTransaction(txId: Int) {
        viewModelScope.launch {
            val msg = repository.approveTransaction(txId)
            _snackbarMessage.value = msg
        }
    }

    fun rejectTransaction(txId: Int) {
        viewModelScope.launch {
            val msg = repository.rejectTransaction(txId)
            _snackbarMessage.value = msg
        }
    }

    fun toggleRulesDialog(show: Boolean) {
        _showRulesDialog.value = show
        if (!show) {
            _selectedTournamentForRules.value = null
        }
    }

    fun openRulesForTournament(tournament: TournamentEntity?) {
        _selectedTournamentForRules.value = tournament
        _showRulesDialog.value = true
    }

    fun toggleAuthSheet(show: Boolean) {
        _showAuthSheet.value = show
    }

    fun toggleSecurityDialog(show: Boolean) {
        _showSecurityDialog.value = show
    }

    fun toggleCreateMatchDialog(show: Boolean) {
        _showCreateMatchDialog.value = show
    }

    fun toggleTopMenuSheet(show: Boolean) {
        _showTopMenuSheet.value = show
    }

    fun toggleAdminChatsDialog(show: Boolean) {
        _showAdminChatsDialog.value = show
    }

    fun toggleWebGuideDialog(show: Boolean) {
        _showWebGuideDialog.value = show
    }

    fun updateAppBranding(newName: String, newSubtitle: String, newLogo: String = "👑") {
        if (newName.isNotBlank()) {
            _appName.value = newName.trim()
        }
        if (newSubtitle.isNotBlank()) {
            _appSubtitle.value = newSubtitle.trim()
        }
        if (newLogo.isNotBlank()) {
            _appLogo.value = newLogo.trim()
        }
        _snackbarMessage.value = "App Name, Subtitle & Logo updated to '${_appName.value}'!"
    }

    fun updateAdminUids(
        superAdminUid: String,
        superAdminIgn: String,
        hamzaUid: String,
        hamzaIgn: String
    ) {
        if (superAdminUid.isNotBlank()) _superAdminUid.value = superAdminUid.trim()
        if (superAdminIgn.isNotBlank()) _superAdminIgn.value = superAdminIgn.trim()
        if (hamzaUid.isNotBlank()) _adminHamzaUid.value = hamzaUid.trim()
        if (hamzaIgn.isNotBlank()) _adminHamzaIgn.value = hamzaIgn.trim()

        viewModelScope.launch {
            val current = userProfile.value
            if (current != null) {
                if (current.role == "SUPER_ADMIN") {
                    repository.updateUserProfile(
                        username = _superAdminIgn.value,
                        freeFireUid = _superAdminUid.value,
                        fullName = current.fullName,
                        phoneNumber = current.phoneNumber
                    )
                } else if (current.role == "ADMIN") {
                    repository.updateUserProfile(
                        username = _adminHamzaIgn.value,
                        freeFireUid = _adminHamzaUid.value,
                        fullName = current.fullName,
                        phoneNumber = current.phoneNumber
                    )
                }
            }
        }
        _snackbarMessage.value = "Admin Free Fire UIDs and IGNs updated successfully!"
    }

    fun postAdminChat(message: String) {
        if (message.isBlank()) return
        val current = _adminChatsList.value.toMutableList()
        val newChat = AdminAnnouncement(
            id = current.size + 1,
            author = "ABDULLAH BAJRAN (Super Admin)",
            message = message.trim(),
            timestamp = "Just Now"
        )
        current.add(0, newChat)
        _adminChatsList.value = current
        _snackbarMessage.value = "Admin announcement broadcasted to all players!"
    }

    fun loginUser(email: String, pass: String) {
        viewModelScope.launch {
            val res = repository.loginUser(email, pass)
            res.onSuccess { msg ->
                _snackbarMessage.value = msg
                _showAuthSheet.value = false
            }.onFailure { err ->
                _snackbarMessage.value = err.message ?: "Login failed"
            }
        }
    }

    fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        pass: String,
        phoneNumber: String,
        promoCode: String,
        referralCode: String,
        ign: String,
        uid: String
    ) {
        viewModelScope.launch {
            val res = repository.registerUser(
                firstName, lastName, email, pass, phoneNumber, promoCode, referralCode, ign, uid
            )
            res.onSuccess { msg ->
                _snackbarMessage.value = msg
                _showAuthSheet.value = false
            }.onFailure { err ->
                _snackbarMessage.value = err.message ?: "Registration failed"
            }
        }
    }

    fun createNewMatch(
        title: String,
        matchType: String,
        mapName: String,
        startTime: String,
        entryFee: Int,
        prizePool: Int,
        perKillBonus: Int,
        totalSlots: Int,
        rules: String
    ) {
        viewModelScope.launch {
            val res = repository.createMatch(
                title, matchType, mapName, startTime, entryFee, prizePool, perKillBonus, totalSlots, rules
            )
            res.onSuccess { msg ->
                _snackbarMessage.value = msg
                _showCreateMatchDialog.value = false
            }.onFailure { err ->
                _snackbarMessage.value = err.message ?: "Match upload failed"
            }
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            val msg = repository.logoutUser()
            _snackbarMessage.value = msg
            _showAuthSheet.value = false
        }
    }

    fun deleteAccountAndUserData() {
        viewModelScope.launch {
            val msg = repository.deleteAccountAndUserData()
            _snackbarMessage.value = msg
            _showAuthSheet.value = true
        }
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun joinTournament(tournamentId: Int, ign: String, uid: String) {
        viewModelScope.launch {
            val result = repository.joinTournament(tournamentId, ign, uid)
            result.onSuccess { msg ->
                _snackbarMessage.value = msg
                closeJoinSheet()
            }.onFailure { err ->
                _snackbarMessage.value = err.message ?: "Failed to join tournament"
            }
        }
    }

    fun addCoins(amount: Int, method: String) {
        viewModelScope.launch {
            val msg = repository.addWalletCoins(amount, method)
            _snackbarMessage.value = msg
            _showDepositSheet.value = false
        }
    }

    fun claimDailyReward() {
        viewModelScope.launch {
            val (success, msg) = repository.claimDailyReward()
            _snackbarMessage.value = msg
        }
    }

    // App Version Upgrade Downloader Simulator
    fun startAppUpgradeDownload() {
        if (appDownloadJob?.isActive == true) return
        appDownloadJob = viewModelScope.launch {
            repository.updateUpgradeProgress("DOWNLOADING", 0.05f)
            _snackbarMessage.value = "Downloading Pro Genius Battle v2.4.0 APK..."

            for (i in 1..20) {
                delay(300)
                val progress = (i * 0.05f).coerceAtMost(1.0f)
                val status = if (progress >= 1.0f) "COMPLETED" else "DOWNLOADING"
                repository.updateUpgradeProgress(status, progress)
            }

            _snackbarMessage.value = "Pro Genius Battle v2.4.0 APK downloaded successfully!"
        }
    }

    fun pauseAppUpgradeDownload() {
        appDownloadJob?.cancel()
        viewModelScope.launch {
            repository.updateUpgradeProgress("PAUSED", upgradeInfo.value?.downloadProgress ?: 0.5f)
            _snackbarMessage.value = "APK Update download paused."
        }
    }

    fun installAppUpdate() {
        viewModelScope.launch {
            repository.updateUpgradeProgress("INSTALLED", 1.0f)
            _snackbarMessage.value = "App updated to v2.4.0 successfully!"
        }
    }

    // Custom Map Pack Resource Downloader Simulator
    fun downloadMapResource(resource: MapResourceEntity) {
        if (mapDownloadJobs[resource.id]?.isActive == true) return

        mapDownloadJobs[resource.id] = viewModelScope.launch {
            val updating = resource.copy(downloadStatus = "DOWNLOADING", progress = 0.05f)
            repository.updateMapResource(updating)

            for (i in 1..10) {
                delay(250)
                val p = (i * 0.1f).coerceAtMost(1.0f)
                val status = if (p >= 1.0f) "COMPLETED" else "DOWNLOADING"
                repository.updateMapResource(updating.copy(downloadStatus = status, progress = p))
            }
            _snackbarMessage.value = "${resource.title} downloaded!"
        }
    }

    fun saveUserProfile(
        username: String,
        freeFireUid: String,
        fullName: String? = null,
        phoneNumber: String? = null
    ) {
        viewModelScope.launch {
            val result = repository.updateUserProfile(username, freeFireUid, fullName, phoneNumber)
            result.onSuccess { msg ->
                _snackbarMessage.value = msg
            }.onFailure { err ->
                _snackbarMessage.value = err.message ?: "Failed to update profile"
            }
        }
    }
}
