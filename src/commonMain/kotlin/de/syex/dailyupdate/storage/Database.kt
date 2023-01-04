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
        updatesAdapter = object : ColumnAdapter<List<Long>, String> {
            override fun decode(databaseValue: String): List<Long> {
                return databaseValue.split(",").map { it.toLong() }
            }

            override fun encode(value: List<Long>): String {
                return value.joinToString(separator = ",") { it.toString() }
            }

        }
    )

    return DailyUpdateDatabase(driver, dailyUpdatesAdapter)
}