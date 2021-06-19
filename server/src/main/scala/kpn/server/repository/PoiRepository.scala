package kpn.server.repository

import kpn.api.common.Poi
import kpn.core.poi.PoiInfo
import kpn.server.analyzer.engine.poi.PoiRef

trait PoiRepository {

  def save(poi: Poi): Unit

  def nodeIds(stale: Boolean = true): Seq[Long]

  def wayIds(stale: Boolean = true): Seq[Long]

  def relationIds(stale: Boolean = true): Seq[Long]

  def get(poiRef: PoiRef): Option[Poi]

  def delete(poiRef: PoiRef): Unit

  def allTiles(stale: Boolean = true): Seq[String]

  def tilePoiInfos(tileName: String, stale: Boolean = true): Seq[PoiInfo]

}
