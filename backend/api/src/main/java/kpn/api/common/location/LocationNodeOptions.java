package kpn.api.common.location;

import kpn.api.common.changes.filter.ServerFilterGroup;

public record LocationNodeOptions(
  ServerFilterGroup integrityCheck,
  ServerFilterGroup integrityCheckFailed,
  ServerFilterGroup fact,
  ServerFilterGroup survey,
  ServerFilterGroup lastUpdated,
  ServerFilterGroup proposed,
  ServerFilterGroup referencedInRoutes,
  Long totalNodeCount
) {
}

/*
package kpn.api.common.location

import kpn.api.common.changes.filter.ServerFilterGroup

case class LocationNodeOptions(
  integrityCheck: ServerFilterGroup,
  integrityCheckFailed: ServerFilterGroup,
  fact: ServerFilterGroup,
  survey: ServerFilterGroup,
  lastUpdated: ServerFilterGroup,
  proposed: ServerFilterGroup,
  referencedInRoutes: ServerFilterGroup,
  totalNodeCount: Long
)

*/
