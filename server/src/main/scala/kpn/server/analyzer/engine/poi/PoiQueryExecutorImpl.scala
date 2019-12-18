package kpn.server.analyzer.engine.poi

import kpn.api.common.LatLon
import kpn.api.common.LatLonImpl
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.poi.PoiCenterQuery
import org.springframework.stereotype.Component

import scala.xml.SAXParseException
import scala.xml.XML

@Component
class PoiQueryExecutorImpl(executor: OverpassQueryExecutor) extends PoiQueryExecutor {

  override def center(poiRef: PoiRef): LatLon = {

    val query = PoiCenterQuery(poiRef)
    val xmlString = executor.executeQuery(None, query)

    val xml = try {
      XML.loadString(xmlString)
    } catch {
      case e: SAXParseException =>
        throw new RuntimeException(s"Could not load xml\n$xmlString", e)
    }

    val center = xml.child \\ "center"
    val latitude = (center \ "@lat").text
    val longitude = (center \ "@lon").text

    LatLonImpl(latitude, longitude)
  }
}
