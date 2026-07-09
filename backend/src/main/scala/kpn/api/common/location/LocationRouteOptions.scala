package kpn.api.common.location

import kpn.api.common.changes.filter.ServerFilterGroup

case class LocationRouteOptions(
  fact: ServerFilterGroup,
  survey: ServerFilterGroup,
  lastUpdated: ServerFilterGroup,
  proposed: ServerFilterGroup,
)
