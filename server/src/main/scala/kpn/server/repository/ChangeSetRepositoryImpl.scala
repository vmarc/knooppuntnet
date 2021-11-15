package kpn.server.repository

import kpn.api.common.ChangeSetSummary
import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSetData
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesFilterPeriod
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.custom.Subset
import kpn.core.common.Time
import kpn.database.actions.changes.MongoQueryChangeSet
import kpn.database.actions.changes.MongoQueryChangeSetCounts
import kpn.database.actions.changes.MongoQueryChangeSetSummaries
import kpn.database.actions.networks.MongoQueryNetworkChangeCount
import kpn.database.actions.networks.MongoQueryNetworkChangeCounts
import kpn.database.actions.networks.MongoQueryNetworkChanges
import kpn.database.actions.nodes.MongoQueryNodeChangeCount
import kpn.database.actions.nodes.MongoQueryNodeChangeCounts
import kpn.database.actions.nodes.MongoQueryNodeChanges
import kpn.database.actions.routes.MongoQueryRouteChangeCount
import kpn.database.actions.routes.MongoQueryRouteChangeCounts
import kpn.database.actions.routes.MongoQueryRouteChanges
import kpn.database.actions.subsets.MongoQuerySubsetChanges
import kpn.database.base.Database
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.network.NetworkChange
import org.springframework.stereotype.Component

@Component
class ChangeSetRepositoryImpl(database: Database) extends ChangeSetRepository {

  private val log = Log(classOf[ChangeSetRepositoryImpl])

  override def saveChangeSetSummary(changeSetSummary: ChangeSetSummary): Unit = {
    database.changes.save(changeSetSummary, log)
  }

  override def saveNetworkChange(networkChange: NetworkChange): Unit = {
    database.networkChanges.save(networkChange, log)
  }

  override def saveNetworkInfoChange(networkInfoChange: NetworkInfoChange): Unit = {
    database.networkInfoChanges.save(networkInfoChange, log)
  }

  override def saveRouteChange(routeChange: RouteChange): Unit = {
    database.routeChanges.save(routeChange, log)
  }

  override def saveNodeChange(nodeChange: NodeChange): Unit = {
    database.nodeChanges.save(nodeChange, log)
  }

  override def changeSet(changeSetId: Long, replicationId: Option[ReplicationId]): Seq[ChangeSetData] = {
    new MongoQueryChangeSet(database).execute(changeSetId, replicationId)
  }

  override def changes(parameters: ChangesParameters): Seq[ChangeSetSummary] = {
    new MongoQueryChangeSetSummaries(database).execute(parameters)
  }

  override def nodeChangesFilter(nodeId: Long, yearOption: Option[String], monthOption: Option[String], dayOption: Option[String]): ChangesFilter = {
    val year = yearOption match {
      case None => Time.now.year
      case Some(year) => year.toInt
    }
    val changeSetCounts = new MongoQueryNodeChangeCounts(database).execute(nodeId, year, monthOption.map(_.toInt))
    ChangesFilter.from(changeSetCounts, Some(year.toString), monthOption, dayOption)
  }

  override def nodeChangesCount(nodeId: Long): Long = {
    new MongoQueryNodeChangeCount(database).execute(nodeId)
  }

  override def routeChangesFilter(routeId: Long, yearOption: Option[String], monthOption: Option[String], dayOption: Option[String]): ChangesFilter = {
    val year = yearOption match {
      case None => Time.now.year
      case Some(year) => year.toInt
    }
    val changeSetCounts = new MongoQueryRouteChangeCounts(database).execute(routeId, year, monthOption.map(_.toInt))
    ChangesFilter.from(changeSetCounts, Some(year.toString), monthOption, dayOption)
  }

  override def routeChangesCount(routeId: Long): Long = {
    new MongoQueryRouteChangeCount(database).execute(routeId)
  }

  override def networkChangesFilter(networkId: Long, yearOption: Option[String], monthOption: Option[String], dayOption: Option[String]): ChangesFilter = {
    val year = yearOption match {
      case None => Time.now.year
      case Some(year) => year.toInt
    }
    val changeSetCounts = new MongoQueryNetworkChangeCounts(database).execute(networkId, year, monthOption.map(_.toInt))
    ChangesFilter.from(changeSetCounts, Some(year.toString), monthOption, dayOption)
  }

  override def networkChangesCount(networkId: Long): Long = {
    new MongoQueryNetworkChangeCount(database).execute(networkId)
  }

  override def changesFilter(subsetOption: Option[Subset], yearOption: Option[String], monthOption: Option[String], dayOption: Option[String]): ChangesFilter = {
    val year = yearOption match {
      case None => Time.now.year
      case Some(year) => year.toInt
    }
    // TODO MONGO does not support subsetOption yet !!!
    val changeSetCounts = new MongoQueryChangeSetCounts(database).execute(year, monthOption.map(_.toInt))
    ChangesFilter.from(changeSetCounts, Some(year.toString), monthOption, dayOption)
  }

  private def changesFilter(keys: Seq[String], year: Option[String], month: Option[String], day: Option[String]): ChangesFilter = {
    val yearPeriods = changesFilterPeriod(4, keys)
    if (yearPeriods.isEmpty) {
      ChangesFilter(Seq.empty)
    }
    else {
      val selectedYear = year.getOrElse(yearPeriods.head.name)
      val modifiedYears = yearPeriods.map { yearPeriod =>
        if (yearPeriod.name == selectedYear) {
          val monthPeriods = changesFilterPeriod(2, keys :+ selectedYear)
          val modifiedMonthPeriods = monthPeriods.map { monthPeriod: ChangesFilterPeriod =>
            if (month.contains(monthPeriod.name)) {
              val dayPeriods = changesFilterPeriod(2, keys ++ Seq(selectedYear, monthPeriod.name))
              val modifiedDays = dayPeriods.map { dayPeriod =>
                if (day.contains(dayPeriod.name)) {
                  dayPeriod.copy(selected = day.nonEmpty, current = day.nonEmpty)
                }
                else {
                  dayPeriod
                }
              }
              monthPeriod.copy(selected = month.nonEmpty, periods = modifiedDays, current = month.nonEmpty && day.isEmpty)
            }
            else {
              monthPeriod
            }
          }
          yearPeriod.copy(selected = true, periods = modifiedMonthPeriods, current = year.nonEmpty && month.isEmpty)
        }
        else {
          yearPeriod
        }
      }
      ChangesFilter(modifiedYears)
    }
  }

  private def changesFilterPeriod(suffixLength: Int, keys: Seq[String]): Seq[ChangesFilterPeriod] = {
    // ChangesView.queryPeriod(changeDatabase, suffixLength, keys)
    Seq.empty
  }

  override def subsetChanges(subset: Subset, parameters: ChangesParameters): Seq[ChangeSetSummary] = {
    new MongoQuerySubsetChanges(database).execute(subset, parameters)
  }

  override def networkChanges(networkId: Long, parameters: ChangesParameters): Seq[NetworkInfoChange] = {
    new MongoQueryNetworkChanges(database).execute(networkId, parameters)
  }

  override def routeChanges(routeId: Long, parameters: ChangesParameters): Seq[RouteChange] = {
    new MongoQueryRouteChanges(database).execute(routeId, parameters)
  }

  override def nodeChanges(nodeId: Long, parameters: ChangesParameters): Seq[NodeChange] = {
    new MongoQueryNodeChanges(database).execute(nodeId, parameters)
  }
}
