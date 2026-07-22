package kpn.api.common

import kpn.api.custom.Timestamp
import kpn.api.id.WithStringId

case class PoiState(
  _id: String,
  imageLink: Option[String] = None,
  imageStatus: Option[String] = None,
  imageStatusDetail: Option[String] = None,
  imageFirstSeen: Option[Timestamp] = None,
  imageLastSeen: Option[Timestamp] = None,
) extends WithStringId
