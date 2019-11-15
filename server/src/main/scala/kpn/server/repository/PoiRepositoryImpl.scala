package kpn.server.repository

import akka.util.Timeout
import kpn.api.common.Poi
import kpn.core.database.Database
import kpn.core.database.views.poi.PoiView
import kpn.core.db.KeyPrefix
import kpn.core.poi.PoiDoc
import kpn.core.poi.PoiInfo
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class PoiRepositoryImpl(poiDatabase: Database) extends PoiRepository {

  private val log = Log(classOf[PoiRepositoryImpl])

  def save(poi: Poi): Boolean = {

    val key = poiDocId(poi)

    poiDatabase.docWithId(key, classOf[PoiDoc]) match {
      case Some(doc) =>
        if (poi == doc.poi) {
          log.info(s"""Poi ${poi.layers} "$key" not saved (no change)""")
          false
        }
        else {
          log.infoElapsed(s"""Poi ${poi.layers} "$key" update""") {
            poiDatabase.save(PoiDoc(key, poi, doc._rev))
            true
          }
        }

      case None =>
        log.infoElapsed(s"""Poi ${poi.layers} "$key" saved""") {
          poiDatabase.save(PoiDoc(key, poi))
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

  def poi(elementType: String, elementId: Long): Option[Poi] = {
    val key = docId(elementType, elementId)
    poiDatabase.docWithId(key, classOf[PoiDoc]).map(_.poi)
  }

  private def poiDocId(poi: Poi): String = {
    docId(poi.elementType, poi.elementId)
  }

  private def docId(elementType: String, elementId: Long): String = {
    s"${KeyPrefix.Poi}:$elementType:$elementId"
  }

}
