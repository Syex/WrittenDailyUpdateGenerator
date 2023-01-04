package de.syex.dailyupdate

interface Update {

    var content: String
}

data class Goal(
    override var content: String = "",
    var completed: Boolean = false
) : Update

data class Meeting(
    override var content: String = "",
) : Update

data class CreatedUpdate(
    val goals: List<Goal>,
    val meetings: List<Meeting>,
    val createdAt: String
)