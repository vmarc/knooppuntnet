package kpn.database.actions.statistics

import kpn.core.doc.Storable

case class ChangeSetCount(year: Long, month: Long, day: Long, impact: Boolean, count: Long) extends Storable
