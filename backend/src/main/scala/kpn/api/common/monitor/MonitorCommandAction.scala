package kpn.api.common.monitor

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait MonitorCommandAction extends EnumEntry with Hyphencase

object MonitorCommandAction extends Enum[MonitorCommandAction] {

  val values: IndexedSeq[MonitorCommandAction] = findValues

  final case object stepAdd extends MonitorCommandAction

  final case object stepActive extends MonitorCommandAction

  final case object stepDone extends MonitorCommandAction
}

