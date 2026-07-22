package kpn.database.actions.nodes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.in
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import kpn.core.doc.NodeRouteRef
import kpn.core.util.Log
import kpn.database.actions.nodes.MongoQueryNodeRouteReferences.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQueryNodeRouteReferences {
  private val log = Log(classOf[MongoQueryNodeRouteReferences])
}

class MongoQueryNodeRouteReferences(database: Database) {

  def execute(nodeIds: Seq[Long]): Seq[NodeRouteRef] = {
    log.debugElapsed {
      val pipeline = buildPipeline(nodeIds)
      val refs = database.routes.aggregate(pipeline, classOf[NodeRouteRef], log)
      (s"node route refs: ${refs.size}", refs)
    }
  }

  private def buildPipeline(nodeIds: Seq[Long]): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          in("base.networkNodeIds", nodeIds *),
        )
      ),
      unwind("$base.networkNodeIds"),
      filter(
        and(
          in("base.networkNodeIds", nodeIds *),
        )
      ),
      unwind("$base.routeTypes"),
      unwind("$base.scopes"),
      project(
        fields(
          excludeId(),
          computed("nodeId", "$base.networkNodeIds"),
          computed("routeId", "$_id"),
          computed("routeType", "$base.routeTypes"),
          computed("routeScope", "$base.scopes"),
          computed("routeName", "$base.name"),
          // TODO redesign - include role
        )
      )
    )
  }
}
