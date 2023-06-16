package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteUpdateStatus

class MonitorUpdateReporterMock extends MonitorUpdateReporter {

  private var _statusses: Seq[MonitorRouteUpdateStatus] = Seq.empty

  override def report(status: MonitorRouteUpdateStatus): Unit = {
    _statusses = _statusses :+ status
  }

  def statusses: Seq[MonitorRouteUpdateStatus] = _statusses

}
