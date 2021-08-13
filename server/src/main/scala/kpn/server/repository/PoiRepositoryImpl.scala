package kpn.server.repository

import kpn.api.common.Poi
import kpn.core.mongo.Database
import kpn.core.mongo.actions.pois.MongoQueryPoiAllTiles
import kpn.core.mongo.actions.pois.MongoQueryPoiElementIds
import kpn.core.mongo.actions.pois.MongoQueryTilePois
import kpn.core.poi.PoiInfo
import kpn.core.util.Log
import kpn.server.analyzer.engine.poi.PoiRef
import org.springframework.stereotype.Component

@Component
class PoiRepositoryImpl(database: Database) extends PoiRepository {

  private val log = Log(classOf[PoiRepositoryImpl])

  override def save(poi: Poi): Unit = {
    database.pois.save(poi)
  }

  override def nodeIds(stale: Boolean = true): Seq[Long] = {
    new MongoQueryPoiElementIds(database).execute("node")
  }

  override def wayIds(stale: Boolean = true): Seq[Long] = {
    new MongoQueryPoiElementIds(database).execute("way")
  }

  override def relationIds(stale: Boolean = true): Seq[Long] = {
    new MongoQueryPoiElementIds(database).execute("relation")
  }

  override def get(poiRef: PoiRef): Option[Poi] = {
    database.pois.findByStringId(poiRef.toId, log)
  }

  override def delete(poiRef: PoiRef): Unit = {
    database.pois.deleteByStringId(poiRef.toId, log)
  }

  override def allTiles(stale: Boolean): Seq[String] = {
    new MongoQueryPoiAllTiles(database).execute()
  }

  override def tilePoiInfos(tileName: String, stale: Boolean): Seq[PoiInfo] = {
    new MongoQueryTilePois(database).execute(tileName)
  }
}
