package kpn.core.tiles

import java.io.File

import kpn.core.tiles.domain.Tile
import kpn.core.util.Log
import kpn.shared.NetworkType
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.TrueFileFilter.TRUE

import scala.collection.JavaConverters._

class TileRepositoryImpl(root: String, extension: String) extends TileRepository {

  private val log = Log(classOf[TileRepositoryImpl])

  override def saveOrUpdate(tileType: String, tile: Tile, tileBytes: Array[Byte]): Unit = {

    val tileName = s"$tileType/${tile.z}/${tile.x}/${tile.y}.$extension"
    val fileName = s"$root/$tileName"
    val file = new File(fileName)

    if (file.exists()) {
      val exisitingTile: Array[Byte] = FileUtils.readFileToByteArray(file: File)

      if (exisitingTile.sameElements(tileBytes)) {
        log.info("no change for tile " + fileName)
      }
      else {
        FileUtils.writeByteArrayToFile(file, tileBytes)
        log.info("saved updated tile " + fileName)
      }
    }
    else {
      FileUtils.writeByteArrayToFile(file, tileBytes)
      log.info("saved tile " + fileName)
    }
  }

  override def existingTileNames(tileType: String, z: Int): Seq[String] = {
    val dir = new File(s"$root/$tileType/$z")
    if (dir.exists) {
      val files = FileUtils.listFiles(new File(s"$root/$tileType/$z"), TRUE, TRUE).asScala.toSeq
      files.map(_.getAbsolutePath.substring(root.length + 1)).map(_.replaceAll("/", "-").replaceAll("." + extension, ""))
    }
    else {
      Seq()
    }
  }

  override def delete(tileNames: Seq[String]): Unit = {
    tileNames.foreach { tileName =>
      new File(s"$root/${tileName.replaceAll("-", "/")}.$extension").delete()
    }
  }
}
