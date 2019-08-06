// TODO migrate to Angular
package kpn.client.components.network.routes

import kpn.client.filter.TimeFilterKind

case class NetworkRouteFilterCriteria(
  page: Int = 1,
  investigate: Option[Boolean] = None,
  accessible: Option[Boolean] = None,
  roleConnection: Option[Boolean] = None,
  lastUpdated: TimeFilterKind.Value = TimeFilterKind.ALL
)
