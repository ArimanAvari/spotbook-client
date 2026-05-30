package com.spotbook.personalguide.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        PlaceEntity::class,
        GroupEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
    abstract fun groupDao(): GroupDao

    companion object {
        const val DATABASE_NAME = "spotbook_local.db"

        fun create(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .addMigrations(MIGRATION_1_2)
                .build()
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE local_place_cards ADD COLUMN server_photo_path TEXT")
                db.execSQL(
                    """
                    UPDATE local_place_cards
                    SET server_photo_path = photo_path, photo_path = NULL
                    WHERE photo_path LIKE 'uploads/%'
                       OR photo_path LIKE 'http://%'
                       OR photo_path LIKE 'https://%'
                    """.trimIndent()
                )
            }
        }
    }
}
