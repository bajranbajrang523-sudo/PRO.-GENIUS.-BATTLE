package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AppUpgradeEntity
import com.example.data.model.MapResourceEntity
import com.example.data.model.TournamentEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WalletTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TournamentDao {
    @Query("SELECT * FROM tournaments ORDER BY id ASC")
    fun getAllTournaments(): Flow<List<TournamentEntity>>

    @Query("SELECT * FROM tournaments WHERE isJoined = 1 ORDER BY id DESC")
    fun getJoinedTournaments(): Flow<List<TournamentEntity>>

    @Query("SELECT * FROM tournaments WHERE id = :id")
    suspend fun getTournamentById(id: Int): TournamentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTournament(tournament: TournamentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTournaments(tournaments: List<TournamentEntity>)

    @Update
    suspend fun updateTournament(tournament: TournamentEntity)

    @Query("SELECT COUNT(*) FROM tournaments")
    suspend fun getTournamentCount(): Int

    @Query("DELETE FROM tournaments WHERE id = :id")
    suspend fun deleteTournamentById(id: Int)

    @androidx.room.Delete
    suspend fun deleteTournament(tournament: TournamentEntity)
}

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallet_transactions ORDER BY timestamp DESC")
    fun getTransactions(): Flow<List<WalletTransactionEntity>>

    @Query("SELECT * FROM wallet_transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): WalletTransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: WalletTransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: WalletTransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<WalletTransactionEntity>)
}

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE userId = 'user_pgb_01'")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE userId = 'user_pgb_01'")
    suspend fun getUserProfileDirect(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET coinBalance = coinBalance + :deltaCoins WHERE userId = 'user_pgb_01'")
    suspend fun updateCoinBalance(deltaCoins: Int)
}

@Dao
interface UpgradeDao {
    @Query("SELECT * FROM app_upgrades WHERE id = 1")
    fun getUpgradeInfo(): Flow<AppUpgradeEntity?>

    @Query("SELECT * FROM app_upgrades WHERE id = 1")
    suspend fun getUpgradeInfoDirect(): AppUpgradeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUpgrade(upgrade: AppUpgradeEntity)

    @Query("SELECT * FROM map_resources")
    fun getMapResources(): Flow<List<MapResourceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMapResources(resources: List<MapResourceEntity>)

    @Update
    suspend fun updateMapResource(resource: MapResourceEntity)
}
