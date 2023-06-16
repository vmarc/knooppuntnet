package kpn.server.monitor.route.update

trait MonitorUpdateSaver {
  def save(context: MonitorUpdateContext, gpxDeleted: Boolean = false): MonitorUpdateContext
}
