package kpn.server.analyzer.engine.poi.image

import kpn.core.tools.config.Dirs
import kpn.server.analyzer.engine.poi.PoiRef

import java.io.File

class PoiImageRepositoryImpl {

  private val root = s"${Dirs.root}/images"

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
