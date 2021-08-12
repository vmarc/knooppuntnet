package kpn.core.mongo.actions.routes

import kpn.api.common.common.Reference
import kpn.core.mongo.Database
import kpn.core.mongo.util.Id
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryRouteNetworkReferences {
  private val log = Log(classOf[MongoQueryRouteNetworkReferences])
}

class MongoQueryRouteNetworkReferences(database: Database) {

  def execute(routeId: Long, log: Log = MongoQueryRouteNetworkReferences.log): Seq[Reference] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(equal("active", true)),
        unwind("$routes"),
        filter(equal("routes.id", routeId)),
        project(
          fields(
            excludeId(),
            computed("networkType", "$summary.networkType"),
            computed("networkScope", "$summary.networkScope"),
            computed("id", "$_id"),
            computed("name", "$summary.name"),
          )
        )
      )
      val references = database.networkInfos.aggregate[Reference](pipeline, log)
      (s"route network references: ${references.size}", references)
    }
  }

  def executeRouteIds(routeIds: Seq[Long], log: Log = MongoQueryRouteNetworkReferences.log): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(equal("active", true)),
        unwind("$relationMembers"),
        filter(in("relationMembers.relationId", routeIds: _*)),
        project(
          fields(
            include("_id")
          )
        )
      )
      val networkIds = database.networks.aggregate[Id](pipeline, log).map(_._id)
      (s"network references: ${networkIds.size}", networkIds)
    }
  }
}
