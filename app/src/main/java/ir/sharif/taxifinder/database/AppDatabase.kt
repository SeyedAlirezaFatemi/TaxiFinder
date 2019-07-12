package ir.sharif.taxifinder.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.sharif.taxifinder.database.drivers.DriverBean
import ir.sharif.taxifinder.database.drivers.DriverDao

@Database(entities = [DriverBean::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun driverDao(): DriverDao
}
