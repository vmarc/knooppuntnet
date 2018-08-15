package kpn.core.repository

import kpn.core.db.couch.Database
import kpn.shared.NetworkType

import scalax.collection.edge.WLUnDiEdge

class GraphRepositoryImpl(database: Database) extends GraphRepository {

  //  private val view = repository.design(AnalyzerDesign.name).view(GraphEdges.name)

  override def edges(networkType: NetworkType): Seq[WLUnDiEdge[Long]] = {
    (0 to 9).flatMap(prefix => edges(networkType, prefix))
  }

  private def edges(networkType: NetworkType, prefix: Int): Seq[WLUnDiEdge[Long]] = {
    //    val startkey = GraphEdges.Key(networkType.name, prefix, Long.MinValue)
    //    val endkey = GraphEdges.Key(networkType.name, prefix, Long.MaxValue)
    //    val result = view.query[GraphEdges.Key, GraphEdges.Value, Nothing](startkey=Some(startkey), endkey=Some(endkey))
    //    result.rows.map { row =>
    //      WLUnDiEdge.newEdge[String, String]((
    //        row.value.startNodeId.toString,
    //        row.value.endNodeId.toString),
    //        row.value.meters,
    //        row.key.routeId.toString
    //      )
    //    }

    Seq()
  }
}
