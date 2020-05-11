package kpn.server.analyzer.engine.poi

import kpn.api.common.LatLon
import kpn.api.common.LatLonImpl
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.poi.PoiCenterQuery
import org.springframework.stereotype.Component

import scala.xml.SAXParseException
import scala.xml.XML

@Component
class PoiQueryExecutorImpl(cachingOverpassQueryExecutor: OverpassQueryExecutor) extends PoiQueryExecutor {

  override def center(poiRef: PoiRef): Option[LatLon] = {

    val query = PoiCenterQuery(poiRef)
    val xmlString = cachingOverpassQueryExecutor.executeQuery(None, query)

    val xml = try {
      XML.loadString(xmlString)
    } catch {
      case e: SAXParseException =>
        throw new RuntimeException(s"Could not load xml\n$xmlString", e)
    }

    val way = xml \\ "way"
    if (way.isEmpty) {
      None
    }
    else {
      val center = way \\ "center"
      if (center.isEmpty) {
        None
      }
      else {
        val latitude = (center \ "@lat").text
        val longitude = (center \ "@lon").text
        Some(LatLonImpl(latitude, longitude))
      }
    }
  }
}
