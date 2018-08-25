package kpn.core.poi

import akka.util.Timeout
import kpn.core.db.KeyPrefix
import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.json.JsonFormats.poiDocFormat
import kpn.core.db.views.PoiDesign
import kpn.core.db.views.PoiView
import kpn.core.util.Log

class PoiRepositoryImpl(database: Database) extends PoiRepository {

  private val log = Log(classOf[PoiRepositoryImpl])

  def save(poi: Poi): Boolean = {

    val key = docId(poi)

    database.optionGet(key, Couch.batchTimeout) match {
      case Some(jsDoc) =>
        val doc = poiDocFormat.read(jsDoc)
        if (poi == doc.poi) {
          log.info(s"""Poi ${poi.layers.head} "$key" not saved (no change)""")
          false
        }
        else {
          log.infoElapsed(s"""Poi ${poi.layers.head} "$key" update""") {
            database.save(key, poiDocFormat.write(PoiDoc(key, poi, doc._rev)))
            true
          }
        }

      case None =>
        log.infoElapsed(s"""Poi ${poi.layers.head} "$key" saved""") {
          database.save(key, poiDocFormat.write(PoiDoc(key, poi)))
          true
        }
    }
  }

  override def allPois(timeout: Timeout, stale: Boolean): Seq[PoiInfo] = {
    database.query(PoiDesign, PoiView, timeout, stale)().map(PoiView.convert)
  }

  private def docId(poi: Poi): String = {
    s"${KeyPrefix.Poi}:${poi.elementType}:${poi.elementId}"
  }

}
