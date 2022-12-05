package kpn.server.analyzer.engine.monitor.changes

import kpn.api.custom.Relation
import kpn.api.custom.Timestamp

trait MonitorRouteLoader {

  def loadInitial(timestamp: Timestamp, routeId: Long): Option[Relation]

  def loadBefore(changeSetId: Long, timestamp: Timestamp, routeId: Long): Option[Relation]

  def loadAfter(changeSetId: Long, timestamp: Timestamp, routeId: Long): Option[Relation]

}
