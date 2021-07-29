package kpn.core.mongo.actions.nodes

import kpn.core.mongo.Database
import kpn.core.mongo.NodeRouteRef
import kpn.core.mongo.actions.nodes.MongoQueryNodeRouteReferences.log
import kpn.core.util.Log
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

  // get the references directly from the route collection instead of a separate collection (see execute2)
  def execute(nodeIds: Seq[Long]): Seq[NodeRouteRef] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("active", true), // because that is in the existing index (should use labels?)
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
            // computed("networkScope", "$summary.networkScope"),
            computed("routeName", "$summary.name"),
          )
        )
      )
      val refs = database.routes.aggregate[NodeRouteRef](pipeline, log)
      (s"node route refs: ${refs.size}", refs)
    }
  }

  def execute2(nodeIds: Seq[Long], log: Log = MongoQueryNodeRouteReferences.log): Seq[NodeRouteRef] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          in("_id", nodeIds: _*),
        )
      )
      val refs = database.nodeRouteRefs.aggregate[NodeRouteRef](pipeline, log)
      (s"node route refs: ${refs.size}", refs)
    }
  }
}
