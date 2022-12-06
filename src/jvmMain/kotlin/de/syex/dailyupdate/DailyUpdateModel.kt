package de.syex.dailyupdate

typealias Goal = Update
typealias Meeting = Update
typealias AdditionalWork = Update

data class DailyUpdateModel(
    val goals: List<Goal> = emptyList(),
    val meetings: List<Meeting> = emptyList(),
    val additionalWork: List<AdditionalWork> = emptyList()
)

data class Update(
    var content: String = "",
    var completed: Boolean? = null
)
