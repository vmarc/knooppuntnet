package kpn.api.common.monitor

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait MonitorCommandAction extends EnumEntry with Hyphencase

object MonitorCommandAction extends Enum[MonitorCommandAction] {

  val values: IndexedSeq[MonitorCommandAction] = findValues

  case object stepAdd extends MonitorCommandAction

  case object stepActive extends MonitorCommandAction

  case object stepDone extends MonitorCommandAction
}

