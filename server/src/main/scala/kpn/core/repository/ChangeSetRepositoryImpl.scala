package kpn.core.repository

import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.json.JsonFormats
import kpn.core.db.json.JsonFormats._
import kpn.core.db.views.ChangesDesign
import kpn.core.db.views.ChangesView
import kpn.core.util.Log
import kpn.shared.ChangeSetSummary
import kpn.shared.ChangeSetSummaryDoc
import kpn.shared.ReplicationId
import kpn.shared.Subset
import kpn.shared.changes.ChangeSetData
import kpn.shared.changes.details.ChangeKey
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.details.NetworkChangeDoc
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.details.NodeChangeDoc
import kpn.shared.changes.details.RouteChange
import kpn.shared.changes.details.RouteChangeDoc
import kpn.shared.changes.filter.ChangesFilter
import kpn.shared.changes.filter.ChangesFilterPeriod
import kpn.shared.changes.filter.ChangesParameters
import spray.http.Uri
import spray.json.JsObject
import spray.json.JsString
import spray.json.JsValue

class ChangeSetRepositoryImpl(database: Database) extends ChangeSetRepository {

  private val log = Log(classOf[ChangeSetRepositoryImpl])

  override def saveChangeSetSummary(changeSetSummary: ChangeSetSummary): Unit = {
    val id = docId("summary", changeSetSummary.key)
    val existingDoc = database.optionGet(id).map(JsonFormats.changeSetSummaryDocFormat.read)
    existingDoc match {
      case None =>
        val doc = ChangeSetSummaryDoc(id, changeSetSummary)
        val value = JsonFormats.changeSetSummaryDocFormat.write(doc)
        database.save(id, value)
      case Some(doc) =>
        if (doc.changeSetSummary != changeSetSummary) {
          val newDoc = ChangeSetSummaryDoc(id, changeSetSummary, doc._rev)
          val value = JsonFormats.changeSetSummaryDocFormat.write(newDoc)
          database.save(id, value)
        }
    }
  }

  override def saveNetworkChange(networkChange: NetworkChange): Unit = {
    val id = docId("network", networkChange.key)
    val existingDoc = database.optionGet(id).map(JsonFormats.networkChangeDocFormat.read)
    existingDoc match {
      case None =>
        val doc = NetworkChangeDoc(id, networkChange)
        val value = JsonFormats.networkChangeDocFormat.write(doc)
        database.save(id, value)
      case Some(doc) =>
        if (doc.networkChange != networkChange) {
          val newDoc = NetworkChangeDoc(id, networkChange, doc._rev)
          val value = JsonFormats.networkChangeDocFormat.write(newDoc)
          database.save(id, value)
        }
    }
  }

  override def saveRouteChange(routeChange: RouteChange): Unit = {
    val id = docId("route", routeChange.key)
    val existingDoc = database.optionGet(id).map(JsonFormats.routeChangeDocFormat.read)
    existingDoc match {
      case None =>
        val doc = RouteChangeDoc(id, routeChange)
        val value = JsonFormats.routeChangeDocFormat.write(doc)
        database.save(id, value)
      case Some(doc) =>
        if (doc.routeChange != routeChange) {
          val newDoc = RouteChangeDoc(id, routeChange, doc._rev)
          val value = JsonFormats.routeChangeDocFormat.write(newDoc)
          database.save(id, value)
        }
    }
  }

  override def saveNodeChange(nodeChange: NodeChange): Unit = {
    val id = docId("node", nodeChange.key)
    val existingDoc = database.optionGet(id).map(JsonFormats.nodeChangeDocFormat.read)
    existingDoc match {
      case None =>
        val doc = NodeChangeDoc(id, nodeChange)
        val value = JsonFormats.nodeChangeDocFormat.write(doc)
        database.save(id, value)
      case Some(doc) =>
        if (doc.nodeChange != nodeChange) {
          val newDoc = NodeChangeDoc(id, nodeChange, doc._rev)
          val value = JsonFormats.nodeChangeDocFormat.write(newDoc)
          database.save(id, value)
        }
    }
  }

  private def docId(elementType: String, key: ChangeKey): String = {
    s"change:${key.changeSetId}:${key.replicationNumber}:$elementType:${key.elementId}"
  }

  override def changeSet(changeSetId: Long, replicationId: Option[ReplicationId], stale: Boolean): Seq[ChangeSetData] = {

    val uriHead = Uri("_all_docs")
    val repl = if (replicationId.isDefined) ":" + replicationId.get.number else ""
    val startKey = {
      s""""change:$changeSetId$repl:""""
    }
    val endKey = {
      s""""change:$changeSetId$repl:zzz""""
    }

    val parameters = Map(
      "startkey" -> startKey,
      "endkey" -> endKey,
      "include_docs" -> "true",
      "reduce" -> "false"
    )

    val uri = uriHead.withQuery(withStale(parameters, stale))

    val rows = database.getRows(uri.toString(), Couch.defaultTimeout)

    val rowObjects = rows.map(_.asJsObject)

    if (rowObjects.isEmpty) {
      Seq()
    }
    else {

      val groups = rowObjects.groupBy { rowObject =>
        rowObject.getFields("key").head match {
          case JsString(string) =>

            val splitted = string.split(":").toSeq
            splitted match {
              case Seq("change", changeSetId1, replicationId1, elementType, elementId1) => replicationId1
              case _ => ""
            }

          case o => ""
        }
      }

      val replicationNumbers = groups.keys.toSeq.sorted

      replicationNumbers.map { replicationNumber =>

        val groupObjects = groups(replicationNumber)

        val objects = groupObjects.map(go => go.getFields("doc").head.asJsObject)

        def filterByType(typeString: String): Seq[JsObject] = {
          objects.filter(_.getFields(typeString).nonEmpty).flatMap(_.getFields(typeString)).map(_.asJsObject)
        }

        val changeSetSummaries = filterByType("changeSetSummary").map(_.convertTo[ChangeSetSummary])
        val networkChanges = filterByType("networkChange").map(_.convertTo[NetworkChange])
        val routeChanges = filterByType("routeChange").map(_.convertTo[RouteChange])
        val nodeChanges = filterByType("nodeChange").map(_.convertTo[NodeChange])

        assert(changeSetSummaries.size == 1, "Expecting single change set summary object, but found " + changeSetSummaries.size)

        ChangeSetData(changeSetSummaries.head, networkChanges, routeChanges, nodeChanges)
      }
    }
  }

  override def changes(parameters: ChangesParameters, stale: Boolean): Seq[ChangeSetSummary] = {
    readChanges("changeSetSummary", parameters, stale).map(_.convertTo[ChangeSetSummary])
  }

  override def nodeChangesFilter(nodeId: Long, year: Option[String], month: Option[String], day: Option[String], stale: Boolean): ChangesFilter = {
    changesFilter(Seq("node", nodeId.toString), year, month, day, stale)
  }

  override def nodeChangesCount(nodeId: Long, stale: Boolean = true): Int = {
    changesCount("node", nodeId.toString, stale)
  }

  override def routeChangesFilter(routeId: Long, year: Option[String], month: Option[String], day: Option[String], stale: Boolean): ChangesFilter = {
    changesFilter(Seq("route", routeId.toString), year, month, day, stale)
  }

  override def routeChangesCount(routeId: Long, stale: Boolean = true): Int = {
    changesCount("route", routeId.toString, stale)
  }

  override def networkChangesFilter(networkId: Long, year: Option[String], month: Option[String], day: Option[String], stale: Boolean): ChangesFilter = {
    changesFilter(Seq("network", networkId.toString), year, month, day, stale)
  }

  override def networkChangesCount(networkId: Long, stale: Boolean = true): Int = {
    changesCount("network", networkId.toString, stale)
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

    val startKey = keyString2(keys :+ suffix(suffixLength, "9"))
    val endKey = keyString2(keys :+ suffix(suffixLength, "0"))

    val queryParameters = Map(
      "startkey" -> startKey,
      "endkey" -> endKey,
      "reduce" -> "true",
      "descending" -> "true",
      "group_level" -> (keys.size + 1).toString
    )

    val rows = query("changesFilterPeriod", queryParameters, stale)
    rows.map(ChangesView.convertToPeriod)
  }

  private def changesCount(elementType: String, id: String, stale: Boolean): Int = {
    val rows = database.groupQuery(2, ChangesDesign, ChangesView, stale = stale)(elementType, id)
    val totals = rows.map(ChangesView.extractTotal)
    if (totals.size == 1) {
      totals.head
    }
    else {
      0
    }
  }

  private def suffix(size: Int, character: String): String = Seq.fill(size)(character).mkString

  override def networkChanges(parameters: ChangesParameters, stale: Boolean): Seq[NetworkChange] = {
    readChanges("networkChange", parameters, stale).map(_.convertTo[NetworkChange])
  }

  override def routeChanges(parameters: ChangesParameters, stale: Boolean): Seq[RouteChange] = {
    readChanges("routeChange", parameters, stale).map(_.convertTo[RouteChange])
  }

  def nodeChanges(parameters: ChangesParameters, stale: Boolean): Seq[NodeChange] = {
    readChanges("nodeChange", parameters, stale).map(_.convertTo[NodeChange])
  }

  override def allChangeSetIds(): Seq[String] = {

    val uriHead = Uri("_all_docs")
    val startKey = {
      """"change:""""
    }
    val endKey = {
      """"change:zzz""""
    }

    val parameters = Map(
      "startkey" -> startKey,
      "endkey" -> endKey,
      "include_docs" -> "false",
      "reduce" -> "false"
    )

    val uri = uriHead.withQuery(parameters)

    val rows = database.getRows(uri.toString(), Couch.defaultTimeout)

    val rowObjects = rows.map(_.asJsObject)

    if (rowObjects.isEmpty) {
      Seq()
    }
    else {
      rows.map { row =>
        val id = row.asJsObject.getFields("id").head.toString
        id.split(":")(1)
      }
      }.distinct.sorted
  }

  private def readChanges(elementType: String, parameters: ChangesParameters, stale: Boolean): Seq[JsObject] = {
    val queryParameters = QueryParameters.from(parameters)
    val rows = query(elementType, queryParameters, stale)
    rows.map { row =>
      val doc = row.asJsObject.getFields("doc").head
      doc.asJsObject.getFields(elementType).head.asJsObject
    }
  }

  private def query(title: String, parameters: Map[String, String], stale: Boolean): Seq[JsValue] = {
    val uriHead = Uri("_design/ChangesDesign/_view/ChangesView")
    val queryParameters = withStale(parameters, stale)
    val uri = uriHead.withQuery(queryParameters)
    log.debugElapsed {
      val rows = database.getRows(uri.toString(), Couch.defaultTimeout)
      (s"[$title] $uri -> ${rows.size} rows", rows)
    }
  }

  private def withStale(parameters: Map[String, String], stale: Boolean): Map[String, String] = {
    if (stale) {
      parameters + ("stale" -> "ok")
    }
    else {
      parameters
    }
  }

  private def keyString(values: String*): String = values.mkString("[\"", "\", \"", "\"]")

  private def keyString2(values: Seq[String]): String = values.mkString("[\"", "\", \"", "\"]")

}
