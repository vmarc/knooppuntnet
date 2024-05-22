package kpn.api.common.location

import kpn.api.common.changes.filter.ServerFilterGroup

case class LocationNodeOptions(
  integrityCheck: ServerFilterGroup,
  integrityCheckFailed: ServerFilterGroup,
  fact: ServerFilterGroup,
  survey: ServerFilterGroup,
  lastUpdated: ServerFilterGroup,
  proposed: ServerFilterGroup,
)
