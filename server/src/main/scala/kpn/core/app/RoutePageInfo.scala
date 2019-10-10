package kpn.core.app

import kpn.shared.RouteSummary
import kpn.shared.PageInfo

case class RoutePageInfo(pageInfo: PageInfo, rows: Seq[RouteSummary], userRouteInfos: Seq[UserRouteInfo])
