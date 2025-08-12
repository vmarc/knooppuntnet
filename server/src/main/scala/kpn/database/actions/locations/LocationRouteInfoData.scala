package kpn.database.actions.locations

import kpn.api.common.data.Tagable
import kpn.api.custom.Day
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp
import kpn.core.doc.Storable

// TODO scala3 move back into using class
case class LocationRouteInfoData(
  id: Long,
  name: String,
  meters: Long,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  tags: Seq[Tag],
  broken: Boolean,
  inaccessible: Boolean
) extends Tagable with Storable
