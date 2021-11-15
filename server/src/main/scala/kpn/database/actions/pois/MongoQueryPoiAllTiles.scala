package kpn.database.actions.pois

import kpn.core.util.Log
import kpn.database.actions.pois.MongoQueryPoiAllTiles.log
import kpn.database.base.Database
import kpn.database.base.StringId
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.unwind

object MongoQueryPoiAllTiles {
  private val log = Log(classOf[MongoQueryPoiAllTiles])
}

class MongoQueryPoiAllTiles(database: Database) {
  def execute(): Seq[String] = {
    log.debugElapsed {
      val pipeline = Seq(
        unwind("$tiles"),
        group("$tiles")
      )

      val tileDocs = database.pois.aggregate[StringId](pipeline, log)
      val tiles = tileDocs.map(_._id).sorted
      (s"tiles: ${tiles.size}", tiles)
    }
  }
}
