package kpn.database.actions.pois

import kpn.database.actions.pois.MongoQueryPoiAllTiles.log
import kpn.database.actions.pois.MongoQueryPoiAllTiles.pipeline
import kpn.database.base.Database
import kpn.database.base.MongoQuery
import kpn.database.base.StringId
import kpn.core.util.Log

object MongoQueryPoiAllTiles extends MongoQuery {
  private val log = Log(classOf[MongoQueryPoiAllTiles])
  private val pipeline = readPipeline("pipeline")
}

class MongoQueryPoiAllTiles(database: Database) {
  def execute(): Seq[String] = {
    log.debugElapsed {
      val tileDocs = database.pois.aggregate[StringId](pipeline.stages, log)
      val tiles = tileDocs.map(_._id).sorted
      (s"tiles: ${tiles.size}", tiles)
    }
  }
}
