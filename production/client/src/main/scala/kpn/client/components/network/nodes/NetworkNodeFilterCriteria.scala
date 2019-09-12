// Migrated to Angular: network-node-filter-criteria.ts
package kpn.client.components.network.nodes

import kpn.client.filter.TimeFilterKind

case class NetworkNodeFilterCriteria(
  tagged: Option[Boolean] = None,
  definedInNetworkRelation: Option[Boolean] = None,
  definedInRouteRelation: Option[Boolean] = None,
  referencedInRoute: Option[Boolean] = None,
  connection: Option[Boolean] = None,
  roleConnection: Option[Boolean] = None,
  integrityCheck: Option[Boolean] = None,
  integrityCheckFailed: Option[Boolean] = None,
  lastUpdated: TimeFilterKind.Value = TimeFilterKind.ALL
)
