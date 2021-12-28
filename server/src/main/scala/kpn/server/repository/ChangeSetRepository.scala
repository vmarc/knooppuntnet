package kpn.server.repository

import kpn.api.common.ChangeSetSummary
import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSetData
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesFilterOption
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.custom.Subset
import kpn.server.analyzer.engine.changes.network.NetworkChange

trait ChangeSetRepository {

  def saveChangeSetSummary(changeSetSummary: ChangeSetSummary): Unit

  def saveNetworkChange(networkChange: NetworkChange): Unit

  def saveNetworkInfoChange(networkInfoChange: NetworkInfoChange): Unit

  def saveRouteChange(routeChange: RouteChange): Unit

  def saveNodeChange(nodeChange: NodeChange): Unit

  def changeSet(changeSetId: Long, replicationId: Option[ReplicationId]): Seq[ChangeSetData]

  def changes(changesParameters: ChangesParameters): Seq[ChangeSetSummary]

  def changesFilter(subset: Option[Subset], year: Option[Long], month: Option[Long], day: Option[Long]): Seq[ChangesFilterOption]

  def subsetChanges(subset: Subset, parameters: ChangesParameters): Seq[ChangeSetSummary]

  def networkChanges(networkId: Long, parameters: ChangesParameters): Seq[NetworkInfoChange]

  def networkChangesFilter(networkId: Long, year: Option[String], month: Option[String], day: Option[String]): ChangesFilter

  def networkChangesCount(networkId: Long): Long

  def routeChanges(routeId: Long, parameters: ChangesParameters): Seq[RouteChange]

  def routeChangesFilter(routeId: Long, year: Option[Long], month: Option[Long], day: Option[Long]): ChangesFilter

  def routeChangesCount(routeId: Long): Long

  def nodeChanges(nodeId: Long, parameters: ChangesParameters): Seq[NodeChange]

  def nodeChangesFilter(nodeId: Long, year: Option[Long], month: Option[Long], day: Option[Long]): ChangesFilter

  def nodeChangesCount(nodeId: Long): Long

}
