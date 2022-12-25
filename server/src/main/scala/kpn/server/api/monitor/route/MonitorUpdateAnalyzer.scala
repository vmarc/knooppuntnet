package kpn.server.api.monitor.route

trait MonitorUpdateAnalyzer {
  def analyze(context: MonitorUpdateContext): MonitorUpdateContext
}
