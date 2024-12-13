package kpn.core.tools.tile

import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.NetworkType

import java.io.File
import java.io.FilenameFilter

case class TileInfo(z: Int, x: Int, y: Int, size: Long) {
  def name: String = {
    s"$z-$x-$y"
  }
}

object TileReportTool {

  def main(args: Array[String]): Unit = {
    new TileReportTool().report()
    println("Done")
  }
}

class TileReportTool {
  def report(): Unit = {
    val tileInfos = loadTileInfos(NetworkType.hiking)
    val sizes = tileInfos.map(_.size)
    println(s"tileCount=${tileInfos.size}, totalSize=${sizes.sum}, maxTileSize=${sizes.max}")
    (ZoomLevel.newMinZoom to ZoomLevel.poiTileMaxZoom).foreach { z =>
      val zoomLevelTiles = tileInfos.filter(_.z == z)
      val max = if (zoomLevelTiles.isEmpty) 0 else zoomLevelTiles.map(_.size).max
      val largestTiles: Seq[TileInfo] = if (zoomLevelTiles.isEmpty) Seq.empty else zoomLevelTiles.filter(_.size == max)
      println(s"zoomLevel=$z, tileCount=${zoomLevelTiles.size}, max=$max, largestTile=${largestTiles.map(_.name)}")
    }
  }

  private val fileFilter = new FilenameFilter {
    override def accept(dir: File, name: String): Boolean = {
      !(Seq(".DS_Store", "analysis", "surface", "survey").contains(name) || name.endsWith(".png"))
    }
  }

  private def loadTileInfos(networkType: NetworkType): Seq[TileInfo] = {
    println(s"loading ${networkType.entryName} tile infos")
    val root = new File("/Users/marc/kpn/tiles", networkType.entryName)
    val zs = root.listFiles(fileFilter).map(dir => dir.getName.toInt).sorted.toSeq
    zs.flatMap { z =>
      val zDir = new File(root, z.toString)
      val xs = zDir.listFiles(fileFilter).map(dir => dir.getName.toInt).sorted.toSeq
      xs.flatMap { x =>
        val xDir = new File(zDir, x.toString)
        xDir.listFiles(fileFilter).sortBy(_.getName).map { tileFile =>
          val y = tileFile.getName.dropRight(".mvt".length).toInt
          val size = tileFile.length()
          TileInfo(z, x, y, size)
        }
      }
    }
  }
}
