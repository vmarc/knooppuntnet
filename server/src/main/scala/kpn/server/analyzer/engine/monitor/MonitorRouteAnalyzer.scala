package kpn.server.analyzer.engine.monitor

import kpn.api.base.MongoId

import scala.xml.Elem

trait MonitorRouteAnalyzer {
  def processNewReference(user: String, routeId: MongoId, filename: String, xml: Elem): Unit
}
