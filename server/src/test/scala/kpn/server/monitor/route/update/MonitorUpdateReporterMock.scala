package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteUpdateStatus
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage

class MonitorUpdateReporterMock extends MonitorUpdateReporter {

  private var _messages: Seq[MonitorRouteUpdateStatusMessage] = Seq.empty

  override def report(message: MonitorRouteUpdateStatusMessage): Unit = {
    _messages = _messages :+ message
  }

  def messages: Seq[MonitorRouteUpdateStatusMessage] = _messages

  def statusses: Seq[MonitorRouteUpdateStatus] = {
    throw new RuntimeException("deprecated")
  }
}
