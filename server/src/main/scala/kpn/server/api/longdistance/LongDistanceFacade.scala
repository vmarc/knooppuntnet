package kpn.server.api.longdistance

import kpn.api.common.longdistance.LongDistanceRoute
import kpn.api.custom.ApiResponse

trait LongDistanceFacade {

  def route(routeId: Long): ApiResponse[LongDistanceRoute]

  def routes(): ApiResponse[Seq[LongDistanceRoute]]

}
