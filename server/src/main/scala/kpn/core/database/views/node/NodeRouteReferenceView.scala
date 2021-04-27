package kpn.core.database.views.node

import kpn.api.common.common.NodeRouteCount
import kpn.api.common.common.NodeRouteRefs
import kpn.api.common.common.Ref
import kpn.api.custom.ScopedNetworkType
import kpn.core.database.Database
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

import java.io.StringWriter

object NodeRouteReferenceView extends View {

  private case class ViewResult(
    rows: Seq[ViewResultRow]
  )

  private case class ViewResultRow(
    key: Seq[String]
  )

  private case class CountResult(
    rows: Seq[CountResultRow]
  )

  private case class CountResultRow(
    key: Seq[String],
    value: Long
  )

  private case class MultiViewResult(
    results: Seq[MultiViewResultRows]
  )

  private case class MultiViewResultRows(
    rows: Seq[MultiViewResultRow]
  )

  private case class MultiViewResultRow(
    key: Seq[String],
    value: Long
  )

  def query(database: Database, scopedNetworkType: ScopedNetworkType, nodeId: Long, stale: Boolean): Seq[Ref] = {

    val query = Query(NodeRouteDesign, NodeRouteReferenceView, classOf[ViewResult])
      .stale(stale)
      .reduce(false)
      .keyStartsWith(scopedNetworkType.networkType.name, scopedNetworkType.networkScope.name, nodeId)

    val result = database.execute(query)
    result.rows.map { row =>
      val key = Fields(row.key)
      Ref(
        key.long(4),
        key.string(3)
      )
    }
  }

  def queryNodeIds(database: Database, scopedNetworkType: ScopedNetworkType, nodeIds: Seq[Long], stale: Boolean): Seq[NodeRouteRefs] = {

    val sw = new StringWriter
    sw.append("""{ "queries": [ """)
    sw.append(nodeIds.map(nodeId => s"""{ "startkey": [ "${scopedNetworkType.networkType.name}", "${scopedNetworkType.networkScope.name}", $nodeId ], "endkey": [ "${scopedNetworkType.networkType.name}", "${scopedNetworkType.networkScope.name}", $nodeId, {} ] } """).mkString(","))
    sw.append("""]} """)
    val body = sw.toString

    val query = Query(NodeRouteDesign, NodeRouteReferenceView, classOf[MultiViewResult])
      .stale(stale)
      .reduce(false)

    val result = database.post(query, body, classOf[MultiViewResult])

    result.results.flatMap { res =>
      if (res.rows.nonEmpty) {
        val nodeId = Fields(res.rows.head.key).long(2)
        val refs = res.rows.map { row =>
          val key = Fields(row.key)
          Ref(
            key.long(4),
            key.string(3)
          )
        }
        Some(NodeRouteRefs(nodeId, refs))
      }
      else {
        None
      }
    }
  }

  def queryCount(database: Database, scopedNetworkType: ScopedNetworkType, stale: Boolean): Seq[NodeRouteCount] = {

    val query = Query(NodeRouteDesign, NodeRouteReferenceView, classOf[CountResult])
      .stale(stale)
      .keyStartsWith(scopedNetworkType.networkType.name, scopedNetworkType.networkScope.name)
      .reduce(true)
      .groupLevel(3)

    val result = database.execute(query)
    result.rows.map { row =>
      val key = Fields(row.key)
      NodeRouteCount(
        key.long(2),
        row.value.toInt
      )
    }
  }

  override def reduce: Option[String] = Some("_sum")

}
