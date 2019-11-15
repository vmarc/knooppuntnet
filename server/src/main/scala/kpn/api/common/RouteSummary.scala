package kpn.api.common

import kpn.api.common.data.Tagable
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

case class RouteSummary(
  id: Long,
  country: Option[Country],
  networkType: NetworkType,
  name: String,
  meters: Long,
  isBroken: Boolean,
  wayCount: Long,
  timestamp: Timestamp,
  nodeNames: Seq[String],
  tags: Tags
) extends Tagable
