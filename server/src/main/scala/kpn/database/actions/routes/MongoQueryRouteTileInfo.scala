package kpn.database.actions.routes

import kpn.api.common.RouteType
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryRouteTileInfo.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.exists
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryRouteTileInfo {
  private val log = Log(classOf[MongoQueryRouteTileInfo])
}

class MongoQueryRouteTileInfo(database: Database) {

  def findByRouteType(routeType: RouteType, nodeNetwork: Boolean): Seq[RouteTileInfo] = {
    log.debugElapsed {
      val pipeline = buildFindByRouteTypePipeline(routeType, nodeNetwork)
      val routes = database.baseRoutes.aggregate[RouteTileInfo](pipeline, log)
      (s"${routes.size} routes", routes)
    }
  }

  def findById(routeId: Long): Option[RouteTileInfo] = {
    log.debugElapsed {
      val pipeline = buildFindByIdPipeline(routeId)
      val routeOption = database.routes.optionAggregate[RouteTileInfo](pipeline, log)
      (s"${routeOption.size} route(s)", routeOption)
    }
  }

  private def buildFindByRouteTypePipeline(routeType: RouteType, nodeNetwork: Boolean) = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("labels", Label.routeType(routeType)),
          exists("summary.countries.0"), // TODO redesign tiles - this condition was added temporarily to avoid problems with lat/lon calculations
          equal("summary.nodeNetwork", nodeNetwork)
        )
      ),
      projectRouteTileInfo()
    )
  }

  private def buildFindByIdPipeline(routeId: Long): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("_id", routeId),
          equal("active", true),
        )
      ),
      projectRouteTileInfo()
    )
  }

  private def projectRouteTileInfo(): Bson = {
    project(
      fields(
        include("_id"),
        computed("name", "$summary.name"),
        include("nodeNetwork"),
        computed("scopes", "$summary.scopes"),
        include("proposed"),
        include("lastSurvey"),
        computed("tags", "$summary.tags"),
        include("segments"),
        include("segmentElements"),
        include("facts"),
        include("paths"),
        include("tiles"),
      )
    )
  }
}
