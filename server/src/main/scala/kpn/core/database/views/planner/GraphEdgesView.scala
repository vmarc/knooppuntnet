package kpn.core.database.views.planner

import kpn.api.common.common.TrackPathKey
import kpn.api.custom.NetworkType
import kpn.core.database.Database
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View
import kpn.core.planner.graph.GraphEdge

/**
 * View to derive graph edges from routes (to be used for routing).
 */
object GraphEdgesView extends View {

  private case class ViewResultRow(key: Seq[String], value: Seq[Long])

  private case class ViewResult(rows: Seq[ViewResultRow])

  def query(database: Database, networkType: NetworkType, stale: Boolean = true): Seq[GraphEdge] = {

    val query = Query(PlannerDesign, GraphEdgesView, classOf[ViewResult])
      .keyStartsWith(networkType.name)
      .reduce(false)
      .stale(stale)

    val result = database.execute(query)

    result.rows.map { row =>
      val key = Fields(row.key)
      GraphEdge(
        sourceNodeId = row.value.head,
        sinkNodeId = row.value(1),
        meters = row.value(2),
        pathKey = TrackPathKey(
          routeId = key.long(1),
          pathType = key.string(2),
          pathIndex = key.long(3)
        )
      )
    }
  }

  override val reduce: Option[String] = Some("_count")
}
