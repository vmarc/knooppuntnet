package kpn.server.repository

import kpn.api.common.ChangeSetSummary
import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSetData
import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesFilterOption
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.custom.Subset
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.database.actions.changes.MongoQueryChangeSet
import kpn.database.actions.changes.MongoQueryChangeSetCounts
import kpn.database.actions.changes.MongoQueryChangeSetSummaries
import kpn.database.actions.networks.MongoQueryNetworkChangeCount
import kpn.database.actions.networks.MongoQueryNetworkChangeCounts
import kpn.database.actions.networks.MongoQueryNetworkChanges
import kpn.database.actions.nodes.MongoQueryNodeChangeCount
import kpn.database.actions.nodes.MongoQueryNodeChangeCounts
import kpn.database.actions.nodes.MongoQueryNodeChanges
import kpn.database.actions.routes.MongoQueryBaseRouteChanges
import kpn.database.actions.routes.MongoQueryRouteChangeCount
import kpn.database.actions.routes.MongoQueryRouteChangeCounts
import kpn.database.actions.routes.MongoQueryRouteChanges
import kpn.database.actions.subsets.MongoQuerySubsetChanges
import kpn.database.base.Database
import org.springframework.stereotype.Component

@Component
class ChangeSetRepository(database: Database) {

  private val log = Log(classOf[ChangeSetRepository])

  def saveChangeSetSummary(changeSetSummary: ChangeSetSummary): Unit = {
    database.changes.save(changeSetSummary, log)
  }

  def saveNetworkChange(networkChange: NetworkChange): Unit = {
    database.networkChanges.save(networkChange, log)
  }

  def saveBaseRouteChange(baseRouteChange: BaseRouteChange): Unit = {
    database.baseRouteChanges.save(baseRouteChange, log)
  }

  def saveRouteChange(routeChange: RouteChange): Unit = {
    database.routeChanges.save(routeChange, log)
  }

  def saveNodeChange(nodeChange: NodeChange): Unit = {
    database.nodeChanges.save(nodeChange, log)
  }

  def changeSet(changeSetId: Long, replicationId: Option[ReplicationId]): Seq[ChangeSetData] = {
    new MongoQueryChangeSet(database).execute(changeSetId, replicationId)
  }

  def changes(parameters: ChangesParameters): Seq[ChangeSetSummary] = {
    new MongoQueryChangeSetSummaries(database).execute(parameters)
  }

  def nodeChangesFilter(nodeId: Long, year: Option[Long], month: Option[Long], day: Option[Long]): Seq[ChangesFilterOption] = {
    val yearInt = year match {
      case None => Time.now.year
      case Some(yearValue) => yearValue.toInt
    }
    val changeSetCounts = new MongoQueryNodeChangeCounts(database).execute(nodeId, yearInt, month.map(_.toInt))
    changeSetCounts.toFilterOptions(year, month, day)
  }

  def nodeChangesCount(nodeId: Long): Long = {
    new MongoQueryNodeChangeCount(database).execute(nodeId)
  }

  def routeChangesFilter(routeId: Long, year: Option[Long], month: Option[Long], day: Option[Long]): Seq[ChangesFilterOption] = {
    val yearInt = year match {
      case None => Time.now.year
      case Some(yearValue) => yearValue.toInt
    }
    val changeSetCounts = new MongoQueryRouteChangeCounts(database).execute(routeId, yearInt, month.map(_.toInt))
    changeSetCounts.toFilterOptions(year, month, day)
  }

  def routeChangesCount(routeId: Long): Long = {
    new MongoQueryRouteChangeCount(database).execute(routeId)
  }

  def networkChangesFilter(networkId: Long, yearOption: Option[String], monthOption: Option[String], dayOption: Option[String]): ChangesFilter = {
    val year = yearOption match {
      case None => Time.now.year
      case Some(yearValue) => yearValue.toInt
    }
    val changeSetCounts = new MongoQueryNetworkChangeCounts(database).execute(networkId, year, monthOption.map(_.toInt))
    ChangesFilter.from(changeSetCounts, Some(year.toString), monthOption, dayOption)
  }

  def networkChangesCount(networkId: Long): Long = {
    new MongoQueryNetworkChangeCount(database).execute(networkId)
  }

  def changesFilter(
    subset: Option[Subset],
    year: Option[Long],
    month: Option[Long],
    day: Option[Long]
  ): Seq[ChangesFilterOption] = {

    val yearInt = year match {
      case None => Time.now.year
      case Some(yearValue) => yearValue.toInt
    }
    val changeSetCounts = new MongoQueryChangeSetCounts(database).execute(subset, yearInt, month.map(_.toInt))
    changeSetCounts.toFilterOptions(year, month, day)
  }

  def subsetChanges(subset: Subset, parameters: ChangesParameters): Seq[ChangeSetSummary] = {
    new MongoQuerySubsetChanges(database).execute(subset, parameters)
  }

  def networkChanges(networkId: Long, parameters: ChangesParameters): Seq[NetworkChange] = {
    new MongoQueryNetworkChanges(database).execute(networkId, parameters)
  }

  def routeChanges(routeId: Long, parameters: ChangesParameters): Seq[RouteChange] = {
    new MongoQueryRouteChanges(database).execute(routeId, parameters)
  }

  def baseRouteChanges(ids: Seq[String]): Seq[BaseRouteChange] = {
    new MongoQueryBaseRouteChanges(database).execute(ids)
  }

  def nodeChanges(nodeId: Long, parameters: ChangesParameters): Seq[NodeChange] = {
    new MongoQueryNodeChanges(database).execute(nodeId, parameters)
  }
}
