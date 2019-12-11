package kpn.server.analyzer.engine.poi

trait KnownPoiCache {

  def contains(poiRef: PoiRef): Boolean

  def add(poiRef: PoiRef): Unit

  def delete(poiRef: PoiRef): Unit

}
