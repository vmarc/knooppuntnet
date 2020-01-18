package kpn.server.analyzer.engine.elevation

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.ShortBuffer
import java.util.zip.GZIPInputStream

import kpn.core.common.LatLonD
import org.apache.commons.io.IOUtils
import org.springframework.stereotype.Component

@Component
class ElevationRepositoryImpl {

  private val unknownElevation: Int = -32768 // magic number indicating 'void data' in HGT file

  private val cache = scala.collection.mutable.Map[String, Option[ShortBuffer]]()

  def tileCount: Int = cache.size

  def elevation(latLon: LatLonD): Option[Int] = {
    val tile = ElevationTile(latLon)
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
      val byteBuffer = ByteBuffer.wrap(byteArray);
      byteBuffer.order(ByteOrder.BIG_ENDIAN).asShortBuffer
    }
    finally {
      gis.close()
    }
  }

}
