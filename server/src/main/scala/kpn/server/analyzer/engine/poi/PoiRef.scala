package kpn.server.analyzer.engine.poi

import kpn.api.common.Poi
import kpn.api.common.data.raw.RawElement
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay

object PoiRef {

  def node(id: Long): PoiRef = {
    PoiRef("node", id)
  }

  def way(id: Long): PoiRef = {
    PoiRef("way", id)
  }

  def relation(id: Long): PoiRef = {
    PoiRef("relation", id)
  }

  def of(element: RawElement): PoiRef = {
    element match {
      case node: RawNode => PoiRef.node(element.id)
      case way: RawWay => PoiRef.way(element.id)
      case relation: RawRelation => PoiRef.relation(element.id)
    }
  }

  def of(poi: Poi): PoiRef = {
    PoiRef(poi.elementType, poi.elementId)
  }

  def from(string: String): PoiRef = {
    val parts = string.split(":")
    PoiRef(parts.head, parts(1).toLong)
  }
}

case class PoiRef(elementType: String, elementId: Long) {
  def toId: String = {
    s"$elementType:$elementId"
  }
  def toCouchId: String = {
    s"poi:$elementType:$elementId"
  }
}
