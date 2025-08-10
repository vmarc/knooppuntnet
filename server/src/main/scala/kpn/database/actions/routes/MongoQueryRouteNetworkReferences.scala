package kpn.database.actions.routes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.in
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.api.common.common.Reference
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQueryRouteNetworkReferences {
  private val log = Log(classOf[MongoQueryRouteNetworkReferences])
}

class MongoQueryRouteNetworkReferences(database: Database) {

  def execute(routeId: Long, log: Log = MongoQueryRouteNetworkReferences.log): Seq[Reference] = {
    log.infoElapsed {
      val pipeline = buildPipeline(routeId)
      val references = database.baseNetworks.aggregate(pipeline, classOf[Reference], log)
      (s"route network references: ${references.size}", references)
    }
  }

  private def buildPipeline(routeId: Long): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("relationIds", routeId),
        )
      ),
      unwind("$members"),
      filter(
        and(
          equal("members.memberType", "relation"),
          equal("members.ref", routeId),
        )
      ),
      project(
        fields(
          excludeId(),
          include("routeType"),
          include("routeScope"),
          computed("id", "$_id"),
          include("name"),
          computed("role", "$members.role"),
        )
      )
    )
  }

  private def buildIdPipeline(routeIds: Seq[Long]): MongoPipeline = {
    Seq(
      filter(equal("active", true)),
      unwind("$relationMembers"),
      filter(in("relationMembers.relationId", routeIds *)),
      project(
        fields(
          include("_id")
        )
      )
    )
  }
}
