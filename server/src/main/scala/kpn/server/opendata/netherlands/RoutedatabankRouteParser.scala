package kpn.server.opendata.netherlands

import kpn.api.common.LatLonImpl
import org.geotools.api.feature.simple.SimpleFeature
import org.geotools.data.geojson.GeoJSONReader
import org.locationtech.jts.geom.LineString

import java.io.InputStream
import java.text.SimpleDateFormat
import scala.collection.mutable.ListBuffer

class RoutedatabankRouteParser {

  private val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")

  def parse(inputStream: InputStream): Seq[RoutedatabankRoute] = {

    val features = toFeatures(inputStream)

    features.map { feature =>
      feature.getDefaultGeometry match {
        case lineString: LineString =>
          val _id = {
            val value = feature.getAttribute("lijnid")
            if (value == null) {
              feature.getAttribute("ogc_fid").toString
            }
            else {
              value.toString
            }
          }
          val regio = {
            val value = feature.getAttribute("regio")
            if (value != null) {
              value.toString
            }
            else {
              ""
            }
          }
          val provincie = {
            val value = feature.getAttribute("provincie")
            if (value != null) {
              value.toString
            }
            else {
              ""
            }
          }
          val lastEditedDate = feature.getAttribute("last_edited_date")

          val updated = if (lastEditedDate == "null") {
            None
          }
          else {
            Some(simpleDateFormat.format(lastEditedDate))
          }

          val coordinates = lineString.getCoordinates.toSeq.map { coordinate =>
            LatLonImpl(
              coordinate.getY.toString,
              coordinate.getX.toString
            )
          }

          RoutedatabankRoute(
            _id,
            updated,
            regio,
            provincie,
            coordinates
          )

        case _ => throw new RuntimeException("unexpected route geometry type")
      }
    }
  }

  private def toFeatures(inputStream: InputStream): Seq[SimpleFeature] = {
    val features = new ListBuffer[SimpleFeature]
    val featureIterator = new GeoJSONReader(inputStream).getFeatures.features()
    while (featureIterator.hasNext) {
      features += featureIterator.next()
    }
    features.toSeq
  }
}
