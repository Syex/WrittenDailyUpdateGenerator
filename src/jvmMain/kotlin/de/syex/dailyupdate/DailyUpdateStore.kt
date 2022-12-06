package de.syex.dailyupdate

import androidx.compose.runtime.mutableStateListOf

class DailyUpdateStore {

    val goals = mutableStateListOf<Goal>()
    val meetings = mutableStateListOf<Update>()

    fun onGoalContentChanged(index: Int, newContent: String) {
        goals[index] = goals[index].copy(content = newContent)
    }

    fun onGoalCompleteChanged(index: Int, completed: Boolean) {
        goals[index] = goals[index].copy(completed = completed)
    }

    fun onGoalAdded() = goals.add(Goal())

    fun onMeetingContentChanged(index: Int, newContent: String) {
        meetings[index] = meetings[index].copy(content = newContent)
    }

    fun onMeetingAdded() = meetings.add(Goal())

    fun createUpdateText(): String {
        val builder = StringBuilder()
        goals.forEach {
            builder.appendLine("🥅 ${it.content}")
        }

        meetings.forEach {
            builder.appendLine("👥 ${it.content}")
        }

        return builder.toString()
    }
}