package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.data.MemberType
import kpn.core.doc.Label
import kpn.core.doc.NodeDoc
import kpn.core.util.Log
import kpn.database.base.Database
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.springframework.stereotype.Component

@Component
class NetworkNodeDocAnalyzer(database: Database) extends NetworkAnalyzer {

  private val log = Log(classOf[NetworkNodeDocAnalyzer])

  override def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext = {
    val nodeDocs = if (context.network.active) {
      val routeNodeIds = context.routeDetails.flatMap(_.nodeRefs).distinct.sorted
      val networkNodeIds = context.network.members.filter(_.memberType == MemberType.Node).map(_.ref)
      val nodeIds = (networkNodeIds ++ routeNodeIds).distinct.sorted
      queryNodes(nodeIds)
    }
    else {
      Seq.empty
    }
    context.copy(
      _nodeDocs = Some(nodeDocs)
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
