package kpn.api.common.location

case class LocationFactsPage(
  summary: LocationSummary,
  locationFacts: Seq[LocationFact]
)
