package de.syex.dailyupdate.storage

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import de.syex.dailyupdate.DailyUpdateDatabase

actual class DriverFactory(private val defaultDirectoryPath: String) {

    actual fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver("jdbc:sqlite:$defaultDirectoryPath/DailyUpdates.db")
        DailyUpdateDatabase.Schema.create(driver)
        return driver
    }
}