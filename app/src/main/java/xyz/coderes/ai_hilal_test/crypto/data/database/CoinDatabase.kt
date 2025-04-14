package xyz.coderes.ai_hilal_test.crypto.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import xyz.coderes.ai_hilal_test.crypto.data.database.CoinEntity

@Database(
    entities = [CoinEntity::class],
    version = 1
)
abstract class CoinDatabase() : RoomDatabase() {
    abstract val favoriteCoinDao: CoinFavoriteDao

    companion object {
        const val DATABASE_NAME = "coin.db"
    }
}