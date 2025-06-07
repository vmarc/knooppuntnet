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
    // new TileViewTool().print("/Users/marc/kpn/tiles/opendata/netherlands/hiking/8/132/83.mvt")
    // new TileViewTool().print("/Users/marc/kpn/tiles/hiking/12/979/1521.mvt") // route 12 extent 256
    // new TileViewTool().print("/Users/marc/kpn/tiles/hiking/14/8591/5639.mvt") // route 14 extent 4096
    // new TileViewTool().print("/Users/marc/kpn/tiles-old/poi/13/4433/2871.mvt") // poi 13 extent 256
    // new TileViewTool().print("/Users/marc/kpn/tiles-old/poi/15/17765/11521.mvt") // poi 15 extent 4096
    new TileViewTool().print("/Users/marc/kpn/tiles/poi/14/8395/5450.mvt") // route 14 extent 4096
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
