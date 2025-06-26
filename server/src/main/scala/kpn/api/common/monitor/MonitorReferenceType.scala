package kpn.api.common.monitor

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait MonitorReferenceType extends EnumEntry with Hyphencase

object MonitorReferenceType extends Enum[MonitorReferenceType] {

  val values: IndexedSeq[MonitorReferenceType] = findValues

  final case object osm extends MonitorReferenceType

  final case object gpx extends MonitorReferenceType

  final case object multiGpx extends MonitorReferenceType
}
