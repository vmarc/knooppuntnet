package kpn.core.database.doc

import kpn.api.common.route.RouteElements

case class RouteElementsDoc(_id: String, routeElements: RouteElements, _rev: Option[String] = None) extends Doc
