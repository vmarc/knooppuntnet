package kpn.shared

import kpn.shared.common.ToStringBuilder
import kpn.shared.data.Tagable
import kpn.shared.data.Tags

case class NodeInfo(
  id: Long,
  active: Boolean,
  orphan: Boolean,
  country: Option[Country],
  name: String,
  names: Seq[NodeName],
  latitude: String,
  longitude: String,
  lastUpdated: Timestamp,
  tags: Tags,
  facts: Seq[Fact],
  location: Option[Location]
) extends Tagable with LatLon {

  def name(networkType: String): String = {
    tags(networkType + "_ref").getOrElse("no-name")
  }

  def name(networkType: NetworkType): String = {
    tags(networkType.nodeTagKey).getOrElse("no-name")
  }

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("id", id).
    field("active", active).
    field("orphan", orphan).
    field("country", country).
    field("name", name).
    field("names", names).
    field("latitude", latitude).
    field("longitude", longitude).
    field("lastUpdated", lastUpdated).
    field("tags", tags).
    field("facts", facts).
    build
}
