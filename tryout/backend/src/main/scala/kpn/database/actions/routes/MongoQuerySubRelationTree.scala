package kpn.database.actions.routes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.core.doc.RouteRelation
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQuerySubRelationTree.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQuerySubRelationTree {
  private val log = Log(classOf[MongoQuerySubRelationTree])
}

class MongoQuerySubRelationTree(database: Database) {

  def execute(routeId: Long): Option[RouteRelation] = {
    log.infoElapsed {
      val pipeline = buildPipeline(routeId)
      val routes = database.baseRoutes.optionAggregate(pipeline, classOf[RouteRelation], log)
      (s"${routes.size} routes", routes)
    }
  }

  private def buildPipeline(routeId: Long): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("_id", routeId),
          equal("active", true),
        )
      ),
      project(
        fields(
          excludeId(),
          include("subRelationTree"),
        )
      )
    )
  }
}
