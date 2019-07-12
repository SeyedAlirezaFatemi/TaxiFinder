package ir.sharif.taxifinder.database.drivers


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DriverDao {
    @Query("SELECT * FROM Driver")
    fun getAll(): List<DriverBean>

    @Query("SELECT * FROM Driver WHERE Driver.driverId = :driverId")
    fun getDriver(driverId: Int): List<DriverBean>

    @Query("SELECT COUNT(*) from Driver")
    fun countComments(): Int

    @Insert
    fun insertAll(vararg comments: DriverBean)

    @Query("DELETE FROM Driver WHERE Driver.driverId= :driverId")
    fun deleteDriver(driverId: Int)

    @Query("DELETE FROM Driver")
    fun nukeTable()
}
