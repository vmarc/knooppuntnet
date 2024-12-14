package kpn.core.doc

import kpn.api.base.WithId
import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.NetworkType
import kpn.api.custom.Day
import kpn.api.custom.Timestamp

case class OrphanRouteDoc(
  _id: Long,
  country: Country,
  networkTypes: Seq[NetworkType],
  name: String,
  meters: Long,
  facts: Seq[Fact],
  lastSurvey: Option[Day],
  lastUpdated: Timestamp
) extends WithId
