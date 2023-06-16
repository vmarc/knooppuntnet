package kpn.server.monitor.route.update

trait MonitorUpdateStructure {
  def update(context: MonitorUpdateContext): MonitorUpdateContext
}
