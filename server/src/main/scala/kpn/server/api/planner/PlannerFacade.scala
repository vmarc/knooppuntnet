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

trait PlannerFacade {

  def mapNodeDetail(user: Option[String], networkType: NetworkType, nodeId: Long): ApiResponse[MapNodeDetail]

  def mapRouteDetail(user: Option[String], routeId: Long): ApiResponse[MapRouteDetail]

  def poiConfiguration(user: Option[String]): ApiResponse[ClientPoiConfiguration]

  def poi(user: Option[String], poiRef: PoiRef): ApiResponse[PoiPage]

  def leg(user: Option[String], params: LegBuildParams): ApiResponse[PlanLegDetail]

  def plan(user: Option[String], networkType: NetworkType, planString: String, proposed: Boolean): ApiResponse[PlanLegDetail]
}
