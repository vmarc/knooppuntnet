package kpn.database.actions.tiles

import kpn.database.actions.tiles.MongoQueryTiles.log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.core.doc.Label
import kpn.core.util.Log
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryTiles {
  private val log = Log(classOf[MongoQueryTiles])
}

class MongoQueryTiles(database: Database) {

  def nodeIds(tileName: String): Seq[Long] = {
    log.debugElapsed {
      val ids = database.nodes.aggregate[Id](pipeline(tileName))
      (s"tile '$tileName', node ids: ${ids.size}", ids.map(_._id))
    }
  }

  def routeIds(tileName: String): Seq[Long] = {
    log.debugElapsed {
      val ids = database.routes.aggregate[Id](pipeline(tileName))
      (s"tile '$tileName', route ids: ${ids.size}", ids.map(_._id))
    }
  }

  private def pipeline(tileName: String): Seq[Bson] = {
    Seq(
      filter(
        and(
          equal("labels", Label.active),
          equal("tiles", tileName)
        )
      ),
      project(
        fields(
          include("_id")
        )
      )
    )
  }
}
