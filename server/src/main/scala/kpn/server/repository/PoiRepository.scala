package kpn.server.repository

import akka.util.Timeout
import kpn.api.common.Poi
import kpn.core.db.couch.Couch
import kpn.core.poi.PoiInfo
import kpn.server.analyzer.engine.poi.PoiRef

trait PoiRepository {

  def save(poi: Poi): Boolean

  def allPois(timeout: Timeout = Couch.batchTimeout, stale: Boolean = true): Seq[PoiInfo]

  def nodeIds(timeout: Timeout = Couch.batchTimeout, stale: Boolean = true): Seq[Long]

  def wayIds(timeout: Timeout = Couch.batchTimeout, stale: Boolean = true): Seq[Long]

  def relationIds(timeout: Timeout = Couch.batchTimeout, stale: Boolean = true): Seq[Long]

  def get(poiRef: PoiRef): Option[Poi]

  def delete(poiRef: PoiRef): Unit

  def allTiles(timeout: Timeout = Couch.batchTimeout, stale: Boolean = true): Seq[String]

  def tilePoiRefs(tileName: String, stale: Boolean = true): Seq[PoiRef]

}
