package kpn.core.loadOld

import kpn.shared.data.raw.RawData

object OsmDataXmlReader {
  def read(fileName: String): RawData = {
    val xml = XmlFile.load(fileName)
    new Parser().parse(xml.head)
  }
}
