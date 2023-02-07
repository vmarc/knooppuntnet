package kpn.server.monitor.route

trait MonitorUpdateReference {
  def update(context: MonitorUpdateContext): MonitorUpdateContext
}
