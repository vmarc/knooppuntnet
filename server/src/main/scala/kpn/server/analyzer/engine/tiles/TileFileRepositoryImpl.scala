package kpn.server.analyzer.engine.tiles

import java.io.File

import kpn.core.util.Log
import kpn.server.analyzer.engine.tile.TileName
import kpn.server.analyzer.engine.tiles.domain.OldTile
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.TrueFileFilter.TRUE

import scala.jdk.CollectionConverters._

class TileFileRepositoryImpl(root: String, extension: String) extends TileFileRepository {

  private val log = Log(classOf[TileFileRepositoryImpl])

  override def saveOrUpdate(tileType: String, tile: OldTile, tileBytes: Array[Byte]): Unit = {

    val fileName = toFileName(tileType, tile)
    val file = new File(fileName)

    if (file.exists()) {
      val existingTile: Array[Byte] = FileUtils.readFileToByteArray(file: File)

      if (existingTile.sameElements(tileBytes)) {
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

  override def deleteTile(tileType: String, tile: OldTile): Unit = {
    delete(Seq(s"${tileType.replaceAll("/", "-")}-${tile.name}"))
  }

  override def existingTileNames(tileType: String, z: Int): Seq[String] = {
    val dir = new File(s"$root/$tileType/$z")
    if (dir.exists) {
      val files = FileUtils.listFiles(dir, TRUE, TRUE).asScala.toSeq.filter(_.getAbsolutePath.endsWith(extension))
      val tileNames = files.map(_.getAbsolutePath.substring(root.length + 1)).map(_.replaceAll("/", "-").replaceAll("\\." + extension, ""))
      tileNames.sorted
    }
    else {
      Seq.empty
    }
  }

  override def delete(tileNames: Seq[String]): Unit = {
    tileNames.foreach { tileName =>
      val networkType = TileName.networkType(tileName)
      val tileNumber = TileName.tileNumber(tileName)
      val filename = s"$root/$networkType/$tileNumber.$extension"
      log.debug(s"delete tile $tileName, file: $filename")
      val file = new File(filename)
      file.delete()
      val fileDir = file.getParentFile
      if (fileDir != null && fileDir.list() != null && fileDir.list().isEmpty) {
        fileDir.delete()
      }
    }
  }

  private def toFileName(tileType: String, tile: OldTile) = {
    s"$root/$tileType/${tile.z}/${tile.x}/${tile.y}.$extension"
  }

}
