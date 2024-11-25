package kpn.core.tools.tile

import no.ecc.vectortile.VectorTileDecoder
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.locationtech.jts.geom.LineString

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.zip.GZIPInputStream

object TileViewTool {

  def main(args: Array[String]): Unit = {
    new TileViewTool().print("/Users/marc/kpn/tiles/hiking/6/31/21.mvt")
    new TileViewTool().print("/Users/marc/kpn/tiles/hiking/13/4197/2724.mvt")
    println("Done")
  }
}

class TileViewTool {

  def print(filename: String): Unit = {
    println(filename)
    val features = load(filename)
    features.forEach(feature => printFeature(feature))
  }

  private def printFeature(feature: VectorTileDecoder.Feature): Unit = {
    val id = feature.getId
    val layer = feature.getLayerName
    val extent = feature.getExtent
    val attributes = feature.getAttributes
    val geometry = feature.getGeometry.asInstanceOf[LineString]
    val coordinates = geometry.getCoordinates().map(coordinate => s"[${coordinate.x},${coordinate.x}]").mkString((","))
    println(s"  id=$id, layer=$layer, extent=$extent, attributes=$attributes")
    println(s"    $coordinates")
  }

  def load(filename: String): VectorTileDecoder.FeatureIterable = {
    val bytes = if (filename.endsWith(".pbf")) {
      loadPbf(filename)
    }
    else {
      loadMvt(filename)
    }
    new VectorTileDecoder().decode(bytes)
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
