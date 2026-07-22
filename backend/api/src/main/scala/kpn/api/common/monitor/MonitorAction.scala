package kpn.api.common.monitor

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait MonitorAction extends EnumEntry with Hyphencase

object MonitorAction extends Enum[MonitorAction] {

  val values: IndexedSeq[MonitorAction] = findValues

  case object add extends MonitorAction

  case object update extends MonitorAction

  case object gpxUpload extends MonitorAction

  case object gpxDelete extends MonitorAction
}
