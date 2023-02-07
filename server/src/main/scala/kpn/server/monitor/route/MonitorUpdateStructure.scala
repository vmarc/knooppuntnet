package kpn.server.monitor.route

trait MonitorUpdateStructure {
  def update(context: MonitorUpdateContext): MonitorUpdateContext
}
