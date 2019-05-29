// TODO migrate to Angular
package kpn.client.components.subset.routes

import kpn.client.filter.TimeFilterKind

case class SubsetOrphanRouteFilterCriteria(
  broken: Option[Boolean] = None,
  lastUpdated: TimeFilterKind.Value = TimeFilterKind.ALL
)
