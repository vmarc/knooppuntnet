package kpn.core.doc

import kpn.api.common.Country
import kpn.api.common.LatLon
import kpn.api.common.NodeName
import kpn.api.common.data.raw.Raw
import kpn.api.custom.Day
import kpn.api.custom.Timestamp

case class NodeBaseData(
  raw: Raw,
  name: Option[String],
  names: Seq[NodeName],
  latitude: String,
  longitude: String,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  country: Option[Country],
  locations: Seq[String],
) extends LatLon with Storable
