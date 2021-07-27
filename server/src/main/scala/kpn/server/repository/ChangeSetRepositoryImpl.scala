package kpn.server.repository

import kpn.api.common.ChangeSetSummary
import kpn.api.common.LocationChangeSetSummary
import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSetData
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesFilterPeriod
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.custom.Subset
import kpn.core.common.Time
import kpn.core.database.doc.ChangeSetDatas
import kpn.core.database.doc.ChangeSetSummaryDoc
import kpn.core.database.doc.LocationChangeSetSummaryDoc
import kpn.core.database.doc.NetworkChangeDoc
import kpn.core.database.doc.NodeChangeDoc
import kpn.core.database.doc.RouteChangeDoc
import kpn.core.database.query.Query
import kpn.core.database.views.changes.ChangesView
import kpn.core.mongo.Database
import kpn.core.mongo.actions.networks.MongoQueryNetworkChangeCount
import kpn.core.mongo.actions.networks.MongoQueryNetworkChangeCounts
import kpn.core.mongo.actions.networks.MongoQueryNetworkChanges
import kpn.core.mongo.actions.nodes.MongoQueryNodeChangeCount
import kpn.core.mongo.actions.nodes.MongoQueryNodeChangeCounts
import kpn.core.mongo.actions.nodes.MongoQueryNodeChanges
import kpn.core.mongo.actions.routes.MongoQueryRouteChangeCount
import kpn.core.mongo.actions.routes.MongoQueryRouteChangeCounts
import kpn.core.mongo.actions.routes.MongoQueryRouteChanges
import kpn.core.mongo.actions.subsets.MongoQuerySubsetChanges
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class ChangeSetRepositoryImpl(
  database: Database,
  // old
  changeDatabase: kpn.core.database.Database,
  mongoEnabled: Boolean
) extends ChangeSetRepository {

  private val log = Log(classOf[ChangeSetRepositoryImpl])

  override def saveChangeSetSummary(changeSetSummary: ChangeSetSummary): Unit = {
    if (mongoEnabled) {
      database.changeSetSummaries.save(changeSetSummary, log)
    }
    else {
      val id = docId("summary", changeSetSummary.key)
      changeDatabase.save(ChangeSetSummaryDoc(id, changeSetSummary))
    }
  }

  override def saveLocationChangeSetSummary(locationChangeSetSummary: LocationChangeSetSummary): Unit = {
    if (mongoEnabled) {
      database.locationChangeSetSummaries.save(locationChangeSetSummary, log)
    }
    else {
      val id = docId("location-summary", locationChangeSetSummary.key)
      changeDatabase.save(LocationChangeSetSummaryDoc(id, locationChangeSetSummary))
    }
  }

  override def saveNetworkChange(networkChange: NetworkChange): Unit = {
    if (mongoEnabled) {
      database.networkChanges.save(networkChange, log)
    }
    else {
      val id = docId("network", networkChange.key)
      changeDatabase.save(NetworkChangeDoc(id, networkChange))
    }
  }

  override def saveRouteChange(routeChange: RouteChange): Unit = {
    if (mongoEnabled) {
      database.routeChanges.save(routeChange, log)
    }
    else {
      val id = docId("route", routeChange.key)
      changeDatabase.save(RouteChangeDoc(id, routeChange))
    }
  }

  override def saveNodeChange(nodeChange: NodeChange): Unit = {
    if (mongoEnabled) {
      database.nodeChanges.save(nodeChange, log)
    }
    else {
      val id = docId("node", nodeChange.key)
      changeDatabase.save(NodeChangeDoc(id, nodeChange))
    }
  }

  override def changeSet(changeSetId: Long, replicationId: Option[ReplicationId], stale: Boolean): Seq[ChangeSetData] = {
    if (mongoEnabled) {
      ???
    }
    else {
      val repl = if (replicationId.isDefined) ":" + replicationId.get.number else ""
      val startKey = {
        s""""change:$changeSetId$repl:""""
      }
      val endKey = {
        s""""change:$changeSetId$repl:zzz""""
      }

      val query = Query("_all_docs", classOf[ChangeSetDatas])
        .startKey(startKey)
        .endKey(endKey)
        .includeDocs(true)
        .reduce(false)
        .stale(stale)

      val result = changeDatabase.execute(query)
      result.datas
    }
  }

  override def changes(parameters: ChangesParameters, stale: Boolean): Seq[ChangeSetSummary] = {
    if (mongoEnabled) {
      ???
    }
    else {
      ChangesView.changes(changeDatabase, parameters, stale)
    }
  }

  override def nodeChangesFilter(nodeId: Long, yearOption: Option[String], monthOption: Option[String], dayOption: Option[String], stale: Boolean): ChangesFilter = {
    if (mongoEnabled) {
      val year = yearOption match {
        case None => Time.now.year
        case Some(year) => year.toInt
      }
      val changeSetCounts = new MongoQueryNodeChangeCounts(database).execute(nodeId, year, monthOption.map(_.toInt))
      ChangesFilter.from(changeSetCounts, Some(year.toString), monthOption, dayOption)
    }
    else {
      changesFilter(Seq("node", nodeId.toString), yearOption, monthOption, dayOption, stale)
    }
  }

  override def nodeChangesCount(nodeId: Long, stale: Boolean = true): Long = {
    if (mongoEnabled) {
      new MongoQueryNodeChangeCount(database).execute(nodeId)
    }
    else {
      ChangesView.queryChangeCount(changeDatabase, "node", nodeId, stale)
    }
  }

  override def routeChangesFilter(routeId: Long, yearOption: Option[String], monthOption: Option[String], dayOption: Option[String], stale: Boolean): ChangesFilter = {
    if (mongoEnabled) {
      val year = yearOption match {
        case None => Time.now.year
        case Some(year) => year.toInt
      }
      val changeSetCounts = new MongoQueryRouteChangeCounts(database).execute(routeId, year, monthOption.map(_.toInt))
      ChangesFilter.from(changeSetCounts, Some(year.toString), monthOption, dayOption)
    }
    else {
      changesFilter(Seq("route", routeId.toString), yearOption, monthOption, dayOption, stale)
    }
  }

  override def routeChangesCount(routeId: Long, stale: Boolean = true): Long = {
    if (mongoEnabled) {
      new MongoQueryRouteChangeCount(database).execute(routeId)
    }
    else {
      ChangesView.queryChangeCount(changeDatabase, "route", routeId, stale)
    }
  }

  override def networkChangesFilter(networkId: Long, yearOption: Option[String], monthOption: Option[String], dayOption: Option[String], stale: Boolean): ChangesFilter = {
    if (mongoEnabled) {
      val year = yearOption match {
        case None => Time.now.year
        case Some(year) => year.toInt
      }
      val changeSetCounts = new MongoQueryNetworkChangeCounts(database).execute(networkId, year, monthOption.map(_.toInt))
      ChangesFilter.from(changeSetCounts, Some(year.toString), monthOption, dayOption)
    }
    else {
      changesFilter(Seq("network", networkId.toString), yearOption, monthOption, dayOption, stale)
    }
  }

  override def networkChangesCount(networkId: Long, stale: Boolean = true): Long = {
    if (mongoEnabled) {
      new MongoQueryNetworkChangeCount(database).execute(networkId)
    }
    else {
      ChangesView.queryChangeCount(changeDatabase, "network", networkId, stale)
    }
  }

  override def changesFilter(subsetOption: Option[Subset], year: Option[String], month: Option[String], day: Option[String], stale: Boolean): ChangesFilter = {
    if (mongoEnabled) {
      ChangesFilter(Seq.empty) // TODO MONGO
    }
    else {
      val prefix = subsetOption match {
        case Some(subset) => subset.country.domain + ":" + subset.networkType.name + ":change-set"
        case None => "change-set"
      }
      changesFilter(Seq(prefix), year, month, day, stale)
    }
  }

  private def changesFilter(keys: Seq[String], year: Option[String], month: Option[String], day: Option[String], stale: Boolean): ChangesFilter = {
    val yearPeriods = changesFilterPeriod(4, keys, stale)
    if (yearPeriods.isEmpty) {
      ChangesFilter(Seq.empty)
    }
    else {
      val selectedYear = year.getOrElse(yearPeriods.head.name)
      val modifiedYears = yearPeriods.map { yearPeriod =>
        if (yearPeriod.name == selectedYear) {
          val monthPeriods = changesFilterPeriod(2, keys :+ selectedYear, stale)
          val modifiedMonthPeriods = monthPeriods.map { monthPeriod: ChangesFilterPeriod =>
            if (month.contains(monthPeriod.name)) {
              val dayPeriods = changesFilterPeriod(2, keys ++ Seq(selectedYear, monthPeriod.name), stale)
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

  private def changesFilterPeriod(suffixLength: Int, keys: Seq[String], stale: Boolean): Seq[ChangesFilterPeriod] = {
    ChangesView.queryPeriod(changeDatabase, suffixLength, keys, stale)
  }

  override def subsetChanges(subset: Subset, parameters: ChangesParameters, stale: Boolean): Seq[ChangeSetSummary] = {
    if (mongoEnabled) {
      new MongoQuerySubsetChanges(database).execute(subset, parameters)
    }
    else {
      ChangesView.subsetChanges(changeDatabase, subset, parameters, stale)
    }
  }

  override def networkChanges(networkId: Long, parameters: ChangesParameters, stale: Boolean): Seq[NetworkChange] = {
    if (mongoEnabled) {
      new MongoQueryNetworkChanges(database).execute(networkId, parameters)
    }
    else {
      ChangesView.networkChanges(changeDatabase, networkId, parameters, stale)
    }
  }

  override def routeChanges(routeId: Long, parameters: ChangesParameters, stale: Boolean): Seq[RouteChange] = {
    if (mongoEnabled) {
      new MongoQueryRouteChanges(database).execute(routeId, parameters)
    }
    else {
      ChangesView.routeChanges(changeDatabase, routeId, parameters, stale)
    }
  }

  override def nodeChanges(nodeId: Long, parameters: ChangesParameters, stale: Boolean): Seq[NodeChange] = {
    if (mongoEnabled) {
      new MongoQueryNodeChanges(database).execute(nodeId, parameters)
    }
    else {
      ChangesView.nodeChanges(changeDatabase, nodeId, parameters, stale)
    }
  }

  private def docId(elementType: String, key: ChangeKey): String = {
    s"change:${key.changeSetId}:${key.replicationNumber}:$elementType:${key.elementId}"
  }
}
