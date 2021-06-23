package kpn.core.mongo

import kpn.api.base.WithId
import kpn.api.common.NodeName
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

case class NodeRouteReference(
  networkType: NetworkType,
  networkScope: NetworkScope,
  routeId: Long,
  routeName: String
)

case class NodeDoc(
  _id: Long,
  labels: Seq[String],
  country: Option[Country],
  name: String,
  names: Seq[NodeName],
  latitude: String,
  longitude: String,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  tags: Tags,
  facts: Seq[Fact],
  factCount: Long,
  tiles: Seq[String],
  locations: Seq[String],
  routeReferences: Seq[NodeRouteReference]
) extends WithId
