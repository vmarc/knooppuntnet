package kpn.server.analyzer.engine.poi

import kpn.api.common.LatLon
import kpn.api.common.LatLonImpl
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryCenters
import kpn.core.poi.PoiCenterQuery
import kpn.core.util.Log
import org.springframework.stereotype.Component

import scala.xml.SAXParseException
import scala.xml.XML

@Component
class PoiQueryExecutorImpl(overpassQueryExecutor: OverpassQueryExecutor) extends PoiQueryExecutor {

  private val log = Log(classOf[PoiQueryExecutorImpl])

  override def center(poiRef: PoiRef): Option[LatLon] = {

    val query = PoiCenterQuery(poiRef)
    val xmlString = overpassQueryExecutor.executeQuery(None, query)

    val xml = try {
      XML.loadString(xmlString)
    } catch {
      case e: SAXParseException =>
        throw new RuntimeException(s"Could not load xml\n$xmlString", e)
    }

    val way = xml \\ poiRef.elementType
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

  override def centers(elementType: String, elementIds: Seq[Long]): Seq[ElementCenter] = {
    val batchSize = 100
    elementIds.sliding(batchSize, batchSize).zipWithIndex.flatMap { case (idsBatch, index) =>
      Log.context(s"${index * batchSize}/${elementIds.size}") {
        log.infoElapsed {
          val query = QueryCenters(elementType, idsBatch)
          val xmlString = overpassQueryExecutor.executeQuery(None, query)
          val xml = try {
            XML.loadString(xmlString)
          } catch {
            case e: SAXParseException =>
              throw new RuntimeException(s"Could not load xml\n$xmlString", e)
          }

          val centers = (xml.head \ elementType).flatMap { node =>
            val idText = (node \ "@id").text
            val id = idText.toLong
            val center = node \\ "center"
            if (center.isEmpty) {
              None
            }
            else {
              val latitude = (center \ "@lat").text
              val longitude = (center \ "@lon").text
              Some(
                ElementCenter(
                  id,
                  LatLonImpl(latitude, longitude)
                )
              )
            }
          }
          (s"$elementType centers ${idsBatch.size} elements: ${idsBatch.mkString(", ")}", centers)
        }
      }
    }.toSeq
  }
}
