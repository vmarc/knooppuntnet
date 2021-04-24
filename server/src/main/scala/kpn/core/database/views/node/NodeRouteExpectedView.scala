package kpn.core.database.views.node

import kpn.api.common.common.NodeRouteExpectedCount
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
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

  def queryScopedNetworkType(database: Database, scopedNetworkType: ScopedNetworkType, stale: Boolean): Seq[NodeRouteExpectedCount] = {

    val query = Query(NodeRouteDesign, NodeRouteExpectedView, classOf[ViewResult])
      .stale(stale)
      .includeDocs(true)
      .reduce(false)
      .keyStartsWith(scopedNetworkType.networkType.name, scopedNetworkType.networkScope.name)

    val result = database.execute(query)
    result.rows.map { row =>
      val locationNames = row.doc.node.location.toSeq.flatMap(_.names)
      val nodeName = row.doc.node.name(scopedNetworkType)
      val key = Fields(row.key)
      NodeRouteExpectedCount(
        key.long(2),
        if (nodeName.isEmpty) "no-name" else nodeName,
        locationNames,
        row.value.toInt
      )
    }.sortBy(n => (n.nodeName, n.nodeId))
  }

  def queryNetworkType(database: Database, networkType: NetworkType, stale: Boolean): Seq[NodeRouteExpectedCount] = {

    val query = Query(NodeRouteDesign, NodeRouteExpectedView, classOf[ViewResult])
      .stale(stale)
      .includeDocs(true)
      .reduce(false)
      .keyStartsWith(networkType.name)

    val result = database.execute(query)
    result.rows.map { row =>
      val locationNames = row.doc.node.location.toSeq.flatMap(_.names)
      val nodeName = row.doc.node.networkTypeName(networkType)
      val key = Fields(row.key)
      NodeRouteExpectedCount(
        key.long(2),
        if (nodeName.isEmpty) "no-name" else nodeName,
        locationNames,
        row.value.toInt
      )
    }.sortBy(n => (n.nodeName, n.nodeId))
  }

  override def reduce: Option[String] = None

}
