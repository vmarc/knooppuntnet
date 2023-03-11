package kpn.server.opendata.netherlands

import org.geotools.data.geojson.GeoJSONReader
import org.locationtech.jts.geom.Point
import org.opengis.feature.simple.SimpleFeature

import java.io.InputStream
import java.text.SimpleDateFormat
import scala.collection.mutable.ListBuffer

class RoutedatabankNodeParser {

  private val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")

  def parse(inputStream: InputStream): Seq[RoutedatabankNode] = {

    val features = toFeatures(inputStream)

    features.map { feature =>
      feature.getDefaultGeometry match {
        case point: Point =>
          val _id = feature.getAttribute("puntid").toString
          val name = feature.getAttribute("knooppuntnummer").toString
          val latitude = point.getY.toString
          val longitude = point.getX.toString
          val provincie = feature.getAttribute("provincie").toString
          val lastEditedDate = feature.getAttribute("last_edited_date")
          val ogcFid = feature.getAttribute("ogc_fid").toString
          val nodeType = feature.getAttribute("soort_knooppunt").toString
          val regio = feature.getAttribute("regio").toString

          val updated = if (lastEditedDate == "null") {
            None
          }
          else {
            Some(simpleDateFormat.format(lastEditedDate))
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

        case _ => throw new RuntimeException("unexpected node geometry type")
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
