package kpn.server.analyzer.engine.poi.image

import java.io.File

import kpn.server.analyzer.engine.poi.PoiRef

class PoiImageRepositoryImpl {

  private val root = "/kpn/images"

  def exists(poiRef: PoiRef): Boolean = {
    file(poiRef).exists()
  }

  def file(poiRef: PoiRef): File = {
    val s = poiRef.elementId.toString
    val dir = s"$root/${s(s.length - 2)}/${s(s.length - 1)}"
    new File(dir).mkdirs()
    new File(dir, s"${poiRef.elementType}-${poiRef.elementId}.jpg")
  }
}
