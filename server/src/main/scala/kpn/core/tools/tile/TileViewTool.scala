package kpn.core.tools.tile

import no.ecc.vectortile.VectorTileDecoder
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.Point

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.zip.GZIPInputStream
import scala.jdk.CollectionConverters.IterableHasAsScala

object TileViewTool {

  def main(args: Array[String]): Unit = {
    new TileViewTool().print("/Users/marc/kpn/tiles/monitor/6/32/21.mvt")
  }
}

class TileViewTool {

  def print(filename: String): Unit = {
    println(filename)
    val features = load(filename)
    features.foreach(feature => printFeature(feature))

    val layers = features.map(_.getLayerName).distinct.sorted

    layers.foreach { layer =>
      val count = features.count(_.getLayerName == layer)
      println(s"layer=$layer, count=$count")
    }
  }

  private def printFeature(feature: VectorTileDecoder.Feature): Unit = {
    val id = feature.getId
    val layer = feature.getLayerName
    val extent = feature.getExtent
    val attributes = feature.getAttributes
    val geometry = feature.getGeometry match {
      case lineString: LineString =>
        lineString.getCoordinates.map(coordinate => s"[${coordinate.x},${coordinate.y}]").mkString(",")
      case point: Point => s"[${point.getX},${point.getY}]"
      case _ => "?"
    }
    println(s"  id=$id, layer=$layer, extent=$extent, attributes=$attributes")
    println(s"    $geometry")
  }

  def load(filename: String): Seq[VectorTileDecoder.Feature] = {
    val bytes = if (filename.endsWith(".pbf")) {
      loadPbf(filename)
    }
    else {
      loadMvt(filename)
    }
    val iterator = new VectorTileDecoder().decode(bytes)
    iterator.asScala.toSeq
  }

  private def loadMvt(filename: String): Array[Byte] = {
    FileUtils.readFileToByteArray(new File(filename))
  }

  private def loadPbf(filename: String): Array[Byte] = {
    val gzipped: InputStream = new FileInputStream(filename)
    val ungzipped: InputStream = new GZIPInputStream(gzipped)
    IOUtils.toByteArray(ungzipped)
  }
}
