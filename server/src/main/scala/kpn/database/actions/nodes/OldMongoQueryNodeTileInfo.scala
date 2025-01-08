package kpn.database.actions.nodes

import kpn.api.common.RouteType
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.actions.nodes.OldMongoQueryNodeTileInfo.log
import kpn.database.actions.nodes.OldMongoQueryNodeTileInfo.projectNodeTileInfo
import kpn.database.base.Database
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object OldMongoQueryNodeTileInfo {
  private val log = Log(classOf[OldMongoQueryNodeTileInfo])

  private val projectNodeTileInfo: Bson = {
    project(
      fields(
        include("_id"),
        include("names"),
        include("latitude"),
        include("longitude"),
        include("lastSurvey"),
        include("tags"),
        include("facts")
      )
    )
  }
}

class OldMongoQueryNodeTileInfo(database: Database) {

  def findByrouteType(routeType: RouteType): Seq[NodeTileInfo] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("labels", Label.active),
            equal("labels", Label.routeType(routeType))
          )
        ),
        projectNodeTileInfo
      )
      val nodes = database.nodes.aggregate[NodeTileInfo](pipeline, log)
      (s"${nodes.size} nodes", nodes)
    }
  }

  def findById(nodeId: Long): Option[NodeTileInfo] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("_id", nodeId),
            equal("labels", Label.active)
          )
        ),
        projectNodeTileInfo
      )
      val nodeOption = database.nodes.optionAggregate[NodeTileInfo](pipeline, log)
      (s"${nodeOption.size} node(s)", nodeOption)
    }
  }
}
