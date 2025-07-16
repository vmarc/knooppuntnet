package kpn.core.tools.tile

import kpn.api.common.tiles.ZoomLevel

import java.io.File
import java.io.FilenameFilter

case class TileInfo(z: Int, x: Int, y: Int, size: Long) {
  def name: String = {
    s"$z-$x-$y"
  }
}

object TileReportTool {

  def main(args: Array[String]): Unit = {
    // new TileReportTool("/Users/marc/kpn/tiles/hiking").report()
    // new TileReportTool("/Users/marc/kpn/tiles/opendata/netherlands/hiking").report()
    new TileReportTool("/Users/marc/kpn/tiles/monitor").report()
    println("Done")
  }
}

class TileReportTool(rootDir: String) {
  def report(): Unit = {
    val tileInfos = loadTileInfos()
    reportSummary(tileInfos)
    reportZoomLevelTileSizes(tileInfos)
  }

  private def reportSummary(tileInfos: Seq[TileInfo]): Unit = {
    val sizes = tileInfos.map(_.size)
    println(s"tileCount=${tileInfos.size}")
    println(s"totalSize=${sizes.sum / 1000}K")
    println(s"maxTileSize=${sizes.max / 1000}K")
    println()
  }

  private def reportZoomLevelTileSizes(tileInfos: Seq[TileInfo]): Unit = {
    println("|zoomLevel|tileCount|max|largestTile|")
    println("|---------|---------|---|---|")
    (ZoomLevel.newMinZoom to ZoomLevel.poiTileMaxZoom).foreach { z =>
      val zoomLevelTiles = tileInfos.filter(_.z == z)
      val max = if (zoomLevelTiles.isEmpty) 0 else zoomLevelTiles.map(_.size).max
      val largestTiles: Seq[TileInfo] = if (zoomLevelTiles.isEmpty) Seq.empty else zoomLevelTiles.filter(_.size == max)
      println(s"|$z|${zoomLevelTiles.size}|${max / 1000}K|${largestTiles.map(_.name).mkString}|")
    }
  }

  private val fileFilter = new FilenameFilter {
    override def accept(dir: File, name: String): Boolean = {
      !(Seq(".DS_Store", "analysis", "surface", "survey").contains(name) || name.endsWith(".png"))
    }
  }

  private def loadTileInfos(): Seq[TileInfo] = {
    println(s"loading tile infos")
    val root = new File(rootDir)
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
