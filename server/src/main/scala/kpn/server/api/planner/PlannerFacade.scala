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

  def mapNodeDetail(networkType: NetworkType, nodeId: Long): ApiResponse[MapNodeDetail]

  def mapRouteDetail(routeId: Long): ApiResponse[MapRouteDetail]

  def poiConfiguration(): ApiResponse[ClientPoiConfiguration]

  def poi(poiRef: PoiRef): ApiResponse[PoiPage]

  def leg(params: LegBuildParams): ApiResponse[PlanLegDetail]

  def plan(networkType: NetworkType, planString: String, proposed: Boolean): ApiResponse[Seq[PlanLegDetail]]

}
