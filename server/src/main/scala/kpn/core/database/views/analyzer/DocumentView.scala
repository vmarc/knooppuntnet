package kpn.core.database.views.analyzer

import kpn.core.database.Database
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object DocumentView extends View {

  case class ViewResultRow(id: String)

  case class ViewResult(rows: Seq[ViewResultRow])

  case class ViewResultCountRow(key: String, value: Long)

  case class ViewResultCount(rows: Seq[ViewResultCountRow])

  case class DocumentCount(prefix: String, count: Long)

  def allNodeIds(database: Database): Seq[Long] = {
    allIds(database: Database, "node")
  }

  def allRouteIds(database: Database): Seq[Long] = {
    allIds(database: Database, "route")
  }

  private def allIds(database: Database, elementType: String): Seq[Long] = {
    val query = Query(AnalyzerDesign, DocumentView, classOf[ViewResult])
      .startKey(s""""$elementType"""")
      .endKey(s""""$elementType:a"""")
      .reduce(false)
      .stale(false)
    val result = database.execute(query)
    result.rows.map { row =>
      row.id.drop(s"$elementType:".length).toLong
    }
  }

  def counts(database: Database): Seq[DocumentCount] = {
    val query = Query(AnalyzerDesign, DocumentView, classOf[ViewResultCount])
      .groupLevel(1)
      .stale(false)
    val result = database.execute(query)
    result.rows.map { row =>
      DocumentCount(row.key, row.value)
    }
  }

  override val map: String =
    """
      |function(doc) {
      |	 var prefix = doc._id.substring(0, doc._id.indexOf(':'));
      |  emit(prefix, 1);
      |}
    """.stripMargin

  override val reduce: Option[String] = Some("_sum")
}
