package kpn.server.repository

import akka.util.Timeout
import kpn.core.db.KeyPrefix
import kpn.core.db.couch.Database
import kpn.core.db.views.PoiDesign
import kpn.core.db.views.PoiView
import kpn.core.poi.PoiDoc
import kpn.core.poi.PoiInfo
import kpn.core.util.Log
import kpn.shared.Poi
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

  override def allPois(timeout: Timeout, stale: Boolean): Seq[PoiInfo] = {

    val pageSize = 100000

    log.info(s"Loading pois")

    val pagingQueryResult = poiDatabase.old.pagingQuery(PoiDesign, PoiView, timeout, stale, pageSize, 0)

    val initialPois = pagingQueryResult.rows.map(PoiView.convert)

    val pageCount = {
      val count = (pagingQueryResult.totalRows / pageSize).toInt
      val rest = pagingQueryResult.totalRows % pageSize
      if (rest > 0) {
        count + 1
      }
      else {
        count
      }
    }

    val remainingPois = (1 until pageCount).flatMap { pageIndex: Int =>

      val progress = (pageIndex * 100) / pageCount
      log.info(s"Loading ${pagingQueryResult.totalRows} pois: page ${pageIndex + 1} of $pageCount ($progress%)")

      val offset = pageIndex * pageSize
      val rows = poiDatabase.old.pagingQuery(PoiDesign, PoiView, timeout, stale, pageSize, offset).rows
      rows.map(PoiView.convert)
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
