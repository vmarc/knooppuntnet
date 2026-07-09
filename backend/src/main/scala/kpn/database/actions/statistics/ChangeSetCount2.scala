package kpn.database.actions.statistics

import kpn.core.doc.Storable

case class ChangeSetCount2(year: Long, month: Long, day: Long, impact: Long, total: Long) extends Storable
