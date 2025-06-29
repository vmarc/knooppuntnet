package kpn.database.actions.routes

import kpn.core.doc.RouteRelation
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQuerySubRelationTree.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQuerySubRelationTree {
  private val log = Log(classOf[MongoQuerySubRelationTree])
}

class MongoQuerySubRelationTree(database: Database) {

  def execute(routeId: Long): Option[RouteRelation] = {
    log.infoElapsed {
      val pipeline = buildPipeline(routeId)
      val routes = database.baseRoutes.optionAggregate[RouteRelation](pipeline, log)
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
