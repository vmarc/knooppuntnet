package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteUpdate

case class MonitorUpdateArgs(
  user: String,
  reporter: MonitorUpdateReporter,
  update: MonitorRouteUpdate,
) {
  def relationId: Long = {
    update.relationId.getOrElse(throw new RuntimeException("relation id needed for update"))
  }
}
