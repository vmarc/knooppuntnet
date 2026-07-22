package kpn.core.doc

import kpn.api.common.Country
import kpn.api.common.LatLon
import kpn.api.common.NodeName
import kpn.api.common.data.raw.Raw
import kpn.api.custom.Day
import kpn.api.id.Storable

case class NodeBaseData(
  raw: Raw,
  name: Option[String],
  names: Seq[NodeName],
  lastSurvey: Option[Day],
  latitude: String,
  longitude: String,
  country: Option[Country],
  locations: Seq[String]
) extends LatLon with Storable
