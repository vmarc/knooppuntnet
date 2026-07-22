package kpn.api.common

import kpn.api.common.location.Location
import kpn.api.common.location.LocationCandidate

case class RouteLocationAnalysis(
  location: Option[Location],
  candidates: Seq[LocationCandidate],
  locationNames: Seq[String]
)
