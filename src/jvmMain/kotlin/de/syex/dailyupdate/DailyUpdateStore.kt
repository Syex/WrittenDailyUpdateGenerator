package de.syex.dailyupdate

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DailyUpdateStore(
    private val database: DailyUpdateDatabase
) {

    val createdUpdates = mutableStateListOf<CreatedUpdate>()
    val goals = mutableStateListOf(Goal())
    val meetings = mutableStateListOf<Meeting>()
    var isInHistoryViewMode = mutableStateOf(false)

    init {
        loadCreatedUpdatesHistory()
    }

    private fun loadCreatedUpdatesHistory() {
        createdUpdates.clear()
        val storedDailyUpdates = database.dailyUpdateQueries.selectAll().executeAsList()
        database.createdDailyUpdateQueries.selectAll().executeAsList().reversed().forEach { storedDailyUpdate ->
            val dailyUpdates = storedDailyUpdate.updates.mapNotNull { id ->
                storedDailyUpdates.find { it.id == id }?.let {
                    if (it.isGoal) Goal(it.content, it.completed ?: false) else Meeting(it.content)
                }
            }
            val goals = dailyUpdates.filterIsInstance<Goal>()
            val meetings = dailyUpdates.filterIsInstance<Meeting>()
            val createdAtDate = LocalDateTime.parse(storedDailyUpdate.created_at)
            val customFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
            val formattedDate = createdAtDate.format(customFormat)
            createdUpdates.add(CreatedUpdate(goals, meetings, formattedDate))
        }
    }

    fun onTapOnCreatedUpdate(index: Int) {
        val createdUpdate = createdUpdates[index]
        goals.clear()
        meetings.clear()
        goals.addAll(createdUpdate.goals)
        meetings.addAll(createdUpdate.meetings)
        isInHistoryViewMode.value = true
    }

    fun onGoalContentChanged(index: Int, newContent: String) {
        goals[index] = goals[index].copy(content = newContent)
    }

    fun onGoalCompletedChanged(index: Int, completed: Boolean) {
        goals[index] = goals[index].copy(completed = completed)
    }

    fun onGoalAdded() = goals.add(Goal())

    fun onMeetingContentChanged(index: Int, newContent: String) {
        meetings[index] = meetings[index].copy(content = newContent)
    }

    fun onMeetingAdded() = meetings.add(Meeting())

    fun onCreateUpdateTextTapped(): String {
        val builder = StringBuilder()
        goals.removeIf { it.content.isBlank() }
        meetings.removeIf { it.content.isBlank() }
        goals.forEach {
            builder.append("🥅 ${it.content}")
            if (isInHistoryViewMode.value) {
                if (it.completed) {
                    builder.append(" ✅")
                } else {
                    builder.append(" ❌")
                }
            }
            builder.appendLine()
        }

        meetings.forEach {
            builder.appendLine("👥 ${it.content}")
        }

        if (!isInHistoryViewMode.value) insertUpdateIntoDatabase()

        return builder.toString()
    }

    private fun insertUpdateIntoDatabase() {
        val insertedUpdateIds = mutableStateListOf<Long>()
        goals.forEach {
            val insertedId = with(database.dailyUpdateQueries) {
                transactionWithResult {
                    insert(it.content, true, it.completed)
                    lastInsertedRowId().executeAsOne()
                }
            }
            insertedUpdateIds.add(insertedId)
        }

        meetings.forEach {
            val insertedId = with(database.dailyUpdateQueries) {
                transactionWithResult {
                    database.dailyUpdateQueries.insert(it.content, isGoal = false, completed = false)
                    lastInsertedRowId().executeAsOne()
                }
            }
            insertedUpdateIds.add(insertedId)
        }

        database.createdDailyUpdateQueries.insert(insertedUpdateIds, LocalDateTime.now().toString())
        loadCreatedUpdatesHistory()
    }

    fun onCleared() {
        goals.clear()
        meetings.clear()
        isInHistoryViewMode.value = false
    }
}