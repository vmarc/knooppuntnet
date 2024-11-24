package kpn.core.tools.tile

import com.wdtinc.mapbox_vector_tile.adapt.jts.MvtReader
import com.wdtinc.mapbox_vector_tile.adapt.jts.TagKeyValueMapConverter
import com.wdtinc.mapbox_vector_tile.adapt.jts.model.JtsLayer
import com.wdtinc.mapbox_vector_tile.adapt.jts.model.JtsMvt
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.zip.GZIPInputStream

object TileViewTool {

  private val geometryFactory = new GeometryFactory()

  def main(args: Array[String]): Unit = {

    new TileViewTool().print("/Users/marc/kpn/tiles/hiking/9/268/150.mvt")
    //new TileViewTool().print("/Users/marc/kpn/tiles/tmp/hiking/14/8600/5709.mvt") // Calmeyn
    //    new TileViewTool().print("/Users/marc/kpn/tiles/hiking/13/4197/2726.mvt") // Calmeyn
    //    new TileViewTool().print("/Users/marc/kpn/tiles/hiking/11/1049/681.mvt") // Calmeyn
    println("Done")
  }
}

class TileViewTool {

  import TileViewTool.geometryFactory

  def print(filename: String): Unit = {
    println(filename)
    val tile = load(filename)
    tile.getLayers.forEach(printLayer)
  }

  private def printLayer(layer: JtsLayer): Unit = {
    println(s"layer ${layer.getName}, extent=${layer.getExtent}, geometries=${layer.getGeometries.size}")
    layer.getGeometries.forEach { geometry =>
      val userData = geometry.getUserData
      println("  " + userData)

      geometry match {
        case lineString: LineString =>
          val message = lineString.getCoordinates.map(coordinate => s"[${coordinate.x},${coordinate.x}]").mkString((","))
          println(s"    geometry $message")
        case _ => println("    geometry not a LineString")
      }
    }
  }

  def load(filename: String): JtsMvt = {
    if (filename.endsWith(".pbf")) {
      loadPbf(filename)
    }
    else {
      loadMvt(filename)
    }
  }

  private def loadMvt(filename: String): JtsMvt = {
    MvtReader.loadMvt(new File(filename), geometryFactory, new TagKeyValueMapConverter())
  }

  private def loadPbf(filename: String): JtsMvt = {
    val gzipped: InputStream = new FileInputStream(filename)
    val ungzipped: InputStream = new GZIPInputStream(gzipped)
    MvtReader.loadMvt(ungzipped, geometryFactory, new TagKeyValueMapConverter())
  }
}
