package kpn.database.actions.nodes

import kpn.core.doc.Label
import kpn.core.doc.NodeRouteRef
import kpn.core.util.Log
import kpn.database.actions.nodes.MongoQueryNodeRouteReferences.log
import kpn.database.base.Database
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
      val pipeline = Seq(
        filter(
          and(
            equal("labels", Label.active),
            in("nodeRefs", nodeIds: _*),
          )
        ),
        unwind("$nodeRefs"),
        filter(
          and(
            in("nodeRefs", nodeIds: _*),
          )
        ),
        project(
          fields(
            computed("nodeId", "$nodeRefs"),
            computed("routeId", "$summary.id"),
            computed("networkType", "$summary.networkType"),
            computed("networkScope", "$summary.networkScope"),
            computed("routeName", "$summary.name"),
          )
        )
      )
      val refs = database.routes.aggregate[NodeRouteRef](pipeline, log)
      (s"node route refs: ${refs.size}", refs)
    }
  }
}
