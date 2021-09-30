package kpn.core.tools.route

import kpn.core.doc.RouteDoc

case class RouteDocPair(oldRoute: RouteDoc, newRoute: RouteDoc) {
  def isIdentical: Boolean = oldRoute == newRoute
}
