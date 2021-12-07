package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.core.doc.Label
import kpn.core.doc.NodeDoc
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.springframework.stereotype.Component

@Component
class NetworkInfoNodeDocAnalyzer(database: Database) extends NetworkInfoAnalyzer {

  private val log = Log(classOf[NetworkInfoNodeDocAnalyzer])

  override def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    val routeNodeIds = context.routeDetails.flatMap(_.nodeRefs).distinct.sorted
    val networkNodeIds = context.networkDoc.nodeMembers.map(_.nodeId)
    val nodeIds = (networkNodeIds ++ routeNodeIds).distinct.sorted
    val nodeDocs = queryNodes(nodeIds)
    context.copy(
      nodeDocs = nodeDocs
    )
  }

  private def queryNodes(nodeIds: Seq[Long]): Seq[NodeDoc] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("labels", Label.active),
            in("_id", nodeIds: _*)
          ),
        )
      )
      val nodes = database.nodes.aggregate[NodeDoc](pipeline, log)
      (s"nodes: ${nodes.size}", nodes)
    }
  }
}
