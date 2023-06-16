package kpn.server.monitor.route.update

trait MonitorUpdateRoute {
  def update(context: MonitorUpdateContext): MonitorUpdateContext
}
