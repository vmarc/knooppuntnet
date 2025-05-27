package kpn.database.actions.nodes

import kpn.core.doc.NodeRouteRef
import kpn.core.util.Log
import kpn.database.actions.nodes.MongoQueryNodeRouteReferences.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields

object MongoQueryNodeRouteReferences {
  private val log = Log(classOf[MongoQueryNodeRouteReferences])
}

class MongoQueryNodeRouteReferences(database: Database) {

  def execute(nodeIds: Seq[Long]): Seq[NodeRouteRef] = {
    log.debugElapsed {
      val pipeline = buildPipeline(nodeIds)
      val refs = database.routes.aggregate[NodeRouteRef](pipeline, log)
      (s"node route refs: ${refs.size}", refs)
    }
  }

  private def buildPipeline(nodeIds: Seq[Long]): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("summary.nodeNetwork", true),
          in("nodeRefs", nodeIds: _*),
        )
      ),
      unwind("$nodeRefs"),
      filter(
        and(
          in("nodeRefs", nodeIds: _*),
        )
      ),
      unwind("$summary.routeTypes"),
      unwind("$summary.scopes"),
      project(
        fields(
          computed("nodeId", "$nodeRefs"),
          computed("routeId", "$summary.id"),
          computed("routeType", "$summary.routeTypes"),
          computed("routeScope", "$summary.scopes"),
          computed("routeName", "$summary.name"),
          // TODO redesign - include role
        )
      )
    )
  }
}
