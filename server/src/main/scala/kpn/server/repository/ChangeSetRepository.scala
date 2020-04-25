package kpn.server.repository

import kpn.api.common.ChangeSetSummary
import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSetData
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.custom.Subset

trait ChangeSetRepository {

  def allNetworkIds(): Seq[Long]

  def allRouteIds(): Seq[Long]

  def allNodeIds(): Seq[Long]

  def saveChangeSetSummary(changeSetSummary: ChangeSetSummary): Unit

  def saveNetworkChange(networkChange: NetworkChange): Unit

  def saveRouteChange(routeChange: RouteChange): Unit

  def saveNodeChange(nodeChange: NodeChange): Unit

  def changeSet(changeSetId: Long, replicationId: Option[ReplicationId], stale: Boolean = true): Seq[ChangeSetData]

  def changes(changesParameters: ChangesParameters, stale: Boolean = true): Seq[ChangeSetSummary]

  def changesFilter(subset: Option[Subset], year: Option[String], month: Option[String], day: Option[String], stale: Boolean = true): ChangesFilter

  def networkChanges(parameters: ChangesParameters, stale: Boolean = true): Seq[NetworkChange]

  def networkChangesFilter(networkId: Long, year: Option[String], month: Option[String], day: Option[String], stale: Boolean = true): ChangesFilter

  def networkChangesCount(networkId: Long, stale: Boolean = true): Long

  def routeChanges(parameters: ChangesParameters, stale: Boolean = true): Seq[RouteChange]

  def routeChangesFilter(routeId: Long, year: Option[String], month: Option[String], day: Option[String], stale: Boolean = true): ChangesFilter

  def routeChangesCount(routeId: Long, stale: Boolean = true): Long

  def nodeChanges(parameters: ChangesParameters, stale: Boolean = true): Seq[NodeChange]

  def nodeChangesFilter(nodeId: Long, year: Option[String], month: Option[String], day: Option[String], stale: Boolean = true): ChangesFilter

  def nodeChangesCount(nodeId: Long, stale: Boolean = true): Long

  def allChangeSetIds(): Seq[String]

}
