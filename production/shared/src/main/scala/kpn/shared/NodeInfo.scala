package kpn.shared

import kpn.shared.common.ToStringBuilder
import kpn.shared.data.Tagable
import kpn.shared.data.Tags

case class NodeInfo(
  id: Long,
  active: Boolean,
  display: Boolean,
  ignored: Boolean,
  orphan: Boolean,
  country: Option[Country],
  name: String,
  rcnName: String,
  rwnName: String,
  rhnName: String,
  rmnName: String,
  rpnName: String,
  rinName: String,
  latitude: String,
  longitude: String,
  lastUpdated: Timestamp,
  tags: Tags,
  facts: Seq[Fact]
) extends Tagable with LatLon {

  def name(networkType: String): String = {
    tags(networkType + "_ref").getOrElse("no-name")
  }

  def name(networkType: NetworkType): String = {
    tags(networkType.nodeTagKey).getOrElse("no-name")
  }

  def allNames: String = Seq(rcnName, rwnName, rhnName, rmnName, rpnName, rinName).filter(_.nonEmpty).mkString(" / ")

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("id", id).
    field("active", active).
    field("display", display).
    field("ignored", ignored).
    field("orphan", orphan).
    field("country", country).
    field("name", name).
    field("rcnName", rcnName).
    field("rwnName", rwnName).
    field("rhnName", rhnName).
    field("rmnName", rmnName).
    field("rpnName", rpnName).
    field("rinName", rinName).
    field("latitude", latitude).
    field("longitude", longitude).
    field("lastUpdated", lastUpdated).
    field("tags", tags).
    field("facts", facts).
    build
}
