package kpn.api.common.location

import kpn.api.common.LocationInfo

case class LocationCandidateInfo(locationInfos: Seq[LocationInfo], percentage: Long)
