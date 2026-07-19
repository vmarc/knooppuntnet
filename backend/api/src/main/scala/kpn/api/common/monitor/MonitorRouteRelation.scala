package kpn.api.common.monitor

import kpn.api.custom.Day
import kpn.api.custom.Timestamp

case class MonitorRouteRelation(
  relationId: Long,
  name: String,
  role: Option[String],
  survey: Option[Day],
  symbol: Option[String],

  /*
    Reference details are only filled in when the route reference type is "multi-gpx".
    Values are None when this MonitorRouteRelation represents the main super route relation
    and that relation has no ways itself, or any subrelation without ways.
   */
  referenceTimestamp: Option[Timestamp],
  referenceFilename: Option[String],
  referenceDistance: Long,

  deviationDistance: Long,
  deviationCount: Long,
  happy: Boolean,
  relations: Seq[MonitorRouteRelation]
)
