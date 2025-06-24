package kpn.server.monitor.route.update

class MonitorContext {
  private var privateContext: MonitorUpdateContext = _

  def set(context: MonitorUpdateContext): Unit = {
    privateContext = context
  }

  def value: MonitorUpdateContext = {
    privateContext
  }
}
