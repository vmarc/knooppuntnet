package kpn.server.analyzer.engine.elevation

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.ShortBuffer
import java.util.zip.GZIPInputStream

import kpn.server.analyzer.engine.tiles.domain.Point
import org.apache.commons.io.IOUtils
import org.springframework.stereotype.Component

@Component
class ElevationRepositoryImpl extends ElevationRepository {

  private val unknownElevation: Int = -32768 // magic number indicating 'void data' in HGT file

  private val cache = scala.collection.mutable.Map[String, Option[ShortBuffer]]()

  override def tileCount: Int = cache.size

  override def elevation(point: Point): Option[Int] = {
    elevationFor(ElevationTile(point))
  }

  override def elevationFor(tile: ElevationTile): Option[Int] = {
    cache.getOrElseUpdate(tile.name, loadTileBuffer(tile.name)).flatMap { tileBuffer =>
      elevationInTile(tileBuffer, tile)
    }
  }

  private def elevationInTile(tileBuffer: ShortBuffer, tile: ElevationTile): Option[Int] = {
    val elevation: Short = tileBuffer.get(tile.bufferIndex)
    if (elevation == unknownElevation) {
      None
    }
    else {
      Some(elevation.toInt)
    }
  }

  private def loadTileBuffer(tilename: String) = {
    val filename = s"/kpn/tiles/hgt/$tilename.hgt.gz"
    val f = new File(filename)
    if (f.exists) {
      Some(hgtFileToBuffer(filename))
    }
    else {
      None
    }
  }

  private def hgtFileToBuffer(filename: String): ShortBuffer = {
    val gis: InputStream = new GZIPInputStream(new FileInputStream(filename))
    try {
      val byteArray = IOUtils.toByteArray(gis)
      val byteBuffer = ByteBuffer.wrap(byteArray)
      byteBuffer.order(ByteOrder.BIG_ENDIAN).asShortBuffer
    }
    finally {
      gis.close()
    }
  }

}
