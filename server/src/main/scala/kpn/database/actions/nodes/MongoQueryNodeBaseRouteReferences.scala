package kpn.database.actions.nodes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.common.common.Reference
import kpn.core.util.Log
import kpn.database.actions.nodes.MongoQueryNodeBaseRouteReferences.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQueryNodeBaseRouteReferences {
  private val log = Log(classOf[MongoQueryNodeBaseRouteReferences])
}

class MongoQueryNodeBaseRouteReferences(database: Database) {

  def execute(nodeId: Long): Seq[Reference] = {
    log.infoElapsed {
      val pipeline = buildPipeline(nodeId)
      val refs = database.baseRoutes.aggregate(pipeline, classOf[Reference], log)
      (s"node route refs: ${refs.size}", refs)
    }
  }

  private def buildPipeline(nodeId: Long): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("summary.nodeNetwork", true),
          equal("networkNodeIds", nodeId),
        )
      ),
      unwind("$summary.routeTypes"),
      unwind("$summary.scopes"),
      project(
        fields(
          excludeId(),
          computed("routeType", "$summary.routeTypes"),
          computed("routeScope", "$summary.scopes"),
          computed("id", "$summary.id"),
          computed("name", "$summary.name")
        )
      ),
      sort(orderBy(ascending("routeType", "routeScope", "routeName")))
    )
  }
}
