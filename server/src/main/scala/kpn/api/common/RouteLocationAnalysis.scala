package kpn.api.common

import kpn.api.common.location.Location
import kpn.api.common.location.LocationCandidate

case class RouteLocationAnalysis(
  location: Option[Location] = None,
  candidates: Seq[LocationCandidate] = Seq.empty,
  locationNames: Seq[String] = Seq.empty
)
