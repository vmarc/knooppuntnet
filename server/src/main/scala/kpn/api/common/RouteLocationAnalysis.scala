package kpn.api.common

import kpn.api.common.location.Location
import kpn.api.common.location.LocationCandidate

case class RouteLocationAnalysis(
  location: Location,
  candidates: Seq[LocationCandidate] = Seq.empty
)
