package kpn.server.monitor.repository

import kpn.api.base.ObjectId
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorRouteDetail
import kpn.core.doc.SuperSegmentElementInfo
import kpn.server.monitor.domain.MonitorGroupRouteCount
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteChange
import kpn.server.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.monitor.domain.MonitorState

trait MonitorRouteRepository {

  def allRouteIds: Seq[Long]

  def saveRoute(route: MonitorRoute): Unit

  def deleteRoute(routeId: ObjectId): Unit

  def deleteRouteReferences(routeId: ObjectId): Unit

  def deleteRouteReference(routeId: ObjectId, subRelationId: Long): Unit

  def deleteRouteReferenceById(objectId: ObjectId): Unit

  def deleteRouteStates(routeId: ObjectId): Unit

  def deleteRouteState(routeId: ObjectId, subRelationId: Long): Unit

  def deleteRouteStateById(objectId: ObjectId): Unit

  def saveRouteState(routeState: MonitorState): Unit

  def saveRouteReference(routeReference: MonitorReference): Unit

  def saveRouteChange(routeChange: MonitorRouteChange): Unit

  def saveRouteChangeGeometry(routeChangeGeometry: MonitorRouteChangeGeometry): Unit

  def routeById(routeId: ObjectId): Option[MonitorRoute]

  def routeByName(groupId: ObjectId, routeName: String): Option[MonitorRoute]

  def routeState(routeId: ObjectId, relationId: Long): Option[MonitorState]

  def routeStates(routeId: ObjectId): Seq[MonitorState]

  def routeStateCount(routeId: ObjectId): Long

  def routeStateSize(routeId: ObjectId): Long

  def routeStateSegments(routeId: ObjectId): Seq[SuperSegmentElementInfo]

  def routeReference(routeId: ObjectId, relationId: Option[Long]): Option[MonitorReference]

  def routeRelationReferenceId(routeId: ObjectId, relationId: Option[Long]): Option[ObjectId]

  def routeReferences(routeId: ObjectId): Seq[MonitorReference]

  def routeReferenceIds(routeId: ObjectId): Seq[MonitorReferenceId]

  def superRouteReferenceSummary(routeId: ObjectId): Option[Long]

  def superRouteStateSummary(routeId: ObjectId): Option[MonitorStateSummary]

  def routeStateSummaries(routeId: ObjectId): Seq[MonitorStateSummary]

  def routeStateIds(routeId: ObjectId): Seq[MonitorStateId]

  def routeChange(monitorRouteId: String, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChange]

  def routeChangeGeometry(monitorRouteId: String, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChangeGeometry]

  def changesCount(parameters: MonitorChangesParameters): Long

  def changes(parameters: MonitorChangesParameters): Seq[MonitorRouteChange]

  def groupRouteCounts(): Seq[MonitorGroupRouteCount]

  def groupRouteDetails(groupId: ObjectId): Seq[MonitorRouteDetail]

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
