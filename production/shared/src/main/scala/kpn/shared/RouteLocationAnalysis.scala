package kpn.shared

case class RouteLocationAnalysis(
  location: Location,
  candidates: Seq[LocationCandidate]
)
