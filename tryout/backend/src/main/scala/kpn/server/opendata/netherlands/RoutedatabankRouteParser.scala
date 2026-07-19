package kpn.server.opendata.netherlands

import kpn.api.common.LatLonImpl
import org.geotools.api.feature.simple.SimpleFeature
import org.geotools.data.geojson.GeoJSONReader
import org.locationtech.jts.geom.LineString

import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import scala.collection.mutable.ListBuffer

class RoutedatabankRouteParser {

  private val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")

  def parse(inputStream: InputStream): Seq[RoutedatabankRoute] = {
    val features = toFeatures(inputStream)
    features.map { feature =>
      feature.getDefaultGeometry match {
        case lineString: LineString => toRoute(feature, lineString)
        case _ => throw new RuntimeException("unexpected route geometry type")
      }
    }
  }

  private def toRoute(feature: SimpleFeature, lineString: LineString): RoutedatabankRoute = {
    val _id = attribute(feature, "lijnid", fallbackAttribute = "ogc_fid")
    val regio = attribute(feature, "regio")
    val provincie = attribute(feature, "provincie")
    val updated = findUpdated(feature)
    val coordinates = toCoordinates(lineString)
    RoutedatabankRoute(
      _id,
      updated,
      regio,
      provincie,
      coordinates
    )
  }

  private def toCoordinates(lineString: LineString): Seq[LatLonImpl] = {
    lineString.getCoordinates.toSeq.map { coordinate =>
      LatLonImpl(
        coordinate.getY.toString,
        coordinate.getX.toString
      )
    }
  }

  private def findUpdated(feature: SimpleFeature): Option[String] = {
    val lastEditedDate = feature.getAttribute("last_edited_date")
    val updated = lastEditedDate match {
      case date: Date => Some(simpleDateFormat.format(date))
      case string: String =>
        if (string == "null") {
          None
        }
        else {
          Some(string.take("yyyy-mm-dd".length))
        }
      case _ => None
    }
    updated
  }

  private def toFeatures(inputStream: InputStream): Seq[SimpleFeature] = {
    val features = new ListBuffer[SimpleFeature]
    val featureIterator = new GeoJSONReader(inputStream).getFeatures.features()
    while (featureIterator.hasNext) {
      features += featureIterator.next()
    }
    features.toSeq
  }

  private def attribute(
    feature: SimpleFeature,
    attributeName: String,
    fallbackAttribute: String = null,
  ): String = {
    val value = feature.getAttribute(attributeName)
    if (value == null) {
      if (fallbackAttribute != null) {
        val fallbackValue = feature.getAttribute(fallbackAttribute)
        if (fallbackValue != null) {
          fallbackValue.toString
        } else {
          ""
        }
      } else {
        ""
      }
    } else {
      value.toString
    }
  }
}
