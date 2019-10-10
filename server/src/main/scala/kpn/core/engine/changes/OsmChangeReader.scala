package kpn.core.engine.changes

import java.io.FileInputStream
import java.io.InputStream
import java.util.zip.GZIPInputStream

import kpn.core.changes.OsmChange
import kpn.core.changes.OsmChangeParser

import scala.xml.XML

class OsmChangeReader(fileName: String) {

  def read: OsmChange = {
    val fileStream: InputStream = new FileInputStream(fileName)
    val gzipStream: InputStream = new GZIPInputStream(fileStream)
    val xml = XML.load(gzipStream)
    new OsmChangeParser().parse(xml)
  }
}
