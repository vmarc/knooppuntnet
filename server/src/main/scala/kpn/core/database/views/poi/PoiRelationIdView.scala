package kpn.core.database.views.poi

import kpn.core.database.Database
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object PoiRelationIdView extends View {

  private case class ViewResultRow(key: String)

  private case class ViewResult(rows: Seq[ViewResultRow])

  def query(database: Database, stale: Boolean = true): Seq[Long] = {

    val query = Query(PoiDesign, PoiRelationIdView, classOf[ViewResult])
      .reduce(true) // using 'reduce' to minimize response size
      .groupLevel(1)
      .stale(stale)

    val result = database.execute(query)
    result.rows.map(_.key.toLong)
  }

  override val map: String =
    """
      |function(doc) {
      |  var poi = doc.poi;
      |  if(poi && poi.elementType == "relation") {
      |    emit(poi.elementId, 1);
      |  }
      |}
    """.stripMargin

  override val reduce: Option[String] = Some("_sum")

}
