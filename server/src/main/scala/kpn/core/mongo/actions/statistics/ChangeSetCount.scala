package kpn.core.mongo.actions.statistics

case class ChangeSetCount(year: Long, month: Long, day: Long, impact: Boolean, count: Long)
