package kpn.database.actions.statistics

import kpn.api.id.Storable

case class ChangeSetCount2(year: Long, month: Long, day: Long, impact: Long, total: Long) extends Storable
