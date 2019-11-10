package kpn.core.app

import kpn.api.common.PageInfo
import kpn.api.common.RouteSummary

case class RoutePageInfo(pageInfo: PageInfo, rows: Seq[RouteSummary], userRouteInfos: Seq[UserRouteInfo])
