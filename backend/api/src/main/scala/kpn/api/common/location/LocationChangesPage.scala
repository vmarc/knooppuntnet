package kpn.api.common.location

import kpn.api.common.LocationChangeSetInfo
import kpn.api.common.changes.filter.ChangesFilterOption

case class LocationChangesPage(
  summary: LocationSummary,
  changeSets: Seq[LocationChangeSetInfo],
  changesCount: Long,
  filterOptions: Seq[ChangesFilterOption]
)
