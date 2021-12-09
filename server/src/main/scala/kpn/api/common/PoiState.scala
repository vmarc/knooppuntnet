package kpn.api.common

import kpn.api.base.WithStringId
import kpn.api.custom.Timestamp

case class PoiState(
  _id: String,
  imageLink: Option[String] = None,
  imageStatus: Option[String] = None,
  imageStatusDetail: Option[String] = None,
  imageFirstSeen: Option[Timestamp] = None,
  imageLastSeen: Option[Timestamp] = None,
) extends WithStringId
