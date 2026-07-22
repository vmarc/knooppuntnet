package kpn.api.common.monitor

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait MonitorReferenceType extends EnumEntry with Hyphencase

object MonitorReferenceType extends Enum[MonitorReferenceType] {

  val values: IndexedSeq[MonitorReferenceType] = findValues

  case object osm extends MonitorReferenceType

  case object osmNow extends MonitorReferenceType

  case object gpx extends MonitorReferenceType // subrelation references for "multi-gpx" routes also have reference type "gpx"

  case object multiGpx extends MonitorReferenceType
}
