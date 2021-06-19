package kpn.server.repository

import kpn.api.common.Poi
import kpn.core.database.doc.PoiDoc
import kpn.core.database.views.poi.PoiNodeIdView
import kpn.core.database.views.poi.PoiRelationIdView
import kpn.core.database.views.poi.PoiTileView
import kpn.core.database.views.poi.PoiWayIdView
import kpn.core.db.KeyPrefix
import kpn.core.mongo.Database
import kpn.core.mongo.actions.pois.MongoQueryPoiAllTiles
import kpn.core.mongo.actions.pois.MongoQueryPoiElementIds
import kpn.core.mongo.actions.pois.MongoQueryTilePois
import kpn.core.poi.PoiInfo
import kpn.core.util.Log
import kpn.server.analyzer.engine.poi.PoiRef
import org.springframework.stereotype.Component

@Component
class PoiRepositoryImpl(
  database: Database,
  // old
  poiDatabase: kpn.core.database.Database,
  mongoEnabled: Boolean
) extends PoiRepository {

  private val log = Log(classOf[PoiRepositoryImpl])

  override def save(poi: Poi): Unit = {
    if (mongoEnabled) {
      database.pois.save(poi)
    }
    else {
      val id = poiDocId(poi)
      poiDatabase.save(PoiDoc(id, poi))
    }
  }

  override def nodeIds(stale: Boolean = true): Seq[Long] = {
    if (mongoEnabled) {
      new MongoQueryPoiElementIds(database).execute("node")
    }
    else {
      PoiNodeIdView.query(poiDatabase, stale)
    }
  }

  override def wayIds(stale: Boolean = true): Seq[Long] = {
    if (mongoEnabled) {
      new MongoQueryPoiElementIds(database).execute("way")
    }
    else {
      PoiWayIdView.query(poiDatabase, stale)
    }
  }

  override def relationIds(stale: Boolean = true): Seq[Long] = {
    if (mongoEnabled) {
      new MongoQueryPoiElementIds(database).execute("relation")
    }
    else {
      PoiRelationIdView.query(poiDatabase, stale)
    }
  }

  override def get(poiRef: PoiRef): Option[Poi] = {
    if (mongoEnabled) {
      database.pois.findByStringId(poiRef.toId, log)
    }
    else {
      val id = docId(poiRef)
      poiDatabase.docWithId(id, classOf[PoiDoc]).map(_.poi)
    }
  }

  override def delete(poiRef: PoiRef): Unit = {
    if (mongoEnabled) {
      database.pois.deleteByStringId(poiRef.toId, log)
    }
    else {
      val id = docId(poiRef)
      poiDatabase.deleteDocWithId(id)
    }
  }

  override def allTiles(stale: Boolean): Seq[String] = {
    if (mongoEnabled) {
      new MongoQueryPoiAllTiles(database).execute()
    }
    else {
      PoiTileView.allTiles(poiDatabase, stale)
    }
  }

  override def tilePoiInfos(tileName: String, stale: Boolean): Seq[PoiInfo] = {
    if (mongoEnabled) {
      new MongoQueryTilePois(database).execute(tileName)
    }
    else {
      PoiTileView.tilePoiInfos(tileName, poiDatabase, stale)
    }
  }

  private def poiDocId(poi: Poi): String = {
    docId(PoiRef.of(poi))
  }

  private def docId(poiRef: PoiRef): String = {
    s"${KeyPrefix.Poi}:${poiRef.elementType}:${poiRef.elementId}"
  }
}
