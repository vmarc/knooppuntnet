package kpn.server.monitor.route

trait MonitorUpdateAnalyzer {
  def analyze(context: MonitorUpdateContext): MonitorUpdateContext
}
