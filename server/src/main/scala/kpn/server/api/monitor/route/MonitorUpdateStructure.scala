package kpn.server.api.monitor.route

trait MonitorUpdateStructure {
  def update(context: MonitorUpdateContext): MonitorUpdateContext
}
