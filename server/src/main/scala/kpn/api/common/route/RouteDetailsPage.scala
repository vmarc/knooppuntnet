package kpn.api.common.route

import kpn.api.common.common.Reference

case class RouteDetailsPage(
  route: RouteInfo,
  networkReferences: Seq[Reference],
  changeCount: Long
)
