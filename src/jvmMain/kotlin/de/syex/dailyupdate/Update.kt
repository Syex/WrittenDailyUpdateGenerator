package de.syex.dailyupdate

typealias Goal = Update
typealias Meeting = Update

data class Update(
    var content: String = "",
    var completed: Boolean? = null
)
