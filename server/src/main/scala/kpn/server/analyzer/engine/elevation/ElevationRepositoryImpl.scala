package kpn.server.analyzer.engine.elevation

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.ShortBuffer
import java.util.zip.GZIPInputStream

import kpn.api.common.LatLon
import org.apache.commons.io.IOUtils
import org.springframework.stereotype.Component

@Component
class ElevationRepositoryImpl {

  private val secondsPerMinute = 60
  private val resolutionInArcSeconds = 3
  private val elevationValuesPerLine = 1201
  private val unknownElevation: Int = -32768 // magic number indicating 'void data' in HGT file

  private val cache = scala.collection.mutable.Map[String, Option[ShortBuffer]]()

  def elevation(latLon: LatLon): Option[Double] = {
    val tilename = tileNameFrom(latLon)
    cache.getOrElseUpdate(tilename, loadTileBuffer(tilename)).flatMap { tileBuffer =>
      elevationInTile(tileBuffer, latLon)
    }
  }

  private def elevationInTile(tileBuffer: ShortBuffer, latLon: LatLon): Option[Double] = {
    val row: Int = elevationValuesPerLine - Math.round(fractionalPart(latLon.lat) * secondsPerMinute * secondsPerMinute / resolutionInArcSeconds).toInt
    val column: Int = Math.round(fractionalPart(latLon.lon) * secondsPerMinute * secondsPerMinute / resolutionInArcSeconds).toInt
    val bufferIndex = (elevationValuesPerLine * (row - 1)) + column
    if (bufferIndex < tileBuffer.limit) {
      val elevation: Short = tileBuffer.get(bufferIndex)
      if (elevation == unknownElevation) {
        None
      }
      else {
        Some(elevation)
      }
    }
    else {
      None
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

  private def tileNameFrom(latLon: LatLon): String = {
    val lat = latLon.lat.toInt
    val lon = latLon.lon.toInt
    val latPref = if (lat < 0) "S" else "N"
    val lonPref = if (lon < 0) "W" else "E"
    "%s%02d%s%03d".format(latPref, lat, lonPref, lon)
  }

  private def fractionalPart(value: Double): Double = {
    val integerPart = value.toLong
    value - integerPart
  }

}
