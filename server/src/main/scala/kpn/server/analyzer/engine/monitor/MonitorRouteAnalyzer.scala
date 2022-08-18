package kpn.server.analyzer.engine.monitor

import kpn.api.base.ObjectId

import scala.xml.Elem

trait MonitorRouteAnalyzer {
  def processNewReference(user: String, routeId: ObjectId, filename: String, xml: Elem): Unit
}
