package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.TournamentDao
import com.example.data.dao.UpgradeDao
import com.example.data.dao.UserDao
import com.example.data.dao.WalletDao
import com.example.data.model.AppUpgradeEntity
import com.example.data.model.MapResourceEntity
import com.example.data.model.TournamentEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WalletTransactionEntity

@Database(
    entities = [
        TournamentEntity::class,
        WalletTransactionEntity::class,
        UserProfileEntity::class,
        AppUpgradeEntity::class,
        MapResourceEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tournamentDao(): TournamentDao
    abstract fun walletDao(): WalletDao
    abstract fun userDao(): UserDao
    abstract fun upgradeDao(): UpgradeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pro_genius_battle_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
