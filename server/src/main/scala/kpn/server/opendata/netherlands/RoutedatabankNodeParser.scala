package kpn.server.opendata.netherlands

import org.geotools.api.feature.simple.SimpleFeature
import org.geotools.data.geojson.GeoJSONReader
import org.locationtech.jts.geom.Point

import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import scala.collection.mutable.ListBuffer

class RoutedatabankNodeParser {

  private val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")

  def parse(inputStream: InputStream): Seq[RoutedatabankNode] = {

    val features = toFeatures(inputStream)

    features.map { feature =>
      feature.getDefaultGeometry match {
        case point: Point =>
          val _id = feature.getAttribute("puntid").toString
          val name = {
            val value = feature.getAttribute("knooppuntnummer")
            if (value == null) {
              val attribute = feature.getAttribute("knooppuntnr")
              if (attribute != null) {
                attribute.toString
              }
              else {
                ""
              }
            }
            else {
              value.toString
            }
          }
          val latitude = point.getY.toString
          val longitude = point.getX.toString
          val provincie = feature.getAttribute("provincie").toString
          val lastEditedDate = feature.getAttribute("last_edited_date")
          val ogcFid = feature.getAttribute("ogc_fid").toString
          val nodeType = {
            val value = feature.getAttribute("soort_knooppunt")
            if (value != null) {
              value.toString
            }
            else {
              ""
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

          RoutedatabankNode(
            _id,
            name,
            latitude,
            longitude,
            provincie,
            updated,
            ogcFid,
            nodeType,
            regio
          )

        case _ =>
          throw new RuntimeException("unexpected node geometry type")
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
