package de.syex.dailyupdate.storage

import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import de.syex.dailyupdate.CreatedDailyUpdateTable
import de.syex.dailyupdate.DailyUpdateDatabase

expect class DriverFactory {

    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): DailyUpdateDatabase {
    val driver = driverFactory.createDriver()
    val dailyUpdatesAdapter = CreatedDailyUpdateTable.Adapter(
        updatesAdapter = object : ColumnAdapter<List<String>, String> {
            override fun decode(databaseValue: String): List<String> {
                return databaseValue.split(",")
            }

            override fun encode(value: List<String>): String {
                return value.joinToString(separator = ",") { it }
            }

        }
    )

    return DailyUpdateDatabase(driver, dailyUpdatesAdapter)
}