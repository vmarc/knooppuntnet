package kpn.database.actions.pois

import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.unwind
import kpn.core.util.Log
import kpn.database.actions.pois.MongoQueryPoiAllTiles.log
import kpn.database.base.Database
import kpn.database.base.StringId

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

      val tileDocs = database.pois.aggregate(pipeline, classOf[StringId], log)
      val tiles = tileDocs.map(_._id).sorted
      (s"tiles: ${tiles.size}", tiles)
    }
  }
}
