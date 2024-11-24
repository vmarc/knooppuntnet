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
    // println(new File("/Users/marc/kpn/tiles/hiking/13/3920/3065.mvt").length())
    new TileReportTool().report()
    println("Done")
  }
}

class TileReportTool {
  def report(): Unit = {
    val tileInfos = loadTileInfos(NetworkType.hiking)
    val sizes = tileInfos.map(_.size)
    println(s"tileCount=${tileInfos.size}, totalSize=${sizes.sum}, maxTileSize=${sizes.max}")
    //    val largestTiles = tileInfos.filter(tileInfo => tileInfo.size > 110000)
    //    largestTiles.foreach(println)
    (ZoomLevel.newMinZoom to ZoomLevel.maxZoom).foreach { z =>
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

  private def loadTileInfos(networkType: NetworkType): Array[TileInfo] = {
    println(s"loading ${networkType.name} tile infos")
    val root = new File("/Users/marc/kpn/tiles", networkType.name)
    val zs = root.listFiles(fileFilter).map(dir => dir.getName.toInt).sorted
    zs.flatMap { z =>
      val zDir = new File(root, z.toString)
      val xs = zDir.listFiles(fileFilter).map(dir => dir.getName.toInt).sorted
      xs.flatMap { x =>
        val xDir = new File(zDir, x.toString)
        xDir.listFiles(fileFilter).sortBy(_.getName).map { tileFile =>
          val y = tileFile.getName.dropRight(".mvt".length).toInt
          val size = tileFile.length()
          //  if (z == 11 && x == 1088 && y == 698) {
          //    println()
          //  }
          //  if (size == 2543717) {
          //    println()
          //  }
          TileInfo(z, x, y, size)
        }
      }
    }
  }
}
