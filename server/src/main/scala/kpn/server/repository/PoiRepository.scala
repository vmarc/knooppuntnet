package kpn.server.repository

import akka.util.Timeout
import kpn.core.db.couch.Couch
import kpn.core.poi.PoiInfo
import kpn.shared.Poi

trait PoiRepository {

  def save(poi: Poi): Boolean

  def allPois(timeout: Timeout = Couch.batchTimeout, stale: Boolean = false): Seq[PoiInfo]

  def poi(elementType: String, elementId: Long): Option[Poi]

}
