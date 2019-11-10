package kpn.api.common

case class RouteLocationAnalysis(
  location: Location,
  candidates: Seq[LocationCandidate] = Seq.empty
)
