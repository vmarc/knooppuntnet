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
import kpn.core.database.Database
import kpn.core.database.doc.ChangeSetDatas
import kpn.core.database.doc.ChangeSetSummaryDoc
import kpn.core.database.doc.LocationChangeSetSummaryDoc
import kpn.core.database.doc.NetworkChangeDoc
import kpn.core.database.doc.NodeChangeDoc
import kpn.core.database.doc.RouteChangeDoc
import kpn.core.database.query.Query
import kpn.core.database.views.changes.ChangesView
import kpn.core.db.couch.ViewResult2
import org.springframework.stereotype.Component

@Component
class ChangeSetRepositoryImpl(changeDatabase: Database) extends ChangeSetRepository {

  override def saveChangeSetSummary(changeSetSummary: ChangeSetSummary): Unit = {
    val id = docId("summary", changeSetSummary.key)
    changeDatabase.save(ChangeSetSummaryDoc(id, changeSetSummary))
  }

  override def saveLocationChangeSetSummary(locationChangeSetSummary: LocationChangeSetSummary): Unit = {
    val id = docId("location-summary", locationChangeSetSummary.key)
    changeDatabase.save(LocationChangeSetSummaryDoc(id, locationChangeSetSummary))
  }

  override def saveNetworkChange(networkChange: NetworkChange): Unit = {
    val id = docId("network", networkChange.key)
    changeDatabase.save(NetworkChangeDoc(id, networkChange))
  }

  override def saveRouteChange(routeChange: RouteChange): Unit = {
    val id = docId("route", routeChange.key)
    changeDatabase.save(RouteChangeDoc(id, routeChange))
  }

  override def saveNodeChange(nodeChange: NodeChange): Unit = {
    val id = docId("node", nodeChange.key)
    changeDatabase.save(NodeChangeDoc(id, nodeChange))
  }

  override def changeSet(changeSetId: Long, replicationId: Option[ReplicationId], stale: Boolean): Seq[ChangeSetData] = {

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

  override def changes(parameters: ChangesParameters, stale: Boolean): Seq[ChangeSetSummary] = {
    ChangesView.changes(changeDatabase, parameters, stale)
  }

  override def nodeChangesFilter(nodeId: Long, year: Option[String], month: Option[String], day: Option[String], stale: Boolean): ChangesFilter = {
    changesFilter(Seq("node", nodeId.toString), year, month, day, stale)
  }

  override def nodeChangesCount(nodeId: Long, stale: Boolean = true): Long = {
    ChangesView.queryChangeCount(changeDatabase, "node", nodeId, stale)
  }

  override def routeChangesFilter(routeId: Long, year: Option[String], month: Option[String], day: Option[String], stale: Boolean): ChangesFilter = {
    changesFilter(Seq("route", routeId.toString), year, month, day, stale)
  }

  override def routeChangesCount(routeId: Long, stale: Boolean = true): Long = {
    ChangesView.queryChangeCount(changeDatabase, "route", routeId, stale)
  }

  override def networkChangesFilter(networkId: Long, year: Option[String], month: Option[String], day: Option[String], stale: Boolean): ChangesFilter = {
    changesFilter(Seq("network", networkId.toString), year, month, day, stale)
  }

  override def networkChangesCount(networkId: Long, stale: Boolean = true): Long = {
    ChangesView.queryChangeCount(changeDatabase, "network", networkId, stale)
  }

  override def changesFilter(subsetOption: Option[Subset], year: Option[String], month: Option[String], day: Option[String], stale: Boolean): ChangesFilter = {
    val prefix = subsetOption match {
      case Some(subset) => subset.country.domain + ":" + subset.networkType.name + ":change-set"
      case None => "change-set"
    }
    changesFilter(Seq(prefix), year, month, day, stale)
  }

  private def changesFilter(keys: Seq[String], year: Option[String], month: Option[String], day: Option[String], stale: Boolean): ChangesFilter = {
    val yearPeriods = changesFilterPeriod(4, keys, stale)
    if (yearPeriods.isEmpty) {
      ChangesFilter(Seq())
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

  override def networkChanges(parameters: ChangesParameters, stale: Boolean): Seq[NetworkChange] = {
    ChangesView.networkChanges(changeDatabase, parameters, stale)
  }

  override def routeChanges(parameters: ChangesParameters, stale: Boolean): Seq[RouteChange] = {
    ChangesView.routeChanges(changeDatabase, parameters, stale)
  }

  def nodeChanges(parameters: ChangesParameters, stale: Boolean): Seq[NodeChange] = {
    ChangesView.nodeChanges(changeDatabase, parameters, stale)
  }

  override def allChangeSetIds(): Seq[String] = {

    val query = Query("_all_docs", classOf[ViewResult2])
      .startKey(""""change:"""")
      .endKey(""""change:zzz"""")
      .reduce(false)

    val result = changeDatabase.execute(query)
    result.rows.flatMap(_.id).map(id => id.split(":")(1)).distinct.sorted
  }

  private def docId(elementType: String, key: ChangeKey): String = {
    s"change:${key.changeSetId}:${key.replicationNumber}:$elementType:${key.elementId}"
  }
}
