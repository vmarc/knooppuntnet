package kpn.core.doc

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.LatLon
import kpn.api.common.NodeName
import kpn.api.common.data.Tagable
import kpn.api.custom.Day
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

case class BaseNodeDoc(
  _id: Long,
  active: Boolean,
  name: Option[String],
  names: Seq[NodeName],
  version: Long,
  changeSetId: Long,
  latitude: String,
  longitude: String,
  lastUpdated: Timestamp,
  tags: Seq[Tag],
  lastSurvey: Option[Day],
  facts: Seq[Fact],
  country: Option[Country],
  locations: Seq[String],
  tiles: Seq[String],
) extends Tagable with LatLon with WithId {

  def deactivated: BaseNodeDoc = {
    copy(active = false)
  }
}
