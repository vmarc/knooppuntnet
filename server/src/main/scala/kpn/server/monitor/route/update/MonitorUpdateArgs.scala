package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.custom.Timestamp

case class MonitorUpdateArgs(
  user: String,
  reporter: MonitorUpdateReporter,
  update: MonitorRouteUpdate,
) {
  def relationId: Long = {
    update.relationId.getOrElse(throw new RuntimeException("relation id needed for update"))
  }

  def referenceTimestamp: Timestamp = {
    update.referenceTimestamp.getOrElse(throw new RuntimeException("referenceTimestamp is required"))
  }
}
