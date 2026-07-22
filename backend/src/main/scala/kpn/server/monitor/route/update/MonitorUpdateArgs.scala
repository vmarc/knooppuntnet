package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.custom.Timestamp
import kpn.core.doc.Storable

case class MonitorUpdateArgs(
  user: String,
  reporter: MonitorUpdateReporter,
  update: MonitorRouteUpdate,
) extends Storable {
  def relationId: Long = {
    update.relationId.getOrElse(throw new RuntimeException("relation id needed for update"))
  }

  def referenceTimestamp: Timestamp = {
    update.referenceTimestamp.getOrElse(throw new RuntimeException("referenceTimestamp is required"))
  }

  def referenceGpx: String = {
    update.referenceGpx.getOrElse(throw new RuntimeException("referenceGpx is required"))
  }
}
