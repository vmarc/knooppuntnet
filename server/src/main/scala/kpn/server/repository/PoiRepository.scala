package kpn.server.repository

import akka.util.Timeout
import kpn.api.common.Poi
import kpn.core.db.couch.Couch
import kpn.core.poi.PoiInfo

trait PoiRepository {

  def save(poi: Poi): Boolean

  def allPois(timeout: Timeout = Couch.batchTimeout, stale: Boolean = true): Seq[PoiInfo]

  def poi(elementType: String, elementId: Long): Option[Poi]

  def delete(elementType: String, elementId: Long): Unit

}
