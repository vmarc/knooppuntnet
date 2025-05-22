package kpn.database.actions.routes

import kpn.core.doc.Label
import kpn.core.doc.ParentRouteData
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryParentRoutes.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.exclude
import org.mongodb.scala.model.Projections.fields

object MongoQueryParentRoutes {
  private val log = Log(classOf[MongoQueryParentRoutes])
}

class MongoQueryParentRoutes(database: Database) {

  def execute(routeId: Long): Seq[ParentRouteData] = {
    log.infoElapsed {
      val pipeline = buildPipeline(routeId)
      val routes = database.baseRoutes.aggregate[ParentRouteData](pipeline, log)
      (s"${routes.size} parent routes", routes)
    }
  }

  private def buildPipeline(routeId: Long): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("subRouteIds", routeId),
          equal("labels", Label.active),
        )
      ),
      project(
        fields(
          exclude("_id"),
          computed("routeId", "$_id"),
          computed("name", "$summary.name"),
        )
      )
    )
  }
}
