package kpn.database.actions.statistics

case class ChangeSetCount(year: Long, month: Long, day: Long, impact: Boolean, count: Long)
