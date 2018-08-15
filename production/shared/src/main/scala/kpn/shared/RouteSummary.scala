package kpn.shared

import kpn.shared.data.Tagable
import kpn.shared.data.Tags

case class RouteSummary(
  id: Long,
  country: Option[Country],
  networkType: NetworkType,
  name: String,
  meters: Int,
  isBroken: Boolean,
  wayCount: Int,
  timestamp: Timestamp,
  nodeNames: Seq[String],
  tags: Tags
) extends Tagable
