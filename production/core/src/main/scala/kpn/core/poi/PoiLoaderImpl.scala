package kpn.core.poi

import org.xml.sax.SAXParseException

import scala.xml.XML

class PoiLoaderImpl(executor: OverpassQueryExecutor) extends PoiLoader {

  private val log = Log(classOf[PoiLoaderImpl])

  override def load(elementType: String,
                    layer: String,
                    bbox: String,
                    condition: String): Seq[Poi] = {

    log.elapsed {
      val query = PoiQuery(elementType, layer, bbox, condition)
      val xmlString = executor.executeQuery(None, query)

      val xml = try {
        XML.loadString(xmlString)
      } catch {
        case e: SAXParseException =>
          throw new RuntimeException(s"Could not load xml\n$xmlString", e)
      }

      val pois = new PoiQueryResultParser().parse(layer, xml)
      (s"Loaded ${pois.size} pois", pois)
    }
  }
}
