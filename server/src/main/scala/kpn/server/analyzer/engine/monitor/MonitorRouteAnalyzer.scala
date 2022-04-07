package kpn.server.analyzer.engine.monitor

import scala.xml.Elem

trait MonitorRouteAnalyzer {
  def processNewReference(user: String, routeDocId: String, filename: String, xml: Elem): Unit
}
