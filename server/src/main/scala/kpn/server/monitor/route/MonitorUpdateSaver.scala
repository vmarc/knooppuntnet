package kpn.server.monitor.route

trait MonitorUpdateSaver {
  def save(context: MonitorUpdateContext): MonitorUpdateContext
}
