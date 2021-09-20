package kpn.core.mongo.actions.routes

import kpn.api.custom.NetworkType
import kpn.core.mongo.Database
import kpn.core.mongo.actions.routes.MongoQueryRouteTileInfo.log
import kpn.core.mongo.actions.routes.MongoQueryRouteTileInfo.projectRouteTileInfo
import kpn.core.mongo.doc.Label
import kpn.core.util.Log
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryRouteTileInfo {
  private val log = Log(classOf[MongoQueryRouteTileInfo])

  private def projectRouteTileInfo: Bson = {
    project(
      fields(
        include("_id"),
        computed("name", "$summary.name"),
        include("proposed"),
        include("lastSurvey"),
        include("tags"),
        include("facts"),
        computed("freePaths", "$analysis.map.freePaths"),
        computed("forwardPath", "$analysis.map.forwardPath"),
        computed("backwardPath", "$analysis.map.backwardPath"),
        computed("backwardPath", "$analysis.map.backwardPath"),
        computed("startTentaclePaths", "$analysis.map.startTentaclePaths"),
        computed("endTentaclePaths", "$analysis.map.endTentaclePaths"),
      )
    )
  }
}

class MongoQueryRouteTileInfo(database: Database) {

  def findByNetworkType(networkType: NetworkType): Seq[RouteTileInfo] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("labels", Label.active),
            equal("labels", Label.networkType(networkType))
          )
        ),
        projectRouteTileInfo
      )
      val routes = database.routes.aggregate[RouteTileInfo](pipeline, log)
      (s"${routes.size} routes", routes)
    }
  }

  def findById(routeId: Long): Option[RouteTileInfo] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("_id", routeId),
            equal("labels", Label.active)
          )
        ),
        projectRouteTileInfo
      )
      val routeOption = database.routes.optionAggregate[RouteTileInfo](pipeline, log)
      (s"${routeOption.size} route(s)", routeOption)
    }
  }
}
