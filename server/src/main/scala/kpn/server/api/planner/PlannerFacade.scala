package kpn.server.api.planner

import kpn.api.common.PoiPage
import kpn.api.common.node.MapNodeDetail
import kpn.api.common.planner.LegBuildParams
import kpn.api.common.planner.PlanLegDetail
import kpn.api.common.route.MapRouteDetail
import kpn.api.common.tiles.ClientPoiConfiguration
import kpn.api.custom.ApiResponse
import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.poi.PoiRef

import javax.servlet.http.HttpServletRequest

trait PlannerFacade {

  def mapNodeDetail(request: HttpServletRequest, user: Option[String], networkType: NetworkType, nodeId: Long): ApiResponse[MapNodeDetail]

  def mapRouteDetail(request: HttpServletRequest, user: Option[String], routeId: Long): ApiResponse[MapRouteDetail]

  def poiConfiguration(request: HttpServletRequest, user: Option[String]): ApiResponse[ClientPoiConfiguration]

  def poi(request: HttpServletRequest, user: Option[String], poiRef: PoiRef): ApiResponse[PoiPage]

  def leg(request: HttpServletRequest, user: Option[String], params: LegBuildParams): ApiResponse[PlanLegDetail]

  def plan(request: HttpServletRequest, user: Option[String], networkType: NetworkType, planString: String, proposed: Boolean): ApiResponse[Seq[PlanLegDetail]]

}
