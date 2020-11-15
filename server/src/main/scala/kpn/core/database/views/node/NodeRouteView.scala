package kpn.core.database.views.node

import kpn.api.common.NodeRoute
import kpn.api.custom.NetworkType
import kpn.core.database.Database
import kpn.core.database.doc.NodeRouteDoc
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object NodeRouteView extends View {

  private case class ViewResult(
    rows: Seq[ViewResultRow]
  )

  private case class ViewResultRow(
    doc: NodeRouteDoc
  )

  def query(database: Database, networkType: NetworkType, stale: Boolean): Seq[NodeRoute] = {

    val query = Query(NodeRouteDesign, NodeRouteView, classOf[ViewResult])
      .stale(stale)
      .keyStartsWith(networkType.name)
      .includeDocs(true)
      .reduce(false)

    val result = database.execute(query)
    result.rows.map(_.doc.nodeRoute)
  }

  override def reduce: Option[String] = Some("_sum")

}
