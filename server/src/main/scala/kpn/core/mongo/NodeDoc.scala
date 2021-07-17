package kpn.core.mongo

import kpn.api.base.WithId
import kpn.api.common.NodeName
import kpn.api.common.common.Reference
import kpn.api.common.node.NodeIntegrity
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

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
  tiles: Seq[String],
  locations: Seq[String],
  integrity: Option[NodeIntegrity],
  routeReferences: Seq[Reference]
) extends WithId {
}
