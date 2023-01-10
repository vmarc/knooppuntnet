package kpn.server.analyzer.engine.tiles

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

import com.wdtinc.mapbox_vector_tile.adapt.jts.MvtEncoder
import com.wdtinc.mapbox_vector_tile.adapt.jts.MvtReader
import com.wdtinc.mapbox_vector_tile.adapt.jts.TagKeyValueMapConverter
import com.wdtinc.mapbox_vector_tile.adapt.jts.model.JtsMvt
import org.locationtech.jts.geom.GeometryFactory

object TileTryout {

  private val geometryFactory = new GeometryFactory()

  def main(args: Array[String]): Unit = {

    val tiles = Seq(
      loadMvt("/kpn/tiles/poi/13/4197/2725.mvt"),
      loadMvt("/kpn/tiles/cycling/13/4197/2725.mvt"),
      loadMvt("/kpn/tiles/hiking/13/4197/2725.mvt"),
      loadPbf("/kpn/tiles/osm-old/13/4197/2725.pbf")
    )

    import scala.jdk.CollectionConverters._
    val layers = tiles.flatMap(_.getLayers.asScala)

    val all = new JtsMvt(layers.asJava)

    val bytes = MvtEncoder.encode(all /*, MvtLayerParams.DEFAULT, new TagKeyValueMapConverter()*/)

    val outputStream = new FileOutputStream("/tmp/tile2.pbf.gz")
    val gzipOutputStream = new GZIPOutputStream(outputStream)
    gzipOutputStream.write(bytes)
    gzipOutputStream.close()

    println("Done")
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
