package ir.sharif.taxifinder.database.drivers


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DriverDao {
    @Query("SELECT * FROM Driver")
    fun getAll(): List<DriverBean>

    @Query("SELECT * FROM Driver WHERE Driver.plate = :plate")
    fun getDriver(plate: String): List<DriverBean>

    @Query("SELECT COUNT(*) from Driver")
    fun countComments(): Int

    @Insert
    fun insertAll(vararg drivers: DriverBean)

    @Query("DELETE FROM Driver WHERE Driver.plate= :plate")
    fun deleteDriver(plate: String)

    @Query("DELETE FROM Driver")
    fun nukeTable()
}
