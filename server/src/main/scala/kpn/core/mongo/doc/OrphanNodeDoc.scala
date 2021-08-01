package kpn.core.mongo.doc

import kpn.api.base.WithStringId
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Timestamp

case class OrphanNodeDoc(
  _id: String,
  country: Country,
  networkType: NetworkType,
  nodeId: Long,
  name: String,
  longName: Option[String],
  proposed: Boolean,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  facts: Seq[Fact]
) extends WithStringId
