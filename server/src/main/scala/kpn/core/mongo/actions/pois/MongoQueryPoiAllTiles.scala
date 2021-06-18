package kpn.core.mongo.actions.pois

import kpn.core.mongo.Database
import kpn.core.mongo.actions.pois.MongoQueryPoiAllTiles.log
import kpn.core.mongo.actions.pois.MongoQueryPoiAllTiles.pipeline
import kpn.core.mongo.util.MongoQuery
import kpn.core.mongo.util.StringId
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
