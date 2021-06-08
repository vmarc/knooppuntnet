package kpn.server.repository

import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.route.RouteInfo

trait MongoRouteRepository {

  def routeWithId(routeId: Long): Option[RouteInfo]

  def routeChangeCount(routeId: Long): Long

  def routeChanges(routeId: Long, parameters: ChangesParameters): Seq[RouteChange]

  def routeChangesFilter(routeId: Long, yearOption: Option[String], monthOption: Option[String], dayOption: Option[String]): ChangesFilter

}
