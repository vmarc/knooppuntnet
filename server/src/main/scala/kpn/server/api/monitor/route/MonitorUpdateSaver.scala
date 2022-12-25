package kpn.server.api.monitor.route

trait MonitorUpdateSaver {
  def save(context: MonitorUpdateContext): MonitorUpdateContext
}
