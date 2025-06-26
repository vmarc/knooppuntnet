package kpn.api.common.monitor

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait MonitorAction extends EnumEntry with Hyphencase

object MonitorAction extends Enum[MonitorAction] {

  val values: IndexedSeq[MonitorAction] = findValues

  final case object add extends MonitorAction

  final case object update extends MonitorAction

  final case object gpxUpload extends MonitorAction

  final case object gpxDelete extends MonitorAction
}
