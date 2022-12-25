package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteProperties

trait MonitorUpdateRoute {
  def update(context: MonitorUpdateContext, routeId: ObjectId, user: String, properties: MonitorRouteProperties): MonitorUpdateContext
}
