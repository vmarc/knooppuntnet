package kpn.server.analyzer.engine.tiles

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.zip.GZIPInputStream

import com.wdtinc.mapbox_vector_tile.adapt.jts.MvtReader
import com.wdtinc.mapbox_vector_tile.adapt.jts.TagKeyValueMapConverter
import com.wdtinc.mapbox_vector_tile.adapt.jts.model.JtsLayer
import com.wdtinc.mapbox_vector_tile.adapt.jts.model.JtsMvt
import org.locationtech.jts.geom.GeometryFactory

object TileViewTool {

  private val geomFactory = new GeometryFactory()

  def main(args: Array[String]): Unit = {
    new TileViewTool().print("/tmp/test/osm-1.pbf")
    println("Done")
  }
}

class TileViewTool() {

  import kpn.server.analyzer.engine.tiles.TileViewTool.geomFactory

  def print(filename: String): Unit = {
    val tile = load(filename)
    tile.getLayers.forEach(printLayer)
  }

  private def printLayer(layer: JtsLayer): Unit = {
    println(s"Layer ${layer.getName} (${layer.getGeometries.size})")
    layer.getGeometries.forEach { geometry =>
      val userData = geometry.getUserData
      println("  " + userData)
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
    MvtReader.loadMvt(new File(filename), geomFactory, new TagKeyValueMapConverter())
  }

  private def loadPbf(filename: String): JtsMvt = {
    val gzipped: InputStream = new FileInputStream(filename)
    val ungzipped: InputStream = new GZIPInputStream(gzipped)
    MvtReader.loadMvt(ungzipped, geomFactory, new TagKeyValueMapConverter())
  }

}
