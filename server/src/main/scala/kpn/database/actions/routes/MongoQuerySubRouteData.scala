package kpn.database.actions.routes

import kpn.core.doc.SubRouteData
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQuerySubRouteData.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQuerySubRouteData {
  private val log = Log(classOf[MongoQuerySubRouteData])
}

class MongoQuerySubRouteData(database: Database) {

  def execute(routeId: Long): Option[SubRouteData] = {
    log.infoElapsed {
      val pipeline = buildPipeline(routeId)
      val routes = database.baseRoutes.optionAggregate[SubRouteData](pipeline, log)
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
          include("_id"),
          computed("name", "$summary.name"),
          include("members"),
          computed("distance", "$summary.meters"),
          include("segments"),
        )
      )
    )
  }
}
