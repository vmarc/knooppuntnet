package kpn.core.loadOld

import kpn.api.common.data.raw.RawData

object OsmDataXmlReader {
  def read(fileName: String): RawData = {
    val xml = XmlFile.load(fileName)
    new Parser().parse(xml.head)
  }
}
