package de.syex.dailyupdate

import androidx.compose.runtime.mutableStateListOf

class DailyUpdateStore(
    private val database: DailyUpdateDatabase
) {

    val goals = mutableStateListOf(Goal())
    val meetings = mutableStateListOf<Meeting>()

    fun onGoalContentChanged(index: Int, newContent: String) {
        goals[index] = goals[index].copy(content = newContent)
    }

    fun onGoalAdded() = goals.add(Goal())

    fun onMeetingContentChanged(index: Int, newContent: String) {
        meetings[index] = meetings[index].copy(content = newContent)
    }

    fun onMeetingAdded() = meetings.add(Goal())

    fun createUpdateText(): String {
        val builder = StringBuilder()
        goals.filter { it.content.isNotBlank() }.forEach {
            builder.appendLine("🥅 ${it.content}")
        }

        meetings.filter { it.content.isNotBlank() }.forEach {
            builder.appendLine("👥 ${it.content}")
        }

        return builder.toString()
    }

    fun onCleared() {
        goals.clear()
        meetings.clear()
    }
}