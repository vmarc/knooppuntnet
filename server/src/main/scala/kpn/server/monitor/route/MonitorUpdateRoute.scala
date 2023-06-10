package kpn.server.monitor.route

trait MonitorUpdateRoute {
  def update(context: MonitorUpdateContext): MonitorUpdateContext
}
