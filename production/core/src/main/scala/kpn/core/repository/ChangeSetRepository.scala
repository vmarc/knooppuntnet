package kpn.core.repository

import kpn.shared.ChangeSetSummary
import kpn.shared.ReplicationId
import kpn.shared.Subset
import kpn.shared.changes.ChangeSetData
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.details.RouteChange
import kpn.shared.changes.filter.ChangesFilter
import kpn.shared.changes.filter.ChangesParameters


trait ChangeSetRepository {

  def saveChangeSetSummary(changeSetSummary: ChangeSetSummary): Unit

  def saveNetworkChange(networkChange: NetworkChange): Unit

  def saveRouteChange(routeChange: RouteChange): Unit

  def saveNodeChange(nodeChange: NodeChange): Unit

  def changeSet(changeSetId: Long, replicationId: Option[ReplicationId], stale: Boolean = true): Seq[ChangeSetData]

  def changes(changesParameters: ChangesParameters, stale: Boolean = true): Seq[ChangeSetSummary]

  def changesFilter(subset: Option[Subset], year: Option[String], month: Option[String], day: Option[String], stale: Boolean = true): ChangesFilter

  def networkChangesFilter(networkId: Long, year: Option[String], month: Option[String], day: Option[String], stale: Boolean = true): ChangesFilter

  def networkChanges(parameters: ChangesParameters, stale: Boolean = true): Seq[NetworkChange]

  def routeChanges(parameters: ChangesParameters, stale: Boolean = true): Seq[RouteChange]

  def nodeChanges(parameters: ChangesParameters, stale: Boolean = true): Seq[NodeChange]

  def allChangeSetIds(): Seq[String]

}
