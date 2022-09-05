package kpn.server.analyzer.engine.monitor

import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteReference

import scala.xml.Elem

trait MonitorRouteAnalyzer {

  def analyze(
    route: MonitorRoute,
    reference: MonitorRouteReference
  ): Unit

  def processGpxFileUpload(
    user: String,
    route: MonitorRoute,
    filename: String,
    xml: Elem
  ): MonitorRouteSaveResult
}
