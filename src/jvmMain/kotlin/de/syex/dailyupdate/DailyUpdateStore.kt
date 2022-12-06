package de.syex.dailyupdate

import androidx.compose.runtime.mutableStateListOf

class DailyUpdateStore {

    val goals = mutableStateListOf<Goal>()

    fun onGoalContentChanged(index: Int, newContent: String) {
        goals[index] = goals[index].copy(content = newContent)
    }

    fun onGoalCompleteChanged(index: Int, completed: Boolean) {
        goals[index] = goals[index].copy(completed = completed)
    }

    fun onGoalAdded() = goals.add(Goal())

    fun createUpdateText(): String {
        val builder = StringBuilder()
        goals.forEach {
            builder.appendLine("🥅 ${it.content}")
        }

        return builder.toString()
    }
}