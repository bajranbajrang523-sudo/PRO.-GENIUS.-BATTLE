package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import android.content.Intent
import android.net.Uri
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Menu
import com.example.ui.screens.AddCoinsBottomSheet
import com.example.ui.screens.AdminApprovalDialog
import com.example.ui.screens.AdminChatsDialog
import com.example.ui.screens.AuthBottomSheet
import com.example.ui.screens.CreateMatchDialog
import com.example.ui.screens.JoinMatchBottomSheet
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.MatchesScreen
import com.example.ui.screens.MyMatchesScreen
import com.example.ui.screens.RoomInfoDialog
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RulesDialog
import com.example.ui.screens.SecurityAndPolicyDialog
import com.example.ui.screens.TopNavigationMenuSheet
import com.example.ui.screens.UpgradeDownloadsScreen
import com.example.ui.screens.WebPublishingGuideDialog
import com.example.ui.screens.WalletScreen
import com.example.ui.screens.WithdrawCoinsBottomSheet
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.FireOrange
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ProGeniusTheme
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.TournamentViewModel
import com.example.util.NotificationHelper

class MainActivity : ComponentActivity() {

    private val viewModel: TournamentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ProGeniusTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: TournamentViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val currentFilter by viewModel.currentFilter.collectAsStateWithLifecycle()
    val filteredTournaments by viewModel.filteredTournaments.collectAsStateWithLifecycle()
    val joinedTournaments by viewModel.joinedTournaments.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val transactions by viewModel.walletTransactions.collectAsStateWithLifecycle()
    val upgradeInfo by viewModel.upgradeInfo.collectAsStateWithLifecycle()
    val mapResources by viewModel.mapResources.collectAsStateWithLifecycle()

    val appName by viewModel.appName.collectAsStateWithLifecycle()
    val appSubtitle by viewModel.appSubtitle.collectAsStateWithLifecycle()
    val appLogo by viewModel.appLogo.collectAsStateWithLifecycle()
    val superAdminUid by viewModel.superAdminUid.collectAsStateWithLifecycle()
    val superAdminIgn by viewModel.superAdminIgn.collectAsStateWithLifecycle()
    val adminHamzaUid by viewModel.adminHamzaUid.collectAsStateWithLifecycle()
    val adminHamzaIgn by viewModel.adminHamzaIgn.collectAsStateWithLifecycle()
    val enteredPlayersList by viewModel.enteredPlayersList.collectAsStateWithLifecycle()
    val showTopMenuSheet by viewModel.showTopMenuSheet.collectAsStateWithLifecycle()
    val showAdminChatsDialog by viewModel.showAdminChatsDialog.collectAsStateWithLifecycle()
    val showWebGuideDialog by viewModel.showWebGuideDialog.collectAsStateWithLifecycle()
    val adminChatsList by viewModel.adminChatsList.collectAsStateWithLifecycle()
    val allTournaments by viewModel.allTournaments.collectAsStateWithLifecycle()

    val joinSheetTournament by viewModel.selectedTournamentForJoin.collectAsStateWithLifecycle()
    val roomModalTournament by viewModel.selectedTournamentForRoom.collectAsStateWithLifecycle()
    val showDepositSheet by viewModel.showDepositSheet.collectAsStateWithLifecycle()
    val showWithdrawSheet by viewModel.showWithdrawSheet.collectAsStateWithLifecycle()
    val showAdminDialog by viewModel.showAdminDialog.collectAsStateWithLifecycle()
    val showCreateMatchDialog by viewModel.showCreateMatchDialog.collectAsStateWithLifecycle()
    val showRulesDialog by viewModel.showRulesDialog.collectAsStateWithLifecycle()
    val selectedTournamentForRules by viewModel.selectedTournamentForRules.collectAsStateWithLifecycle()
    val showAuthSheet by viewModel.showAuthSheet.collectAsStateWithLifecycle()
    val showSecurityDialog by viewModel.showSecurityDialog.collectAsStateWithLifecycle()

    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(Unit) {
        NotificationHelper.createNotificationChannel(context)
    }

    LaunchedEffect(userProfile?.isLoggedIn) {
        if (userProfile == null || userProfile?.isLoggedIn == false) {
            viewModel.toggleAuthSheet(true)
        }
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(GoldPrimary)
                                    .padding(2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(appLogo, fontSize = 18.sp)
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Column {
                                Text(
                                    appName,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = GoldPrimary,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp,
                                    maxLines = 1
                                )
                                Text(
                                    appSubtitle,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CyberCyan,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        // Top Balance Quick Badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(DarkSurface)
                                .border(1.dp, GoldPrimary, RoundedCornerShape(20.dp))
                                .clickable { viewModel.selectTab(AppTab.WALLET) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                "${userProfile?.coinBalance ?: 0}",
                                color = GoldPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.selectTab(AppTab.PROFILE) }) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = "User Profile",
                            tint = if (currentTab == AppTab.PROFILE) CyberCyan else GoldPrimary
                        )
                    }
                    IconButton(onClick = { viewModel.toggleAdminChatsDialog(true) }) {
                        Icon(Icons.Default.Chat, contentDescription = "Admin Chats", tint = FireOrange)
                    }
                    IconButton(onClick = { viewModel.toggleTopMenuSheet(true) }) {
                        Icon(Icons.Default.Menu, contentDescription = "Main App Menu", tint = GoldPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = TextPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                contentColor = TextPrimary
            ) {
                NavigationBarItem(
                    selected = currentTab == AppTab.MATCHES,
                    onClick = { viewModel.selectTab(AppTab.MATCHES) },
                    icon = { Icon(Icons.Default.SportsEsports, contentDescription = "Matches") },
                    label = { Text("MATCHES") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GoldPrimary,
                        selectedTextColor = GoldPrimary,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted,
                        indicatorColor = DarkSurface
                    )
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.MY_MATCHES,
                    onClick = { viewModel.selectTab(AppTab.MY_MATCHES) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (joinedTournaments.isNotEmpty()) {
                                    Badge(containerColor = CyberCyan) { Text("${joinedTournaments.size}") }
                                }
                            }
                        ) {
                            Icon(Icons.Default.SportsEsports, contentDescription = "My Matches")
                        }
                    },
                    label = { Text("MY ROOMS") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CyberCyan,
                        selectedTextColor = CyberCyan,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted,
                        indicatorColor = DarkSurface
                    )
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.WALLET,
                    onClick = { viewModel.selectTab(AppTab.WALLET) },
                    icon = { Icon(Icons.Default.Wallet, contentDescription = "Wallet") },
                    label = { Text("WALLET") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GoldPrimary,
                        selectedTextColor = GoldPrimary,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted,
                        indicatorColor = DarkSurface
                    )
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.PROFILE,
                    onClick = { viewModel.selectTab(AppTab.PROFILE) },
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Profile") },
                    label = { Text("PROFILE") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CyberCyan,
                        selectedTextColor = CyberCyan,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted,
                        indicatorColor = DarkSurface
                    )
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.LEADERBOARD,
                    onClick = { viewModel.selectTab(AppTab.LEADERBOARD) },
                    icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Ranks") },
                    label = { Text("RANKS") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = FireOrange,
                        selectedTextColor = FireOrange,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted,
                        indicatorColor = DarkSurface
                    )
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = DarkBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppTab.MATCHES -> MatchesScreen(
                    tournaments = filteredTournaments,
                    selectedFilter = currentFilter,
                    onSelectFilter = { viewModel.setFilter(it) },
                    onJoinClick = { viewModel.openJoinSheet(it) },
                    onViewRoomClick = { viewModel.openRoomModal(it) },
                    onOpenRulesClick = { viewModel.openRulesForTournament(it) },
                    onOpenUpgradeScreen = { viewModel.selectTab(AppTab.UPGRADE_DOWNLOADS) }
                )

                AppTab.MY_MATCHES -> MyMatchesScreen(
                    joinedTournaments = joinedTournaments,
                    onViewRoomClick = { viewModel.openRoomModal(it) },
                    onOpenRulesClick = { viewModel.openRulesForTournament(it) },
                    onExploreClick = { viewModel.selectTab(AppTab.MATCHES) }
                )

                AppTab.WALLET -> WalletScreen(
                    userProfile = userProfile,
                    transactions = transactions,
                    onAddCoinsClick = { viewModel.toggleDepositSheet(true) },
                    onWithdrawCoinsClick = { viewModel.toggleWithdrawSheet(true) },
                    onOpenAdminPanelClick = { viewModel.toggleAdminDialog(true) },
                    onClaimDailyReward = { viewModel.claimDailyReward() }
                )

                AppTab.PROFILE -> ProfileScreen(
                    userProfile = userProfile,
                    joinedTournaments = joinedTournaments,
                    onSaveProfile = { ign, uid, name, phone ->
                        viewModel.saveUserProfile(ign, uid, name, phone)
                    },
                    onViewRoomClick = { viewModel.openRoomModal(it) },
                    onOpenRulesClick = { viewModel.openRulesForTournament(it) },
                    onExploreMatches = { viewModel.selectTab(AppTab.MATCHES) },
                    onOpenWallet = { viewModel.selectTab(AppTab.WALLET) },
                    onOpenSecurity = { viewModel.toggleSecurityDialog(true) },
                    onLogout = { viewModel.logoutUser() }
                )

                AppTab.LEADERBOARD -> LeaderboardScreen()

                AppTab.UPGRADE_DOWNLOADS -> UpgradeDownloadsScreen(
                    upgradeInfo = upgradeInfo,
                    mapResources = mapResources,
                    onStartUpgradeDownload = { viewModel.startAppUpgradeDownload() },
                    onPauseUpgradeDownload = { viewModel.pauseAppUpgradeDownload() },
                    onInstallUpgrade = { viewModel.installAppUpdate() },
                    onDownloadMapResource = { viewModel.downloadMapResource(it) }
                )
            }
        }
    }

    // Modals & BottomSheets
    joinSheetTournament?.let { tournament ->
        JoinMatchBottomSheet(
            tournament = tournament,
            userProfile = userProfile,
            onDismiss = { viewModel.closeJoinSheet() },
            onConfirmJoin = { ign, uid ->
                viewModel.joinTournament(tournament.id, ign, uid)
                NotificationHelper.showJoinConfirmationNotification(
                    context = context,
                    tournamentTitle = tournament.title,
                    matchTime = tournament.startTime
                )
                NotificationHelper.showTournamentStartAlert(
                    context = context,
                    tournamentTitle = tournament.title,
                    matchTime = tournament.startTime
                )
            },
            onSubmitDirectPayment = { amount, method, senderNumber, tid, ign, uid ->
                viewModel.submitJazzCashEasyPaisaDeposit(amount, method, senderNumber, tid)
                viewModel.joinTournament(tournament.id, ign, uid)
                NotificationHelper.showJoinConfirmationNotification(
                    context = context,
                    tournamentTitle = tournament.title,
                    matchTime = tournament.startTime
                )
                NotificationHelper.showTournamentStartAlert(
                    context = context,
                    tournamentTitle = tournament.title,
                    matchTime = tournament.startTime
                )
            }
        )
    }

    roomModalTournament?.let { tournament ->
        RoomInfoDialog(
            tournament = tournament,
            onDismiss = { viewModel.closeRoomModal() }
        )
    }

    if (showDepositSheet) {
        AddCoinsBottomSheet(
            onDismiss = { viewModel.toggleDepositSheet(false) },
            onConfirmAddCoins = { amount, method ->
                viewModel.addCoins(amount, method)
            },
            onSubmitJazzCashEasyPaisaDeposit = { amount, method, senderNumber, tid ->
                viewModel.submitJazzCashEasyPaisaDeposit(amount, method, senderNumber, tid)
            }
        )
    }

    if (showWithdrawSheet) {
        WithdrawCoinsBottomSheet(
            userBalance = userProfile?.coinBalance ?: 0,
            onDismiss = { viewModel.toggleWithdrawSheet(false) },
            onConfirmWithdraw = { amount, method, accountNumber, accountTitle ->
                viewModel.submitJazzCashEasyPaisaWithdrawal(amount, method, accountNumber, accountTitle)
            }
        )
    }

    if (showAdminDialog) {
        AdminApprovalDialog(
            transactions = transactions,
            tournaments = allTournaments,
            enteredPlayers = enteredPlayersList,
            currentAppName = appName,
            currentAppSubtitle = appSubtitle,
            currentAppLogo = appLogo,
            superAdminUid = superAdminUid,
            superAdminIgn = superAdminIgn,
            adminHamzaUid = adminHamzaUid,
            adminHamzaIgn = adminHamzaIgn,
            onDismiss = { viewModel.toggleAdminDialog(false) },
            onApprove = { txId -> viewModel.approveTransaction(txId) },
            onReject = { txId -> viewModel.rejectTransaction(txId) },
            onOpenCreateMatch = { viewModel.toggleCreateMatchDialog(true) },
            onUpdateBranding = { newName, newSub, newLogo -> viewModel.updateAppBranding(newName, newSub, newLogo) },
            onUpdateAdminUids = { sUid, sIgn, hUid, hIgn -> viewModel.updateAdminUids(sUid, sIgn, hUid, hIgn) }
        )
    }

    if (showTopMenuSheet) {
        val context = androidx.compose.ui.platform.LocalContext.current
        TopNavigationMenuSheet(
            userProfile = userProfile,
            appName = appName,
            appLogo = appLogo,
            onDismiss = { viewModel.toggleTopMenuSheet(false) },
            onSelectTab = { viewModel.selectTab(it) },
            onOpenDeposit = { viewModel.toggleDepositSheet(true) },
            onOpenWithdraw = { viewModel.toggleWithdrawSheet(true) },
            onOpenProfile = {
                viewModel.selectTab(AppTab.PROFILE)
                viewModel.toggleTopMenuSheet(false)
            },
            onOpenSecurity = { viewModel.toggleSecurityDialog(true) },
            onOpenRules = { viewModel.toggleRulesDialog(true) },
            onOpenAdminChats = { viewModel.toggleAdminChatsDialog(true) },
            onOpenAdminPanel = { viewModel.toggleAdminDialog(true) },
            onOpenWhatsApp = {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/923207394660"))
                    context.startActivity(intent)
                } catch (e: Exception) {
                    // Fallback
                }
            },
            onOpenWebGuide = { viewModel.toggleWebGuideDialog(true) }
        )
    }

    if (showWebGuideDialog) {
        WebPublishingGuideDialog(
            appName = appName,
            onDismiss = { viewModel.toggleWebGuideDialog(false) }
        )
    }

    if (showAdminChatsDialog) {
        AdminChatsDialog(
            announcements = adminChatsList,
            userProfile = userProfile,
            onDismiss = { viewModel.toggleAdminChatsDialog(false) },
            onPostAnnouncement = { viewModel.postAdminChat(it) }
        )
    }

    if (showCreateMatchDialog) {
        CreateMatchDialog(
            onDismiss = { viewModel.toggleCreateMatchDialog(false) },
            onCreateMatch = { title, matchType, mapName, startTime, entryFee, prizePool, perKillBonus, totalSlots, rules ->
                viewModel.createNewMatch(
                    title, matchType, mapName, startTime, entryFee, prizePool, perKillBonus, totalSlots, rules
                )
            }
        )
    }

    if (showRulesDialog) {
        RulesDialog(
            tournament = selectedTournamentForRules,
            onDismiss = { viewModel.toggleRulesDialog(false) }
        )
    }

    if (showAuthSheet) {
        AuthBottomSheet(
            userProfile = userProfile,
            onDismiss = { viewModel.toggleAuthSheet(false) },
            onLogin = { email, pass -> viewModel.loginUser(email, pass) },
            onRegister = { firstName, lastName, email, pass, phoneNumber, promoCode, referralCode, ign, uid ->
                viewModel.registerUser(firstName, lastName, email, pass, phoneNumber, promoCode, referralCode, ign, uid)
            },
            onLogout = { viewModel.logoutUser() }
        )
    }

    if (showSecurityDialog) {
        SecurityAndPolicyDialog(
            onDismiss = { viewModel.toggleSecurityDialog(false) },
            onDeleteAccount = { viewModel.deleteAccountAndUserData() }
        )
    }
}
