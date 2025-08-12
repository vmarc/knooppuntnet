package kpn.core.doc

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.RouteType
import kpn.api.custom.Day
import kpn.api.custom.Timestamp

case class OrphanNodeDoc(
  _id: String,
  country: Country,
  routeType: RouteType,
  nodeId: Long,
  name: String,
  longName: Option[String],
  proposed: Boolean,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  facts: Seq[Fact]
) extends WithStringId
