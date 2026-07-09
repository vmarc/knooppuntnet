package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorMessage
import kpn.api.common.monitor.MonitorRouteUpdateStatus

class MonitorUpdateReporterMock extends MonitorUpdateReporter {

  private var _messages: Seq[MonitorMessage] = Seq.empty

  override def report(message: MonitorMessage): Unit = {
    _messages = _messages :+ message
  }

  def messages: Seq[MonitorMessage] = _messages

  def statusses: Seq[MonitorRouteUpdateStatus] = {
    throw new RuntimeException("deprecated")
  }
}
