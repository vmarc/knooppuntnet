package kpn.core.facade.pages

import kpn.shared.route.RoutePage

trait RoutePageBuilder {
  def build(user: Option[String], routeId: Long): Option[RoutePage]
}
