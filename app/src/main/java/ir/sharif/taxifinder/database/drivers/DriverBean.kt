package ir.sharif.taxifinder.database.drivers

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "Driver", primaryKeys = ["driverId"])
class DriverBean(
    @ColumnInfo(name = "driverId")
    var driverId: Int,
    @ColumnInfo(name = "msisdn")
    var msisdn: String? = null,
    @ColumnInfo(name = "plate")
    var plate: String? = null,
    @ColumnInfo(name = "carBrand")
    var carBrand: String? = null,
    @ColumnInfo(name = "firstName")
    var firstName: String? = null,
    @ColumnInfo(name = "lastName")
    var lastName: String? = null,
    @ColumnInfo(name = "imageUrl")
    var imageUrl: String? = null
)
