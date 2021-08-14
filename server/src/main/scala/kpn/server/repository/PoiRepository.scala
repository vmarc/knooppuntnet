package kpn.server.repository

import kpn.api.common.Poi
import kpn.core.poi.PoiInfo
import kpn.server.analyzer.engine.poi.PoiRef

trait PoiRepository {

  def save(poi: Poi): Unit

  def nodeIds(): Seq[Long]

  def wayIds(): Seq[Long]

  def relationIds(): Seq[Long]

  def get(poiRef: PoiRef): Option[Poi]

  def delete(poiRef: PoiRef): Unit

  def allTiles(): Seq[String]

  def tilePoiInfos(tileName: String): Seq[PoiInfo]

}
