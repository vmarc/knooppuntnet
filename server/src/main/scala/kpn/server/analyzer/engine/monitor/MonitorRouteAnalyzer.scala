package kpn.server.analyzer.engine.monitor

import kpn.server.api.monitor.domain.MonitorRoute

import scala.xml.Elem

trait MonitorRouteAnalyzer {
  def processNewReference(user: String, route: MonitorRoute, filename: String, xml: Elem): Unit
}
