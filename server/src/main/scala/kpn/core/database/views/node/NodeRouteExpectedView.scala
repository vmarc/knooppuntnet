package kpn.core.database.views.node

import kpn.api.common.common.NodeRouteExpectedCount
import kpn.api.custom.NetworkType
import kpn.core.database.Database
import kpn.core.database.doc.NodeDoc
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object NodeRouteExpectedView extends View {

  private case class ViewResult(
    rows: Seq[ViewResultRow]
  )

  private case class ViewResultRow(
    key: Seq[String],
    value: Long,
    doc: NodeDoc
  )

  def query(database: Database, networkType: NetworkType, stale: Boolean): Seq[NodeRouteExpectedCount] = {

    val query = Query(NodeRouteDesign, NodeRouteExpectedView, classOf[ViewResult])
      .stale(stale)
      .includeDocs(true)
      .reduce(false)
      .keyStartsWith(networkType.name)

    val result = database.execute(query)
    result.rows.map { row =>
      val locationNames = row.doc.node.location.toSeq.flatMap(_.names)
      val nodeName = row.doc.node.name(networkType)
      val key = Fields(row.key)
      NodeRouteExpectedCount(
        key.long(1),
        if (nodeName.length == 0) "no-name" else nodeName,
        locationNames,
        row.value.toInt
      )
    }
  }

  override def reduce: Option[String] = None

}
