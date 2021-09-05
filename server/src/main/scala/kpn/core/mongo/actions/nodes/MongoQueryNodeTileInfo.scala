package kpn.core.mongo.actions.nodes

import kpn.api.custom.NetworkType
import kpn.core.mongo.Database
import kpn.core.mongo.actions.nodes.MongoQueryNodeTileInfo.log
import kpn.core.mongo.actions.nodes.MongoQueryNodeTileInfo.projectNodeTileInfo
import kpn.core.util.Log
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryNodeTileInfo {
  private val log = Log(classOf[MongoQueryNodeTileInfo])

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

class MongoQueryNodeTileInfo(database: Database) {

  def findByNetworkType(networkType: NetworkType): Seq[NodeTileInfo] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("labels", "active"),
            equal("labels", s"network-type-${networkType.name}")
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
            equal("labels", "active")
          )
        ),
        projectNodeTileInfo
      )
      val nodeOption = database.nodes.optionAggregate[NodeTileInfo](pipeline, log)
      (s"${nodeOption.size} node(s)", nodeOption)
    }
  }
}
