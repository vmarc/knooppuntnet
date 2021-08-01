package kpn.core.mongo.doc

import kpn.api.base.WithId
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Timestamp

case class OrphanRouteDoc(
  _id: Long,
  country: Country,
  networkType: NetworkType,
  name: String,
  meters: Long,
  facts: Seq[Fact],
  lastSurvey: String,
  lastUpdated: Timestamp
) extends WithId
