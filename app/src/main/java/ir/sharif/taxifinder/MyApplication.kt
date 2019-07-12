package ir.sharif.taxifinder

import android.app.Application
import androidx.room.Room
import ir.sharif.taxifinder.database.AppDatabase
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class MyApplication : Application() {

    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iran_sans.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "db"
        ).build()
    }
}