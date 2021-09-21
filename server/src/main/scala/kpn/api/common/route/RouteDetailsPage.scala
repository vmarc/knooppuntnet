package kpn.api.common.route

import kpn.api.common.common.Reference

case class RouteDetailsPage(
  route: RouteDetailsPageData,
  networkReferences: Seq[Reference],
  changeCount: Long
)
