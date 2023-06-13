package kpn.server.monitor.repository

import kpn.api.base.ObjectId
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorRouteSegmentInfo
import kpn.server.monitor.domain.MonitorGroupRouteCount
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteChange
import kpn.server.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import kpn.server.monitor.domain.OldMonitorRoute
import kpn.server.monitor.domain.OldMonitorRouteReference
import kpn.server.monitor.domain.OldMonitorRouteState

trait MonitorRouteRepository {

  def allRouteIds: Seq[Long]

  def saveRoute(route: MonitorRoute): Unit

  def deleteRoute(routeId: ObjectId): Unit

  def deleteRouteReferences(routeId: ObjectId): Unit

  def deleteRouteReference(routeId: ObjectId, subRelationId: Long): Unit

  def deleteRouteStates(routeId: ObjectId): Unit

  def deleteRouteState(routeId: ObjectId, subRelationId: Long): Unit

  def saveRouteState(routeState: MonitorRouteState): Unit

  def saveRouteReference(routeReference: MonitorRouteReference): Unit

  def saveRouteChange(routeChange: MonitorRouteChange): Unit

  def saveRouteChangeGeometry(routeChangeGeometry: MonitorRouteChangeGeometry): Unit

  def routeById(routeId: ObjectId): Option[MonitorRoute]

  def routeByName(groupId: ObjectId, routeName: String): Option[MonitorRoute]

  def oldRouteByName(groupId: ObjectId, routeName: String): Option[OldMonitorRoute]

  def routeState(routeId: ObjectId, relationId: Long): Option[MonitorRouteState]

  def oldRouteState(routeId: ObjectId): Option[OldMonitorRouteState]

  def routeStates(routeId: ObjectId): Seq[MonitorRouteState]

  def routeStateCount(routeId: ObjectId): Long

  def routeStateSize(routeId: ObjectId): Long

  def routeStateSegments(routeId: ObjectId): Seq[MonitorRouteSegmentInfo]

  def routeReference(routeId: ObjectId): Option[MonitorRouteReference]

  def routeRelationReference(routeId: ObjectId, relationId: Long): Option[MonitorRouteReference]

  def routeReferences(routeId: ObjectId): Seq[MonitorRouteReference]

  def oldRouteReferenceRouteWithId(routeId: ObjectId): Option[OldMonitorRouteReference]

  def superRouteReferenceSummary(routeId: ObjectId): Option[Long]

  def superRouteStateSummary(routeId: ObjectId): Option[MonitorRouteStateSummary]

  def routeStateSummaries(routeId: ObjectId): Seq[MonitorRouteStateSummary]

  def routeChange(monitorRouteId: String, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChange]

  def routeChangeGeometry(monitorRouteId: String, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChangeGeometry]

  def changesCount(parameters: MonitorChangesParameters): Long

  def changes(parameters: MonitorChangesParameters): Seq[MonitorRouteChange]

  def groupRouteCounts(): Seq[MonitorGroupRouteCount]

  def groupChangesCount(groupName: String, parameters: MonitorChangesParameters): Long

  def groupChanges(groupName: String, parameters: MonitorChangesParameters): Seq[MonitorRouteChange]

  def routeChangesCount(monitorRouteId: String, parameters: MonitorChangesParameters): Long

  def routeChanges(monitorRouteId: String, parameters: MonitorChangesParameters): Seq[MonitorRouteChange]

  def routes(): Seq[MonitorRoute]

  def routeChange(changeKey: ChangeKey): Option[MonitorRouteChange]

  def routeChangeGeometry(changeKey: ChangeKey): Option[MonitorRouteChangeGeometry]

  def routeReferenceKey(monitorRouteId: String): Option[String]

  def routeNames(groupId: ObjectId): Seq[String]
}
