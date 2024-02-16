package kpn.core.tools.tile

import com.wdtinc.mapbox_vector_tile.adapt.jts.MvtReader
import com.wdtinc.mapbox_vector_tile.adapt.jts.TagKeyValueMapConverter
import com.wdtinc.mapbox_vector_tile.adapt.jts.model.JtsLayer
import com.wdtinc.mapbox_vector_tile.adapt.jts.model.JtsMvt
import org.locationtech.jts.geom.GeometryFactory

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.zip.GZIPInputStream

object TileViewTool {

  private val geometryFactory = new GeometryFactory()

  def main(args: Array[String]): Unit = {
    new TileViewTool().print("/home/marcv/Downloads/14_8390_5401.mvt")
    println("Done")
  }
}

class TileViewTool {

  import TileViewTool.geometryFactory

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
    MvtReader.loadMvt(new File(filename), geometryFactory, new TagKeyValueMapConverter())
  }

  private def loadPbf(filename: String): JtsMvt = {
    val gzipped: InputStream = new FileInputStream(filename)
    val ungzipped: InputStream = new GZIPInputStream(gzipped)
    MvtReader.loadMvt(ungzipped, geometryFactory, new TagKeyValueMapConverter())
  }

}
