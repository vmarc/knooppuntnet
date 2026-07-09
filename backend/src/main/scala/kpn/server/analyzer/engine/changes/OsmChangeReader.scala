package kpn.server.analyzer.engine.changes

import java.io.FileInputStream
import java.io.InputStream
import java.util.zip.GZIPInputStream

import kpn.server.analyzer.engine.changes.changes.OsmChange
import kpn.server.analyzer.engine.changes.changes.OsmChangeParser

import scala.xml.XML

class OsmChangeReader(fileName: String) {

  def read: OsmChange = {
    val fileStream: InputStream = new FileInputStream(fileName)
    val gzipStream: InputStream = new GZIPInputStream(fileStream)
    val xml = XML.load(gzipStream)
    new OsmChangeParser().parse(xml)
  }
}
