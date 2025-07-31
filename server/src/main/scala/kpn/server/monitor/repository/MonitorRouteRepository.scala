package kpn.server.monitor.repository

import kpn.api.base.ObjectId
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorRouteDetail
import kpn.api.common.monitor.MonitorRouteDeviationInfo
import kpn.api.common.monitor.MonitorRouteRelationInfo
import kpn.server.analyzer.engine.tiles.domain.TileId
import kpn.server.monitor.domain.MonitorGroupRouteCount
import kpn.server.monitor.domain.MonitorGroupRouteInfo
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorReferenceTileInfo
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteChange
import kpn.server.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.domain.MonitorStateTile
import kpn.server.monitor.domain.OldMonitorReference

trait MonitorRouteRepository {

  // *** MonitorRoute ***

  def allRouteIds: Seq[Long]

  def routes(): Seq[MonitorRoute]

  def routeById(routeId: ObjectId): Option[MonitorRoute]

  def routeByName(groupId: ObjectId, routeName: String): Option[MonitorRoute]

  def saveRoute(route: MonitorRoute): Unit

  def deleteRoute(routeId: ObjectId): Unit

  def routeNames(groupId: ObjectId): Seq[String]

  // *** MonitorReference ***

  def reference(routeId: ObjectId, relationId: Option[Long]): Option[MonitorReference]

  def saveReference(reference: MonitorReference): Unit

  def references(routeId: ObjectId): Seq[MonitorReference]

  def oldReferences(routeId: ObjectId): Seq[OldMonitorReference]

  def deleteReferences(routeId: ObjectId): Unit

  def deleteReference(routeId: ObjectId, subRelationId: Long): Unit

  def deleteReferenceById(objectId: ObjectId): Unit

  def routeRelationReferenceId(routeId: ObjectId, relationId: Option[Long]): Option[ObjectId]

  def referenceIds(routeId: ObjectId): Seq[MonitorReferenceId]

  def superRouteReferenceSummary(routeId: ObjectId): Option[Long]

  // *** MonitorState ***

  def saveState(state: MonitorState): Unit

  def saveStateTile(stateTile: MonitorStateTile): Unit

  def deleteStates(routeId: ObjectId): Unit

  def deleteStateTiles(routeId: ObjectId): Unit

  def deleteStateTile(tileId: ObjectId): Unit

  def deleteState(routeId: ObjectId, subRelationId: Long): Unit

  def deleteStateById(objectId: ObjectId): Unit

  def state(routeId: ObjectId, relationId: Long): Option[MonitorState]

  def stateTiles(routeId: ObjectId, relationId: Long): Seq[MonitorStateTile]

  def states(routeId: ObjectId): Seq[MonitorState]

  def stateCount(routeId: ObjectId): Long

  def stateSize(routeId: ObjectId): Long

  def superRouteStateSummary(routeId: ObjectId): Option[MonitorStateSummary]

  def stateSummaries(routeId: ObjectId): Seq[MonitorStateSummary]

  def stateIds(routeId: ObjectId): Seq[MonitorStateId]

  // *** changes ***

  def saveRouteChange(routeChange: MonitorRouteChange): Unit

  def saveRouteChangeGeometry(routeChangeGeometry: MonitorRouteChangeGeometry): Unit

  def routeChange(monitorRouteId: String, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChange]

  def routeChangeGeometry(monitorRouteId: String, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChangeGeometry]

  def changesCount(parameters: MonitorChangesParameters): Long

  def changes(parameters: MonitorChangesParameters): Seq[MonitorRouteChange]

  def groupRouteCounts(): Seq[MonitorGroupRouteCount]

  def groupRouteInfos(): Seq[MonitorGroupRouteInfo]

  def groupRouteDetails(groupId: ObjectId): Seq[MonitorRouteDetail]

  def groupChangesCount(groupName: String, parameters: MonitorChangesParameters): Long

  def groupChanges(groupName: String, parameters: MonitorChangesParameters): Seq[MonitorRouteChange]

  def routeChangesCount(monitorRouteId: String, parameters: MonitorChangesParameters): Long

  def routeChanges(monitorRouteId: String, parameters: MonitorChangesParameters): Seq[MonitorRouteChange]

  def routeChange(changeKey: ChangeKey): Option[MonitorRouteChange]

  def routeChangeGeometry(changeKey: ChangeKey): Option[MonitorRouteChangeGeometry]

  def routeReferenceKey(monitorRouteId: String): Option[String]

  def stateTileIds(): Seq[TileId]

  def referenceTileIds(): Seq[TileId]

  def stateTilesBytTileId(tileId: TileId): Seq[MonitorStateTile]

  def referenceTiles(tileId: TileId): Seq[MonitorReferenceTileInfo]

  def routeDeviations(routeId: ObjectId): Seq[MonitorRouteDeviationInfo]

  def routeMemberCount(relationId: Long): Long

  def stateDeviationInfos(routeId: ObjectId): Seq[MonitorStateDeviationInfo]

  def routeRelationInfos(relationIds: Seq[Long]): Seq[MonitorRouteRelationInfo]
}
