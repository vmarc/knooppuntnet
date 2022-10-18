package kpn.server.repository

import kpn.api.common.Poi
import kpn.api.common.poi.LocationPoiInfo
import kpn.api.common.poi.LocationPoiParameters
import kpn.api.custom.LocationKey
import kpn.core.poi.PoiInfo
import kpn.core.util.Log
import kpn.database.actions.pois.MongoQueryLocationPoiCount
import kpn.database.actions.pois.MongoQueryLocationPois
import kpn.database.actions.pois.MongoQueryPoiAllTiles
import kpn.database.actions.pois.MongoQueryPoiElementIds
import kpn.database.actions.pois.MongoQueryTilePois
import kpn.database.base.Database
import kpn.server.analyzer.engine.poi.PoiRef
import org.springframework.stereotype.Component

@Component
class PoiRepositoryImpl(database: Database) extends PoiRepository {

  private val log = Log(classOf[PoiRepositoryImpl])

  override def save(poi: Poi): Unit = {
    database.pois.save(poi)
  }

  override def nodeIds(): Seq[Long] = {
    new MongoQueryPoiElementIds(database).execute("node")
  }

  override def wayIds(): Seq[Long] = {
    new MongoQueryPoiElementIds(database).execute("way")
  }

  override def relationIds(): Seq[Long] = {
    new MongoQueryPoiElementIds(database).execute("relation")
  }

  override def get(poiRef: PoiRef): Option[Poi] = {
    database.pois.findByStringId(poiRef.toId, log)
  }

  override def delete(poiRef: PoiRef): Unit = {
    database.pois.deleteByStringId(poiRef.toId, log)
  }

  override def allTiles(): Seq[String] = {
    new MongoQueryPoiAllTiles(database).execute()
  }

  override def tilePoiInfos(tileName: String): Seq[PoiInfo] = {
    new MongoQueryTilePois(database).execute(tileName)
  }

  override def locationPois(locationName: String, parameters: LocationPoiParameters, layers: Seq[String]): Seq[LocationPoiInfo] = {
    new MongoQueryLocationPois(database).execute(locationName, parameters, layers)
  }

  override def locationPoiCount(locationName: String, layers: Seq[String]): Long = {
    new MongoQueryLocationPoiCount(database).execute(locationName, layers)
  }
}
