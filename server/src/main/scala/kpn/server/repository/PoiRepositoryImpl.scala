package kpn.server.repository

import akka.util.Timeout
import kpn.api.common.Poi
import kpn.core.database.Database
import kpn.core.database.views.poi.PoiNodeIdView
import kpn.core.database.views.poi.PoiRelationIdView
import kpn.core.database.views.poi.PoiTileView
import kpn.core.database.views.poi.PoiView
import kpn.core.database.views.poi.PoiWayIdView
import kpn.core.db.KeyPrefix
import kpn.core.db.couch.Couch
import kpn.core.poi.PoiDoc
import kpn.core.poi.PoiInfo
import kpn.core.util.Log
import kpn.server.analyzer.engine.poi.PoiRef
import org.springframework.stereotype.Component

@Component
class PoiRepositoryImpl(poiDatabase: Database) extends PoiRepository {

  private val log = Log(classOf[PoiRepositoryImpl])

  override def save(poi: Poi): Boolean = {

    val id = poiDocId(poi)

    poiDatabase.docWithId(id, classOf[PoiDoc]) match {
      case Some(doc) =>
        if (poi == doc.poi) {
          log.info(s"""Poi ${poi.layers} "$id" not saved (no change)""")
          false
        }
        else {
          log.infoElapsed(s"""Poi ${poi.layers} "$id" update""") {
            poiDatabase.save(PoiDoc(id, poi, doc._rev))
            true
          }
        }

      case None =>
        log.infoElapsed(s"""Poi ${poi.layers} "$id" saved""") {
          poiDatabase.save(PoiDoc(id, poi))
          true
        }
    }
  }

  override def allPois(timeout: Timeout, stale: Boolean = true): Seq[PoiInfo] = {

    val pageSize = 10000

    log.info(s"Loading pois")

    val initialResult = PoiView.query(poiDatabase, pageSize, 0, stale)
    val initialPois = initialResult.pois

    val pageCount = {
      val count = initialResult.totalRows / pageSize
      val rest = initialResult.totalRows % pageSize
      if (rest > 0) {
        count + 1
      }
      else {
        count
      }
    }

    val remainingPois = (1L until pageCount).flatMap { pageIndex: Long =>
      val progress = (pageIndex * 100) / pageCount
      log.info(s"Loading ${initialResult.totalRows} pois: page ${pageIndex + 1} of $pageCount ($progress%)")
      val skip = pageIndex * pageSize
      val remainderResult = PoiView.query(poiDatabase, pageSize, skip, stale)
      remainderResult.pois
    }

    initialPois ++ remainingPois
  }

  override def nodeIds(timeout: Timeout, stale: Boolean = true): Seq[Long] = {
    PoiNodeIdView.query(poiDatabase, stale)
  }

  override def wayIds(timeout: Timeout, stale: Boolean = true): Seq[Long] = {
    PoiWayIdView.query(poiDatabase, stale)
  }

  override def relationIds(timeout: Timeout, stale: Boolean = true): Seq[Long] = {
    PoiRelationIdView.query(poiDatabase, stale)
  }

  override def get(poiRef: PoiRef): Option[Poi] = {
    val id = docId(poiRef)
    poiDatabase.docWithId(id, classOf[PoiDoc]).map(_.poi)
  }

  override def delete(poiRef: PoiRef): Unit = {
    val id = docId(poiRef)
    poiDatabase.deleteDocWithId(id)
  }

  override def allTiles(timeout: Timeout = Couch.batchTimeout, stale: Boolean): Seq[String] = {
    PoiTileView.allTiles(poiDatabase, stale)
  }

  override def tilePoiInfos(tileName: String, stale: Boolean): Seq[PoiInfo] = {
    PoiTileView.tilePoiInfos(tileName, poiDatabase, stale)
  }

  private def poiDocId(poi: Poi): String = {
    docId(PoiRef.of(poi))
  }

  private def docId(poiRef: PoiRef): String = {
    s"${KeyPrefix.Poi}:${poiRef.elementType}:${poiRef.elementId}"
  }

}
