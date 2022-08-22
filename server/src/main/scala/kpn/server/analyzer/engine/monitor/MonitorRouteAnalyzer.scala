package kpn.server.analyzer.engine.monitor

import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteReference

import scala.xml.Elem

trait MonitorRouteAnalyzer {
  def analyze(route: MonitorRoute, reference: MonitorRouteReference): Unit

  def processNewReference(user: String, route: MonitorRoute, filename: String, xml: Elem): String
}
