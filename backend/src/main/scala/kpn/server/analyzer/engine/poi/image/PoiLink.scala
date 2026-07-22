package kpn.server.analyzer.engine.poi.image

import kpn.server.analyzer.engine.poi.PoiRef

import scala.io.Source

object PoiLink {

  def linksFromFile(filename: String): Seq[PoiLink] = {
    val source = Source.fromFile(filename)
    try {
      source.getLines().toList.map { line =>
        val splitted = line.split("\\|")
        PoiLink(splitted(0), splitted(1), splitted(2).toLong, splitted(3))
      }
    }
    finally {
      source.close()
    }
  }
}

case class PoiLink(linkType: String, elementType: String, elementId: Long, url: String) {
  def toRef: PoiRef = PoiRef(elementType, elementId)
}
