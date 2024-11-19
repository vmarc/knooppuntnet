package kpn.database.actions.routes

import kpn.api.custom.NetworkType
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryRouteTileInfo.log
import kpn.database.actions.routes.MongoQueryRouteTileInfo.projectRouteTileInfo
import kpn.database.base.Database
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

  private def projectRouteTileInfo: Bson = {
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

class MongoQueryRouteTileInfo(database: Database) {

  def findByNetworkType(networkType: NetworkType, nodeNetwork: Boolean): Seq[RouteTileInfo] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("labels", Label.active),
            equal("labels", Label.networkType(networkType)),
            exists("summary.countries.0"), // TODO redesign tiles - this condition was added temporarily to avoid problems with lat/lon calculations
            equal("summary.nodeNetwork", nodeNetwork)
          )
        ),
        projectRouteTileInfo
      )
      val routes = database.routeDetails.aggregate[RouteTileInfo](pipeline, log)
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
