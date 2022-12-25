package kpn.server.api.monitor.route

trait MonitorUpdateReference {
  def update(context: MonitorUpdateContext): MonitorUpdateContext
}
