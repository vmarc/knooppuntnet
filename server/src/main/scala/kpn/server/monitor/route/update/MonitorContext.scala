package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage

class MonitorContext {
  private var privateContext: MonitorUpdateContext = _

  def set(context: MonitorUpdateContext): Unit = {
    privateContext = context
  }

  def value: MonitorUpdateContext = {
    privateContext
  }

  def report(message: MonitorRouteUpdateStatusMessage): Unit = {
    privateContext.reporter.report(message)
  }

  def stepActive(stepId: String): Unit = {
    privateContext.reporter.stepActive(stepId)
  }

  def stepDone(stepId: String): Unit = {
    privateContext.reporter.stepDone(stepId)
  }
}
